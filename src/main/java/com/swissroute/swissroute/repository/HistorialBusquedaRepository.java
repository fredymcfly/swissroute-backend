package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.HistorialBusqueda;
import com.swissroute.swissroute.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialBusquedaRepository extends JpaRepository<HistorialBusqueda, Long> {

    Page<HistorialBusqueda> findByUsuario(Usuario usuario, Pageable pageable);

    HistorialBusqueda findFirstByIdAndUsuarioId(Long id, Long usuarioId);

    void deleteById(Long id);

    void deleteByUsuario(Usuario usuario);
}