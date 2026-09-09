package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.Aeronaves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AeronavesRepository extends JpaRepository<Aeronaves, Long> {
    Aeronaves findByMatricula(String matricula);
}