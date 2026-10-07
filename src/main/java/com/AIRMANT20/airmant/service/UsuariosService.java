package com.AIRMANT20.airmant.service;

import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {

    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Usuarios> listar() {
        return usuariosRepository.findAll();
    }

    public Optional<Usuarios> obtenerPorId(Long id) {
        return usuariosRepository.findById(id);
    }

    public Usuarios crear(Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    public ResultadoOperacion<Usuarios> login(String nombreUsuario, String password) {
        Usuarios usuario = usuariosRepository.findByNombreUsuario(nombreUsuario);

        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            return ResultadoOperacion.error("Usuario o contraseña incorrectos");
        }

        return ResultadoOperacion.exito(usuario);
    }
}