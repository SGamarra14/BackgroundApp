package com.grupo3.backgroundapp.CategoriasAdmin.PeliculasA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.play.integrity.internal.m;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderPelicula extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderPelicula.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA NORMAL EL ITEM*/
        void OnItemLongClick(View view, int position); /*ADMIN MANTIENE PRESIONADO EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderPelicula.ClickListener clickListener){
        mClickListener = clickListener;
    }

        public ViewHolderPelicula(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnItemLongClick(view,getAdapterPosition());
                return true;
            }
        });
    }

    public void SeteoPeliculas(Context context, String nombre, int vista, String imagen){
        ImageView ImagenPelicula;
        TextView NombreImagenPelicula;
        TextView VistaPelicula;

        //CONEXION CON EL ITEM
        ImagenPelicula = mView.findViewById(R.id.ImagenPelicula);
        NombreImagenPelicula = mView.findViewById(R.id.NombreImagenPelicula);
        VistaPelicula = mView.findViewById(R.id.VistaPelicula);


        NombreImagenPelicula.setText(nombre);


        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        VistaPelicula.setText(VistaString);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(ImagenPelicula);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
