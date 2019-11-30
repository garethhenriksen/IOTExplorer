package com.garethhenriksen.IOT.controller;

import com.garethhenriksen.IOT.config.kafka.JsonPOJODeserializer;
import com.garethhenriksen.IOT.config.kafka.JsonPOJOSerializer;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
public class KafkaStreamController {

    KafkaStreams metricsStream;

    KafkaStreams metricsWindowedStream;

    KStream<IOTMessageKey, IOTMessage> iotMessageStream;

    KStream<IOTMessageKey, IOTMessage> iotMessageStream2;

    @Bean
    public KafkaStreams metricsStream() {
        return metricsStream;
    }

    @Autowired
    public KafkaStreamController() {

    }

    /**
     * @param deviceTypeId
     * @param deviceId
     * @param groupId
     * @param query
     * @return
     */
    public IOTMessageDTO getValue(Integer deviceTypeId, Integer deviceId, Integer groupId, String query) {
        IOTMessageDTO returnMessage = new IOTMessageDTO();
        if (query.equalsIgnoreCase("AVG")) {
            ReadOnlyKeyValueStore<IOTMessageKey, Double> averageStore = metricsStream
                    .store("average-metrics-store", QueryableStoreTypes.<IOTMessageKey, Double>keyValueStore());
            KeyValueIterator<IOTMessageKey, Double> avgIterator = averageStore.all();
            Double sum = 0.0;
            Double count = 0.0;
            while (avgIterator.hasNext()) {
                KeyValue<IOTMessageKey, Double> kv = avgIterator.next();
                log.info(kv.key.getDeviceId()+ " is getDeviceId");
                log.info(kv.key.getDeviceTypeId() + " is getDeviceTypeId");
                log.info(kv.key.getGroupId() + " is getGroupId");
                Boolean isDeviceTypeId = (deviceTypeId == null) ? true : (deviceTypeId.equals(kv.key.getDeviceTypeId()));
                Boolean isDeviceId = (deviceId == null) ? true : (deviceId.equals(kv.key.getDeviceId()));
                Boolean isGroupId = (groupId == null) ? true : (groupId.equals(kv.key.getGroupId()));

                if (isDeviceTypeId && isDeviceId && isGroupId) {
                    sum += kv.value;
                    count++;
                }
            }

            returnMessage.setValue(sum / count);
            return returnMessage;
        } else if (query.equalsIgnoreCase("MIN")) {
            ReadOnlyKeyValueStore<IOTMessageKey, Double> minStore = metricsStream
                    .store("min-metrics-store", QueryableStoreTypes.<IOTMessageKey, Double>keyValueStore());
            KeyValueIterator<IOTMessageKey, Double> minIterator = minStore.all();
            Double min = Double.MAX_VALUE;
            while (minIterator.hasNext()) {
                KeyValue<IOTMessageKey, Double> kv = minIterator.next();
                log.info(kv.key.getDeviceId() + " is getDeviceId");
                log.info(kv.key.getDeviceTypeId() + " is getDeviceTypeId");
                log.info(kv.key.getGroupId() + " is getGroupId");
                Boolean isDeviceTypeId = (deviceTypeId == null) ? true : (deviceTypeId.equals(kv.key.getDeviceTypeId()));
                Boolean isDeviceId = (deviceId == null) ? true : (deviceId.equals(kv.key.getDeviceId()));
                Boolean isGroupId = (groupId == null) ? true : (groupId.equals(kv.key.getGroupId()));

                if (isDeviceTypeId && isDeviceId && isGroupId) {
                    if (kv.value < min) {
                        min = kv.value;
                    }
                }
            }
            returnMessage.setValue(min);
            return returnMessage;
        } else if (query.equalsIgnoreCase("MAX")) {
            ReadOnlyKeyValueStore<IOTMessageKey, Double> maxStore = metricsStream
                    .store("max-metrics-store", QueryableStoreTypes.<IOTMessageKey, Double>keyValueStore());
            KeyValueIterator<IOTMessageKey, Double> maxIterator = maxStore.all();
            Double max = Double.MIN_VALUE;
            while (maxIterator.hasNext()) {
                KeyValue<IOTMessageKey, Double> kv = maxIterator.next();
                log.info(kv.key.getDeviceId() + " is getDeviceId");
                log.info(kv.key.getDeviceTypeId() + " is getDeviceTypeId");
                log.info(kv.key.getGroupId() + " is getGroupId");
                Boolean isDeviceTypeId = (deviceTypeId == null) ? true : (deviceTypeId.equals(kv.key.getDeviceTypeId()));
                Boolean isDeviceId = (deviceId == null) ? true : (deviceId.equals(kv.key.getDeviceId()));
                Boolean isGroupId = (groupId == null) ? true : (groupId.equals(kv.key.getGroupId()));

                if (isDeviceTypeId && isDeviceId && isGroupId) {
                    if (kv.value > max) {
                        max = kv.value;
                    }
                }
            }
            returnMessage.setValue(max);
            return returnMessage;
        }
        return null;
    }

