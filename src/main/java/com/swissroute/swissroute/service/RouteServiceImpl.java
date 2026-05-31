package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.entity.FavoriteRoute;
import com.swissroute.swissroute.exception.RutaYaExisteException;
import com.swissroute.swissroute.mapper.RouteMapper;
import com.swissroute.swissroute.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
}
