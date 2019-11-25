package com.garethhenriksen.IOT.model;

import lombok.*;

@Data
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusPosition {
    private String Id;
    private long timestamp;
    private Location location;
    private double milesPerHour;
}
