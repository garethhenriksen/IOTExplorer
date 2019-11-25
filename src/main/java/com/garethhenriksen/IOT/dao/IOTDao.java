package com.garethhenriksen.IOT.dao;

import com.garethhenriksen.IOT.model.BusPosition;
import com.garethhenriksen.IOT.model.BusPositionsDTO;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;

import java.util.Date;
import java.util.UUID;

public interface IOTDao {
    int insertMessage(UUID id, IOTMessage message);

    default int insertMessage(IOTMessage message) {
        UUID id = UUID.randomUUID();
        return insertMessage(id, message);
    }

    IOTMessageDTO getMessages(Integer deviceTypeId, Integer deviceId, Integer groupId, String query,
                              Date startDate, Date endDate);

    void insertBusPosition(BusPosition busPosition);

    BusPositionsDTO getBusLatestPositions();
}
