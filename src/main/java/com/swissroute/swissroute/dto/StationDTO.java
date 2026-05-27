package com.swissroute.swissroute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private String id;
    private String nombre;
    private double latitud;
    private double longitud;
}