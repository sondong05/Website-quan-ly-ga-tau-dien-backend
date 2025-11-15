package com.schedule_service.service;

import com.schedule_service.dto.ScheduleRequest;
import com.schedule_service.dto.ScheduleResponse;
import com.schedule_service.model.Schedule;
import com.schedule_service.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repo;

    public List<ScheduleResponse> getAllSchedules() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ScheduleResponse getSchedule(Long id) {
        Schedule s = repo.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        return mapToResponse(s);
    }

    public ScheduleResponse createSchedule( ScheduleRequest req) {
        Schedule s = Schedule.builder()
                .trainId(req.getTrainId())
                .departureStation(req.getDepartureStation())
                .arrivalStation(req.getArrivalStation())
                .departureTime(req.getDepartureTime())
                .arrivalTime(req.getArrivalTime())
                .status(req.getStatus() == null ? "ACTIVE" : req.getStatus())
                .totalSeats(req.getTotalSeats())
                .bookedSeats(0)
                .build();
        return mapToResponse(repo.save(s));
    }

    public ScheduleResponse updateSchedule(Long id, ScheduleRequest req) {
        Schedule s = repo.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        s.setTrainId(req.getTrainId());
        s.setDepartureStation(req.getDepartureStation());
        s.setArrivalStation(req.getArrivalStation());
        s.setDepartureTime(req.getDepartureTime());
        s.setArrivalTime(req.getArrivalTime());
        s.setStatus(req.getStatus());
        s.setTotalSeats(req.getTotalSeats());
        return mapToResponse(repo.save(s));
    }

    public ScheduleResponse updateStatus(Long id, String status) {
        Schedule s = repo.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        s.setStatus(status);
        return mapToResponse(repo.save(s));
    }

    public ScheduleResponse reserveSeat(Long id, int seatsToBook) {
        Schedule s = repo.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (s.getBookedSeats() + seatsToBook > s.getTotalSeats()) {
            throw new RuntimeException("Not enough available seats");
        }

        s.setBookedSeats(s.getBookedSeats() + seatsToBook);
        return mapToResponse(repo.save(s));
    }

    private ScheduleResponse mapToResponse(Schedule s) {
        return ScheduleResponse.builder()
                .id(s.getId())
                .trainId(s.getTrainId())
                .departureStation(s.getDepartureStation())
                .arrivalStation(s.getArrivalStation())
                .departureTime(s.getDepartureTime())
                .arrivalTime(s.getArrivalTime())
                .status(s.getStatus())
                .totalSeats(s.getTotalSeats())
                .bookedSeats(s.getBookedSeats())
                .build();
    }
}