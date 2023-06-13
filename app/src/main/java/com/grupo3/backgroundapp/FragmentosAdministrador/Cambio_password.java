package com.grupo3.backgroundapp.FragmentosAdministrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.grupo3.backgroundapp.InicioSesion;
import com.grupo3.backgroundapp.MainActivity;
import com.grupo3.backgroundapp.MainActivityAdministrador;
import com.grupo3.backgroundapp.R;

import java.util.HashMap;

public class Cambio_password extends AppCompatActivity {

    TextView PassActual;
    EditText ActualPassET, NuevoPassET;
    Button CAMBIARPASSBTN, IRINICIOBTN;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Cambiar Contraseña");

        PassActual = findViewById(R.id.PassActual);
        ActualPassET = findViewById(R.id.ActualPassET);
        NuevoPassET = findViewById(R.id.NuevoPassET);
        CAMBIARPASSBTN = findViewById(R.id.CAMBIARPASSBTN);
        IRINICIOBTN = findViewById(R.id.IRINICIOBTN);

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(Cambio_password.this);

        Query query = BASE_DE_DATOS_ADMINISTRADORES.orderByChild("CORREO").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    pass = "" + ds.child("PASSWORD").getValue();
                    PassActual.setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        CAMBIARPASSBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ACTUAL_PASS = ActualPassET.getText().toString().trim();
                String NUEVO_PASS = NuevoPassET.getText().toString().trim();

                if (TextUtils.isEmpty(ACTUAL_PASS)) {
                    Toast.makeText(Cambio_password.this, "Ingrese su contraseña actual", Toast.LENGTH_SHORT).show();
                }

                if (!ACTUAL_PASS.equals(pass)) {
                    Toast.makeText(Cambio_password.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }

                if (NUEVO_PASS.equals(pass)) {
                    Toast.makeText(Cambio_password.this, "La nueva contraseña debe ser diferente a la anterior", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(NUEVO_PASS)) {
                    Toast.makeText(Cambio_password.this, "Ingrese su nueva contraseña", Toast.LENGTH_SHORT).show();
                }

                if (NUEVO_PASS.length() < 6) {
                    Toast.makeText(Cambio_password.this, "La contraseña debe tener 6 carácteres o más", Toast.LENGTH_SHORT).show();
                }

                if (!NUEVO_PASS.equals(pass) && !ACTUAL_PASS.equals("") && !NUEVO_PASS.equals("") && NUEVO_PASS.length() >= 6) {
                    Cambio_Password(ACTUAL_PASS, NUEVO_PASS);
                } else {
                    NuevoPassET.setFocusable(true);
                }
            }
        });

        IRINICIOBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cambio_password.this, MainActivityAdministrador.class));
                //finish();//???
            }
        });
    }

    private void Cambio_Password(String pass_actual, String nueva_pass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), pass_actual);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(nueva_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        String NUEVO_PASS = NuevoPassET.getText().toString().trim();
                        HashMap<String, Object> resultado = new HashMap<>();
                        resultado.put("PASSWORD", NUEVO_PASS);

                        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(resultado).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Cambio_password.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();

                                //cerrar sesion luego de actualizar contraseña
                                firebaseAuth.signOut();
                                startActivity(new Intent(Cambio_password.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Cambio_password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Cambio_password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Cambio_password.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}