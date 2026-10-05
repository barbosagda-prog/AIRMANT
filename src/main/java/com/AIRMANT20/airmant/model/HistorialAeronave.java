package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_aeronaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialAeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    private Long idAeronave;

    private String matriculaAeronave; // copia, sobrevive aunque se elimine la aeronave

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    private String accion; // Creada / Modificada / Eliminada

    private LocalDateTime fecha;

    private String detalle;
}