package com.grupo3.backgroundapp.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.grupo3.backgroundapp.CategoriasAdmin.MusicaA.MusicaA;
import com.grupo3.backgroundapp.CategoriasAdmin.PeliculasA.PeliculasA;
import com.grupo3.backgroundapp.CategoriasAdmin.SeriesA.SeriesA;
import com.grupo3.backgroundapp.CategoriasAdmin.VideojuegosA.VideojuegosA;
import com.grupo3.backgroundapp.R;

public class InicioAdmin extends Fragment {

    Button Peliculas, Series, Musica, Videojuegos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Peliculas = view.findViewById(R.id.Peliculas);
        Series = view.findViewById(R.id.Series);
        Musica = view.findViewById(R.id.Musica);
        Videojuegos = view.findViewById(R.id.Videojuegos);

        Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PeliculasA.class));
            }
        });

        Series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SeriesA.class));
            }
        });

        Musica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MusicaA.class));
            }
        });

        Videojuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VideojuegosA.class));
            }
        });


        return view;
    }
}