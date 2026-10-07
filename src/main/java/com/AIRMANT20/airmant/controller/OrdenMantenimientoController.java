package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.dto.OrdenMantenimientoRequest;
import com.AIRMANT20.airmant.model.HistorialOrden;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.service.OrdenMantenimientoService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenMantenimientoController {

    @Autowired
    private OrdenMantenimientoService ordenService;

    @GetMapping
    public List<OrdenMantenimiento> listar() {
        return ordenService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenMantenimiento> obtenerPorId(@PathVariable Long id) {
        return ordenService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialOrden> historial(@PathVariable Long id) {
        return ordenService.historial(id);
    }

    @GetMapping("/buscar")
    public List<OrdenMantenimiento> buscar(@RequestParam(required = false) String matricula,
                                           @RequestParam(required = false) String estado) {
        return ordenService.buscar(matricula, estado);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody OrdenMantenimientoRequest req) {
        ResultadoOperacion<OrdenMantenimiento> resultado = ordenService.crear(idUsuario, req);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody String nuevoEstado) {
        ResultadoOperacion<OrdenMantenimiento> resultado = ordenService.cambiarEstado(id, idUsuario, nuevoEstado);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.ok(resultado.getDatos());
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarOrden(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody OrdenMantenimientoRequest req) {
        ResultadoOperacion<OrdenMantenimiento> resultado = ordenService.cerrar(id, idUsuario, req);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.ok(resultado.getDatos());
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }
}