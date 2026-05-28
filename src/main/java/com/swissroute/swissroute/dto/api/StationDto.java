package com.swissroute.swissroute.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {

    private Long id;
    private String name;
    /*
    private Integer score;
    private CoordinateDto coordinate;
    private Long distance;

    public StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

     */
}
