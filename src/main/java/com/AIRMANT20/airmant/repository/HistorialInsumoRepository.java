package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.HistorialInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialInsumoRepository extends JpaRepository<HistorialInsumo, Long> {
    List<HistorialInsumo> findByIdInsumoOrderByFechaDesc(Long idInsumo);
    List<HistorialInsumo> findAllByOrderByFechaDesc(); // historial general, útil para auditoría global
}
