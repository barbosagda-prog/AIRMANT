package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.repository.AeronavesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aeronaves")
public class AeronavesController {

    @Autowired
    private AeronavesRepository aeronavesRepository;

    @GetMapping
    public List<Aeronaves> listar() {
        return aeronavesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeronaves> obtenerPorId(@PathVariable Long id) {
        return aeronavesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aeronaves crear(@RequestBody Aeronaves aeronave) {
        return aeronavesRepository.save(aeronave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aeronaves> actualizar(@PathVariable Long id, @RequestBody Aeronaves datosNuevos) {
        return aeronavesRepository.findById(id)
                .map(aeronaveExistente -> {
                    aeronaveExistente.setMatricula(datosNuevos.getMatricula());
                    aeronaveExistente.setNumeroSerie(datosNuevos.getNumeroSerie());
                    aeronaveExistente.setFabricante(datosNuevos.getFabricante());
                    aeronaveExistente.setModeloFabricacion(datosNuevos.getModeloFabricacion());
                    aeronaveExistente.setTipoAeronave(datosNuevos.getTipoAeronave());
                    aeronaveExistente.setCategoria(datosNuevos.getCategoria());
                    aeronaveExistente.setTipoMotor(datosNuevos.getTipoMotor());
                    aeronaveExistente.setAnioFabricacion(datosNuevos.getAnioFabricacion());
                    aeronaveExistente.setEstado(datosNuevos.getEstado());
                    aeronaveExistente.setHorasVuelo(datosNuevos.getHorasVuelo());
                    aeronaveExistente.setProximaInspeccion(datosNuevos.getProximaInspeccion());
                    return ResponseEntity.ok(aeronavesRepository.save(aeronaveExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!aeronavesRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        aeronavesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}