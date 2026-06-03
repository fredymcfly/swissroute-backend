package com.swissroute.swissroute.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutas_favoritas")
public class RutaFavorita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    @Column(name = "origen", nullable = false, length = 100)
    private String origen;

    @NotBlank
    @Size(max = 100)
    @Column(name = "destino", nullable = false, length = 100)
    private String destino;

    @Size(max = 50)
    @Column(name = "tipo_transporte", length = 50)
    private String tipoTransporte;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructor para crear nueva ruta (sin ID)
    public RutaFavorita(String nombre, String origen, String destino, String tipoTransporte, Usuario usuario) {
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.tipoTransporte = tipoTransporte;
        this.usuario = usuario;
    }

    // Pre-persist timestamp
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
