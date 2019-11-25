package com.garethhenriksen.IOT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garethhenriksen.IOT.model.BusPosition;
import com.garethhenriksen.IOT.model.Location;
import com.google.transit.realtime.GtfsRealtime;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

@Slf4j
@Controller
@EnableScheduling
public class RTDVehicleController {
    @Autowired
    KafkaTemplate<String, BusPosition> rtdKafkaTemplate;

    private static HashMap<String, BusPosition> previousBusPositionMap = new HashMap<String, BusPosition>();

    @Scheduled(cron = "*/30 * * * * *")
    private void getBusPositions() {
        try {
            // Docs for stream source available here: http://www.rtd-denver.com/gtfs-developer-guide.shtml#samples
            log.info("Getting latest vehicle positions from RTD feed.");

            // get latest vehicle positions
            String userName = "RTDgtfsRT";
            String password = "realT!m3Feed";
            String url = "http://www.rtd-denver.com/google_sync/VehiclePosition.pb";

            Unirest.get(url)
                    .basicAuth(userName, password)
                    .thenConsume(rawResponse -> {
                        try {
                            InputStream stream = rawResponse.getContent();

                            GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(stream);

                            for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                                GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();

                                Location location = new Location();
                                location.setLatitude(vehiclePosition.getPosition().getLatitude());
                                location.setLongitude(vehiclePosition.getPosition().getLongitude());

                                BusPosition busPosition = new BusPosition();
                                busPosition.setId(vehiclePosition.getVehicle().getId());
                                busPosition.setTimestamp(vehiclePosition.getTimestamp());
                                busPosition.setLocation(location);

                                Message<BusPosition> message = MessageBuilder
                                        .withPayload(busPosition)
                                        .setHeader(KafkaHeaders.TOPIC, "rtd-bus-position")
                                        .setHeader(KafkaHeaders.MESSAGE_KEY, entity.getVehicle().getVehicle().getId())
                                        .build();

                                // publish to `rtd-bus-position` Kafka topic
                                rtdKafkaTemplate.send(message);
                            }
                        } catch (Exception e) {
                            throw new Error(e);
                        }
                    });

            log.info("Published latest vehicle positions to Kafka.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void analyzeBusPositionStream() {
        log.info("analyzeBusPositionStream");
        String inputTopic = "rtd-bus-position";
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstreams-rtd-streamer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> rtdBusPositionStream = builder.stream("rtd-bus-position");

        rtdBusPositionStream.mapValues(value -> {
            value = enrichBusPosition(value);
            return value;
        }).to("rtd-bus-position-enriched");

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static String enrichBusPosition(String busPositionString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BusPosition busPosition = objectMapper.readValue(busPositionString, BusPosition.class);

            // current bus ID
            String busId = busPosition.getId();
            log.info(busPosition.toString());
            // if there is a previous location for that bus ID, calculate the speed based on its previous position/timestamp.
            if (previousBusPositionMap.containsKey(busId)){
                BusPosition previousBusPosition = previousBusPositionMap.get(busId);

                // calculate distance and time between last two measurements
                HaversineDistanceCalculator haversineDistanceCalculator = new HaversineDistanceCalculator();
                double distance = haversineDistanceCalculator.calculateDistance(
                        previousBusPosition.getLocation().getLatitude(),
                        previousBusPosition.getLocation().getLongitude(),
                        busPosition.getLocation().getLatitude(),
                        busPosition.getLocation().getLongitude()); // distance is in kilometers

                long timedelta = busPosition.getTimestamp() - previousBusPosition.getTimestamp(); // time delta is in seconds
                double milesPerHour = calculateMilesPerHour(distance, timedelta);

                busPosition.setMilesPerHour(milesPerHour);
            }
            previousBusPositionMap.put(busId, busPosition);

            String busPositionS = objectMapper.writeValueAsString(busPosition);
            return busPositionS;
        } catch (Exception e) {
            log.error("error while trying to enrich RTDBusPositions with the miles per hour: " , e);
        }

        return null;

    }

    private static double calculateMilesPerHour(double meters, long seconds) {
        if (seconds == 0){
            return 0;
        } else {
            double metersPerSecond = meters * 1000 / seconds;
            return metersPerSecond * 2.2369;
        }
    }

    static class HaversineDistanceCalculator {

        final int R = 6371; // Radius of the earth in kilometers

        public Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2){
            Double latDistance = toRad(lat2-lat1);
            Double lonDistance = toRad(lon2-lon1);
            Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                            Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            Double distance = R * c;

            return distance; // in kilometers
        }

        private static Double toRad(Double value) {
            return value * Math.PI / 180;
        }

    }
}
