package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.HerramientaUtilizada;
import com.AIRMANT20.airmant.model.Herramientas;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.repository.HerramientaUtilizadaRepository;
import com.AIRMANT20.airmant.repository.HerramientasRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/herramientas")
public class HerramientaUtilizadaController {

    @Autowired private HerramientaUtilizadaRepository herramientaUtilizadaRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private HerramientasRepository herramientasRepository;

    @GetMapping
    public List<HerramientaUtilizada> listar(@PathVariable Long idOrden) {
        return herramientaUtilizadaRepository.findByOrden_IdOrden(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrarUso(@PathVariable Long idOrden, @RequestBody Map<String, Object> body) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResponseEntity.badRequest().body("Orden no encontrada");

        Long idHerramienta = Long.valueOf(body.get("idHerramienta").toString());
        Optional<Herramientas> herramientaOpt = herramientasRepository.findById(idHerramienta);
        if (herramientaOpt.isEmpty()) return ResponseEntity.badRequest().body("Herramienta no encontrada");

        Herramientas herramienta = herramientaOpt.get();

        // Advertencia (no bloqueante) si la calibración está vencida
        if ("Vencida".equalsIgnoreCase(herramienta.getEstadoCalibracion())) {
            return ResponseEntity.badRequest().body(
                    "No se puede usar '" + herramienta.getNombre() + "': su calibración está vencida."
            );
        }

        HerramientaUtilizada uso = new HerramientaUtilizada();
        uso.setOrden(orden.get());
        uso.setHerramienta(herramienta);
        uso.setFechaUso(LocalDate.now());

        return ResponseEntity.ok(herramientaUtilizadaRepository.save(uso));
    }
}
