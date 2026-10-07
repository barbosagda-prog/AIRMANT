package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.HerramientaUtilizada;
import com.AIRMANT20.airmant.service.HerramientaUtilizadaService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/herramientas")
public class HerramientaUtilizadaController {

    @Autowired
    private HerramientaUtilizadaService herramientaUtilizadaService;

    @GetMapping
    public List<HerramientaUtilizada> listar(@PathVariable Long idOrden) {
        return herramientaUtilizadaService.listar(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrarUso(@PathVariable Long idOrden, @RequestBody Map<String, Object> body) {
        ResultadoOperacion<HerramientaUtilizada> resultado = herramientaUtilizadaService.registrarUso(idOrden, body);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }
}
