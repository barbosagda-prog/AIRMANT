package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.RepuestoUtilizado;
import com.AIRMANT20.airmant.model.Repuestos;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.RepuestoUtilizadoRepository;
import com.AIRMANT20.airmant.repository.RepuestosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/repuestos")
public class RepuestoUtilizadoController {

    @Autowired private RepuestoUtilizadoRepository repuestoUtilizadoRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private RepuestosRepository repuestosRepository;

    @GetMapping
    public List<RepuestoUtilizado> listar(@PathVariable Long idOrden) {
        return repuestoUtilizadoRepository.findByOrden_IdOrden(idOrden);
    }

    @PostMapping
    public ResponseEntity<?> registrarUso(@PathVariable Long idOrden, @RequestBody Map<String, Object> body) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResponseEntity.badRequest().body("Orden no encontrada");

        Long idRepuesto = Long.valueOf(body.get("idRepuesto").toString());
        Integer cantidad = Integer.valueOf(body.get("cantidadUtilizada").toString());

        Optional<Repuestos> repuestoOpt = repuestosRepository.findById(idRepuesto);
        if (repuestoOpt.isEmpty()) return ResponseEntity.badRequest().body("Repuesto no encontrado");

        Repuestos repuesto = repuestoOpt.get();
        if (repuesto.getCantidadStock() < cantidad) {
            return ResponseEntity.badRequest().body(
                    "Stock insuficiente. Disponible: " + repuesto.getCantidadStock() + ", solicitado: " + cantidad
            );
        }

        // Descontar del inventario
        repuesto.setCantidadStock(repuesto.getCantidadStock() - cantidad);
        repuestosRepository.save(repuesto);

        RepuestoUtilizado uso = new RepuestoUtilizado();
        uso.setOrden(orden.get());
        uso.setRepuesto(repuesto);
        uso.setCantidadUtilizada(cantidad);
        uso.setNumeroSerie((String) body.get("numeroSerie"));
        uso.setEstadoComponente((String) body.get("estadoComponente"));

        return ResponseEntity.ok(repuestoUtilizadoRepository.save(uso));
    }
}