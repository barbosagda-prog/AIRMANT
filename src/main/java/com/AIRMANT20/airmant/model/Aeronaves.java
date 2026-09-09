package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "aeronaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aeronaves {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAeronave;

    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotBlank(message = "El número de serie es obligatorio")
    private String numeroSerie;

    @NotBlank(message = "El fabricante es obligatorio")
    private String fabricante;

    @NotBlank(message = "El modelo de fabricación es obligatorio")
    private String modeloFabricacion;

    @NotBlank(message = "El tipo de aeronave es obligatorio")
    private String tipoAeronave;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "El tipo de motor es obligatorio")
    private String tipoMotor;

    @NotNull(message = "El número de motores es obligatorio")
    private Integer numeroMotor;

    @NotNull(message = "El peso máximo de despegue es obligatorio")
    private Double pesoMaximoDespegue;

    @NotNull(message = "La capacidad es obligatoria")
    private Integer capacidad;

    @NotBlank(message = "La configuración de cabina es obligatoria")
    private String configuracionCabina;

    @NotNull(message = "El año de fabricación es obligatorio")
    private Integer anioFabricacion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "Las horas de vuelo son obligatorias")
    private Double horasVuelo;

    private LocalDate proximaInspeccion;
}