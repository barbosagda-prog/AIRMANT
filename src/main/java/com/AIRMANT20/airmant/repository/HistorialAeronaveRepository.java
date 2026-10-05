package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.HistorialAeronave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialAeronaveRepository extends JpaRepository<HistorialAeronave, Long> {
    List<HistorialAeronave> findByIdAeronaveOrderByFechaDesc(Long idAeronave);
}