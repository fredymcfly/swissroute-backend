package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.EstacionFavorita;
import com.swissroute.swissroute.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstacionFavoritaRepository extends JpaRepository<EstacionFavorita, Long> {
    Optional<EstacionFavorita> findByUsuarioIdAndEstacionIdExterno(Long usuarioId, String estacionIdExterno);
    List<EstacionFavorita> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
    boolean existsByUsuarioIdAndEstacionIdExterno(Long usuarioId, String estacionIdExterno);
}