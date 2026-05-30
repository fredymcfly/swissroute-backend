package com.swissroute.swissroute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historial_busquedas")
public class HistorialBusqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origen;

    private String destino;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "num_resultados")
    private Integer numResultados;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    public HistorialBusqueda(String origen, String destino, LocalDateTime fechaConsulta, Integer numResultados, Usuario usuario) {
        this.origen = origen;
        this.destino = destino;
        this.fechaConsulta = fechaConsulta;
        this.numResultados = numResultados;
        this.usuario = usuario;
    }

}