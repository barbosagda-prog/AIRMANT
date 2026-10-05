package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.TareaMantenimiento;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.TareaMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/tareas")
public class TareaMantenimientoController {

    @Autowired private TareaMantenimientoRepository tareaRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;

    @GetMapping
    public List<TareaMantenimiento> listar(@PathVariable Long idOrden) {
        return tareaRepository.findByOrden_IdOrden(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@PathVariable Long idOrden, @RequestBody TareaMantenimiento tarea) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResponseEntity.badRequest().body("Orden no encontrada");

        tarea.setOrden(orden.get());
        if (tarea.getEstado() == null) tarea.setEstado("Pendiente");
        return ResponseEntity.ok(tareaRepository.save(tarea));
    }

    @PutMapping("/{idTarea}/completar")
    public ResponseEntity<?> completar(@PathVariable Long idOrden, @PathVariable Long idTarea) {
        return tareaRepository.findById(idTarea)
                .map(tarea -> {
                    tarea.setEstado("Completada");
                    return ResponseEntity.ok(tareaRepository.save(tarea));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idTarea}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idOrden, @PathVariable Long idTarea) {
        if (!tareaRepository.existsById(idTarea)) {
            return ResponseEntity.notFound().build();
        }
        tareaRepository.deleteById(idTarea);
        return ResponseEntity.noContent().build();
    }
}