package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.dto.LoginRequest;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.service.ResultadoOperacion;
import com.AIRMANT20.airmant.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        ResultadoOperacion<Usuarios> resultado = usuariosService.login(req.getNombreUsuario(), req.getPassword());

        if (resultado.getTipo() == ResultadoOperacion.Tipo.ERROR_VALIDACION) {
            return ResponseEntity.status(401).body(Map.of("mensaje", resultado.getMensajeError()));
        }

        Usuarios usuario = resultado.getDatos();
        return ResponseEntity.ok(Map.of(
                "token", "sesion-" + usuario.getId_usuarios(),
                "rol", usuario.getRol(),
                "nombre", usuario.getNombres(),
                "id", usuario.getId_usuarios()
        ));
    }
}