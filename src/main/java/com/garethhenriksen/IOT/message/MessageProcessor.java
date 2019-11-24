package com.garethhenriksen.IOT.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.service.IOTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class MessageProcessor {

    @Autowired
    private IOTService iotService;


    public MessageProcessor() {

    }


    public void processMessage(String message) {
        // get json
        // analyse to certain type
        // save in db
        System.out.println("processMessage: " + message);
        log.info("processMessage: " + message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            IOTMessage iotMessage = objectMapper.readValue(message, IOTMessage.class);
            log.info("iotMessage: " + iotMessage.toString());

            iotService.addMessage(iotMessage);
        } catch (Exception e) {
            log.error("error: " + message, e);
        }

    }
}








