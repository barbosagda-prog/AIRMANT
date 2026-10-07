package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.RepuestoUtilizado;
import com.AIRMANT20.airmant.service.RepuestoUtilizadoService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/repuestos")
public class RepuestoUtilizadoController {

    @Autowired
    private RepuestoUtilizadoService repuestoUtilizadoService;

    @GetMapping
    public List<RepuestoUtilizado> listar(@PathVariable Long idOrden) {
        return repuestoUtilizadoService.listar(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrarUso(@PathVariable Long idOrden, @RequestBody Map<String, Object> body) {
        ResultadoOperacion<RepuestoUtilizado> resultado = repuestoUtilizadoService.registrarUso(idOrden, body);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }
}