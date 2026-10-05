package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.RepuestoUtilizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepuestoUtilizadoRepository extends JpaRepository<RepuestoUtilizado, Long> {
    List<RepuestoUtilizado> findByOrden_IdOrden(Long idOrden);
}
