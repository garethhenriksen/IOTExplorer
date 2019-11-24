package com.garethhenriksen.IOT.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
public class IOTMessage {

    private Integer deviceId;
    private Integer deviceTypeId;
    private Integer groupId;
    private Double value;
    private Date timestamp;

    public IOTMessage() {

    }

    public IOTMessage(@JsonProperty("deviceId") Integer deviceId,
                      @JsonProperty("deviceTypeId") Integer deviceTypeId,
                      @JsonProperty("groupId") Integer groupID,
                      @JsonProperty("value") Double value,
                      @JsonProperty("timestamp") Date timestamp) {
        this.deviceId = deviceId;
        this.deviceTypeId = deviceTypeId;
        this.groupId = groupID;
        this.value = value;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "deviceId[" + this.deviceId + "]" +
                "deviceTypeId[" + this.deviceTypeId + "]" +
                "groupId[" + this.groupId + "]" +
                "value[" + this.value + "]" +
                "timestamp[" + this.timestamp + "]";
    }
}
