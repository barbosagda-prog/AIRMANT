package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Aeronaves;
import com.AIRMANT20.airmant.model.HistorialAeronave;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.AeronavesRepository;
import com.AIRMANT20.airmant.repository.HistorialAeronaveRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aeronaves")
public class AeronavesController {

    @Autowired private AeronavesRepository aeronavesRepository;
    @Autowired private HistorialAeronaveRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping
    public List<Aeronaves> listar() {
        return aeronavesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeronaves> obtenerPorId(@PathVariable Long id) {
        return aeronavesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialAeronave> historial(@PathVariable Long id) {
        return historialRepository.findByIdAeronaveOrderByFechaDesc(id);
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

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody Aeronaves aeronave) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        Aeronaves guardada = aeronavesRepository.save(aeronave);
        registrarHistorial(guardada.getIdAeronave(), guardada.getMatricula(), usuario.get(), "Creada", "Aeronave registrada en el sistema");

        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody Aeronaves datosNuevos) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        return aeronavesRepository.findById(id)
                .map(aeronaveExistente -> {
                    aeronaveExistente.setMatricula(datosNuevos.getMatricula());
                    aeronaveExistente.setNumeroSerie(datosNuevos.getNumeroSerie());
                    aeronaveExistente.setFabricante(datosNuevos.getFabricante());
                    aeronaveExistente.setModeloFabricacion(datosNuevos.getModeloFabricacion());
                    aeronaveExistente.setTipoAeronave(datosNuevos.getTipoAeronave());
                    aeronaveExistente.setCategoria(datosNuevos.getCategoria());
                    aeronaveExistente.setTipoMotor(datosNuevos.getTipoMotor());
                    aeronaveExistente.setNumeroMotor(datosNuevos.getNumeroMotor());
                    aeronaveExistente.setPesoMaximoDespegue(datosNuevos.getPesoMaximoDespegue());
                    aeronaveExistente.setCapacidad(datosNuevos.getCapacidad());
                    aeronaveExistente.setConfiguracionCabina(datosNuevos.getConfiguracionCabina());
                    aeronaveExistente.setAnioFabricacion(datosNuevos.getAnioFabricacion());
                    aeronaveExistente.setEstado(datosNuevos.getEstado());
                    aeronaveExistente.setHorasVuelo(datosNuevos.getHorasVuelo());
                    aeronaveExistente.setProximaInspeccion(datosNuevos.getProximaInspeccion());
                    Aeronaves actualizada = aeronavesRepository.save(aeronaveExistente);

                    registrarHistorial(actualizada.getIdAeronave(), actualizada.getMatricula(), usuario.get(), "Modificada", "Datos de la aeronave actualizados");

                    return ResponseEntity.ok(actualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long idUsuario) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        Optional<Aeronaves> aeronave = aeronavesRepository.findById(id);
        if (aeronave.isEmpty()) return ResponseEntity.notFound().build();

        registrarHistorial(id, aeronave.get().getMatricula(), usuario.get(), "Eliminada", "Aeronave eliminada del sistema");
        aeronavesRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}