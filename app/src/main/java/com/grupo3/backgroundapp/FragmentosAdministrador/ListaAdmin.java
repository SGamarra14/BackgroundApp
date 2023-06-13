package com.grupo3.backgroundapp.FragmentosAdministrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo3.backgroundapp.Adaptador.Adaptador;
import com.grupo3.backgroundapp.Modelo.Administrador;
import com.grupo3.backgroundapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaAdmin extends Fragment {

    RecyclerView administradores_recyclerView;
    Adaptador adaptador;
    List<Administrador> administradoresList;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_lista_admin, container, false);

        administradores_recyclerView = view.findViewById(R.id.administradores_recyclerView);
        administradores_recyclerView.setHasFixedSize(true);
        administradores_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        administradoresList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        ObtenerLista();

        return view;
    }

    private void ObtenerLista() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                administradoresList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Administrador administrador = ds.getValue(Administrador.class);
                    //se visualizan todos los admin menos el que inicia sesion
                    assert administrador != null;
                    assert user != null;
                    if (!administrador.getUID().equals(user.getUid())) {
                        administradoresList.add(administrador);
                    }
                    //administradoresList.add(administrador);

                    adaptador = new Adaptador(getActivity(), administradoresList);
                    administradores_recyclerView.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BuscarAdministrador(String consulta) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");
        reference.orderByChild("APELLIDOS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                administradoresList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Administrador administrador = ds.getValue(Administrador.class);
                    //se visualizan todos los admin menos el que inicia sesion
                    assert administrador != null;
                    assert user != null;
                    if (!administrador.getUID().equals(user.getUid())) {
                        //buscar por nombre y correo
                        if (administrador.getNOMBRES().toLowerCase().contains(consulta.toLowerCase()) ||
                                administrador.getCORREO().toLowerCase().contains(consulta.toLowerCase())) {
                            administradoresList.add(administrador);
                        }
                    }
                    //administradoresList.add(administrador);

                    adaptador = new Adaptador(getActivity(), administradoresList);
                    administradores_recyclerView.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override //crea el menu
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar, menu);
        MenuItem item = menu.findItem(R.id.buscar_administrador);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String consulta) {
                if (!TextUtils.isEmpty(consulta.trim())) {
                    BuscarAdministrador(consulta);
                } else {
                    ObtenerLista();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String consulta) {
                if (!TextUtils.isEmpty(consulta.trim())) {
                    BuscarAdministrador(consulta);
                } else {
                    ObtenerLista();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override //visualizar el menu
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
}