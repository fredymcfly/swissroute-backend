package com.swissroute.swissroute.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RutaFavoritaDTO {

    // Getters y setters
    private Long id;
    private String nombre;
    private String origen;
    private String destino;
    private String tipoTransporte;
    private LocalDateTime createdAt;
    


}