package com.garethhenriksen.IOT.config.kafka;

import com.garethhenriksen.IOT.model.IOTMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.Closeable;
import java.util.Map;

@Slf4j
public class IOTMessageDeserializer implements Closeable, AutoCloseable, Deserializer<IOTMessage> {

    static private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public IOTMessage deserialize(String topic, byte[] bytes) {
        try {
            // Transform the bytes to String
            String person = new String(bytes);
            // Return the Person object created from the String 'person'
            return gson.fromJson(person, IOTMessage.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading bytes", e);
        }
    }

    @Override
    public void close() {

    }
}