package com.grupo3.backgroundapp.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo3.backgroundapp.Detalle.Detalle_Administrador;
import com.grupo3.backgroundapp.Modelo.Administrador;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder>{

    private Context context;
    private List<Administrador> administradores;

    public Adaptador(Context context, List<Administrador> administradores) {
        this.context = context;
        this.administradores = administradores;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflar el Admin layout
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //obtiene datos del modelo
        String UID = administradores.get(position).getUID();
        String IMAGEN = administradores.get(position).getIMAGEN();
        String NOMBRES = administradores.get(position).getNOMBRES();
        String APELLIDOS = administradores.get(position).getAPELLIDOS();
        String CORREO = administradores.get(position).getCORREO();
        int EDAD = administradores.get(position).getEDAD();
        String EdadString = String.valueOf(EDAD);

        //seteo de datos
        holder.NombresADMIN.setText(NOMBRES);
        holder.CorreoADMIN.setText(CORREO);

        try {
            //si existe imagen en bd
            Picasso.get().load(IMAGEN).placeholder(R.drawable.perfil).into(holder.PerfilADMIN);
        } catch (Exception e) {
            //si no existe
            Picasso.get().load(R.drawable.perfil).into(holder.PerfilADMIN);
        }

        //al hacer click en un item admin
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detalle_Administrador.class);
                //se pasan los datos a las sigt actividad
                intent.putExtra("UID", UID);
                intent.putExtra("NOMBRES", NOMBRES);
                intent.putExtra("APELLIDOS", APELLIDOS);
                intent.putExtra("CORREO", CORREO);
                intent.putExtra("EDAD", EdadString);
                intent.putExtra("IMAGEN", IMAGEN);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return administradores.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView PerfilADMIN;
        TextView NombresADMIN, CorreoADMIN;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            PerfilADMIN = itemView.findViewById(R.id.PerfilADMIN);
            NombresADMIN = itemView.findViewById(R.id.NombresAdmin);
            CorreoADMIN = itemView.findViewById(R.id.CorreoAdmin);
        }

    }

}
