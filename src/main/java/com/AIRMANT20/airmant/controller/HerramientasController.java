package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Herramientas;
import com.AIRMANT20.airmant.repository.HerramientasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
public class HerramientasController {

    @Autowired
    private HerramientasRepository herramientasRepository;

    @GetMapping
    public List<Herramientas> listar() {
        return herramientasRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Herramientas> obtenerPorId(@PathVariable Long id) {
        return herramientasRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Herramientas crear(@RequestBody Herramientas herramienta) {
        return herramientasRepository.save(herramienta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramientas> actualizar(@PathVariable Long id, @RequestBody Herramientas datosNuevos) {
        return herramientasRepository.findById(id)
                .map(herramientaExistente -> {
                    herramientaExistente.setCodigo(datosNuevos.getCodigo());
                    herramientaExistente.setNombre(datosNuevos.getNombre());
                    herramientaExistente.setEstadoCalibracion(datosNuevos.getEstadoCalibracion());
                    herramientaExistente.setFechaVencimientoCalibracion(datosNuevos.getFechaVencimientoCalibracion());
                    herramientaExistente.setEstado(datosNuevos.getEstado());
                    herramientaExistente.setUbicacion(datosNuevos.getUbicacion());
                    return ResponseEntity.ok(herramientasRepository.save(herramientaExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!herramientasRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        herramientasRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}