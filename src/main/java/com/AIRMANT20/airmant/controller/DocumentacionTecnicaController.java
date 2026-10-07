package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.DocumentacionTecnica;
import com.AIRMANT20.airmant.service.DocumentacionTecnicaService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/documentos")
public class DocumentacionTecnicaController {

    @Autowired
    private DocumentacionTecnicaService documentacionService;

    @GetMapping
    public List<DocumentacionTecnica> listar(@PathVariable Long idOrden) {
        return documentacionService.listar(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrar(@PathVariable Long idOrden, @RequestBody DocumentacionTecnica documento) {
        ResultadoOperacion<DocumentacionTecnica> resultado = documentacionService.registrar(idOrden, documento);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @DeleteMapping("/{idDocumento}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idOrden, @PathVariable Long idDocumento) {
        ResultadoOperacion<Void> resultado = documentacionService.eliminar(idDocumento);
        return resultado.getTipo() == ResultadoOperacion.Tipo.EXITO
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}