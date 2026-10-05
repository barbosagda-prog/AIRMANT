package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.TareaMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaMantenimientoRepository extends JpaRepository<TareaMantenimiento, Long> {
    List<TareaMantenimiento> findByOrden_IdOrden(Long idOrden);
}