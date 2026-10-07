package com.AIRMANT20.airmant.service;

public class ResultadoOperacion<T> {

    public enum Tipo { EXITO, ERROR_VALIDACION, NO_ENCONTRADO }

    private final Tipo tipo;
    private final T datos;
    private final String mensajeError;

    private ResultadoOperacion(Tipo tipo, T datos, String mensajeError) {
        this.tipo = tipo;
        this.datos = datos;
        this.mensajeError = mensajeError;
    }

    public static <T> ResultadoOperacion<T> exito(T datos) {
        return new ResultadoOperacion<>(Tipo.EXITO, datos, null);
    }

    public static <T> ResultadoOperacion<T> error(String mensaje) {
        return new ResultadoOperacion<>(Tipo.ERROR_VALIDACION, null, mensaje);
    }

    public static <T> ResultadoOperacion<T> noEncontrado() {
        return new ResultadoOperacion<>(Tipo.NO_ENCONTRADO, null, null);
    }

    public Tipo getTipo() { return tipo; }
    public T getDatos() { return datos; }
    public String getMensajeError() { return mensajeError; }
}