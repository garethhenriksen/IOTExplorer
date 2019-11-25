package com.garethhenriksen.IOT.model;

import lombok.*;

import java.util.List;

@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
public class BusPositionsDTO {
    List<BusPosition> busPositionList;
}
