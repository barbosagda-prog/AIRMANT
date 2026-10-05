package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "repuestos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repuestos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepuesto;

    @NotBlank(message = "El código (P/N) es obligatorio")
    @Column(unique = true)
    private String codigo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull(message = "La cantidad en stock es obligatoria")
    private Integer cantidadStock;

    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unidadMedida;

    private String proveedor;

    @NotBlank(message = "El estado es obligatorio")
    private String estado; // Disponible / Agotado / Descontinuado
}