package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<FavoriteRoute, Long> {

    boolean existsByUsuarioIdAndNombre(Long usuarioId, String nombre);
}
