package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.EstacionFavoritaRequest;
import com.swissroute.swissroute.dto.EstacionFavoritaResponse;
import com.swissroute.swissroute.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EstacionFavoritaService {
    
    /**
     * Crea una nueva estación favorita para el usuario
     * @param request datos de la estación
     * @return la estación creada
     */
    EstacionFavoritaResponse crearEstacionFavorita(EstacionFavoritaRequest request);
    
    /**
     * Obtiene todas las estaciones favoritas del usuario
     * @return lista de estaciones favoritas
     */
    List<EstacionFavoritaResponse> obtenerEstacionesFavoritas();
    
    /**
     * Elimina una estación favorita
     * @param id ID de la estación
     */
    void eliminarEstacionFavorita(Long id);
}