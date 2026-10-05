package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "manuales_tecnicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManualTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idManual;

    @NotBlank(message = "El tipo de manual es obligatorio")
    private String tipoManual; // AMM / CMM / SRM / IPC / AD / SB / Otro

    @NotBlank(message = "El fabricante es obligatorio")
    private String fabricante;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    private String numeroAta;

    @NotBlank(message = "El idioma es obligatorio")
    private String idioma;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    private String fuente;

    private LocalDate fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_registrado_por", nullable = false)
    private Usuarios registradoPor;

    @ManyToOne
    @JoinColumn(name = "id_tarea")
    private TareaMantenimiento tarea; // opcional: si se agregó durante la ejecución de una tarea
}