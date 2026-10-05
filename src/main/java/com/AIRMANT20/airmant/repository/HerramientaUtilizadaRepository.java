package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.HerramientaUtilizada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HerramientaUtilizadaRepository extends JpaRepository<HerramientaUtilizada, Long> {
    List<HerramientaUtilizada> findByOrden_IdOrden(Long idOrden);
}
