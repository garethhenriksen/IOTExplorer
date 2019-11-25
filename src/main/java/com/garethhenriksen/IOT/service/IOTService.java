package com.garethhenriksen.IOT.service;

import com.garethhenriksen.IOT.dao.IOTDao;
import com.garethhenriksen.IOT.model.BusPosition;
import com.garethhenriksen.IOT.model.BusPositionsDTO;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class IOTService {

    private final IOTDao iotDao;

    @Autowired
    public IOTService(@Qualifier("postgres") IOTDao iotDao) {
        this.iotDao = iotDao;
    }

    public int addMessage(IOTMessage message) {
        return iotDao.insertMessage(message);
    }

    public IOTMessageDTO getMessagesWithParameters(Integer deviceTypeId, Integer deviceId, Integer groupId, String query,
                                                   Date startDate, Date endDate) {
        return iotDao.getMessages(deviceTypeId, deviceId, groupId, query, startDate, endDate);
    }

    public void updateBusPosition(BusPosition busPosition) {
        iotDao.insertBusPosition(busPosition);
    }

    public BusPositionsDTO getBusPosition() {
        return iotDao.getBusLatestPositions();
    }
}
