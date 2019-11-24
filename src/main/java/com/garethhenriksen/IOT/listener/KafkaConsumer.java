package com.garethhenriksen.IOT.listener;

import com.garethhenriksen.IOT.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Slf4j
@Controller
public class KafkaConsumer {

    @Autowired
    private MessageProcessor mp;

    @KafkaListener(topics = "iot_message")
    public void listen(String message) {
        mp.processMessage(message);
    }
}
