package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.HistorialManual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialManualRepository extends JpaRepository<HistorialManual, Long> {
    List<HistorialManual> findByManual_IdManualOrderByFechaDesc(Long idManual);
}