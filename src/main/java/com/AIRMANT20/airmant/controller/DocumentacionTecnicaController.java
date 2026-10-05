package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.DocumentacionTecnica;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.repository.DocumentacionTecnicaRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/documentos")
public class DocumentacionTecnicaController {

    @Autowired private DocumentacionTecnicaRepository documentacionRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;

    @GetMapping
    public List<DocumentacionTecnica> listar(@PathVariable Long idOrden) {
        return documentacionRepository.findByOrden_IdOrden(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@PathVariable Long idOrden, @RequestBody DocumentacionTecnica documento) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResponseEntity.badRequest().body("Orden no encontrada");

        documento.setOrden(orden.get());
        return ResponseEntity.ok(documentacionRepository.save(documento));
    }

    @DeleteMapping("/{idDocumento}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idOrden, @PathVariable Long idDocumento) {
        if (!documentacionRepository.existsById(idDocumento)) {
            return ResponseEntity.notFound().build();
        }
        documentacionRepository.deleteById(idDocumento);
        return ResponseEntity.noContent().build();
    }
}