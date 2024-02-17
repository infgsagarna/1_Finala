package com.example.a1_finala.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.a1_finala.data.entities.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    public long insertUser(Usuario user);

    @Insert
    public void insertUser(Usuario... user);

    @Query("SELECT * FROM usuarios WHERE usuario_id = :userId")
    Usuario getUserById(long userId);

    @Query("Select nombre from usuarios where usuario_id=:userId")
    public String getUserNameById(long userId);
    @Query("SELECT * FROM usuarios WHERE email = :email")
    public Usuario getUserByEmail(String email);

    @Delete
    void deleteUser(Usuario user);
}
