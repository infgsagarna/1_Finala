package com.example.a1_finala.data.clase_aux;

public class InvitadosInfo {


    private String fechaInvitacion;
    private String nombreInvitado;

    public InvitadosInfo() {

    }

    public InvitadosInfo(String fechaInvitacion, String nombreInvitado) {
        this.fechaInvitacion = fechaInvitacion;
        this.nombreInvitado = nombreInvitado;
    }

    public String getFechaInvitacion() {
        return fechaInvitacion;
    }

    public void setFechaInvitacion(String fechaInvitacion) {
        this.fechaInvitacion = fechaInvitacion;
    }

    public String getNombreInvitado() {
        return nombreInvitado;
    }

    public void setNombreInvitado(String nombreInvitado) {
        this.nombreInvitado = nombreInvitado;
    }
}
