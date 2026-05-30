package com.swissroute.swissroute.mapper;

import com.swissroute.swissroute.dto.StationBoardDTO;
import com.swissroute.swissroute.dto.external.ExternalStationBoard;

public class StationBoardMapper {

    public static StationBoardDTO fromExternal(ExternalStationBoard s) {
        return new StationBoardDTO(
                s.name(),
                s.category(),
                s.to(),
                s.stop().departure()
        );
    }
}
