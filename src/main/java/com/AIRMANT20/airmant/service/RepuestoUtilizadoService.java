package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.RepuestoUtilizado;
import com.AIRMANT20.airmant.model.Repuestos;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.RepuestoUtilizadoRepository;
import com.AIRMANT20.airmant.repository.RepuestosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RepuestoUtilizadoService {

    @Autowired private RepuestoUtilizadoRepository repuestoUtilizadoRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;
    @Autowired private RepuestosRepository repuestosRepository;

    public List<RepuestoUtilizado> listar(Long idOrden) {
        return repuestoUtilizadoRepository.findByOrden_IdOrden(idOrden);
    }

    public ResultadoOperacion<RepuestoUtilizado> registrarUso(Long idOrden, Map<String, Object> body) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResultadoOperacion.error("Orden no encontrada");

        Long idRepuesto = Long.valueOf(body.get("idRepuesto").toString());
        Integer cantidad = Integer.valueOf(body.get("cantidadUtilizada").toString());

        Optional<Repuestos> repuestoOpt = repuestosRepository.findById(idRepuesto);
        if (repuestoOpt.isEmpty()) return ResultadoOperacion.error("Repuesto no encontrado");

        Repuestos repuesto = repuestoOpt.get();
        if (repuesto.getCantidadStock() < cantidad) {
            return ResultadoOperacion.error(
                    "Stock insuficiente. Disponible: " + repuesto.getCantidadStock() + ", solicitado: " + cantidad
            );
        }

        repuesto.setCantidadStock(repuesto.getCantidadStock() - cantidad);
        repuestosRepository.save(repuesto);

        RepuestoUtilizado uso = new RepuestoUtilizado();
        uso.setOrden(orden.get());
        uso.setRepuesto(repuesto);
        uso.setCantidadUtilizada(cantidad);
        uso.setNumeroSerie((String) body.get("numeroSerie"));
        uso.setEstadoComponente((String) body.get("estadoComponente"));

        return ResultadoOperacion.exito(repuestoUtilizadoRepository.save(uso));
    }
}
