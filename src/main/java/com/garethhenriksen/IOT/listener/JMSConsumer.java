package com.garethhenriksen.IOT.listener;

import com.garethhenriksen.IOT.message.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class JMSConsumer {

    @Autowired
    private MessageProcessor mp;

    @JmsListener(destination = "jms.queue")
    public void listener(String message) {
        log.info("Received Message: " + message);
        mp.processMessage(message);
    }
}