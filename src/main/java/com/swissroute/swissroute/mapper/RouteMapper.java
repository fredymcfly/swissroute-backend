package com.swissroute.swissroute.mapper;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.entity.FavoriteRoute;

import java.time.LocalDateTime;

public class RouteMapper {

    public FavoriteRouteDTO toDto(FavoriteRoute route) {
        return new FavoriteRouteDTO(
                route.getNombre(),
                route.getFrom(),
                route.getTo(),
                route.getTransport()
        );
    }

    public FavoriteRoute toEntity(FavoriteRouteDTO route, Long userId) {
        return new FavoriteRoute(
                userId,
                route.name(),
                route.from(),
                route.to(),
                route.transport(),
                LocalDateTime.now()
        );
    }
}
