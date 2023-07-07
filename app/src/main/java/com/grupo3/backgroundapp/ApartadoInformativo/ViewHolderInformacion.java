package com.grupo3.backgroundapp.ApartadoInformativo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderInformacion extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderInformacion.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderInformacion.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderInformacion(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });
    }

    public void SeteoInformacion (Context context, String nombre, String imagen){
        ImageView ImagenInformativo;
        TextView TituloinformativoTXT;

        //CONEXION CON EL ITEM
        ImagenInformativo = mView.findViewById(R.id.ImagenInformativo);
        TituloinformativoTXT = mView.findViewById(R.id.TituloinformativoTXT);

        TituloinformativoTXT.setText(nombre);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenInformativo);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenInformativo);
        }
    }

}
