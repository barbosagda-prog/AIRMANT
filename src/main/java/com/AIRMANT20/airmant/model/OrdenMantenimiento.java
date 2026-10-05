package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ordenes_mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    private String numeroOrden;

    @NotNull(message = "La fecha de creación es obligatoria")
    private LocalDate fechaCreacion;

    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    private String tipoMantenimiento; // Preventivo / Correctivo / Inspección

    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad; // Baja / Media / Alta / AOG

    @NotBlank(message = "El estado es obligatorio")
    private String estado; // Abierta / En proceso / En espera / Finalizada / Cancelada

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @Column(length = 2000)
    private String descripcionTrabajo;

    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;

    @Column(length = 2000)
    private String trabajoRealizado;

    private String resultado;

    @Column(length = 1000)
    private String observaciones;

    private LocalDate fechaCierre;

    @ManyToOne
    @JoinColumn(name = "id_aeronave", nullable = false)
    private Aeronaves aeronave;

    @ManyToOne
    @JoinColumn(name = "id_tecnico_responsable", nullable = false)
    private Usuarios tecnicoResponsable;

    @ManyToOne
    @JoinColumn(name = "id_inspector")
    private Usuarios inspector;
}