package com.garethhenriksen.IOT.controller;

import com.garethhenriksen.IOT.config.kafka.IOTMessageSerde;
import com.garethhenriksen.IOT.config.kafka.JsonPOJODeserializer;
import com.garethhenriksen.IOT.config.kafka.JsonPOJOSerializer;
import com.garethhenriksen.IOT.model.IOTMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@EnableKafka
public class KafkaStreamController {
    private static HashMap<String, IOTMessage> previousIOTMessageMap = new HashMap<String, IOTMessage>();

    @Bean
    public void init() {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<IOTMessage> pageViewSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", IOTMessage.class);
        pageViewSerializer.configure(serdeProps, false);

        final Deserializer<IOTMessage> pageViewDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", IOTMessage.class);
        pageViewDeserializer.configure(serdeProps, false);
        final Serde<IOTMessage> pageViewSerde = Serdes.serdeFrom(pageViewSerializer, pageViewDeserializer);
        final StreamsBuilder builder = new StreamsBuilder();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstreams-iot-streamer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, IOTMessageSerde.class);
        KStream<String, IOTMessage> rtdBusPositionStream = builder.stream("iot_message", Consumed.with(Serdes.String(), pageViewSerde));

        // posting the enriched IOTMessage to "iot_message_difference" topic
        rtdBusPositionStream.mapValues(iotMessage -> {
            iotMessage = enrichIOTMessage(iotMessage);
            return iotMessage;
        }).to("iot_message_difference");

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    /**
     * Calculates the difference from the previous IOT Message received.
     * @param iotMessage
     * @return
     */
    private IOTMessage enrichIOTMessage(IOTMessage iotMessage) {
        IOTMessageKey messageKey = new IOTMessageKey(iotMessage.getDeviceTypeId(), iotMessage.getDeviceId(), iotMessage.getGroupId());
        if (previousIOTMessageMap.containsKey(messageKey.toString())) {
            IOTMessage prevMessage = previousIOTMessageMap.get(messageKey.toString());

            // calculate the difference from the previous message
            Double diff = iotMessage.getValue() - prevMessage.getValue();
            iotMessage.setDifference(diff);
        } else {
            iotMessage.setDifference(0.0);
        }
        previousIOTMessageMap.put(messageKey.toString(), iotMessage);

        return iotMessage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class IOTMessageKey {
        private final Integer deviceTypeId;
        private final Integer deviceId;
        private final Integer groupId;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @Setter
    public static class MetricsCountAndSum {
        private final Long count;
        private final Long sum;
        private final Double min;
        private final Double max;
    }
}
