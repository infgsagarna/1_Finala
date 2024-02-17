package com.example.a1_finala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1_finala.data.AppDatabase;
import com.example.a1_finala.data.entities.ListaInvitados;
import com.example.a1_finala.data.entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private AppDatabase bd; // Declarar tu instancia de la base de datos

    private SharedPreferences sharedPreferences;

    private Usuario oUser;

    //String email;
    //String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bd = AppDatabase.getDatabase(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("DatosLogin", Context.MODE_PRIVATE);
        leerDatosLogin();



        setup();


    }

    private void setup() {
        //String title="Autenticación";
        //this.setTitle(title);


        Button botsup=findViewById(R.id.bRegistrar);
        Button botsin=findViewById(R.id.bLogear);


        EditText e=findViewById(R.id.editTextEmail);
        EditText p=findViewById(R.id.editTextPass);



        //email=e.getText().toString();
        //pass=p.getText().toString();



        botsup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //  Log.d(TAG, "checkpoint_signup");
                String email=e.getText().toString();
                String pass=p.getText().toString();
                System.out.println(email);
                System.out.println(pass);
                signUp(email,pass);


                //  escribirDatos(email,pass);
            }

        });

        botsin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //  Log.d(TAG, "checkpoint_signin");
             //   String email=e.getText().toString();
             //   String pass=p.getText().toString();
                String email=e.getText().toString();
                String pass=p.getText().toString();
                System.out.println(email);
                System.out.println(pass);
                signIn(email,pass);
                //  escribirDatos(email,pass);
            }

        });
    }

    public void showAlert(){

        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Chain together various setter methods to set the dialog characteristics
        /*builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });*/
        builder.setPositiveButton("Aceptar", null);
        builder.setMessage("Se ha producido un error autenticando al usuario")
                .setTitle("Error");
        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void signUp(String email, String pass){
        if(!email.isEmpty() && !pass.isEmpty()){
            //mAuth = FirebaseAuth.getInstance();
            // Log.d(TAG, "checkpoint_signup");
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                //showHome(task.getResult().getUser().getEmail(),ProviderType.BASIC);
                                guardarDatos();
                                registrarRoom();
                                nextActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                showAlert();
                            }
                        }
                    });

        }
    }

    private void nextActivity() {
        Intent intent = new Intent(MainActivity.this, ListaInvitadosActivity.class);
        intent.putExtra("userId", oUser.getUserId());
        intent.putExtra("listaId", oUser.getListaId());
        startActivity(intent);
    }

    public void signIn(String email, String pass){
        if(!email.isEmpty() && !pass.isEmpty()){
            //mAuth = FirebaseAuth.getInstance();
            // Log.d(TAG, "checkpoint_signin");
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                //showHome(email,ProviderType.BASIC);
                                //nextActivitira igaro baino lehen oUser behar dugu
                                oUser=bd.usuarioDao().getUserByEmail(email);
                                //Firestoren login berri bat gehitzeko
                                actualizarRegistro();
                                nextActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                showAlert();
                            }
                        }
                    });

        }
    }

    public String fechaActual(){
        String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    private void actualizarRegistro() {
        String fecha=fechaActual();
        Map<String, Object> docData = new HashMap<>();
        docData.put("fecha", fecha);
        EditText etEmail = findViewById(R.id.editTextEmail);
        String email=etEmail.getText().toString();
        // Agregar un documento con un ID autogenerado
        db.collection(email)
                .add(docData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    private void registrarRoom() {
        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextPass);

        // Obtener el texto del EditText
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        oUser = bd.usuarioDao().getUserByEmail(email);

        if (oUser == null ) {
            Log.d(TAG, "Registro:success");
            // Crear usuario



            //***********EL NOMBRE SERA LO INTRODUCIDO ANTES DE LA ARROBA**********///////
            String nombre = email.split("@")[0];

            // Crear lista tarea para el usuario.
            ListaInvitados listaTareas = new ListaInvitados("Lista " + nombre, null);

            long listaId=bd.listaInvitadosDao().insertList(listaTareas);


            //Crear Usuario
            oUser = new Usuario(nombre, email, password,listaId);

            //hasiera batean ez da beharrezkoa
            long userId = bd.usuarioDao().insertUser(oUser);
            //   Log.d(TAG, "  userId: " + userId);
            oUser.setUserId(userId);

            // Mostrar mensaje.
            Toast.makeText(MainActivity.this, "Registrado: " + oUser.getNombre(),
                    Toast.LENGTH_SHORT).show();
            guardarDatos();

            // LLamar a listaCompraActivity.
            /*Intent intent = new Intent(MainActivity.this, ListaInvitadosActivity.class);
            intent.putExtra("userId", oUser.getUserId());
            intent.putExtra("listaId", oUser.getListaId());
            startActivity(intent);*/
        } else {
            Log.d(TAG, "Registro:Failed");
            Toast.makeText(MainActivity.this, "Email ya registrado.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void leerDatosLogin() {

        String email = sharedPreferences.getString("Email", "");
        String password = sharedPreferences.getString("Pass", "");

        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextPass);

        etEmail.setText(email);
        etPass.setText(password);

    }


    private void guardarDatos() {
        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPass = findViewById(R.id.editTextPass);

        // Obtener el texto del EditText
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Pass", password);
        editor.apply();
    }

}