package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.HistorialBusquedaDTO;
import com.swissroute.swissroute.entity.HistorialBusqueda;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.HistorialBusquedaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialBusquedaService {

    private final HistorialBusquedaRepository historialBusquedaRepository;

    public HistorialBusquedaService(HistorialBusquedaRepository historialBusquedaRepository) {
        this.historialBusquedaRepository = historialBusquedaRepository;
    }

    public void guardarBusqueda(String origen, String destino, int numResultados, Usuario usuario) {
        HistorialBusqueda historial = new HistorialBusqueda(
                origen,
                destino,
                LocalDateTime.now(),
                numResultados,
                usuario
        );

        historialBusquedaRepository.save(historial);
    }

    public Page<HistorialBusquedaDTO> obtenerHistorial(Usuario usuario, Pageable pageable) {
        return historialBusquedaRepository.findByUsuario(usuario, pageable)
                .map(this::toDTO);
    }

    public void eliminarEntrada(Long id, Usuario usuario) {
        historialBusquedaRepository.deleteByIdAndUsuario(id, usuario);
    }

    public void eliminarTodo(Usuario usuario) {
        historialBusquedaRepository.deleteByUsuario(usuario);
    }

    private HistorialBusquedaDTO toDTO(HistorialBusqueda historial) {
        return new HistorialBusquedaDTO(
                historial.getId(),
                historial.getOrigen(),
                historial.getDestino(),
                historial.getFechaConsulta(),
                historial.getNumResultados()
        );
    }
}