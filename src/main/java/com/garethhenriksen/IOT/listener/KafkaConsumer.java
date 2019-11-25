package com.garethhenriksen.IOT.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garethhenriksen.IOT.message.MessageProcessor;
import com.garethhenriksen.IOT.model.BusPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class KafkaConsumer {

    @Autowired
    private MessageProcessor mp;

    @KafkaListener(topics = "iot_message")
    public void listen(String message) {
        mp.processMessage(message);
    }

    @KafkaListener(topics = "rtd-bus-position-enriched", groupId = "my-group3")
    public void listenToRTDEnriched(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BusPosition busPosition = objectMapper.readValue(message, BusPosition.class);
            mp.processBusPosition(busPosition);
        } catch (Exception e) {
            log.error("Exception while listening to RTDEnriched: " + message, e);
        }
    }
}
