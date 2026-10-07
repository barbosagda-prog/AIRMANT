package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.HistorialInsumo;
import com.AIRMANT20.airmant.model.Insumos;
import com.AIRMANT20.airmant.service.InsumosService;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumosController {

    @Autowired
    private InsumosService insumosService;

    @GetMapping
    public List<Insumos> listar() {
        return insumosService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insumos> obtenerPorId(@PathVariable Long id) {
        return insumosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialInsumo> historialDeInsumo(@PathVariable Long id) {
        return insumosService.historialDeInsumo(id);
    }

    @GetMapping("/historial")
    public List<HistorialInsumo> historialGeneral() {
        return insumosService.historialGeneral();
    }

    @GetMapping("/proximos-vencer")
    public List<Insumos> proximosAVencer(@RequestParam(defaultValue = "30") int dias) {
        return insumosService.proximosAVencer(dias);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestParam Long idUsuario, @RequestBody Insumos insumo) {
        ResultadoOperacion<Insumos> resultado = insumosService.crear(idUsuario, insumo);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestParam Long idUsuario, @RequestBody Insumos datosNuevos) {
        ResultadoOperacion<Insumos> resultado = insumosService.actualizar(id, idUsuario, datosNuevos);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.ok(resultado.getDatos());
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long idUsuario) {
        ResultadoOperacion<Void> resultado = insumosService.eliminar(id, idUsuario);
        return switch (resultado.getTipo()) {
            case EXITO -> ResponseEntity.noContent().build();
            case NO_ENCONTRADO -> ResponseEntity.notFound().build();
            case ERROR_VALIDACION -> ResponseEntity.badRequest().body(resultado.getMensajeError());
        };
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarProximosAVencer(@RequestParam(defaultValue = "30") int dias) throws IOException {
        List<Insumos> proximos = insumosService.proximosAVencer(dias);

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