    @PostConstruct
    public void init() {
        String inputTopic = "iot_message";
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstreams-iot-streamer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<IOTMessage> pageViewSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", IOTMessage.class);
        pageViewSerializer.configure(serdeProps, false);

        final Deserializer<IOTMessage> pageViewDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", IOTMessage.class);
        pageViewDeserializer.configure(serdeProps, false);
        final Serde<IOTMessage> pageViewSerde = Serdes.serdeFrom(pageViewSerializer, pageViewDeserializer);

        final StreamsBuilder builder = new StreamsBuilder();
        iotMessageStream = builder.stream("iot_message", Consumed.with(new IOTMessageKeySerde(), pageViewSerde));
        KTable<IOTMessageKey, MetricsCountAndSum> aggregateTable = iotMessageStream.groupByKey()
                .aggregate(new Initializer<MetricsCountAndSum>() {
                               @Override
                               public MetricsCountAndSum apply() {
                                   return new MetricsCountAndSum(0L, 0L, 1000000.0, 0.0);
                               }
                           }, new Aggregator<IOTMessageKey, IOTMessage, MetricsCountAndSum>() {
                               @Override
                               public MetricsCountAndSum apply(IOTMessageKey key, IOTMessage value, MetricsCountAndSum current) {
                                   Long newCount = current.getCount() + 1;
                                   Long newSum = current.getSum() + (new Double(value.getValue())).longValue();
                                   Double newMin = current.getMin();
                                   Double newMax = current.getMax();
                                   // stupid issue having when testing as streams had started with value of 0
                                   if (value.getValue() < current.getMin()) {
                                       newMin = value.getValue();
                                   }

                                   if (value.getValue() > current.getMax()) {
                                       newMax = value.getValue();
                                   }

                                   MetricsCountAndSum metricsCountAndSum = new MetricsCountAndSum(newCount, newSum, newMin, newMax);
                                   return metricsCountAndSum;
                               }
                           },
                        Materialized.with(new IOTMessageKeySerde(), new MetricsCountAndSumSerde()));

        aggregateTable.mapValues(new ValueMapperWithKey<IOTMessageKey, MetricsCountAndSum, Double>() {
            @Override
            public Double apply(IOTMessageKey key, MetricsCountAndSum countAndSum) {
                Double average = countAndSum.getSum() / countAndSum.getCount().doubleValue();
                return average;
            }
        }, Materialized.<IOTMessageKey, Double, KeyValueStore<Bytes, byte[]>>as("average-metrics-store").withKeySerde(new IOTMessageKeySerde())
                .withValueSerde(Serdes.Double()));

        aggregateTable.mapValues(new ValueMapperWithKey<IOTMessageKey, MetricsCountAndSum, Double>() {
            @Override
            public Double apply(IOTMessageKey key, MetricsCountAndSum countAndSum) {
                return countAndSum.getMin();
            }
        }, Materialized.<IOTMessageKey, Double, KeyValueStore<Bytes, byte[]>>as("min-metrics-store")
                .withKeySerde(new IOTMessageKeySerde())
                .withValueSerde(Serdes.Double()));

        aggregateTable.mapValues(new ValueMapperWithKey<IOTMessageKey, MetricsCountAndSum, Double>() {
            @Override
            public Double apply(IOTMessageKey key, MetricsCountAndSum countAndSum) {
                return countAndSum.getMax();
            }
        }, Materialized.<IOTMessageKey, Double, KeyValueStore<Bytes, byte[]>>as("max-metrics-store")
                .withKeySerde(new IOTMessageKeySerde())
                .withValueSerde(Serdes.Double()));

        metricsStream = new KafkaStreams(builder.build(), props);
        metricsStream.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.error("metricsAverageStream Uncaught exception in Thread {0} - {1}", new Object[]{t, e.getMessage()});
                e.printStackTrace();
            }
        });
        metricsStream.start();
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

    public static class IOTMessageKeySerde implements Serde<IOTMessageKey> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public void close() {
        }

        @Override
        public Serializer<IOTMessageKey> serializer() {
            return new Serializer<IOTMessageKey>() {
                @Override
                public void configure(Map<String, ?> configs, boolean isKey) {
                }

                @Override
                public byte[] serialize(String topic, IOTMessageKey data) {
                    String countAndSum = data.getDeviceTypeId() + "_" + data.getDeviceId() + "_" + data.getGroupId();
                    return countAndSum.getBytes();
                }

                @Override
                public void close() {
                }
            };
        }

        @Override
        public Deserializer<IOTMessageKey> deserializer() {
            return new Deserializer<IOTMessageKey>() {
                @Override
                public void configure(Map<String, ?> configs, boolean isKey) {
                }

                @Override
                public IOTMessageKey deserialize(String topic, byte[] key) {
                    String keyStr = new String(key);
                    IOTMessageKey iOTMessageKey = new IOTMessageKey(0, 0, 0);

                    try {
                        if (keyStr.split("_").length > 0) {
                            Integer deviceTypeId = Integer.valueOf(keyStr.split("_")[0]);
                            Integer deviceId = Integer.valueOf(keyStr.split("_")[1]);
                            Integer groupId = Integer.valueOf(keyStr.split("_")[2]);

                            iOTMessageKey = new IOTMessageKey(deviceTypeId, deviceId, groupId);
                            return iOTMessageKey;
                        }
                    } catch (Exception e) {
                        log.error("error parsing key[" + keyStr + "]");
                    }
                    return iOTMessageKey;
                }

                @Override
                public void close() {
                }
            };
        }

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

    public static class MetricsCountAndSumSerde implements Serde<MetricsCountAndSum> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public void close() {
        }

        @Override
        public Serializer<MetricsCountAndSum> serializer() {
            return new Serializer<MetricsCountAndSum>() {
                @Override
                public void configure(Map<String, ?> configs, boolean isKey) {
                }

                @Override
                public byte[] serialize(String topic, MetricsCountAndSum data) {
                    String countAndSum = data.getCount() + ":" + data.getSum() + ":" + data.getMin() + ":" + data.getMax();
                    return countAndSum.getBytes();
                }

                @Override
                public void close() {
                }
            };
        }

        @Override
        public Deserializer<MetricsCountAndSum> deserializer() {
            return new Deserializer<MetricsCountAndSum>() {
                @Override
                public void configure(Map<String, ?> configs, boolean isKey) {
                }

                @Override
                public MetricsCountAndSum deserialize(String topic, byte[] countAndSum) {
                    String countAndSumStr = new String(countAndSum);
                    MetricsCountAndSum countAndSumObject = new MetricsCountAndSum(0L, 0L, 0.0, 0.0);

                    try {
                        String[] splitArray = countAndSumStr.split(":");
                        Long count = Long.valueOf(splitArray[0]);
                        Long sum = Long.valueOf(splitArray[1]);

                        Double min = Double.valueOf(splitArray[2]);
                        Double max = Double.valueOf(splitArray[3]);

                        countAndSumObject = new MetricsCountAndSum(count, sum, min, max);
                    } catch (Exception e) {
                        log.error("exception attempting to deserialize MetricsCountAndSum");
                    }

                    return countAndSumObject;
                }

                @Override
                public void close() {
                }
            };
        }
    }
}
