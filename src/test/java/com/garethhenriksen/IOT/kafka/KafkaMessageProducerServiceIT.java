//package com.garethhenriksen.IOT.kafka;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.garethhenriksen.IOT.controller.IOTController;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
//import org.springframework.kafka.test.utils.ContainerTestUtils;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertEquals;
//import static org.springframework.kafka.test.assertj.KafkaConditions.key;
//
//@RunWith(SpringRunner.class)
//@DirtiesContext
//@SpringBootTest()
//@Slf4j
//public class KafkaMessageProducerServiceIT {
//    private static String TOPIC_NAME = "iot_message";
//
//    @Autowired
//    private IOTController kafkaMessageProducerService;
//
//    @ClassRule
//    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, TOPIC_NAME);
//
//    private KafkaMessageListenerContainer<String, String> container;
//
//    private BlockingQueue<ConsumerRecord<String, String>> consumerRecords;
//
//    @Before
//    public void setUp() {
//        consumerRecords = new LinkedBlockingQueue<>();
//
//        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);
//
//        Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(
//                "my-group", "false", embeddedKafka.getEmbeddedKafka());
//
//        DefaultKafkaConsumerFactory<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProperties);
//
//        container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
//        container.setupMessageListener((MessageListener<String, String>) record -> {
//            log.info("Listened message='{}'", record.toString());
//            consumerRecords.add(record);
//        });
//        container.start();
//
//        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
//    }
//
//    @After
//    public void tearDown() {
//        container.stop();
//    }
//
//    @Test
//    public void it_should_send() throws InterruptedException, IOException {
//        String iotMessage = "{\"deviceId\": 1, \"deviceTypeId\":1, \"groupId\":1, \"value\":20}";
//        kafkaMessageProducerService.publishKafkaMessage(iotMessage);
//
//        ConsumerRecord<String, String> received = consumerRecords.poll(10, TimeUnit.SECONDS);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString( iotMessage );
//
//        log.info(json);
//
////        assertEquals(received, iotMessage);
////
////        assertThat(received).has(key(null));
//    }
//
//}
