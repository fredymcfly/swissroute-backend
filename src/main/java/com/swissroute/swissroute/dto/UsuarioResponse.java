package com.swissroute.swissroute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    
	private Long id;
    private String nombre;
    private String email;
    private String ciudadBase;
    private LocalDateTime createdAt;
}