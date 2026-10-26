package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.Repuestos;
import com.AIRMANT20.airmant.repository.RepuestosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepuestosService {

    @Autowired private RepuestosRepository repuestosRepository;

    public List<Repuestos> listar() {
        return repuestosRepository.findAll();
    }

    public Optional<Repuestos> obtenerPorId(Long id) {
        return repuestosRepository.findById(id);
    }

    public Repuestos crear(Repuestos repuesto) {
        return repuestosRepository.save(repuesto);
    }

    public ResultadoOperacion<Repuestos> actualizar(Long id, Repuestos datosNuevos) {
        Optional<Repuestos> existente = repuestosRepository.findById(id);
        if (existente.isEmpty()) return ResultadoOperacion.noEncontrado();

        Repuestos repuesto = existente.get();
        repuesto.setCodigo(datosNuevos.getCodigo());
        repuesto.setDescripcion(datosNuevos.getDescripcion());
        repuesto.setCategoria(datosNuevos.getCategoria());
        repuesto.setCantidadStock(datosNuevos.getCantidadStock());
        repuesto.setUnidadMedida(datosNuevos.getUnidadMedida());
        repuesto.setProveedor(datosNuevos.getProveedor());
        repuesto.setEstado(datosNuevos.getEstado());

        return ResultadoOperacion.exito(repuestosRepository.save(repuesto));
    }

    public ResultadoOperacion<Void> eliminar(Long id) {
        if (!repuestosRepository.existsById(id)) return ResultadoOperacion.noEncontrado();
        repuestosRepository.deleteById(id);
        return ResultadoOperacion.exito(null);
    }
}