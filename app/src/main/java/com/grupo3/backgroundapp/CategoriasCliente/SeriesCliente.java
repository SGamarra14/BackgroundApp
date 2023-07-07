package com.grupo3.backgroundapp.CategoriasCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grupo3.backgroundapp.CategoriasAdmin.SeriesA.AgregarSerie;
import com.grupo3.backgroundapp.CategoriasAdmin.SeriesA.Serie;
import com.grupo3.backgroundapp.CategoriasAdmin.SeriesA.SeriesA;
import com.grupo3.backgroundapp.CategoriasAdmin.SeriesA.ViewHolderSerie;
import com.grupo3.backgroundapp.DetalleCliente.DetalleCliente;
import com.grupo3.backgroundapp.R;

public class SeriesCliente extends AppCompatActivity {

    RecyclerView recyclerViewSerieC;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Serie, ViewHolderSerie> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Serie> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Series");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewSerieC = findViewById(R.id.recyclerViewSerieC);
        recyclerViewSerieC.setHasFixedSize(true);

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("SERIE");

        dialog = new Dialog(SeriesCliente.this);

        ListarImagenesSerie();
    }

    private void ListarImagenesSerie() {
        options = new FirebaseRecyclerOptions.Builder<Serie>().setQuery(mRef, Serie.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Serie, ViewHolderSerie>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderSerie viewHolderSerie, int i, @NonNull Serie serie) {
                viewHolderSerie.SeteoSeries(
                        getApplicationContext(),
                        serie.getNombre(),
                        serie.getVistas(),
                        serie.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderSerie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //INFLAR EN ITEM
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie,parent,false);
                ViewHolderSerie viewHolderSerie = new ViewHolderSerie(itemView);

                viewHolderSerie.setOnClickListener(new ViewHolderSerie.ClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        //OBTENER LOS DATOS DE LA IMAGEN
                        String Imagen= getItem(position).getImagen();
                        String Nombres= getItem(position).getNombre();
                        int Vistas= getItem(position).getVistas();

                        //CONVERTIR A STRING LA VISTA
                        String VistaString= String.valueOf(Vistas);

                        //PASAMOS A LA ACTIVIDAD DETALLE CLIENTE
                        Intent intent = new Intent(SeriesCliente.this, DetalleCliente.class);

                        //DATOS A PASAR
                        intent.putExtra("Imagen",Imagen);
                        intent.putExtra("Nombre",Nombres);
                        intent.putExtra("Vista",Vistas);

                        startActivity(intent);
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {


                    }
                });
                return viewHolderSerie;
            }
        };
        //al iniciar se lista en 2 columnas
        sharedPreferences = SeriesCliente.this.getSharedPreferences("SERIES", MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "Dos");

        //elegir ordenar por 2 o 3 columnas
        if (ordenar_en.equals("Dos")) {
            recyclerViewSerieC.setLayoutManager(new GridLayoutManager(SeriesCliente.this, 2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewSerieC.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("Tres")) {
            recyclerViewSerieC.setLayoutManager(new GridLayoutManager(SeriesCliente.this, 3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewSerieC.setAdapter(firebaseRecyclerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes() {
        TextView OrdenarTXT;
        Button Dos_Columnas, Tres_Columnas;

        dialog.setContentView(R.layout.dialog_ordenar);

        OrdenarTXT = dialog.findViewById(R.id.OrdenarTXT);
        Dos_Columnas = dialog.findViewById(R.id.Dos_Columnas);
        Tres_Columnas = dialog.findViewById(R.id.Tres_Columnas);

        //evento dos columnas
        Dos_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar", "Dos");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });

        //evento tres columnas
        Tres_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar", "Tres");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}