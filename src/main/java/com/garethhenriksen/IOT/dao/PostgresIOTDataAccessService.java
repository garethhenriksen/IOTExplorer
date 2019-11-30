package com.garethhenriksen.IOT.dao;

import com.garethhenriksen.IOT.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository("postgres")
public class PostgresIOTDataAccessService implements IOTDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresIOTDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertMessage(UUID id, IOTMessage message) {
        final String sql = "INSERT INTO IOT_Message(deviceId, deviceTypeId, groupId, value, timestamp) VALUES(?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, message.getDeviceId(), message.getDeviceTypeId(), message.getGroupId(), message.getValue(), message.getTimestamp());
    }

    @Override
    public IOTMessageDTO getMessages(Integer deviceTypeId, Integer deviceId, Integer groupId, String query, Date startDate, Date endDate) {
        StringBuilder baseSql;
        StringBuilder additionalSql = new StringBuilder(" ");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (deviceTypeId != null) {
            additionalSql.append(" AND deviceTypeId = ").append(deviceTypeId);
        }

        if (deviceId != null) {
            additionalSql.append(" AND deviceId = ").append(deviceId);
        }

        if (groupId != null) {
            additionalSql.append(" AND groupId = ").append(groupId);
        }

        if (startDate == null && endDate != null) {
            additionalSql.append(" AND timestamp <= ").append("\'").append(dateFormat.format(endDate)).append("\'");
        }

        if (startDate != null && endDate == null) {
            additionalSql.append(" AND timestamp >= ").append("\'").append(dateFormat.format(startDate)).append("\'");
        }

        if (startDate != null && endDate != null) {
            additionalSql.append(" AND timestamp >= ").append("\'").append(dateFormat.format(startDate)).append("\'")
                    .append(" AND timestamp <= ").append("\'").append(dateFormat.format(endDate)).append("\'");
        }

        if (query == null) {
            baseSql = new StringBuilder("SELECT deviceId, deviceTypeId, groupId, value, timestamp FROM IOT_Message WHERE 1 = 1");

            return executeQuery(baseSql.append(additionalSql).toString());
        } else if (query.equalsIgnoreCase("AVG")) {
            baseSql = new StringBuilder("SELECT AVG(value) as value FROM IOT_Message WHERE 1 = 1");
            return executeQuery(baseSql.append(additionalSql).toString(), "value");
        } else if (query.equalsIgnoreCase("SUM")) {
            baseSql = new StringBuilder("SELECT SUM(value) as value FROM IOT_Message WHERE 1 = 1");
            return executeQuery(baseSql.append(additionalSql).toString(), "value");
        } else if (query.equalsIgnoreCase("MIN")) {
            baseSql = new StringBuilder("SELECT MIN(value) as value FROM IOT_Message WHERE 1 = 1");
            return executeQuery(baseSql.append(additionalSql).toString(), "value");
        } else if (query.equalsIgnoreCase("MAX")) {
            baseSql = new StringBuilder("SELECT MAX(value) as value FROM IOT_Message WHERE 1 = 1");
            return executeQuery(baseSql.append(additionalSql).toString(), "value");
        } else if (query.equalsIgnoreCase("MEDIAN")) {
            baseSql = new StringBuilder("SELECT percentile_disc(0.5) within group (order by IOT_Message.value) as value from IOT_Message WHERE 1 = 1");
            return executeQuery(baseSql.append(additionalSql).toString(), "value");
        } else {
            return null;
        }
    }

    private IOTMessageDTO executeQuery(String sql, String returnField) {
        IOTMessageDTO dto = new IOTMessageDTO();

        dto.setValue(jdbcTemplate.queryForObject(sql, (resultSet, i) -> {
            return resultSet.getDouble(returnField);
        }));

        return dto;
    }

    private IOTMessageDTO executeQuery(String sql) {
        IOTMessageDTO dto = new IOTMessageDTO();
        List<IOTMessage> listOfMessages = new ArrayList<>();
        listOfMessages.addAll(jdbcTemplate.query(sql, (resultSet, i) -> {
            Integer deviceId = resultSet.getInt("deviceId");
            Integer deviceTypeId = resultSet.getInt("deviceTypeId");
            Integer groupId = resultSet.getInt("groupId");
            Double value = resultSet.getDouble("value");
            Date timestamp = resultSet.getTimestamp("timestamp");

            return new IOTMessage(deviceId, deviceTypeId, groupId, value, timestamp);
        }));
        dto.setMessages(listOfMessages);
        return dto;
    }
}
