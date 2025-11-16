package com.station_service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;
    private boolean active;
}
