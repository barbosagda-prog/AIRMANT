package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenMantenimientoRepository extends JpaRepository<OrdenMantenimiento, Long> {
    List<OrdenMantenimiento> findByEstado(String estado);
    List<OrdenMantenimiento> findByAeronave_Matricula(String matricula);
    OrdenMantenimiento findByNumeroOrden(String numeroOrden);
}