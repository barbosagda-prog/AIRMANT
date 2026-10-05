package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "herramientas_utilizadas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HerramientaUtilizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHerramientaUtilizada;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenMantenimiento orden;

    @ManyToOne
    @JoinColumn(name = "id_herramienta", nullable = false)
    private Herramientas herramienta;

    private LocalDate fechaUso;
}