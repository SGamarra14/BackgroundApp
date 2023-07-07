package com.grupo3.backgroundapp.Categorias.Cat_Firebase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCF extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderCF.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderCF.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderCF(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });
    }

    public void SeteoCategoriaF(Context context, String categoria, String imagen){
        ImageView ImagenCategoriaF;
        TextView NombreCategoriaF;

        //CONEXION CON EL ITEM
        ImagenCategoriaF = mView.findViewById(R.id.ImagenCategoriaF);
        NombreCategoriaF = mView.findViewById(R.id.NombreCategoriaF);

        NombreCategoriaF.setText(categoria);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaF);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaF);
        }
    }

}
