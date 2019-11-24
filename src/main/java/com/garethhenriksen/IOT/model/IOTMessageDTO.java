package com.garethhenriksen.IOT.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IOTMessageDTO {
    Double value;
    List<IOTMessage> messages;

}
