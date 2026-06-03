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
public class RutaFavoritaRequest {

    // Getters y setters
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 100, message = "El origen no puede exceder 100 caracteres")
    private String origen;
    
    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 100, message = "El destino no puede exceder 100 caracteres")
    private String destino;
    
    @Size(max = 100, message = "El tipo de transporte no puede exceder 100 caracteres")
    private String tipoTransporte;

}