package com.station_service.controller;

import com.station_service.dto.StationRequest;
import com.station_service.dto.StationResponse;
import com.station_service.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public List<StationResponse> getAll(@RequestParam(required = false) String city) {
        return stationService.getAllStations(city);
    }

    @GetMapping("/{id}")
    public StationResponse getOne(@PathVariable Long id) {
        return stationService.getStation(id);
    }

    @PostMapping
    public StationResponse create(@RequestBody StationRequest req) {
        return stationService.createStation(req);
    }

    @PutMapping("/{id}")
    public StationResponse update(@PathVariable Long id, @RequestBody StationRequest req) {
        return stationService.updateStation(id, req);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        stationService.deleteStation(id);
        return "Deleted successfully";
    }

    @PatchMapping("/{id}/toggle")
    public StationResponse toggle(@PathVariable Long id) {
        return stationService.toggleStation(id);
    }
}
