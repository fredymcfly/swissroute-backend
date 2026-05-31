package com.swissroute.swissroute.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "rutas_favoritas")
public class FavoriteRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, length = 100)
    private Long userId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "origen", nullable = false, length = 100)
    private String from;

    @Column(name = "destino", nullable = false, length = 100)
    private String to;

    @Column(name = "tipo_transporte", nullable = false, length = 100)
    private String transport;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FavoriteRoute(Long userId, String name, String from, String s, String transport, LocalDateTime now) {
    }
}