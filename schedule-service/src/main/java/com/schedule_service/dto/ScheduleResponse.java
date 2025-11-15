package com.schedule_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleResponse {
    private Long id;
    private Long trainId;
    private String departureStation;
    private String arrivalStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String status;
    private int totalSeats;
    private int bookedSeats;
}
