package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.Insumos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumosRepository extends JpaRepository<Insumos, Long> {
    Insumos findByCodigo(String codigo);
}