package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.HistorialInsumo;
import com.AIRMANT20.airmant.model.Insumos;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.HistorialInsumoRepository;
import com.AIRMANT20.airmant.repository.InsumosRepository;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/insumos")
public class InsumosController {

    @Autowired private InsumosRepository insumosRepository;
    @Autowired private HistorialInsumoRepository historialRepository;
    @Autowired private UsuariosRepository usuariosRepository;

    @GetMapping
    public List<Insumos> listar() {
        return insumosRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insumos> obtenerPorId(@PathVariable Long id) {
        return insumosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialInsumo> historialDeInsumo(@PathVariable Long id) {
        return historialRepository.findByIdInsumoOrderByFechaDesc(id);
    }

    @GetMapping("/historial")
    public List<HistorialInsumo> historialGeneral() {
        return historialRepository.findAllByOrderByFechaDesc();
    }

    @GetMapping("/proximos-vencer")
    public List<Insumos> proximosAVencer(@RequestParam(defaultValue = "30") int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return insumosRepository.findAll().stream()
                .filter(i -> i.getFechaVencimiento() != null && !i.getFechaVencimiento().isAfter(limite))
                .collect(Collectors.toList());
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

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody Insumos insumo) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        Insumos guardado = insumosRepository.save(insumo);
        registrarHistorial(guardado.getIdInsumo(), guardado.getCodigo(), usuario.get(), "Creado", "Insumo registrado en el catálogo");

        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody Insumos datosNuevos) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        return insumosRepository.findById(id)
                .map(insumoExistente -> {
                    insumoExistente.setCodigo(datosNuevos.getCodigo());
                    insumoExistente.setDescripcion(datosNuevos.getDescripcion());
                    insumoExistente.setCategoria(datosNuevos.getCategoria());
                    insumoExistente.setCantidadStock(datosNuevos.getCantidadStock());
                    insumoExistente.setUnidadMedida(datosNuevos.getUnidadMedida());
                    insumoExistente.setProveedor(datosNuevos.getProveedor());
                    insumoExistente.setEstado(datosNuevos.getEstado());
                    insumoExistente.setFechaVencimiento(datosNuevos.getFechaVencimiento());
                    Insumos actualizado = insumosRepository.save(insumoExistente);

                    registrarHistorial(actualizado.getIdInsumo(), actualizado.getCodigo(), usuario.get(), "Modificado", "Datos del insumo actualizados");

                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long idUsuario) {
        Optional<Usuarios> usuario = usuariosRepository.findById(idUsuario);
        if (usuario.isEmpty()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        Optional<Insumos> insumo = insumosRepository.findById(id);
        if (insumo.isEmpty()) return ResponseEntity.notFound().build();

        registrarHistorial(id, insumo.get().getCodigo(), usuario.get(), "Eliminado", "Insumo eliminado del catálogo");
        insumosRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarProximosAVencer(@RequestParam(defaultValue = "30") int dias) throws IOException {
        List<Insumos> proximos = proximosAVencer(dias);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Insumos por vencer");

        Row header = sheet.createRow(0);
        String[] columnas = {"Código", "Descripción", "Categoría", "Stock", "Unidad", "Proveedor", "Estado", "Fecha de vencimiento"};
        for (int i = 0; i < columnas.length; i++) {
            header.createCell(i).setCellValue(columnas[i]);
        }

        int filaIdx = 1;
        for (Insumos insumo : proximos) {
            Row fila = sheet.createRow(filaIdx++);
            fila.createCell(0).setCellValue(insumo.getCodigo());
            fila.createCell(1).setCellValue(insumo.getDescripcion());
            fila.createCell(2).setCellValue(insumo.getCategoria());
            fila.createCell(3).setCellValue(insumo.getCantidadStock());
            fila.createCell(4).setCellValue(insumo.getUnidadMedida());
            fila.createCell(5).setCellValue(insumo.getProveedor() != null ? insumo.getProveedor() : "");
            fila.createCell(6).setCellValue(insumo.getEstado());
            fila.createCell(7).setCellValue(insumo.getFechaVencimiento().toString());
        }

        for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        workbook.write(salida);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "insumos_proximos_vencer.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(salida.toByteArray());
    }
}