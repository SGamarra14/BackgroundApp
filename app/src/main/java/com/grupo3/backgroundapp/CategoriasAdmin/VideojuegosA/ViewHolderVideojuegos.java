package com.grupo3.backgroundapp.CategoriasAdmin.VideojuegosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderVideojuegos.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA NORMAL EL ITEM*/
        void OnItemLongClick(View view, int position); /*ADMIN MANTIENE PRESIONADO EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderVideojuegos(@NonNull View itemView) {
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
                mClickListener.OnItemClick(view,getAdapterPosition());
                return true;
            }
        });
    }

    public void SeteoVideojuegos(Context context, String nombre, int vista, String imagen){
        ImageView ImagenVideojuegos;
        TextView NombreImagenVideojuegos;
        TextView VistaVideojuegos;

        //CONEXION CON EL ITEM
        ImagenVideojuegos = mView.findViewById(R.id.ImagenVideojuegos);
        NombreImagenVideojuegos = mView.findViewById(R.id.NombreImagenVideojuegos);
        VistaVideojuegos = mView.findViewById(R.id.VistaVideojuegos);


        NombreImagenVideojuegos.setText(nombre);


        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);

        VistaVideojuegos.setText(VistaString);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(ImagenVideojuegos);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
