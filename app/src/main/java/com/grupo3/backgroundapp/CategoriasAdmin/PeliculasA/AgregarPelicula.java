package com.grupo3.backgroundapp.CategoriasAdmin.PeliculasA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grupo3.backgroundapp.CategoriasAdmin.MusicaA.MusicaA;
import com.grupo3.backgroundapp.R;

public class AgregarPelicula extends AppCompatActivity {

    TextView VistaPeliculas;
    EditText NombrePeliculas;
    ImageView ImagenAgregarPelicula;
    Button PublicarPelicula;

    String RutaDeAlmacenamiento = "Pelicula_Subida/";
    String RutaDeBaseDeDatos = "PELICULAS";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;

    int CODIGO_DE_SOLICITUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pelicula);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaPeliculas = findViewById(R.id.VistaPeliculas);
        NombrePeliculas = findViewById(R.id.NombrePeliculas);
        ImagenAgregarPelicula = findViewById(R.id.ImagenAgregarPelicula);
        PublicarPelicula = findViewById(R.id.PublicarPelicula);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarPelicula.this);

        ImagenAgregarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);
            }
        });

        PublicarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubirImagen();
            }
        });
    }

    private void SubirImagen() {
        if(RutaArchivoUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo la imagen PELICULA...");
            progressDialog.show();
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));
            storageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            Uri downloadUri = uriTask.getResult();

                            String mNombre = NombrePeliculas.getText().toString();
                            String mVista = VistaPeliculas.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Pelicula pelicula = new Pelicula(downloadUri.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(pelicula);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarPelicula.this, "Subido exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarPelicula.this, MusicaA.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarPelicula.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressDialog.setTitle("Publicando");
                            progressDialog.setCancelable(false);

                        }
                    });
        }
        else{
            Toast.makeText(this, "DEBE ASIGNAR UNA IMAGEN", Toast.LENGTH_SHORT).show();
        }
    }

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            RutaArchivoUri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                ImagenAgregarPelicula.setImageBitmap(bitmap);

            } catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}