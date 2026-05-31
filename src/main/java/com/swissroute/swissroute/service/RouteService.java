package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;

import java.util.List;

public interface RouteService {

    FavoriteRouteDTO saveFavoriteRoute(FavoriteRouteDTO request, Long userId);
    FavoriteRouteDTO updateRoute(Long id, FavoriteRouteDTO request, Long userId);
    void deleteRoute(Long routeId, Long userId);
    List<FavoriteRouteDTO> getAllRoutes(Long userId);
}
