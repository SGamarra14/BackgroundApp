package com.grupo3.backgroundapp.CategoriasClienteFirebase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

public class ViewHolderImgCatFElegida extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolderImgCatFElegida.ClickListener mClickListener;

    public interface ClickListener{
        void OnItemClick(View view, int position); /*ADMIN PRESIONA EL ITEM*/
    }

    //METODO PARA PODER PRESIONAR O MANTENER PRESIONADO UN ITEM
    public void  setOnClickListener(ViewHolderImgCatFElegida.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderImgCatFElegida(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mClickListener.OnItemClick(view,getBindingAdapterPosition());
            }
        });
    }

    public void SeteoCategoriaFElegida(Context context, String nombre, int vista, String imagen){

        ImageView ImgCatFElegida;
        TextView NombreImg_Cat_Elegida, Vista_Img_Cat_Elegida;

        //CONEXION CON EL ITEM
        ImgCatFElegida = mView.findViewById(R.id.ImgCatFElegida);
        NombreImg_Cat_Elegida = mView.findViewById(R.id.NombreImg_Cat_Elegida);
        Vista_Img_Cat_Elegida = mView.findViewById(R.id.Vista_Img_Cat_Elegida);

        NombreImg_Cat_Elegida.setText(nombre);
        //CONVERTIR A STRING EL PARAMETRO VISTA
        String VistaString = String.valueOf(vista);
        Vista_Img_Cat_Elegida.setText(VistaString);

        //CONTROLAR LOS POSIBLES ERRORES
        try{
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImgCatFElegida);
        }catch(Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImgCatFElegida);
        }
    }
}
