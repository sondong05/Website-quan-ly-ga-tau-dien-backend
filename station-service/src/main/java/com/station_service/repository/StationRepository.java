package com.station_service.repository;


import com.station_service.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findByCityContainingIgnoreCase(String city);
}