package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.HistorialInsumo;
import com.AIRMANT20.airmant.model.Insumos;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.HistorialInsumoRepository;
import com.AIRMANT20.airmant.repository.InsumosRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsumosService {

    @Autowired private InsumosRepository insumosRepository;
    @Autowired private HistorialInsumoRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    public List<Insumos> listar() {
        return insumosRepository.findAll();
    }

    public Optional<Insumos> obtenerPorId(Long id) {
        return insumosRepository.findById(id);
    }

    public List<HistorialInsumo> historialDeInsumo(Long id) {
        return historialRepository.findByIdInsumoOrderByFechaDesc(id);
    }

    public List<HistorialInsumo> historialGeneral() {
        return historialRepository.findAllByOrderByFechaDesc();
    }

    public List<Insumos> proximosAVencer(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return insumosRepository.findAll().stream()
                .filter(i -> i.getFechaVencimiento() != null && !i.getFechaVencimiento().isAfter(limite))
                .collect(Collectors.toList());
    }

    public ResultadoOperacion<Insumos> crear(Long idUsuario, Insumos insumo) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Insumos guardado = insumosRepository.save(insumo);
        registrarHistorial(guardado.getIdInsumo(), guardado.getCodigo(), usuario.get(), "Creado", "Insumo registrado en el catálogo");

        return ResultadoOperacion.exito(guardado);
    }

    public ResultadoOperacion<Insumos> actualizar(Long id, Long idUsuario, Insumos datosNuevos) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Optional<Insumos> existente = insumosRepository.findById(id);
        if (existente.isEmpty()) {
            return ResultadoOperacion.noEncontrado();
        }

        Insumos insumo = existente.get();
        insumo.setCodigo(datosNuevos.getCodigo());
        insumo.setDescripcion(datosNuevos.getDescripcion());
        insumo.setCategoria(datosNuevos.getCategoria());
        insumo.setCantidadStock(datosNuevos.getCantidadStock());
        insumo.setUnidadMedida(datosNuevos.getUnidadMedida());
        insumo.setProveedor(datosNuevos.getProveedor());
        insumo.setEstado(datosNuevos.getEstado());
        insumo.setFechaVencimiento(datosNuevos.getFechaVencimiento());

        Insumos actualizado = insumosRepository.save(insumo);
        registrarHistorial(actualizado.getIdInsumo(), actualizado.getCodigo(), usuario.get(), "Modificado", "Datos del insumo actualizados");

        return ResultadoOperacion.exito(actualizado);
    }

    public ResultadoOperacion<Void> eliminar(Long id, Long idUsuario) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Optional<Insumos> insumo = insumosRepository.findById(id);
        if (insumo.isEmpty()) {
            return ResultadoOperacion.noEncontrado();
        }

        registrarHistorial(id, insumo.get().getCodigo(), usuario.get(), "Eliminado", "Insumo eliminado del catálogo");
        insumosRepository.deleteById(id);

        return ResultadoOperacion.exito(null);
    }

    private void registrarHistorial(Long idInsumo, String codigo, Usuarios usuario, String accion, String detalle) {
        HistorialInsumo entrada = new HistorialInsumo();
        entrada.setIdInsumo(idInsumo);
        entrada.setCodigoInsumo(codigo);
        entrada.setUsuario(usuario);
        entrada.setAccion(accion);
        entrada.setFecha(LocalDateTime.now());
        entrada.setDetalle(detalle);
        historialRepository.save(entrada);
    }
}