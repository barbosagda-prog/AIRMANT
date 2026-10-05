package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "repuestos_utilizados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepuestoUtilizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepuestoUtilizado;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenMantenimiento orden;

    @ManyToOne
    @JoinColumn(name = "id_repuesto", nullable = false)
    private Repuestos repuesto;

    @NotNull(message = "La cantidad utilizada es obligatoria")
    private Integer cantidadUtilizada;

    private String numeroSerie; // opcional, si aplica

    private String estadoComponente; // Nuevo / Reparado / Usado
}