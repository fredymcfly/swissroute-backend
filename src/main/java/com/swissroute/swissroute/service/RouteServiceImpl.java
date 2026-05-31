package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.entity.FavoriteRoute;
import com.swissroute.swissroute.exception.RutaYaExisteException;
import com.swissroute.swissroute.mapper.RouteMapper;
import com.swissroute.swissroute.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService{

    private final TransportService transportService;
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public RouteServiceImpl(WebClient webClient, TransportService transportService, RouteRepository routeRepository, RouteMapper routeMapper) {
        this.transportService = transportService;
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public FavoriteRouteDTO saveFavoriteRoute(FavoriteRouteDTO request, Long userId) {

        if (routeRepository.existsByUsuarioIdAndNombre(userId, request.name())) {
            throw new RutaYaExisteException("Ya existe una ruta con ese nombre para este usuario");
        }

        //valida simplemente que haya ruta
        List<ConnectionDTO> getConnections = transportService.getConnections(request.from(), request.to(), null, null, request.transport());
        if (getConnections == null || getConnections.isEmpty()) {
            throw new RuntimeException("No se encontró ninguna ruta válida en la API externa");
        }

        FavoriteRoute route = routeMapper.toEntity(request, userId);
        routeRepository.save(route);
        return routeMapper.toDto(route);
    }

    public List<FavoriteRouteDTO> getAllRoutes(Long userId) {
        return routeRepository.findByUserId(userId)
                .stream()
                .map(routeMapper::toDto)
                .toList();
    }

    public FavoriteRouteDTO updateRoute(Long id, FavoriteRouteDTO request, Long userId) {

        FavoriteRoute route = routeRepository.findByRouteIdAndRouteId(id, userId)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));


        if (request.name() != null && !request.name().isBlank()) {
            // Validar duplicado si cambia el nombre
            if (routeRepository.existsByUsuarioIdAndNombre(userId, request.name())
                    && !route.getNombre().equals(request.name())) {
                throw new RutaYaExisteException("Ya existe una ruta con ese nombre para este usuario");
            }
            route.setNombre(request.name());
        }

        if (request.from() != null) route.setFrom(request.from());
        if (request.to() != null) route.setTo(request.to());
        if (request.transport() != null) route.setTransport(request.transport());
        routeRepository.save(route);
        return routeMapper.toDto(route);
    }

    public void deleteRoute(Long routeId, Long userId) {

        FavoriteRoute ruta = routeRepository.findByRouteIdAndRouteId(routeId, userId)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
        routeRepository.delete(ruta);
    }


}
