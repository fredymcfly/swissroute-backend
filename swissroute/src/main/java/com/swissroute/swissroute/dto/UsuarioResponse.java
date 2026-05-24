package com.swissroute.swissroute.dto;

import java.time.LocalDateTime;

public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private String ciudadBase;
    private LocalDateTime createdAt;

    // Constructors
    public UsuarioResponse() {}

    public UsuarioResponse(Long id, String nombre, String email, String ciudadBase, LocalDateTime createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.ciudadBase = ciudadBase;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCiudadBase() {
        return ciudadBase;
    }

    public void setCiudadBase(String ciudadBase) {
        this.ciudadBase = ciudadBase;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}