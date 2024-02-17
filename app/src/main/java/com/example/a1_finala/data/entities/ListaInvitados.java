package com.example.a1_finala.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lista_invitados")
public class ListaInvitados {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lista_id")
    private long listaId;

    @NonNull
    @ColumnInfo(name = "nombre_lista_invitados")
    private String nombre;


    @ColumnInfo(name = "fecha_ultima_invitacion")
    private String fechaUltimaInvitacion;

    public ListaInvitados() {

    }

    public ListaInvitados(@NonNull String nombre, String fechaUltimaInvitacion) {
        this.nombre = nombre;
        this.fechaUltimaInvitacion = fechaUltimaInvitacion;
    }

    public long getListaId() {
        return listaId;
    }

    public void setListaId(long listaId) {
        this.listaId = listaId;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public String getFechaUltimaInvitacion() {
        return fechaUltimaInvitacion;
    }

    public void setFechaUltimaInvitacion(String fechaUltimaInvitacion) {
        this.fechaUltimaInvitacion = fechaUltimaInvitacion;
    }
}
