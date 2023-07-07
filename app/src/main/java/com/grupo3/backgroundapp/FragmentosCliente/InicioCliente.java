package com.grupo3.backgroundapp.FragmentosCliente;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grupo3.backgroundapp.ApartadoInformativo.Informacion;
import com.grupo3.backgroundapp.ApartadoInformativo.ViewHolderInformacion;
import com.grupo3.backgroundapp.Categorias.Cat_Dispositivo.CategoriaD;
import com.grupo3.backgroundapp.Categorias.Cat_Dispositivo.ViewHolderCD;
import com.grupo3.backgroundapp.Categorias.Cat_Firebase.CategoriaF;
import com.grupo3.backgroundapp.Categorias.Cat_Firebase.ViewHolderCF;
import com.grupo3.backgroundapp.Categorias.ControladorCD;
import com.grupo3.backgroundapp.CategoriasClienteFirebase.ListaCategoriaFirebase;
import com.grupo3.backgroundapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InicioCliente extends Fragment {

    RecyclerView recyclerViewCategoriasD, recyclerViewCategoriasF, recyclerViewInfo;
    FirebaseDatabase firebaseDatabaseD, firebaseDatabaseF, firebaseDatabaseInfo;
    DatabaseReference referenceD, referenceF, referenceInfo;
    LinearLayoutManager linearLayoutManagerD, linearLayoutManagerF, linearLayoutManagerInfo;

    FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD> firebaseRecyclerAdapterD;
    FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF> firebaseRecyclerAdapterF;
    FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion> firebaseRecyclerAdapterInfo;

    FirebaseRecyclerOptions<CategoriaD> optionsD;
    FirebaseRecyclerOptions<CategoriaF> optionsF;
    FirebaseRecyclerOptions<Informacion> optionsInfo;

    TextView fecha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_inicio_cliente, container, false);

        firebaseDatabaseD=FirebaseDatabase.getInstance();
        firebaseDatabaseF=FirebaseDatabase.getInstance();
        firebaseDatabaseInfo=FirebaseDatabase.getInstance();

        referenceD=firebaseDatabaseD.getReference("CATEGORIAS_D");
        referenceF=firebaseDatabaseF.getReference("CATEGORIAS_F");
        referenceInfo=firebaseDatabaseInfo.getReference("INFORMACION");

        linearLayoutManagerD=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManagerF=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManagerInfo=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        recyclerViewCategoriasD=view.findViewById(R.id.recyclerViewCategoriasD);
        recyclerViewCategoriasD.setHasFixedSize(true);
        recyclerViewCategoriasD.setLayoutManager(linearLayoutManagerD);

        recyclerViewCategoriasF=view.findViewById(R.id.recyclerViewCategoriasF);
        recyclerViewCategoriasF.setHasFixedSize(true);
        recyclerViewCategoriasF.setLayoutManager(linearLayoutManagerF);

        recyclerViewInfo=view.findViewById(R.id.recyclerViewInfo);
        recyclerViewInfo.setHasFixedSize(true);
        recyclerViewInfo.setLayoutManager(linearLayoutManagerInfo);

        fecha = view.findViewById(R.id.fecha);

        //FECHA ACTUAL
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");
        String StringFecha = simpleDateFormat.format(date);
        fecha.setText(StringFecha);

        VerCategoriasD();
        VerCategoriasF();
        VerApartadoInformativo();

        return view;
    }



    private void VerCategoriasD(){
        optionsD=new FirebaseRecyclerOptions.Builder<CategoriaD>().setQuery(referenceD,CategoriaD.class).build();
        firebaseRecyclerAdapterD= new FirebaseRecyclerAdapter<CategoriaD, ViewHolderCD>(optionsD) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCD viewHolderCD, int position, @NonNull CategoriaD categoriaD) {
                viewHolderCD.SeteoCategoriaD(
                        getActivity(),
                        categoriaD.getCategoria(),
                        categoriaD.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EL LAYOUT
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_dispositivo,parent,false);
                ViewHolderCD viewHolderCD= new ViewHolderCD(itemView);

                viewHolderCD.setOnClickListener(new ViewHolderCD.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                        //OBTENEMOS EL NOMBRE DE LA CATEGORIA
                        String categoria= getItem(position).getCategoria();

                        //PASAMOS EL NOMBRE DE LA CATEGORIA
                        Intent intent=new Intent(view.getContext(), ControladorCD.class);
                        intent.putExtra("Categoria",categoria);
                        startActivity(intent);
                        Toast.makeText(getActivity(), categoria, Toast.LENGTH_SHORT).show();
                    }
                });


                return viewHolderCD;
            }
        };
        recyclerViewCategoriasD.setAdapter(firebaseRecyclerAdapterD);
    }

    private void VerCategoriasF() {
        optionsF=new FirebaseRecyclerOptions.Builder<CategoriaF>().setQuery(referenceD,CategoriaF.class).build();
        firebaseRecyclerAdapterF= new FirebaseRecyclerAdapter<CategoriaF, ViewHolderCF>(optionsF) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderCF viewHolderCF, int position, @NonNull CategoriaF categoriaF) {
                viewHolderCF.SeteoCategoriaF(
                        getActivity(),
                        categoriaF.getCategoria(),
                        categoriaF.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderCF onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EL LAYOUT
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_firebase,parent,false);
                ViewHolderCF viewHolderCF= new ViewHolderCF(itemView);

                viewHolderCF.setOnClickListener(new ViewHolderCF.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {

                        //OBTENER EL NOMBRE DE LA CATEGORIA
                        String NOMBRE_CATEGORIA = getItem(position).getCategoria();

                        //PASAR EL NOMBRE DE LA CATEGORIA A LA SGTE ACTIVIDAD
                        Intent intent = new Intent(view.getContext(), ListaCategoriaFirebase.class);
                        intent.putExtra("NOMBRE_CATEGORIA", NOMBRE_CATEGORIA);
                        Toast.makeText(view.getContext(), "CATEGORIA SELECCIONADA = "+ NOMBRE_CATEGORIA, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });


                return viewHolderCF;
            }
        };
        recyclerViewCategoriasF.setAdapter(firebaseRecyclerAdapterF);
    }


    private void VerApartadoInformativo() {
        optionsInfo=new FirebaseRecyclerOptions.Builder<Informacion>().setQuery(referenceInfo,Informacion.class).build();
        firebaseRecyclerAdapterInfo = new FirebaseRecyclerAdapter<Informacion, ViewHolderInformacion>(optionsInfo) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderInformacion viewHolderInformacion, int position, @NonNull Informacion informacion) {
                viewHolderInformacion.SeteoInformacion(
                        getActivity(),
                        informacion.getNombre(),
                        informacion.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderInformacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EL LAYOUT
                View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.apartado_informativo,parent,false);
                ViewHolderInformacion viewHolderInformacion= new ViewHolderInformacion(itemView);

                viewHolderInformacion.setOnClickListener(new ViewHolderInformacion.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {


                    }
                });


                return viewHolderInformacion;
            }
        };
        recyclerViewInfo.setAdapter(firebaseRecyclerAdapterInfo);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapterD!=null && firebaseRecyclerAdapterF!=null && firebaseRecyclerAdapterInfo!=null){
            firebaseRecyclerAdapterD.startListening();
            firebaseRecyclerAdapterF.startListening();
            firebaseRecyclerAdapterInfo.startListening();
        }
    }
}