package com.train_service.controller;


import com.train_service.dto.TrainRequest;
import com.train_service.dto.TrainResponse;
import com.train_service.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    private void checkRole(String role, boolean allowDelete) {
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        if (allowDelete && !role.equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can delete");
        }
    }

    @GetMapping
    public List<TrainResponse> getAll(@RequestHeader("X-User-Role") String role) {
        checkRole(role, false);
        return trainService.getAllTrains();
    }

    @GetMapping("/{id}")
    public TrainResponse getOne(@RequestHeader("X-User-Role") String role,
                                @PathVariable Long id) {
        checkRole(role, false);
        return trainService.getTrain(id);
    }

    @PostMapping
    public TrainResponse create(@RequestHeader("X-User-Role") String role,
                                @RequestBody TrainRequest req) {
        checkRole(role, false);
        return trainService.createTrain(req);
    }

    @PutMapping("/{id}")
    public TrainResponse update(@RequestHeader("X-User-Role") String role,
                                @PathVariable Long id,
                                @RequestBody TrainRequest req) {
        checkRole(role, false);
        return trainService.updateTrain(id, req);
    }

    @DeleteMapping("/{id}")
    public String delete(@RequestHeader("X-User-Role") String role,
                         @PathVariable Long id) {
        checkRole(role, true);
        trainService.deleteTrain(id);
        return "Deleted successfully";
    }

    @PatchMapping("/{id}/status")
    public TrainResponse updateStatus(@RequestHeader("X-User-Role") String role,
                                      @PathVariable Long id,
                                      @RequestBody String status) {
        checkRole(role, false);
        return trainService.updateStatus(id, status);
    }
}
