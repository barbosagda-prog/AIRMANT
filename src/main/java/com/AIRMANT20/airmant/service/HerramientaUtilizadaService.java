package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.HerramientaUtilizada;
import com.AIRMANT20.airmant.model.Herramientas;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.repository.HerramientaUtilizadaRepository;
import com.AIRMANT20.airmant.repository.HerramientasRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HerramientaUtilizadaService {

    @Autowired private HerramientaUtilizadaRepository herramientaUtilizadaRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private HerramientasRepository herramientasRepository;

    public List<HerramientaUtilizada> listar(Long idOrden) {
        return herramientaUtilizadaRepository.findByOrden_IdOrden(idOrden);
    }

    public ResultadoOperacion<HerramientaUtilizada> registrarUso(Long idOrden, Map<String, Object> body) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResultadoOperacion.error("Orden no encontrada");

        Long idHerramienta = Long.valueOf(body.get("idHerramienta").toString());
        Optional<Herramientas> herramientaOpt = herramientasRepository.findById(idHerramienta);
        if (herramientaOpt.isEmpty()) return ResultadoOperacion.error("Herramienta no encontrada");

        Herramientas herramienta = herramientaOpt.get();

        if ("Vencida".equalsIgnoreCase(herramienta.getEstadoCalibracion())) {
            return ResultadoOperacion.error(
                    "No se puede usar '" + herramienta.getNombre() + "': su calibración está vencida."
            );
        }

        HerramientaUtilizada uso = new HerramientaUtilizada();
        uso.setOrden(orden.get());
        uso.setHerramienta(herramienta);
        uso.setFechaUso(LocalDate.now());

        return ResultadoOperacion.exito(herramientaUtilizadaRepository.save(uso));
    }
}