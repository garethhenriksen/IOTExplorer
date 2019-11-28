package com.garethhenriksen.IOT.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garethhenriksen.IOT.model.BusPosition;
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

    /**
     * @param message
     */
    public void processMessage(IOTMessage message) {
        iotService.addMessage(message);
    }

    public void processMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            IOTMessage iotMessage = objectMapper.readValue(message, IOTMessage.class);

            processMessage(iotMessage);
        } catch (Exception e) {
            log.error("error: " + message, e);
        }
    }

    /**
     * @param message
     */
    public void processBusPosition(BusPosition message) {
        try {
            iotService.updateBusPosition(message);
        } catch (Exception e) {
            log.error("error: " + message, e);
        }
    }
}








