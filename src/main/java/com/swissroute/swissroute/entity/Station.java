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
@Table(name = "estaciones_favoritas")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_estacion", nullable = false, length = 100)
    private String name;

    @Column(name = "estacion_id_externo")
    private Long stationApiId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "usuario_id")
    private Long userId;

    @Column(name = "deleted")
    private boolean deleted = false;
}