package com.schedule_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long trainId;

    private String departureStation;

    private String arrivalStation;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String status; // ACTIVE, CANCELLED, DELAYED

    private int totalSeats;

    private int bookedSeats;
}

