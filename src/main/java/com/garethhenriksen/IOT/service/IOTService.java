package com.garethhenriksen.IOT.service;

import com.garethhenriksen.IOT.dao.IOTDao;
import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    public List<IOTMessage> getMessages() {
        return iotDao.getMessages();
    }

    public IOTMessageDTO getMessagesWithParameters(String deviceTypeId, String deviceId, String groupId, String query,
                                                   Date startDate, Date endDate) {
        return iotDao.getMessages(deviceTypeId, deviceId, groupId, query, startDate, endDate);
    }

    public Double getMessagesWithParameter(String query) {
        return iotDao.getValue(query);
    }
}
