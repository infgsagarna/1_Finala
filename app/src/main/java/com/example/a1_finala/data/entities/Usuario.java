package com.example.a1_finala.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios", indices = {@Index(value = "email", unique = true)},
        foreignKeys = @ForeignKey(
                entity = ListaInvitados.class,
                parentColumns = "lista_id", // Este es el id de la lista de ListaInvitados
                childColumns = "lista_id", // Este es el id de la lista de de esta clase
                onDelete = ForeignKey.CASCADE))
public class Usuario {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "usuario_id")
    private long userId;

    @NonNull
    private String nombre;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    @ColumnInfo(name = "lista_id")
    private long listaId;

    public Usuario() {

    }

    public Usuario(@NonNull String nombre, @NonNull String email, @NonNull String password, long listaId) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.listaId = listaId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public long getListaId() {
        return listaId;
    }

    public void setListaId(long listaId) {
        this.listaId = listaId;
    }
}
