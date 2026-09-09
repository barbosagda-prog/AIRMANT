package com.AIRMANT20.airmant.dto;

public class LoginRequest {
    private String nombreUsuario;
    private String password;

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
