package com.garethhenriksen.IOT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garethhenriksen.IOT.model.BusPositionsDTO;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import com.garethhenriksen.IOT.service.IOTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;
import java.net.URLDecoder;
import java.util.Date;


@Slf4j
@RestController
@Validated
@RequestMapping("/")
public class IOTController {

    private final IOTService iotService;

    @Autowired
    private KafkaTemplate<String, IOTMessage> kafkaTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    private static final String TOPIC = "iot_message";

    @Autowired
    public IOTController(IOTService iotService) {
        this.iotService = iotService;
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping(value = "/publish/kafka")
    public String publishKafkaMessage(@RequestBody(required = false) String message) {
        try {
            log.info("message" + URLDecoder.decode(message, "UTF-8"));
            final String decodedMessage = URLDecoder.decode(message, "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            IOTMessage obj = mapper.readValue(decodedMessage, IOTMessage.class);
            kafkaTemplate.send(new ProducerRecord<>(TOPIC, obj.getDeviceTypeId() + "_" + obj.getDeviceId(), obj));
        } catch (Exception e) {
            log.error("Exception while attempting to publish[" + message + "]", e);
            return "Publish Unsuccessfully";
        }
        return "Published Successfully";
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping(value = "/publish/activemq")
    public String publishJMSMessage(@RequestBody(required = true) final String message) {
        try {
            jmsTemplate.convertAndSend(queue, URLDecoder.decode(message, "UTF-8"));
        } catch (Exception e) {
            log.error("Exception while attempting to publish[" + message + "]", e);
            return "Publish Unsuccessfully";
        }
        return "Published Successfully";
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/messages")
    public IOTMessageDTO getMessages(@RequestParam(name = "deviceTypeId", required = false) Integer deviceTypeId,
                                     @RequestParam(name = "deviceId", required = false) Integer deviceId,
                                     @RequestParam(name = "groupId", required = false) Integer groupId,
                                     @RequestParam(name = "query", required = false) String query,
                                     @RequestParam(name = "startDate", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startDate,
                                     @RequestParam(name = "endDate", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endDate) {
        return iotService.getMessagesWithParameters(deviceTypeId, deviceId, groupId, query, startDate, endDate);
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/bus")
    public BusPositionsDTO getBusPositions() {
        return iotService.getBusPosition();
    }
}
