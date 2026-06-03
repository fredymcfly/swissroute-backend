package com.swissroute.swissroute.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String origen;

    @NotNull
    private String destino;

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "num_resultados")
    private Integer numResultados;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    public HistorialBusqueda(String origen, String destino, LocalDateTime fechaConsulta, Integer numResultados, Usuario usuario) {
        this.origen = origen;
        this.destino = destino;
        this.fechaConsulta = fechaConsulta;
        this.numResultados = numResultados;
        this.usuario = usuario;
    }

}