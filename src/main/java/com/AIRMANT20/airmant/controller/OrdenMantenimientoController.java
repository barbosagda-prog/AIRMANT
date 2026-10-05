package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.dto.OrdenMantenimientoRequest;
import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.model.HistorialOrden;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.AeronavesRepository;
import com.AIRMANT20.airmant.repository.HistorialOrdenRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenMantenimientoController {

    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private AeronavesRepository aeronavesRepository;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private HistorialOrdenRepository historialRepository;

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

    @GetMapping("/{id}/historial")
    public List<HistorialOrden> historial(@PathVariable Long id) {
        return historialRepository.findByIdOrdenOrderByFechaDesc(id);
    }

    @GetMapping("/buscar")
    public List<OrdenMantenimiento> buscar(@RequestParam(required = false) String matricula,
                                           @RequestParam(required = false) String estado) {
        if (matricula != null) return ordenRepository.findByAeronave_Matricula(matricula);
        if (estado != null) return ordenRepository.findByEstado(estado);
        return ordenRepository.findAll();
    }

    private void registrarHistorial(Long idOrden, String numeroOrden, Usuarios usuario, String accion, String detalle) {
        HistorialOrden entrada = new HistorialOrden();
        entrada.setIdOrden(idOrden);
        entrada.setNumeroOrdenSnapshot(numeroOrden);
        entrada.setUsuario(usuario);
        entrada.setAccion(accion);
        entrada.setFecha(LocalDateTime.now());
        entrada.setDetalle(detalle);
        historialRepository.save(entrada);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody OrdenMantenimientoRequest req) {
        Optional<Usuarios> registrante = usuariosRepository.findById(idUsuario);
        if (registrante.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

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
        OrdenMantenimiento ordenFinal = ordenRepository.save(guardada);

        registrarHistorial(ordenFinal.getIdOrden(), ordenFinal.getNumeroOrden(), registrante.get(), "Creada", "Orden de mantenimiento creada");

        return ResponseEntity.ok(ordenFinal);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody String nuevoEstado) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        return ordenRepository.findById(id)
                .map(orden -> {
                    String estadoLimpio = nuevoEstado.replace("\"", "");
                    orden.setEstado(estadoLimpio);
                    OrdenMantenimiento actualizada = ordenRepository.save(orden);

                    registrarHistorial(actualizada.getIdOrden(), actualizada.getNumeroOrden(), usuario.get(), "Estado cambiado", "Nuevo estado: " + estadoLimpio);

                    return ResponseEntity.ok(actualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarOrden(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody OrdenMantenimientoRequest req) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

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
                    OrdenMantenimiento cerrada = ordenRepository.save(orden);

                    registrarHistorial(cerrada.getIdOrden(), cerrada.getNumeroOrden(), usuario.get(), "Finalizada", "Orden cerrada: " + req.getResultado());

                    return ResponseEntity.ok(cerrada);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}