package com.garethhenriksen.IOT.listener;

import com.garethhenriksen.IOT.message.MessageProcessor;
import com.garethhenriksen.IOT.model.IOTMessage;
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
    public void listen(IOTMessage message) {
        mp.processMessage(message);
    }
}
