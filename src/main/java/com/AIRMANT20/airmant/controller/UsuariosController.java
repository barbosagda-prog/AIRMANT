package com.AIRMANT20.airmant.controller;

import com.AIRMANT20.airmant.model.Usuarios;
import com.AIRMANT20.airmant.repository.UsuariosRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuarios> getAll()
    {
        return usuariosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuarios getById(@PathVariable Long id)
    {
        return usuariosRepository.findById(id).orElse(null );
    }

    @PostMapping
    public Usuarios create(@RequestBody Usuarios usuarios)
    {
        usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
        return usuariosRepository.save(usuarios);
    }

    @PutMapping("/{id}")
    public Usuarios Update(@PathVariable Long id, @RequestBody @NonNull Usuarios usuarios)
    {
        usuarios.setId_usuarios(id);
        usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword()));
        return usuariosRepository.save(usuarios);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        usuariosRepository.deleteById(id);
    }
}