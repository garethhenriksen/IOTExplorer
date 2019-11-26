package com.garethhenriksen.IOT.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("iot_message", 1, (short) 1);
    }

    @Bean
    public NewTopic topic2() {
        return new NewTopic("rtd-bus-position-enriched", 1, (short) 1);
    }

    @Bean
    public NewTopic topic3() {
        return new NewTopic("iot_message_enriched", 1, (short) 1);
    }
}