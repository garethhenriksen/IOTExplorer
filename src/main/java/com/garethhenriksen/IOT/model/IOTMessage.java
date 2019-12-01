package com.garethhenriksen.IOT.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class IOTMessage {

    public Integer deviceId;
    public Integer deviceTypeId;
    public Integer groupId;
    public Double value;
    public Date timestamp;
    public Double difference;

    public IOTMessage() {

    }
    public IOTMessage(@JsonProperty("deviceId") Integer deviceId,
                      @JsonProperty("deviceTypeId") Integer deviceTypeId,
                      @JsonProperty("groupId") Integer groupID,
                      @JsonProperty("value") Double value,
                      @JsonProperty("timestamp")  Date timestamp,
                      @JsonProperty("difference") Double difference) {
        this.deviceId = deviceId;
        this.deviceTypeId = deviceTypeId;
        this.groupId = groupID;
        this.value = value;
        this.timestamp = timestamp;
        if(difference == null) {
            this.difference = 0.0;
        } else {
            this.difference = difference;
        }
    }

    @Override
    public String toString() {
        return "deviceId[" + this.deviceId + "]" +
                "deviceTypeId[" + this.deviceTypeId + "]" +
                "groupId[" + this.groupId + "]" +
                "value[" + this.value + "]" +
                "timestamp[" + this.timestamp + "]" +
                "difference[" + this.difference + "]";
    }
}
