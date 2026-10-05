package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "herramientas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Herramientas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHerramienta;

    @NotBlank(message = "El código es obligatorio")
    @Column(unique = true)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El estado de calibración es obligatorio")
    private String estadoCalibracion; // Vigente / Vencida / No requiere

    private LocalDate fechaVencimientoCalibracion;

    @NotBlank(message = "El estado general es obligatorio")
    private String estado; // Disponible / En uso / Fuera de servicio

    private String ubicacion;
}