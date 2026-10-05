package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.DocumentacionTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentacionTecnicaRepository extends JpaRepository<DocumentacionTecnica, Long> {
    List<DocumentacionTecnica> findByOrden_IdOrden(Long idOrden);
}