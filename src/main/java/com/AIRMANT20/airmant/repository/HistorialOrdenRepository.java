package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.HistorialOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialOrdenRepository extends JpaRepository<HistorialOrden, Long> {
    List<HistorialOrden> findByIdOrdenOrderByFechaDesc(Long idOrden);
}