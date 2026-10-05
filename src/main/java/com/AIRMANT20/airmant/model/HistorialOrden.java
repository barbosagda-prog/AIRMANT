package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    private Long idOrden;

    private String numeroOrdenSnapshot;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    private String accion; // Creada / Estado cambiado / Finalizada

    private LocalDateTime fecha;

    private String detalle;
}