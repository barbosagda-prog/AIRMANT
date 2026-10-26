package com.AIRMANT20.airmant.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name= "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuarios;

    @NotNull (message = "Documento obligatorio")
    private Integer documento;

    @NotBlank (message = "Nombre Obligatorio")
    private String nombres;

    @NotBlank (message = "Apellidos obligatorios")
    private String apellidos;

    @NotBlank (message = "Ingrese nombre de usuario asignado")
    private String nombreUsuario;

    @NotBlank (message = "Email obligatorio")
    @Email (message = "Ingrese un Email correcto")
    private String email;

    @NotBlank (message = "Ingrese una contraseña")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull (message = "Ingrese numero correcto")
    private String telefono;

    @NotBlank (message = "Ingrese su rol correcto")
    private String rol;

}
