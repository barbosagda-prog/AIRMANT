package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.model.HistorialAeronave;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.AeronavesRepository;
import com.AIRMANT20.airmant.repository.HistorialAeronaveRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AeronavesService {

    @Autowired private AeronavesRepository aeronavesRepository;
    @Autowired private HistorialAeronaveRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    public List<Aeronaves> listar() {
        return aeronavesRepository.findAll();
    }

    public Optional<Aeronaves> obtenerPorId(Long id) {
        return aeronavesRepository.findById(id);
    }

    public List<HistorialAeronave> historial(Long id) {
        return historialRepository.findByIdAeronaveOrderByFechaDesc(id);
    }

    public ResultadoOperacion<Aeronaves> crear(Long idUsuario, Aeronaves aeronave) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Aeronaves guardada = aeronavesRepository.save(aeronave);
        registrarHistorial(guardada.getIdAeronave(), guardada.getMatricula(), usuario.get(), "Creada", "Aeronave registrada en el sistema");

        return ResultadoOperacion.exito(guardada);
    }

    public ResultadoOperacion<Aeronaves> actualizar(Long id, Long idUsuario, Aeronaves datosNuevos) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Optional<Aeronaves> existente = aeronavesRepository.findById(id);
        if (existente.isEmpty()) {
            return ResultadoOperacion.noEncontrado();
        }

        Aeronaves aeronave = existente.get();
        aeronave.setMatricula(datosNuevos.getMatricula());
        aeronave.setNumeroSerie(datosNuevos.getNumeroSerie());
        aeronave.setFabricante(datosNuevos.getFabricante());
        aeronave.setModeloFabricacion(datosNuevos.getModeloFabricacion());
        aeronave.setTipoAeronave(datosNuevos.getTipoAeronave());
        aeronave.setCategoria(datosNuevos.getCategoria());
        aeronave.setTipoMotor(datosNuevos.getTipoMotor());
        aeronave.setNumeroMotor(datosNuevos.getNumeroMotor());
        aeronave.setPesoMaximoDespegue(datosNuevos.getPesoMaximoDespegue());
        aeronave.setCapacidad(datosNuevos.getCapacidad());
        aeronave.setConfiguracionCabina(datosNuevos.getConfiguracionCabina());
        aeronave.setAnioFabricacion(datosNuevos.getAnioFabricacion());
        aeronave.setEstado(datosNuevos.getEstado());
        aeronave.setHorasVuelo(datosNuevos.getHorasVuelo());
        aeronave.setProximaInspeccion(datosNuevos.getProximaInspeccion());

        Aeronaves actualizada = aeronavesRepository.save(aeronave);
        registrarHistorial(actualizada.getIdAeronave(), actualizada.getMatricula(), usuario.get(), "Modificada", "Datos de la aeronave actualizados");

        return ResultadoOperacion.exito(actualizada);
    }

    public ResultadoOperacion<Void> eliminar(Long id, Long idUsuario) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            return ResultadoOperacion.error("Usuario no encontrado");
        }

        Optional<Aeronaves> aeronave = aeronavesRepository.findById(id);
        if (aeronave.isEmpty()) {
            return ResultadoOperacion.noEncontrado();
        }

        registrarHistorial(id, aeronave.get().getMatricula(), usuario.get(), "Eliminada", "Aeronave eliminada del sistema");
        aeronavesRepository.deleteById(id);

        return ResultadoOperacion.exito(null);
    }

    private void registrarHistorial(Long idAeronave, String matricula, Usuarios usuario, String accion, String detalle) {
        HistorialAeronave entrada = new HistorialAeronave();
        entrada.setIdAeronave(idAeronave);
        entrada.setMatriculaAeronave(matricula);
        entrada.setUsuario(usuario);
        entrada.setAccion(accion);
        entrada.setFecha(LocalDateTime.now());
        entrada.setDetalle(detalle);
        historialRepository.save(entrada);
    }
}