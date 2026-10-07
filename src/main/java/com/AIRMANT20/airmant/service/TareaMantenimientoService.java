package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.model.TareaMantenimiento;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import com.AIRMANT20.airmant.repository.TareaMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaMantenimientoService {

    @Autowired private TareaMantenimientoRepository tareaRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;

    public List<TareaMantenimiento> listar(Long idOrden) {
        return tareaRepository.findByOrden_IdOrden(idOrden);
    }

    public ResultadoOperacion<TareaMantenimiento> registrar(Long idOrden, TareaMantenimiento tarea) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResultadoOperacion.error("Orden no encontrada");

        tarea.setOrden(orden.get());
        if (tarea.getEstado() == null) tarea.setEstado("Pendiente");

        return ResultadoOperacion.exito(tareaRepository.save(tarea));
    }

    public ResultadoOperacion<TareaMantenimiento> completar(Long idTarea) {
        Optional<TareaMantenimiento> tarea = tareaRepository.findById(idTarea);
        if (tarea.isEmpty()) return ResultadoOperacion.noEncontrado();

        tarea.get().setEstado("Completada");
        return ResultadoOperacion.exito(tareaRepository.save(tarea.get()));
    }

    public ResultadoOperacion<Void> eliminar(Long idTarea) {
        if (!tareaRepository.existsById(idTarea)) return ResultadoOperacion.noEncontrado();
        tareaRepository.deleteById(idTarea);
        return ResultadoOperacion.exito(null);
    }
}