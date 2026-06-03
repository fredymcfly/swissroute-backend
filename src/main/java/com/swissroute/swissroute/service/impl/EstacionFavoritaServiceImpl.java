package com.swissroute.swissroute.service.impl;

import com.swissroute.swissroute.dto.EstacionFavoritaRequest;
import com.swissroute.swissroute.dto.EstacionFavoritaResponse;
import com.swissroute.swissroute.entity.EstacionFavorita;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.EstacionNoEncontradaException;
import com.swissroute.swissroute.exception.EstacionYaExisteException;
import com.swissroute.swissroute.repository.EstacionFavoritaRepository;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.EstacionFavoritaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstacionFavoritaServiceImpl implements EstacionFavoritaService {

    private final EstacionFavoritaRepository estacionFavoritaRepository;
    private final UsuarioRepository usuarioRepository;

    public EstacionFavoritaServiceImpl(EstacionFavoritaRepository estacionFavoritaRepository, 
                                     UsuarioRepository usuarioRepository) {
        this.estacionFavoritaRepository = estacionFavoritaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public EstacionFavoritaResponse crearEstacionFavorita(EstacionFavoritaRequest request) {
        // Obtener el usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EstacionNoEncontradaException("Usuario no encontrado"));

        // Validar que la estación no exista ya para este usuario
        if (estacionFavoritaRepository.existsByUsuarioIdAndEstacionIdExterno(usuario.getId(), request.getEstacionIdExterno())) {
            throw new EstacionYaExisteException(request.getEstacionIdExterno());
        }

        // Crear la nueva estación favorita
        EstacionFavorita estacion = new EstacionFavorita();
        estacion.setEstacionIdExterno(request.getEstacionIdExterno());
        estacion.setNombreEstacion(request.getNombre());
        estacion.setUsuarioId(usuario.getId());
        estacion.setUsuario(usuario);

        EstacionFavorita saved = estacionFavoritaRepository.save(estacion);
        return toDTO(saved);
    }

    @Override
    public List<EstacionFavoritaResponse> obtenerEstacionesFavoritas() {
        // Obtener el usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EstacionNoEncontradaException("Usuario no encontrado"));

        // Obtener y retornar las estaciones favoritas ordenadas por fecha de creación descendente
        return estacionFavoritaRepository.findByUsuarioIdOrderByCreatedAtDesc(usuario.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarEstacionFavorita(Long id) {
        // Obtener el usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EstacionNoEncontradaException("Usuario no encontrado"));

        // Buscar la estación favorita
        EstacionFavorita estacion = estacionFavoritaRepository.findById(id)
                .orElseThrow(() -> new EstacionNoEncontradaException(id));

        // Verificar que la estación pertenece al usuario
        if (!estacion.getUsuario().getId().equals(usuario.getId())) {
            throw new EstacionNoEncontradaException("No tienes permisos para eliminar esta estación favorita");
        }

        // Eliminar la estación favorita
        estacionFavoritaRepository.delete(estacion);
    }

    /**
     * Mapea Entity a DTO
     */
    private EstacionFavoritaResponse toDTO(EstacionFavorita estacion) {
        EstacionFavoritaResponse dto = new EstacionFavoritaResponse();
        dto.setId(estacion.getId());
        dto.setEstacionIdExterno(estacion.getEstacionIdExterno());
        dto.setNombre(estacion.getNombreEstacion());
        dto.setCreatedAt(estacion.getCreatedAt());
        return dto;
    }
}