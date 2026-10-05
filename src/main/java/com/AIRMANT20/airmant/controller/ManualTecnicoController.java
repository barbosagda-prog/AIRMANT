package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.*;
import com.AIRMANT20.airmant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/manuales")
public class ManualTecnicoController {

    @Autowired private ManualTecnicoRepository manualRepository;
    @Autowired private HistorialManualRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private TareaMantenimientoRepository tareaRepository;

    @GetMapping
    public List<ManualTecnico> listar() {
        return manualRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManualTecnico> obtenerPorId(@PathVariable Long id) {
        return manualRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialManual> historial(@PathVariable Long id) {
        return historialRepository.findByManual_IdManualOrderByFechaDesc(id);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        Long idRegistradoPor = Long.valueOf(body.get("idRegistradoPor").toString());
        Optional<Usuarios> registrador = usuariosRepository.findById(idRegistradoPor);
        if (registrador.isEmpty()) return ResponseEntity.badRequest().body("Usuario registrador no encontrado");

        ManualTecnico manual = new ManualTecnico();
        manual.setTipoManual((String) body.get("tipoManual"));
        manual.setFabricante((String) body.get("fabricante"));
        manual.setModelo((String) body.get("modelo"));
        manual.setNumeroAta((String) body.get("numeroAta"));
        manual.setIdioma((String) body.get("idioma"));
        manual.setNumeroDocumento((String) body.get("numeroDocumento"));
        manual.setFuente((String) body.get("fuente"));
        manual.setFechaRegistro(LocalDate.now());
        manual.setRegistradoPor(registrador.get());

        if (body.get("idTarea") != null) {
            tareaRepository.findById(Long.valueOf(body.get("idTarea").toString()))
                    .ifPresent(manual::setTarea);
        }

        ManualTecnico guardado = manualRepository.save(manual);

        HistorialManual entrada = new HistorialManual();
        entrada.setManual(guardado);
        entrada.setUsuario(registrador.get());
        entrada.setAccion("Registrado");
        entrada.setFecha(LocalDateTime.now());
        entrada.setDetalle("Manual registrado en el sistema");
        historialRepository.save(entrada);

        return ResponseEntity.ok(guardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!manualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manualRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}