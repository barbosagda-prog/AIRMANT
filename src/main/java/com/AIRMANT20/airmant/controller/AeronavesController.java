package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.model.HistorialAeronave;
import com.AIRMANT20.airmant.service.AeronavesService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aeronaves")
public class AeronavesController {

    @Autowired
    private AeronavesService aeronavesService;

    @GetMapping
    public List<Aeronaves> listar() {
        return aeronavesService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeronaves> obtenerPorId(@PathVariable Long id) {
        return aeronavesService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialAeronave> historial(@PathVariable Long id) {
        return aeronavesService.historial(id);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody Aeronaves aeronave) {
        ResultadoOperacion<Aeronaves> resultado = aeronavesService.crear(idUsuario, aeronave);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody Aeronaves datosNuevos) {
        ResultadoOperacion<Aeronaves> resultado = aeronavesService.actualizar(id, idUsuario, datosNuevos);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.ok(resultado.getDatos());
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long idUsuario) {
        ResultadoOperacion<Void> resultado = aeronavesService.eliminar(id, idUsuario);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.noContent().build();
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }
}