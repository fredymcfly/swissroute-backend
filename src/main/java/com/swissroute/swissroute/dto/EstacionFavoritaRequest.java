package com.swissroute.swissroute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EstacionFavoritaRequest {

    // Getters y setters
    @NotBlank(message = "El ID externo de la estación es obligatorio")
    @Size(max = 100, message = "El ID externo de la estación no puede exceder 100 caracteres")
    private String estacionIdExterno;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
}