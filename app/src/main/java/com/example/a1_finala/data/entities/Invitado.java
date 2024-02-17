package com.example.a1_finala.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "invitados",
        foreignKeys = @ForeignKey(
                entity = ListaInvitados.class,
                parentColumns = "lista_id",
                childColumns = "lista_id",
                onDelete = ForeignKey.CASCADE))
public class Invitado {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "invitado_id")
    private long invitadoId;

    @ColumnInfo(name = "fecha_invitacion")
    private String fechaInvitacion;

    @ColumnInfo(name = "nombre_invitado")
    private String nombre;

    @NonNull
    @ColumnInfo(name = "lista_id")
    private long listaId;

    public Invitado() {

    }

    public Invitado(String fechaInvitacion, String nombre, long listaId) {
        this.fechaInvitacion = fechaInvitacion;
        this.nombre = nombre;
        this.listaId = listaId;
    }

    public long getInvitadoId() {
        return invitadoId;
    }

    public void setInvitadoId(long invitadoId) {
        this.invitadoId = invitadoId;
    }

    public String getFechaInvitacion() {
        return fechaInvitacion;
    }

    public void setFechaInvitacion(String fechaInvitacion) {
        this.fechaInvitacion = fechaInvitacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getListaId() {
        return listaId;
    }

    public void setListaId(long listaId) {
        this.listaId = listaId;
    }
}
