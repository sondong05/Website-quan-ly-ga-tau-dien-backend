package com.station_service.controller;

import com.station_service.dto.StationRequest;
import com.station_service.dto.StationResponse;
import com.station_service.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // THÊM
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // THÊM

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    // THÊM: Hàm kiểm tra Role (giống TrainController)
    private void checkRole(String role, boolean allowDelete) {
        // Chỉ ADMIN và MANAGER được phép
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("MANAGER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        // Chỉ ADMIN được xóa
        if (allowDelete && !role.equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can delete");
        }
    }

    // Ai cũng có thể xem (GET) -> OK
    @GetMapping
    public List<StationResponse> getAll(@RequestParam(required = false) String city) {
        return stationService.getAllStations(city);
    }

    // Ai cũng có thể xem (GET) -> OK
    @GetMapping("/{id}")
    public StationResponse getOne(@PathVariable Long id) {
        return stationService.getStation(id);
    }

    // LỖI: Thiếu phân quyền
    // SỬA: Thêm @RequestHeader và checkRole
    @PostMapping
    public StationResponse create(@RequestHeader("X-User-Role") String role,
                                  @RequestBody StationRequest req) {
        checkRole(role, false);
        return stationService.createStation(req);
    }

    // LỖI: Thiếu phân quyền
    // SỬA: Thêm @RequestHeader và checkRole
    @PutMapping("/{id}")
    public StationResponse update(@RequestHeader("X-User-Role") String role,
                                  @PathVariable Long id,
                                  @RequestBody StationRequest req) {
        checkRole(role, false);
        return stationService.updateStation(id, req);
    }

    // LỖI: Thiếu phân quyền
    // SỬA: Thêm @RequestHeader và checkRole (true)
    @DeleteMapping("/{id}")
    public String delete(@RequestHeader("X-User-Role") String role,
                         @PathVariable Long id) {
        checkRole(role, true); // Yêu cầu quyền Admin
        stationService.deleteStation(id);
        return "Deleted successfully";
    }

    // LỖI: Thiếu phân quyền
    // SỬA: Thêm @RequestHeader và checkRole
    @PatchMapping("/{id}/toggle")
    public StationResponse toggle(@RequestHeader("X-User-Role") String role,
                                  @PathVariable Long id) {
        checkRole(role, false);
        return stationService.toggleStation(id);
    }
}