package com.grupo3.backgroundapp.CategoriasAdmin.SeriesA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderSerie extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderSerie.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA NORMAL EL ITEM*/
        void OnItemLongClick(View view, int position); /*ADMIN MANTIENE PRESIONADO EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderSerie.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderSerie(@NonNull View itemView) {
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

    public void SeteoSeries(Context context, String nombre, int vista, String imagen){
        ImageView Imagen_Serie;
        TextView NombreImagen_Serie;
        TextView Vista_Serie;

        //CONEXION CON EL ITEM
        Imagen_Serie = mView.findViewById(R.id.Imagen_Serie);
        NombreImagen_Serie = mView.findViewById(R.id.NombreImagen_Serie);
        Vista_Serie = mView.findViewById(R.id.Vista_Serie);


        NombreImagen_Serie.setText(nombre);


        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        Vista_Serie.setText(VistaString);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(Imagen_Serie);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
