package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.HistorialManual;
import com.AIRMANT20.airmant.model.ManualTecnico;
import com.AIRMANT20.airmant.service.ManualTecnicoService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manuales")
public class ManualTecnicoController {

    @Autowired
    private ManualTecnicoService manualTecnicoService;

    @GetMapping
    public List<ManualTecnico> listar() {
        return manualTecnicoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManualTecnico> obtenerPorId(@PathVariable Long id) {
        return manualTecnicoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialManual> historial(@PathVariable Long id) {
        return manualTecnicoService.historial(id);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        ResultadoOperacion<ManualTecnico> resultado = manualTecnicoService.crear(body);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        ResultadoOperacion<Void> resultado = manualTecnicoService.eliminar(id);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.noContent().build();
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }
}