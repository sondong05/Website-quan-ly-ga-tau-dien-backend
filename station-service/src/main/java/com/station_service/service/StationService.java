package com.station_service.service;

import com.station_service.dto.StationRequest;
import com.station_service.dto.StationResponse;
import com.station_service.exception.ResourceNotFoundException; // THÊM
import com.station_service.model.Station;
import com.station_service.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepo;

    // ... (getAllStations không đổi) ...
    public List<StationResponse> getAllStations(String city) {
        List<Station> stations;
        if (city != null && !city.isEmpty()) {
            stations = stationRepo.findByCityContainingIgnoreCase(city);
        } else {
            stations = stationRepo.findAll();
        }
        return stations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public StationResponse getStation(Long id) {
        // SỬA: Ném exception 404
        Station s = stationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
        return mapToResponse(s);
    }

    public StationResponse createStation(StationRequest req) {
        Station s = Station.builder()
                .name(req.getName())
                .city(req.getCity())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .active(true)
                .build();
        return mapToResponse(stationRepo.save(s));
    }

    public StationResponse updateStation(Long id, StationRequest req) {
        // SỬA: Ném exception 404
        Station s = stationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
        s.setName(req.getName());
        s.setCity(req.getCity());
        s.setAddress(req.getAddress());
        s.setLatitude(req.getLatitude());
        s.setLongitude(req.getLongitude());
        return mapToResponse(stationRepo.save(s));
    }

    public void deleteStation(Long id) {
        // SỬA: Thêm kiểm tra tồn tại
        if (!stationRepo.existsById(id)) {
            throw new ResourceNotFoundException("Station not found with id: " + id);
        }
        stationRepo.deleteById(id);
    }

    public StationResponse toggleStation(Long id) {
        // SỬA: Ném exception 404
        Station s = stationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + id));
        s.setActive(!s.isActive());
        return mapToResponse(stationRepo.save(s));
    }

    private StationResponse mapToResponse(Station s) {
        // ... (Không đổi) ...
        return StationResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .city(s.getCity())
                .address(s.getAddress())
                .latitude(s.getLatitude())
                .longitude(s.getLongitude())
                .active(s.isActive())
                .build();
    }
}