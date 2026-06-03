package com.swissroute.swissroute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estaciones_favoritas")
public class EstacionFavorita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "estacion_id_externo", nullable = false, length = 100)
    private String estacionIdExterno;

    @Column(name = "nombre_estacion", nullable = false, length = 150)
    private String nombreEstacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, insertable = false, updatable = false)
    private Usuario usuario;

    // Pre-persist timestamp
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}