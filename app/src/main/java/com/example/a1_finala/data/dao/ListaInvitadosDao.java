package com.example.a1_finala.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.a1_finala.data.clase_aux.InvitadosInfo;
import com.example.a1_finala.data.entities.Invitado;
import com.example.a1_finala.data.entities.ListaInvitados;

import java.util.List;

@Dao
public interface ListaInvitadosDao {

    @Insert
    public long insertList(ListaInvitados listaInvitados);


    //***********BEHARREZKOA LISTATAREAS EZABATZEKO
    @Query("SELECT * FROM lista_invitados WHERE lista_id = :listaId")
    public ListaInvitados getListaTareasById(long listaId);

    @Query("SELECT nombre_invitado as nombreInvitado, fecha_invitacion as fechaInvitacion" +
            " FROM invitados WHERE lista_id=:listaId")
    public List<InvitadosInfo> getInvitadosInfo(long listaId);


    @Query("SELECT * FROM invitados WHERE lista_id=:listaId")
    public List<Invitado> getInvitados(long listaId);


    @Query("select fecha_ultima_invitacion from lista_invitados where lista_id=:listaId")
    public String getUltimaFechaById(long listaId);
    @Query("select nombre_lista_invitados  from lista_invitados where lista_id=:listaId")
    public String getNombreListaById(long listaId);


    @Query("UPDATE lista_invitados SET fecha_ultima_invitacion= :fecha WHERE lista_id = :listId")
    public void updateListaInvitados(long listId, String fecha);
    @Delete
    void deleteListaInvitados(ListaInvitados lInvitados);
}
