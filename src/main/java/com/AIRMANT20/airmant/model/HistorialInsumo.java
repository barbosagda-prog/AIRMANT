package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_insumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    private Long idInsumo;

    private String codigoInsumo; // copia del código, sobrevive aunque se elimine el insumo

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    private String accion; // Creado / Modificado / Eliminado

    private LocalDateTime fecha;

    private String detalle;
}