package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.DocumentacionTecnica;
import com.AIRMANT20.airmant.model.OrdenMantenimiento;
import com.AIRMANT20.airmant.repository.DocumentacionTecnicaRepository;
import com.AIRMANT20.airmant.repository.OrdenMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentacionTecnicaService {

    @Autowired private DocumentacionTecnicaRepository documentacionRepository;
    @Autowired private OrdenMantenimientoRepository ordenRepository;

    public List<DocumentacionTecnica> listar(Long idOrden) {
        return documentacionRepository.findByOrden_IdOrden(idOrden);
    }

    public ResultadoOperacion<DocumentacionTecnica> registrar(Long idOrden, DocumentacionTecnica documento) {
        Optional<OrdenMantenimiento> orden = ordenRepository.findById(idOrden);
        if (orden.isEmpty()) return ResultadoOperacion.error("Orden no encontrada");

        documento.setOrden(orden.get());
        return ResultadoOperacion.exito(documentacionRepository.save(documento));
    }

    public ResultadoOperacion<Void> eliminar(Long idDocumento) {
        if (!documentacionRepository.existsById(idDocumento)) return ResultadoOperacion.noEncontrado();
        documentacionRepository.deleteById(idDocumento);
        return ResultadoOperacion.exito(null);
    }
}
