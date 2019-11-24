package com.garethhenriksen.IOT.controller;

import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import com.garethhenriksen.IOT.service.IOTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;
import java.net.URLDecoder;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/")
public class IOTController {

    private final IOTService iotService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

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
//            kafkaTemplate.send(TOPIC, URLDecoder.decode(message, "UTF-8"));
            ListenableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, decodedMessage);

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    System.out.println("Sent message=[" + decodedMessage +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }
                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=["
                            + decodedMessage + "] due to : " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Exception while attempting to publish[" + message + "]", e);
            return "Publish Unsuccessfully";
        }
        return "Published Successfully";
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping(value = "/publish")
    public String publishJMSMessage(@RequestBody(required = false) final String message) {
        try {
            log.info("message" + URLDecoder.decode(message, "UTF-8"));
            jmsTemplate.convertAndSend(queue, URLDecoder.decode(message, "UTF-8"));
        } catch (Exception e) {
            log.error("Exception while attempting to publish[" + message + "]", e);
            return "Publish Unsuccessfully";
        }
        return "Published Successfully";
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @GetMapping(value = "/messages")
    public IOTMessageDTO test(@RequestParam(name = "deviceTypeId", required = false) String deviceTypeId,
                              @RequestParam(name = "deviceId", required = false) String deviceId,
                              @RequestParam(name = "groupId", required = false) String groupId,
                              @RequestParam(name = "query", required = false) String query,
                              @RequestParam(name = "startDate", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startDate,
                              @RequestParam(name = "endDate", required = false)
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endDate) {

        log.info("deviceTypeId" + deviceTypeId + "\n" +
                "deviceId" + deviceId + "\n" +
                "groupId" + groupId + "\n" +
                "query" + query + "\n" +
                "startDate" + startDate + "\n" +
                "endDate" + endDate + "\n");
        return iotService.getMessagesWithParameters(deviceTypeId, deviceId, groupId, query, startDate, endDate);
    }
}
