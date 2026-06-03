package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.RutaFavoritaDTO;
import com.swissroute.swissroute.dto.RutaFavoritaRequest;
import com.swissroute.swissroute.entity.RutaFavorita;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.RutaNoEncontradaException;
import com.swissroute.swissroute.exception.RutaYaExisteException;
import com.swissroute.swissroute.repository.RutaFavoritaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RutaFavoritaService {

    private final RutaFavoritaRepository rutaFavoritaRepository;

    public RutaFavoritaService(RutaFavoritaRepository rutaFavoritaRepository) {
        this.rutaFavoritaRepository = rutaFavoritaRepository;
    }

    /**
     * Crea una nueva ruta favorita para el usuario
     * @param request datos de la ruta
     * @param usuario usuario autenticado
     * @return la ruta creada
     */
    public RutaFavoritaDTO crearRuta(RutaFavoritaRequest request, Usuario usuario) {
        // Validar que el nombre sea único para este usuario
        if (rutaFavoritaRepository.existsByUsuarioAndNombre(usuario, request.getNombre())) {
            throw new RutaYaExisteException(request.getNombre());
        }

        RutaFavorita ruta = new RutaFavorita(
                request.getNombre(),
                request.getOrigen(),
                request.getDestino(),
                request.getTipoTransporte(),
                usuario
        );

        RutaFavorita saved = rutaFavoritaRepository.save(ruta);
        return toDTO(saved);
    }

    /**
     * Obtiene todas las rutas favoritas de un usuario paginadas
     * @param usuario usuario autenticado
     * @param pageable paginación
     * @return página de rutas
     */
    public Page<RutaFavoritaDTO> obtenerRutas(Usuario usuario, Pageable pageable) {
        return rutaFavoritaRepository.findByUsuarioOrderByCreatedAtDesc(usuario, pageable)
                .map(this::toDTO);
    }

    /**
     * Obtiene todas las rutas favoritas sin paginación
     * @param usuario usuario autenticado
     * @return lista de rutas
     */
    public List<RutaFavoritaDTO> obtenerTodasRutas(Usuario usuario) {
        return rutaFavoritaRepository.findAllByUsuarioIdOrderByCreatedAtDesc(usuario.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Obtiene una ruta por ID
     * @param id ID de la ruta
     * @param usuario usuario autenticado
     * @return la ruta o null si no existe
     */
    public RutaFavoritaDTO obtenerRutaPorId(Long id, Usuario usuario) {
        RutaFavorita ruta = rutaFavoritaRepository.findById(id)
                .orElseThrow(() -> new RutaNoEncontradaException(id));
        
        // Verificar que pertenezca al usuario
        if (!ruta.getUsuario().getId().equals(usuario.getId())) {
            throw new RutaNoEncontradaException(id);
        }
        
        return toDTO(ruta);
    }

    /**
     * Actualiza una ruta favorita
     * @param id ID de la ruta
     * @param request nuevos datos
     * @param usuario usuario autenticado
     * @return la ruta actualizada
     */
    public RutaFavoritaDTO actualizarRuta(Long id, RutaFavoritaRequest request, Usuario usuario) {
        RutaFavorita ruta = rutaFavoritaRepository.findById(id)
                .orElseThrow(() -> new RutaNoEncontradaException(id));
        
        // Verificar que pertenezca al usuario
        if (!ruta.getUsuario().getId().equals(usuario.getId())) {
            throw new RutaNoEncontradaException(id);
        }

        // Validar que el nombre sea único (si cambió el nombre)
        if (!ruta.getNombre().equals(request.getNombre()) && 
            rutaFavoritaRepository.existsByUsuarioAndNombre(usuario, request.getNombre())) {
            throw new RutaYaExisteException(request.getNombre());
        }

        ruta.setNombre(request.getNombre());
        ruta.setOrigen(request.getOrigen());
        ruta.setDestino(request.getDestino());
        ruta.setTipoTransporte(request.getTipoTransporte());

        RutaFavorita saved = rutaFavoritaRepository.save(ruta);
        return toDTO(saved);
    }

    /**
     * Elimina una ruta favorita
     * @param id ID de la ruta
     * @param usuario usuario autenticado
     */
    public void eliminarRuta(Long id, Usuario usuario) {
        RutaFavorita ruta = rutaFavoritaRepository.findById(id)
                .orElseThrow(() -> new RutaNoEncontradaException(id));
        
        // Verificar que pertenezca al usuario
        if (!ruta.getUsuario().getId().equals(usuario.getId())) {
            throw new RutaNoEncontradaException(id);
        }
        
        rutaFavoritaRepository.delete(ruta);
    }

    /**
     * Elimina todas las rutas de un usuario
     * @param usuario usuario autenticado
     */
    public void eliminarTodasRutas(Usuario usuario) {
        rutaFavoritaRepository.deleteByUsuario(usuario);
    }

    /**
     * Mapea Entity a DTO
     */
    private RutaFavoritaDTO toDTO(RutaFavorita ruta) {
        RutaFavoritaDTO dto = new RutaFavoritaDTO();
        dto.setId(ruta.getId());
        dto.setNombre(ruta.getNombre());
        dto.setOrigen(ruta.getOrigen());
        dto.setDestino(ruta.getDestino());
        dto.setTipoTransporte(ruta.getTipoTransporte());
        dto.setCreatedAt(ruta.getCreatedAt());
        return dto;
    }
}
