package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "insumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insumos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;

    @NotBlank(message = "El código es obligatorio")
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
    private String estado;

    private LocalDate fechaVencimiento; // opcional: no todos los insumos vencen
}