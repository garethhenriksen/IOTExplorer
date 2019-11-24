package com.garethhenriksen.IOT.dao;

import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IOTDao {
    int insertMessage(UUID id, IOTMessage message);

    default int insertMessage(IOTMessage message) {
        UUID id = UUID.randomUUID();
        return insertMessage(id, message);
    }

    List<IOTMessage> getMessages();

    IOTMessageDTO getMessages(String deviceTypeId, String deviceId, String groupId, String query,
                              Date startDate, Date endDate);

    Double getValue(String query);
}
