package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.dto.LoginRequest;
import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Usuarios usuario = usuariosRepository.findByNombreUsuario(req.getNombreUsuario());

        if (usuario == null || !passwordEncoder.matches(req.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "Usuario o contraseña incorrectos"));
        }

        return ResponseEntity.ok(Map.of(
                "token", "sesion-" + usuario.getId_usuarios(),
                "rol", usuario.getRol(),
                "nombre", usuario.getNombres(),
                "id", usuario.getId_usuarios()
        ));
    }
}