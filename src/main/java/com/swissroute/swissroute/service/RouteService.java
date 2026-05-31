package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;

public interface RouteService {

    FavoriteRouteDTO saveFavoriteRoute(FavoriteRouteDTO request, Long userId);
}
