package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.TareaMantenimiento;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import com.AIRMANT20.airmant.service.TareaMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/tareas")
public class TareaMantenimientoController {

    @Autowired
    private TareaMantenimientoService tareaService;

    @GetMapping
    public List<TareaMantenimiento> listar(@PathVariable Long idOrden) {
        return tareaService.listar(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@PathVariable Long idOrden, @RequestBody TareaMantenimiento tarea) {
        ResultadoOperacion<TareaMantenimiento> resultado = tareaService.registrar(idOrden, tarea);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @PutMapping("/{idTarea}/completar")
    public ResponseEntity<?> completar(@PathVariable Long idOrden, @PathVariable Long idTarea) {
        ResultadoOperacion<TareaMantenimiento> resultado = tareaService.completar(idTarea);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.ok(resultado.getDatos());
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }

    @DeleteMapping("/{idTarea}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idOrden, @PathVariable Long idTarea) {
        ResultadoOperacion<Void> resultado = tareaService.eliminar(idTarea);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}