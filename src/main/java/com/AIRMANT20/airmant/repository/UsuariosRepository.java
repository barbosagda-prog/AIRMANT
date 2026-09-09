package com.AIRMANT20.airmant.repository;

import com.AIRMANT20.airmant.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long>
{
    Usuarios findByNombreUsuario(String nombreUsuario);
}
