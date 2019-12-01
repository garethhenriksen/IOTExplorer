package com.garethhenriksen.IOT.config.kafka;

import com.garethhenriksen.IOT.model.IOTMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class IOTMessageSerde implements Serde<IOTMessage> {
    private IOTMessageSerializer serializer = new IOTMessageSerializer();
    private IOTMessageDeserializer deserializer = new IOTMessageDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<IOTMessage> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<IOTMessage> deserializer() {
        return deserializer;
    }
}
