package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.HistorialBusqueda;
import com.swissroute.swissroute.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialBusquedaRepository extends JpaRepository<HistorialBusqueda, Long> {

    Page<HistorialBusqueda> findByUsuario(Usuario usuario, Pageable pageable);

    boolean existsByIdAndUsuario(Long id, Usuario usuario);

    void deleteByIdAndUsuario(Long id, Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}