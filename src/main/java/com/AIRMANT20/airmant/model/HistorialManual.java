package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_manuales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_manual", nullable = false)
    private ManualTecnico manual;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuario;

    private String accion; // Registrado / Modificado / Vinculado a tarea

    private LocalDateTime fecha;

    private String detalle;
}