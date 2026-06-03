package com.swissroute.swissroute.repository;

import com.swissroute.swissroute.entity.RutaFavorita;
import com.swissroute.swissroute.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutaFavoritaRepository extends JpaRepository<RutaFavorita, Long> {
    
    /**
     * Busca una ruta por usuario y nombre para validar unicidad
     */
    Optional<RutaFavorita> findByUsuarioIdAndNombre(Long usuarioId, String nombre);
    
    /**
     * Lista todas las rutas de un usuario ordenadas por fecha de creacion
     */
    List<RutaFavorita> findAllByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
    
    /**
     * Pagina las rutas de un usuario
     */
    Page<RutaFavorita> findByUsuarioOrderByCreatedAtDesc(Usuario usuario, Pageable pageable);
    
    /**
     * Verifica si existe una ruta con el mismo nombre para un usuario
     */
    boolean existsByUsuarioAndNombre(Usuario usuario, String nombre);
    
    /**
     * Borra todas las rutas de un usuario
     */
    void deleteByUsuario(Usuario usuario);
}
