package com.grupo3.backgroundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity {

    EditText Correo,Password;
    Button Acceder;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        ActionBar actionBar = getSupportActionBar(); //CREAMOS EL ACCIONAR
        assert actionBar != null;  //AFIRMAMOS QUE EL ACTIONBAR NO SE NULO
        actionBar.setTitle("Iniicio sesion"); //LE ASIGNAMOS UN TITULO
        actionBar.setDisplayHomeAsUpEnabled(true); //HABILITAMOS EL BOTON DE RETROCESO
        actionBar.setDisplayShowHomeEnabled(true);

        Correo = findViewById(R.id.Correo);
        Password = findViewById(R.id.Password);
        Acceder = findViewById(R.id.Acceder);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(InicioSesion.this);
        progressDialog.setMessage("Ingresando espere por favor");
        progressDialog.setCancelable(false);

        Acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CONVERTIMOS A STRING EDITTEXT CORREO Y CONTRASEÑA
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Correo.setError("Correo Invalido");
                    Correo.setFocusable(true);
                }
                else if(pass.length()<6){
                    Password.setError("Contraseña debe de ser mayor o igual a 6");
                    Password.setFocusable(true);
                } else{
                    LoginAdministradores(correo,pass);
                }
            }
        });
    }

    private void LoginAdministradores(String correo, String pass) {
        progressDialog.show();
        progressDialog.setCancelable(true);
        firebaseAuth.signInWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(InicioSesion.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //si el inicio de sesion fue exitoso
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(InicioSesion.this,MainActivityAdministrador.class));
                            assert user != null;
                            Toast.makeText(InicioSesion.this,"Bienvenido "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else{
                            progressDialog.dismiss();
                            UsuarioInvalido();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        UsuarioInvalido();
                    }
                });
    }

    private void UsuarioInvalido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InicioSesion.this);
        builder.setCancelable(false);
        builder.setTitle("HA OCURRIDO UN ERROR");
        builder.setMessage("Verifique si el correo o contraseña son correctos")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }
}