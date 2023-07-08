package com.grupo3.backgroundapp.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.grupo3.backgroundapp.InicioSesion;
import com.grupo3.backgroundapp.R;

public class Login_admin extends Fragment {

    Button Acceder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);

        Acceder = view.findViewById(R.id.Acceder);

        Acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InicioSesion.class));

                /*Intent intent = new Intent(getActivity(),InicioSesion.class);
                startActivity(intent);*/
            }
        });

        return view;
    }
}