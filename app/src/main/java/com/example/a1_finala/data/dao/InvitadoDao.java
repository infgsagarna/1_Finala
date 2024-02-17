package com.example.a1_finala.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.a1_finala.data.entities.Invitado;

@Dao
public interface InvitadoDao {

    @Insert
    public long insertarInvitado(Invitado invitado);

    @Insert
    public void insertarInvitados(Invitado... invitados);

    /*@Query("Select * from tareas where texto_tarea=:nombreTarea")
    public Tarea getTareaByName(String nombreTarea);*/

    @Query("Select * from invitados where nombre_invitado=:nombreInvitado AND lista_id=:listaId")
    public Invitado getInvitadoByNameAndListaId(String nombreInvitado,long listaId);

    @Delete
    void deleteInvitado(Invitado invitado);
}
