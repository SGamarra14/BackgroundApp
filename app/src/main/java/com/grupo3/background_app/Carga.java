package com.grupo3.background_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Carga extends AppCompatActivity {

    TextView app_name, desarrollador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carga);

        //cambio de letra
        String ubicacion = "fuentes/Akira.otf";
        String ubicacion1 = "fuentes/AkiraBold.otf";
        String ubicacion2 = "fuentes/AkiraOutline.otf";
        String ubicacion3 = "fuentes/AkiraSuperBold.otf";
        Typeface tf = Typeface.createFromAsset(Carga.this.getAssets(),ubicacion);
        Typeface tf1 = Typeface.createFromAsset(Carga.this.getAssets(),ubicacion1);
        Typeface tf2 = Typeface.createFromAsset(Carga.this.getAssets(),ubicacion2);
        Typeface tf3 = Typeface.createFromAsset(Carga.this.getAssets(),ubicacion3);
        //cambio de letra

        app_name = findViewById(R.id.app_name);
        desarrollador = findViewById(R.id.desarrollador);

        final int DURACION = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //CODIGO QUE SE EJECUTARA PASADO LOS 3 SEGS
                Intent intent = new Intent(Carga.this, MainActivityAdministrador.class);
                startActivity(intent);
                finish();
            }
        }, DURACION);

        app_name.setTypeface(tf);
        desarrollador.setTypeface(tf);
    }
}