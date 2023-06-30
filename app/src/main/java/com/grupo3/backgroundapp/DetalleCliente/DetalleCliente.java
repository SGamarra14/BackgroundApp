package com.grupo3.backgroundapp.DetalleCliente;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.snapshot.StringNode;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class DetalleCliente extends AppCompatActivity {
    ImageView ImagenDetalle;
    TextView NombreImagenDetalle;
    TextView VistaDetalle;

    FloatingActionButton fabDescargar, fabCompartir, fabEstablecer;

    Bitmap bitmap;

    private  Uri imageUri=null;

    //SOLICITUD DE ALMACENAMIENTO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cliente);

        ActionBar actionBar= getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalle");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenDetalle=findViewById(R.id.ImagenDetalle);
        NombreImagenDetalle=findViewById(R.id.NombreImagenDetalle);
        VistaDetalle=findViewById(R.id.VistaDetalle);

        fabDescargar=findViewById(R.id.fabDescargar);
        fabCompartir=findViewById(R.id.fabCompartir);
        fabEstablecer=findViewById(R.id.fabEstablecer);

        String Imagen= getIntent().getStringExtra("Imagen");
        String Nombre= getIntent().getStringExtra("Nombre");
        String Vista= getIntent().getStringExtra("Vista");

        try {
            Picasso.get().load(Imagen).placeholder(R.drawable.detalle_imagen).into(ImagenDetalle);
        }catch (Exception e){
            Picasso.get().load(R.drawable.detalle_imagen).into(ImagenDetalle);
        }

        NombreImagenDetalle.setText(Nombre);
        VistaDetalle.setText(Vista);

        fabDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SI LA VERSION DE ANDROID ES MAYOR O IGUAL A 11
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    if(ContextCompat.checkSelfPermission(DetalleCliente.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        DecargarImagen_11();
                    }
                    else{
                        SolicitudPermisoDescargaAndorid110Superior.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                //SI LA VERSION ES MAYOR O IGUAL A 6
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //SI EL PERMISO ES DENEGADO
                if(ContextCompat.checkSelfPermission(DetalleCliente.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    DecargarImagen();
                }
                else{
                    SolicitudPermisoDescarga.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                }
            //SI LA VERSION DE ANDROID ES MENOR A 6
            else{
                DecargarImagen();
            }
            }
        });

        fabCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompartirImagen_Actualizado();
            }
        });

        fabEstablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EstablecerImagen();
            }
        });

        StrictMode.VmPolicy.Builder builder= new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    private void DecargarImagen_11() {
        bitmap= ((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
        OutputStream fos;
        //Obtenemos el nombre de la imagen
        String nombre_imagen= NombreImagenDetalle.getText().toString();
        try {
            ContentResolver resolver= getContentResolver();
            ContentValues contentValues= new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,nombre_imagen);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+File.separator+"/BACKGROUNDAPP/");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos= resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            Objects.requireNonNull(fos);
            Toast.makeText(this,"Imagen descargada con exito", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"No se pudo descaragar la imagen"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private  void DecargarImagen(){
        //Obtener el mapa de bits de la imagen
        bitmap=((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();

        //OBTENER FECHA DE DESCARGA
        String FechaDescarga= new SimpleDateFormat("'Fecha Descarga: ' yyyy_MM_dd 'Hora: ' HH:mm:ss",
                Locale.getDefault()).format(System.currentTimeMillis());

        //DEFINIR LA RUTA DEL ALMACENAMIENTO
        File ruta = Environment.getExternalStorageDirectory();
        //DEFINIR EL NOMBRE DE LA CARPETA
        File NombreCarpeta= new File(ruta+"/BACKGROUNDAPP/");
        NombreCarpeta.mkdir();
        //Definir el nombre de la imagen descargada
        String ObtenerNombreImagen=NombreImagenDetalle.getText().toString();
        String NombreImagen = ObtenerNombreImagen+" "+FechaDescarga+".JPEG";
        File file= new File(NombreCarpeta,NombreImagen);
        OutputStream outputStream;
        try {
            outputStream= new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, "LA IMAGEN SE HA DESCRAGADO CON EXITO", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private  void CompartirImagen(){
        try {
            //OBTENER EL MAPA DE BITS
            bitmap=((BitmapDrawable) ImagenDetalle.getDrawable()).getBitmap();

            //OBTENER EL NOMBRE DE LA IMAGEN
            String NombbreImagen=NombreImagenDetalle.getText().toString();
            File file = new File(getExternalCacheDir(),NombbreImagen+"JPEG");

            //FLUJO DE SALIDA
            FileOutputStream fileOutputStream= new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,true);

            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpeg");
            startActivity(Intent.createChooser(intent,"Compartir"));

        }catch (Exception e){
            Toast.makeText(DetalleCliente.this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void EstablecerImagen(){
        //OBTENER EL MAPA DE BITS
        bitmap=((BitmapDrawable)ImagenDetalle.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager= WallpaperManager.getInstance(getApplicationContext());

        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this,"Establecido con éxito",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void CompartirImagen_Actualizado(){
        Uri contentUri= getContentUri();
        Intent sharedIntent= new Intent(Intent.ACTION_SEND);

        sharedIntent.setType("image/jpeg");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT,"Asunto");
        sharedIntent.putExtra(Intent.EXTRA_STREAM,contentUri);
        sharedIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(sharedIntent);
    }

    private Uri getContentUri(){
        BitmapDrawable bitmapDrawable=(BitmapDrawable)ImagenDetalle.getDrawable();
        bitmap=bitmapDrawable.getBitmap();
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                ImageDecoder.Source source= ImageDecoder.createSource(getContentResolver(),imageUri);
                bitmap= ImageDecoder.decodeBitmap(source);
            }
            else{
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            }
        }catch (Exception e){

        }

        File imageFolder= new File(getCacheDir(),"images");
        Uri contentUri=null;
        try {
            imageFolder.mkdirs();
            File file= new File(imageFolder,"shared_image.png");
            FileOutputStream stream= new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            stream.flush();
            stream.close();
            contentUri= FileProvider.getUriForFile(this,"com.grupo3.backgroundapp.fileprovider",file);
        }catch (Exception e){

        }

        return contentUri;
    }

    private ActivityResultLauncher<String> SolicitudPermisoDescarga=registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        //SI EL PERMISO PARA DESCARGAR IMAGEN ES EXITOSO
        if(isGranted){
            DecargarImagen();
        }else{
            Toast.makeText(this, "El permiso a sido denegado", Toast.LENGTH_SHORT).show();
        }
    });

    private ActivityResultLauncher<String> SolicitudPermisoDescargaAndorid110Superior=registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if(isGranted){
                    DecargarImagen_11();
                }else{
                    Toast.makeText(this, "El permiso a sido denegado", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}