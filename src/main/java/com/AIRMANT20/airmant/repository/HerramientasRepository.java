package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.Herramientas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HerramientasRepository extends JpaRepository<Herramientas, Long> {
    Herramientas findByCodigo(String codigo);
}