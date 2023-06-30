package com.grupo3.backgroundapp.Categorias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.grupo3.backgroundapp.CategoriasCliente.MusicaCliente;
import com.grupo3.backgroundapp.CategoriasCliente.PeliculasCliente;
import com.grupo3.backgroundapp.CategoriasCliente.SeriesCliente;
import com.grupo3.backgroundapp.CategoriasCliente.VideojuegosCliente;
import com.grupo3.backgroundapp.R;

public class ControladorCD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_cd);

        String CategoriaRecuperada= getIntent().getStringExtra("Categoria");

        if(CategoriaRecuperada.equals("Peliculas")){
            startActivity(new Intent(ControladorCD.this, PeliculasCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Series")){
            startActivity(new Intent(ControladorCD.this, SeriesCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Musica")){
            startActivity(new Intent(ControladorCD.this, MusicaCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Videojuegos")){
            startActivity(new Intent(ControladorCD.this, VideojuegosCliente.class));
            finish();
        }
    }
}