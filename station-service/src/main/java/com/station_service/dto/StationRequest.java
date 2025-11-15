package com.station_service.dto;

import lombok.Data;

@Data
public class StationRequest {
    private String name;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
}
