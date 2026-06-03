package com.swissroute.swissroute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EstacionFavoritaResponse {

    private Long id;
    
    private String estacionIdExterno;
    
    private String nombre;
    
    private LocalDateTime createdAt;
}