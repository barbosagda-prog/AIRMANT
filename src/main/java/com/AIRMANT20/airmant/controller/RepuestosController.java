package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Repuestos;
import com.AIRMANT20.airmant.service.RepuestosService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestosController {

    @Autowired
    private RepuestosService repuestosService;

    @GetMapping
    public List<Repuestos> listar() {
        return repuestosService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Repuestos> obtenerPorId(@PathVariable Long id) {
        return repuestosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Repuestos crear(@RequestBody Repuestos repuesto) {
        return repuestosService.crear(repuesto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Repuestos> actualizar(@PathVariable Long id, @RequestBody Repuestos datosNuevos) {
        ResultadoOperacion<Repuestos> resultado = repuestosService.actualizar(id, datosNuevos);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.ok(resultado.getDatos())
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ResultadoOperacion<Void> resultado = repuestosService.eliminar(id);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}