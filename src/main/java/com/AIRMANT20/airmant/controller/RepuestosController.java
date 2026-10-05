package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Repuestos;
import com.AIRMANT20.airmant.repository.RepuestosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestosController {

    @Autowired
    private RepuestosRepository repuestosRepository;

    @GetMapping
    public List<Repuestos> listar() {
        return repuestosRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repuestos> obtenerPorId(@PathVariable Long id) {
        return repuestosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Repuestos crear(@RequestBody Repuestos repuesto) {
        return repuestosRepository.save(repuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repuestos> actualizar(@PathVariable Long id, @RequestBody Repuestos datosNuevos) {
        return repuestosRepository.findById(id)
                .map(repuestoExistente -> {
                    repuestoExistente.setCodigo(datosNuevos.getCodigo());
                    repuestoExistente.setDescripcion(datosNuevos.getDescripcion());
                    repuestoExistente.setCategoria(datosNuevos.getCategoria());
                    repuestoExistente.setCantidadStock(datosNuevos.getCantidadStock());
                    repuestoExistente.setUnidadMedida(datosNuevos.getUnidadMedida());
                    repuestoExistente.setProveedor(datosNuevos.getProveedor());
                    repuestoExistente.setEstado(datosNuevos.getEstado());
                    return ResponseEntity.ok(repuestosRepository.save(repuestoExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repuestosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repuestosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}