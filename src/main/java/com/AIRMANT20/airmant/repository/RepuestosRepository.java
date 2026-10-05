package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.Repuestos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepuestosRepository extends JpaRepository<Repuestos, Long> {
    Repuestos findByCodigo(String codigo);
}