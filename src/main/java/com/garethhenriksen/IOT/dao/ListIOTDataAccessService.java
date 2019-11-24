package com.garethhenriksen.IOT.dao;

import com.garethhenriksen.IOT.model.IOTMessage;
import com.garethhenriksen.IOT.model.IOTMessageDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository("list")
public class ListIOTDataAccessService implements IOTDao {

    private static List<IOTMessage> DB = new ArrayList<>();

    @Override
    public int insertMessage(UUID id, IOTMessage message) {
        System.out.println(message);
        DB.add(message);
        return 1;
    }

    @Override
    public List<IOTMessage> getMessages() {
        return DB;
    }

    @Override
    public IOTMessageDTO getMessages(String deviceTypeId, String deviceId, String groupId, String query, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Double getValue(String query) {
        return null;
    }


}
