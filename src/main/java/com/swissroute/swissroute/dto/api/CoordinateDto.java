package com.swissroute.swissroute.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateDto {

    private String type;
    private double x;
    private double y;
}
