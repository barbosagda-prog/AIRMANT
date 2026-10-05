package com.AIRMANT20.airmant.dto;

public class OrdenMantenimientoRequest {
    private String tipoMantenimiento;
    private String prioridad;
    private String motivo;
    private String descripcionTrabajo;
    private Long idAeronave;
    private Long idTecnicoResponsable;
    private Long idInspector;
    private String trabajoRealizado;
    private String resultado;
    private String observaciones;

    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDescripcionTrabajo() { return descripcionTrabajo; }
    public void setDescripcionTrabajo(String descripcionTrabajo) { this.descripcionTrabajo = descripcionTrabajo; }
    public Long getIdAeronave() { return idAeronave; }
    public void setIdAeronave(Long idAeronave) { this.idAeronave = idAeronave; }
    public Long getIdTecnicoResponsable() { return idTecnicoResponsable; }
    public void setIdTecnicoResponsable(Long idTecnicoResponsable) { this.idTecnicoResponsable = idTecnicoResponsable; }
    public Long getIdInspector() { return idInspector; }
    public void setIdInspector(Long idInspector) { this.idInspector = idInspector; }
    public String getTrabajoRealizado() { return trabajoRealizado; }
    public void setTrabajoRealizado(String trabajoRealizado) { this.trabajoRealizado = trabajoRealizado; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}