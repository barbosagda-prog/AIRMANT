package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.*;
import com.AIRMANT20.airmant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ManualTecnicoService {

    @Autowired private ManualTecnicoRepository manualRepository;
    @Autowired private HistorialManualRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private TareaMantenimientoRepository tareaRepository;

    public List<ManualTecnico> listar() {
        return manualRepository.findAll();
    }

    public Optional<ManualTecnico> obtenerPorId(Long id) {
        return manualRepository.findById(id);
    }

    public List<HistorialManual> historial(Long id) {
        return historialRepository.findByManual_IdManualOrderByFechaDesc(id);
    }

    public ResultadoOperacion<ManualTecnico> crear(Map<String, Object> body) {
        Long idRegistradoPor = Long.valueOf(body.get("idRegistradoPor").toString());
        Optional<Usuarios> registrador = usuariosRepository.findById(idRegistradoPor);
        if (registrador.isEmpty()) {
            return ResultadoOperacion.error("Usuario registrador no encontrado");
        }

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

        return ResultadoOperacion.exito(guardado);
    }

    public ResultadoOperacion<Void> eliminar(Long id) {
        if (!manualRepository.existsById(id)) {
            return ResultadoOperacion.noEncontrado();
        }
        manualRepository.deleteById(id);
        return ResultadoOperacion.exito(null);
    }
}