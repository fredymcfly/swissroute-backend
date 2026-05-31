package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.entity.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<FavoriteRoute, Long> {

    boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre);

    List<FavoriteRoute> findByUserId(Long userId);

    Optional<FavoriteRoute> findByRouteIdAndRouteId(Long routeId, Long userId);
}
