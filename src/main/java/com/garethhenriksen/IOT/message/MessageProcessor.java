package com.garethhenriksen.IOT.message;

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
}








