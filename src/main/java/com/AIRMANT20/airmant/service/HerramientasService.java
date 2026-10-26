package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.Herramientas;
import com.AIRMANT20.airmant.repository.HerramientasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HerramientasService {

    @Autowired private HerramientasRepository herramientasRepository;

    public List<Herramientas> listar() {
        return herramientasRepository.findAll();
    }

    public Optional<Herramientas> obtenerPorId(Long id) {
        return herramientasRepository.findById(id);
    }

    public Herramientas crear(Herramientas herramienta) {
        return herramientasRepository.save(herramienta);
    }

    public ResultadoOperacion<Herramientas> actualizar(Long id, Herramientas datosNuevos) {
        Optional<Herramientas> existente = herramientasRepository.findById(id);
        if (existente.isEmpty()) return ResultadoOperacion.noEncontrado();

        Herramientas herramienta = existente.get();
        herramienta.setCodigo(datosNuevos.getCodigo());
        herramienta.setNombre(datosNuevos.getNombre());
        herramienta.setEstadoCalibracion(datosNuevos.getEstadoCalibracion());
        herramienta.setFechaVencimientoCalibracion(datosNuevos.getFechaVencimientoCalibracion());
        herramienta.setEstado(datosNuevos.getEstado());
        herramienta.setUbicacion(datosNuevos.getUbicacion());

        return ResultadoOperacion.exito(herramientasRepository.save(herramienta));
    }

    public ResultadoOperacion<Void> eliminar(Long id) {
        if (!herramientasRepository.existsById(id)) return ResultadoOperacion.noEncontrado();
        herramientasRepository.deleteById(id);
        return ResultadoOperacion.exito(null);
    }
}