package com.schedule_service.controller;

import com.schedule_service.dto.ScheduleRequest;
import com.schedule_service.dto.ScheduleResponse;
import com.schedule_service.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    private void checkRole(String role, boolean allowDelete) {
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("MANAGER") && !role.equalsIgnoreCase("OPERATOR")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        if (allowDelete && !role.equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can delete");
        }
    }

    @GetMapping
    public List<ScheduleResponse> getAll(@RequestHeader("X-User-Role") String role) {
        checkRole(role, false);
        return service.getAllSchedules();
    }

    @GetMapping("/{id}")
    public ScheduleResponse getOne(@RequestHeader("X-User-Role") String role,
                                   @PathVariable Long id) {
        checkRole(role, false);
        return service.getSchedule(id);
    }

    @PostMapping
    public ScheduleResponse create(@RequestHeader("X-User-Role") String role,
                                   @RequestBody ScheduleRequest req) {
        checkRole(role, false);
        return service.createSchedule(req);
    }

    @PutMapping("/{id}")
    public ScheduleResponse update(@RequestHeader("X-User-Role") String role,
                                   @PathVariable Long id,
                                   @RequestBody ScheduleRequest req) {
        checkRole(role, false);
        return service.updateSchedule(id, req);
    }

    @PatchMapping("/{id}/status")
    public ScheduleResponse updateStatus(@RequestHeader("X-User-Role") String role,
                                         @PathVariable Long id,
                                         @RequestBody String status) {
        checkRole(role, false);
        return service.updateStatus(id, status);
    }

    @PatchMapping("/{id}/reserve")
    public ScheduleResponse reserve(@RequestHeader("X-User-Role") String role,
                                    @RequestHeader("X-User-Id") String userId,
                                    @PathVariable Long id,
                                    @RequestParam int seats) {
        if (!role.equalsIgnoreCase("OPERATOR") && !role.equalsIgnoreCase("MANAGER") && !role.equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return service.reserveSeat(id, seats);
    }
}
