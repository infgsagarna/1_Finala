package com.example.a1_finala.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.a1_finala.data.dao.InvitadoDao;
import com.example.a1_finala.data.dao.ListaInvitadosDao;
import com.example.a1_finala.data.dao.UsuarioDao;
import com.example.a1_finala.data.entities.Invitado;
import com.example.a1_finala.data.entities.ListaInvitados;
import com.example.a1_finala.data.entities.Usuario;

@Database(entities = {Usuario.class, ListaInvitados.class, Invitado.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract InvitadoDao invitadoDao();
    public abstract ListaInvitadosDao listaInvitadosDao();


    // Puedes crear una instancia única de la base de datos (Patrón Singleton)
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "lista_invitados_database").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
