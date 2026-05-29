package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    boolean existsByUserandStationId (Long userId, Long stationId);

    //Optional<Station> getStationByUserIdAndStationI(Long userId, Long stationId);

    List<Station> getFavStationByUserId(Long userId);

}
