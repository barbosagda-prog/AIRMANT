package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import com.AIRMANT20.airmant.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping
    public List<Usuarios> listar() {
        return usuariosService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerPorId(@PathVariable Long id) {
        return usuariosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuarios usuario) {
        ResultadoOperacion<Usuarios> resultado = usuariosService.crear(usuario);
        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.badRequest().body(resultado.getMensajeError());
        }
        return ResponseEntity.ok(resultado.getDatos());
    }
}