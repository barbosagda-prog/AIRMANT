package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Herramientas;
import com.AIRMANT20.airmant.service.HerramientasService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
public class HerramientasController {

    @Autowired
    private HerramientasService herramientasService;

    @GetMapping
    public List<Herramientas> listar() {
        return herramientasService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Herramientas> obtenerPorId(@PathVariable Long id) {
        return herramientasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Herramientas crear(@RequestBody Herramientas herramienta) {
        return herramientasService.crear(herramienta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramientas> actualizar(@PathVariable Long id, @RequestBody Herramientas datosNuevos) {
        ResultadoOperacion<Herramientas> resultado = herramientasService.actualizar(id, datosNuevos);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.ok(resultado.getDatos())
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ResultadoOperacion<Void> resultado = herramientasService.eliminar(id);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}