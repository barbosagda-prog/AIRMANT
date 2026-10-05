    package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.dto.OrdenMantenimientoRequest;
import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.AeronavesRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenMantenimientoController {

    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private AeronavesRepository aeronavesRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping
    public List<OrdenMantenimiento> listar() {
        return ordenRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenMantenimiento> obtenerPorId(@PathVariable Long id) {
        return ordenRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<OrdenMantenimiento> buscar(@RequestParam(required = false) String matricula,
                                           @RequestParam(required = false) String estado) {
        if (matricula != null) return ordenRepository.findByAeronave_Matricula(matricula);
        if (estado != null) return ordenRepository.findByEstado(estado);
        return ordenRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody OrdenMantenimientoRequest req) {
        Optional<Aeronaves> aeronave = aeronavesRepository.findById(req.getIdAeronave());
        Optional<Usuarios> tecnico = usuariosRepository.findById(req.getIdTecnicoResponsable());

        if (aeronave.isEmpty()) return ResponseEntity.badRequest().body("Aeronave no encontrada");
        if (tecnico.isEmpty()) return ResponseEntity.badRequest().body("Técnico no encontrado");

        OrdenMantenimiento orden = new OrdenMantenimiento();
        orden.setAeronave(aeronave.get());
        orden.setTecnicoResponsable(tecnico.get());
        orden.setTipoMantenimiento(req.getTipoMantenimiento());
        orden.setPrioridad(req.getPrioridad());
        orden.setEstado("Abierta");
        orden.setMotivo(req.getMotivo());
        orden.setDescripcionTrabajo(req.getDescripcionTrabajo());
        orden.setFechaCreacion(LocalDate.now());

        OrdenMantenimiento guardada = ordenRepository.save(orden);

        // Generar número de orden usando el id ya asignado por la base de datos
        guardada.setNumeroOrden("OM-" + LocalDate.now().getYear() + "-" + String.format("%04d", guardada.getIdOrden()));
        return ResponseEntity.ok(ordenRepository.save(guardada));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        return ordenRepository.findById(id)
                .map(orden -> {
                    orden.setEstado(nuevoEstado.replace("\"", ""));
                    return ResponseEntity.ok(ordenRepository.save(orden));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarOrden(@PathVariable Long id, @RequestBody OrdenMantenimientoRequest req) {
        return ordenRepository.findById(id)
                .map(orden -> {
                    orden.setTrabajoRealizado(req.getTrabajoRealizado());
                    orden.setResultado(req.getResultado());
                    orden.setObservaciones(req.getObservaciones());
                    orden.setEstado("Finalizada");
                    orden.setFechaCierre(LocalDate.now());
                    if (req.getIdInspector() != null) {
                        usuariosRepository.findById(req.getIdInspector()).ifPresent(orden::setInspector);
                    }
                    return ResponseEntity.ok(ordenRepository.save(orden));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}