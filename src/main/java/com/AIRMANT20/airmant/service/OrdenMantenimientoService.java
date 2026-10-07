package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.dto.OrdenMantenimientoRequest;
import com.AIRMANT20.airmant.model.*;
import com.AIRMANT20.airmant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenMantenimientoService {

    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private AeronavesRepository aeronavesRepository;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private HistorialOrdenRepository historialRepository;

    public List<OrdenMantenimiento> listar() {
        return ordenRepository.findAll();
    }

    public Optional<OrdenMantenimiento> obtenerPorId(Long id) {
        return ordenRepository.findById(id);
    }

    public List<OrdenMantenimiento> buscar(String matricula, String estado) {
        if (matricula != null) return ordenRepository.findByAeronave_Matricula(matricula);
        if (estado != null) return ordenRepository.findByEstado(estado);
        return ordenRepository.findAll();
    }

    public List<HistorialOrden> historial(Long id) {
        return historialRepository.findByIdOrdenOrderByFechaDesc(id);
    }

    public ResultadoOperacion<OrdenMantenimiento> crear(Long idUsuario, OrdenMantenimientoRequest req) {
        Optional<Usuarios> registrante = usuariosRepository.findById(idUsuario);
        if (registrante.isEmpty()) return ResultadoOperacion.error("Usuario no encontrado");

        Optional<Aeronaves> aeronave = aeronavesRepository.findById(req.getIdAeronave());
        if (aeronave.isEmpty()) return ResultadoOperacion.error("Aeronave no encontrada");

        Optional<Usuarios> tecnico = usuariosRepository.findById(req.getIdTecnicoResponsable());
        if (tecnico.isEmpty()) return ResultadoOperacion.error("Técnico no encontrado");

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
        guardada.setNumeroOrden("OM-" + LocalDate.now().getYear() + "-" + String.format("%04d", guardada.getIdOrden()));
        OrdenMantenimiento ordenFinal = ordenRepository.save(guardada);

        registrarHistorial(ordenFinal.getIdOrden(), ordenFinal.getNumeroOrden(), registrante.get(), "Creada", "Orden de mantenimiento creada");

        return ResultadoOperacion.exito(ordenFinal);
    }

    public ResultadoOperacion<OrdenMantenimiento> cambiarEstado(Long id, Long idUsuario, String nuevoEstado) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResultadoOperacion.error("Usuario no encontrado");

        Optional<OrdenMantenimiento> ordenOpt = ordenRepository.findById(id);
        if (ordenOpt.isEmpty()) return ResultadoOperacion.noEncontrado();

        OrdenMantenimiento orden = ordenOpt.get();
        String estadoLimpio = nuevoEstado.replace("\"", "");
        orden.setEstado(estadoLimpio);
        OrdenMantenimiento actualizada = ordenRepository.save(orden);

        registrarHistorial(actualizada.getIdOrden(), actualizada.getNumeroOrden(), usuario.get(), "Estado cambiado", "Nuevo estado: " + estadoLimpio);

        return ResultadoOperacion.exito(actualizada);
    }

    public ResultadoOperacion<OrdenMantenimiento> cerrar(Long id, Long idUsuario, OrdenMantenimientoRequest req) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResultadoOperacion.error("Usuario no encontrado");

        Optional<OrdenMantenimiento> ordenOpt = ordenRepository.findById(id);
        if (ordenOpt.isEmpty()) return ResultadoOperacion.noEncontrado();

        OrdenMantenimiento orden = ordenOpt.get();
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

        return ResultadoOperacion.exito(cerrada);
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
}
