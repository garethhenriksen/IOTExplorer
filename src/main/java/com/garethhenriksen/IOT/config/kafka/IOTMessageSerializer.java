package com.garethhenriksen.IOT.config.kafka;

import com.garethhenriksen.IOT.model.IOTMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Closeable;
import java.util.Map;

@Slf4j
public class IOTMessageSerializer implements Closeable, AutoCloseable, Serializer<IOTMessage> {

    static private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, IOTMessage person) {
        // Transform the IOTMessage object to String
        String line = gson.toJson(person);
        return line.getBytes();
    }

    @Override
    public void close() {

    }
}