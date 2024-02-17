package com.example.a1_finala;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1_finala.data.AppDatabase;
import com.example.a1_finala.data.clase_aux.InvitadosInfo;
import com.example.a1_finala.data.entities.Invitado;
import com.example.a1_finala.data.entities.ListaInvitados;
import com.example.a1_finala.data.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListaInvitadosActivity extends AppCompatActivity {
    private AppDatabase bd; // Declarar tu instancia de la base de datos
    //--

    private static final String TAG = "ListaInvitadosActivity";
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    String nombreInvitado;

    long userId;
    long listaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_invitados);

        // Leer el ID del usuario
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getLongExtra("userId", 0L);
            listaId = intent.getLongExtra("listaId", 0L);
            Toast.makeText(ListaInvitadosActivity.this, "UserID: " + userId,
                    Toast.LENGTH_SHORT).show();
        }


        // Cargar base de datos.
        bd = AppDatabase.getDatabase(this);

        //Bienvenido "usuario"
        String nombre = bd.usuarioDao().getUserNameById(userId);
        TextView tvUsuario = findViewById(R.id.textViewUsuario);
        tvUsuario.setText("Bienvenido "+nombre);

        //Poner el nombre de la lista, para este usuario
        String nombreLista = bd.listaInvitadosDao().getNombreListaById(listaId);
        TextView tvNomLista = findViewById(R.id.textViewNombreLista);
        tvNomLista.setText(nombreLista);

        //Poner la fecha de última tarea, para este usuario
        cambiarFechaUltimaInvitacion();

        leerInvitados();

        // listeners de los botones.
        Button bGuardar = findViewById(R.id.bGuardarInvitado);
        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar Invitados
                guardarInvitado();
            }
        });

        Button bEliminarLista = findViewById(R.id.bEliminarLista);
        bEliminarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Eliminar lista.
                eliminarLista();
            }
        });

        Button bEliminar = findViewById(R.id.bEliminarInvitado);
        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // eliminar Invitado
                eliminarInvitado();
            }
        });
    }

    private void eliminarInvitado() {
        // leer datos introducidos.
        getDatos();
        // crear objeto.
        Invitado oInvitado;
        //oTarea = bd.tareaDao().getTareaByName(nombreTarea);
        oInvitado = bd.invitadoDao().getInvitadoByNameAndListaId(nombreInvitado,listaId);
        if (oInvitado != null) {
            // eliminar tarea.
            bd.invitadoDao().deleteInvitado(oInvitado);
            // actualizar lista tareas.
            leerInvitados();
        } else {
            Toast.makeText(ListaInvitadosActivity.this, "Invitado inexistente: " + nombreInvitado,
                    Toast.LENGTH_SHORT).show();
        }
        cambiarFechaUltimaInvitacion();
    }

    // ORAIN EZABATU BEHARREKOA LISTA DA ETA EZ USUARIO
    private void eliminarLista() {
        Usuario oUser = bd.usuarioDao().getUserById(userId);
        long listaId=oUser.getListaId();
        ListaInvitados listaInvitados=bd.listaInvitadosDao().getListaTareasById(listaId);
        bd.listaInvitadosDao().deleteListaInvitados(listaInvitados);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });

        Toast.makeText(ListaInvitadosActivity.this, "Lista eliminada: " + oUser.getNombre(),
                Toast.LENGTH_SHORT).show();
        finish();

    }

    private void guardarInvitado() {
        // Leer datos introducidos.
        getDatos();

        // crear objeto tarea
        Invitado oInvitado;
        //long listaId=bd.listaTareasDao().getListaIdByUserId(userId);
        long invitadoId;

        //obtener String con fecha actual
        String fecha=fechaActual();

        oInvitado = bd.invitadoDao().getInvitadoByNameAndListaId(nombreInvitado,listaId);
        if (oInvitado == null){ // si es una nueva tarea para esta lista de tareas, meterla en la BS.
            oInvitado = new Invitado(fecha,nombreInvitado,listaId);
            invitadoId = bd.invitadoDao().insertarInvitado(oInvitado);
            bd.listaInvitadosDao().updateListaInvitados(listaId,fecha);
            //  oTarea.setProductoId(prodId);
        }else {
            Toast.makeText(ListaInvitadosActivity.this, "Invitado existente.",
                    Toast.LENGTH_SHORT).show();
        }

        leerInvitados();
        cambiarFechaUltimaInvitacion();
    }

    private String fechaActual() {

        String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;

        //Update de la ultima fecha de actualización de la lista de tareas
    /*    long ahora = System.currentTimeMillis();
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(ahora);
        int d = calendario.get(Calendar.DAY_OF_MONTH);
        String dia=String.valueOf(d);
        if (d < 10)
            dia="0"+d;
        int m = calendario.get(Calendar.MONTH)+1;
        String mes=String.valueOf(m);
        if (m < 10)
            mes="0"+m;
        String anno = String.valueOf(calendario.get(Calendar.YEAR));
        return dia+"/"+mes+"/"+anno;*/

    }

    private void leerInvitados() {

        // long listaId = userId;
        List<InvitadosInfo> InvitadosList =  bd.listaInvitadosDao().getInvitadosInfo(userId);


        String listaInvitados = "";
        for (int i = 0; i < InvitadosList.size();i++) {
            //String tareaInfo = "- "+TareasList.get(i).getNombreListaTareas() + "\n          Última tarea: " + TareasList.get(i).getFechaUltimaTarea()
            String invitadoInfo = "- Nombre: " + InvitadosList.get(i).getNombreInvitado()
                    + "\nFecha: " + InvitadosList.get(i).getFechaInvitacion();
            listaInvitados += invitadoInfo + "\n\n";

        }

        TextView tLista = findViewById(R.id.tLista);
        // Atención!!! operación asíncrona
        tLista.setText(listaInvitados);

    }



    private void getDatos() {

        EditText etTarea = findViewById(R.id.editTextInviado);

        // Obtener el texto del EditText
        nombreInvitado = etTarea.getText().toString();

    }

    public void cambiarFechaUltimaInvitacion(){
        //Poner la fecha de última invitacion, para este usuario
        String fechaUltima = bd.listaInvitadosDao().getUltimaFechaById(listaId);
        TextView tvFechaUlima = findViewById(R.id.textViewFechaUltima);
        tvFechaUlima.setText(fechaUltima);
    }
}
