package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "documentacion_tecnica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentacionTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocumento;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenMantenimiento orden;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento; // AMM / CMM / SRM / IPC / AD / SB / Otro

    @NotBlank(message = "El código o referencia es obligatorio")
    private String codigoReferencia;

    private String numeroRevision;

    private String capituloSeccion;
}