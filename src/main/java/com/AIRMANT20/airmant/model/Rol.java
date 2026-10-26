package com.AIRMANT20.airmant.model;

public enum Rol {
    ADMINISTRADOR,
    GERENTE_MANTENIMIENTO,
    COORDINADOR_MANTENIMIENTO,
    GESTOR_PERSONAL,
    TECNICO_MANTENIMIENTO,
    INSPECTOR_CALIDAD,
    INGENIERO_AERONAUTICO,
    INGENIERO_PLANIFICACION,
    RESP_AERONAVEGABILIDAD,
    RESP_CONTROL_DOCUMENTAL,
    ALMACENISTA,
    RESP_HERRAMIENTAS,
    COORDINADOR_COMPRAS,
    RESP_FINANCIERO,
    RESP_SEGURIDAD_OPERACIONAL,
    AUDITOR_INTERNO;

    public static boolean esValido(String valor) {
        if (valor == null) return false;
        for (Rol r : values()) {
            if (r.name().equals(valor)) return true;
        }
        return false;
    }
}