package com.grupo3.backgroundapp.FragmentosAdministrador;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import com.google.firebase.storage.UploadTask;
import com.grupo3.backgroundapp.MainActivity;
import com.grupo3.backgroundapp.MainActivityAdministrador;
import com.grupo3.backgroundapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class PerfilAdmin extends Fragment {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_ADMINISTRADORES;
    StorageReference storageReference;
    String rutaDeAlmacenamiento = "Fotos_perfil_administradores/*"; //crea una carpeta en firebase storage

    private Uri imagen_uri;
    private String imagen_perfil;
    private ProgressDialog progressDialog;

    //declaracion ids de objetos de vista perfil admin
    ImageView FotoPerfilImg;
    TextView UIDPerfil, NombresPerfil, ApellidosPerfil, CorreoPerfil, PasswordPerfil, EdadPerfil;
    Button ActualizarPass, ActualizarDatos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        FotoPerfilImg = view.findViewById(R.id.FotoPerfilImg);

        UIDPerfil = view.findViewById(R.id.UIDPerfil);
        NombresPerfil = view.findViewById(R.id.NombresPerfil);
        ApellidosPerfil = view.findViewById(R.id.ApellidosPerfil);
        CorreoPerfil = view.findViewById(R.id.CorreoPerfil);
        PasswordPerfil = view.findViewById(R.id.PasswordPerfil);
        EdadPerfil = view.findViewById(R.id.EdadPerfil);

        ActualizarPass = view.findViewById(R.id.ActualizarPass);
        ActualizarDatos = view.findViewById(R.id.ActualizarDatos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        BASE_DE_DATOS_ADMINISTRADORES = FirebaseDatabase.getInstance().getReference("BASE DE DATOS ADMINISTRADORES");

        BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //obtener datos
                    String uid = "" + snapshot.child("UID").getValue();
                    String nombre = "" + snapshot.child("NOMBRES").getValue();
                    String apellidos = "" + snapshot.child("APELLIDOS").getValue();
                    String correo = "" + snapshot.child("CORREO").getValue();
                    String pass = "" + snapshot.child("PASSWORD").getValue();
                    String edad = "" + snapshot.child("EDAD").getValue();
                    String imagen = "" + snapshot.child("IMAGEN").getValue();

                    UIDPerfil.setText(uid);
                    NombresPerfil.setText(nombre);
                    ApellidosPerfil.setText(apellidos);
                    CorreoPerfil.setText(correo);
                    PasswordPerfil.setText(pass);
                    EdadPerfil.setText(edad);

                    try {
                        //Si existe la imagen en la bd
                        Picasso.get().load(imagen).placeholder(R.drawable.perfil).into(FotoPerfilImg);
                    } catch (Exception e) {
                        //si no existe en la bd
                        Picasso.get().load(R.drawable.perfil).into(FotoPerfilImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FotoPerfilImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarImagenPerfilAdministrador();
            }
        });

        return view;
    }

    private void CambiarImagenPerfilAdministrador() {
        String [] opcion = {"Cambiar foto de perfil"};

        //crear alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //asignar titulo alert dialog
        builder.setTitle("Elija una opción");
        builder.setItems(opcion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    imagen_perfil = "IMAGEN";
                    Elegirfoto();
                }
            }
        });
        builder.create().show();
    }

    private void Elegirfoto() {
        String [] opciones = {"Cámara","Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar imagen de:");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //al seleccionar elegir imagen desde camara
                if(i == 0) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Elegir_de_camara();
                    } else {
                    SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                    }
                }

                if(i == 1) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Elegir_de_galeria();
                    } else {
                        SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
            }
        });
        builder.create().show();
    }

    private void Elegir_de_galeria() {
        Intent GaleriaIntent = new Intent(Intent.ACTION_PICK);
        GaleriaIntent.setType("image/*");
        ObtenerImagenGaleria.launch(GaleriaIntent);
    }

    private void Elegir_de_camara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto Temporal");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción temporal");
        imagen_uri = (getActivity()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);//Objects.requireNonNull(getActivity()))
        //actividad para abrir la camara
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagen_uri);
        ObtenerImagenCamara.launch(camaraIntent);
    }

    private ActivityResultLauncher<String> SolicitudPermisoCamara = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted) {
            Elegir_de_camara();
        } else {
            Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    });

    private ActivityResultLauncher<String> SolicitudPermisoGaleria = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted) {
            Elegir_de_galeria();
        } else {
            Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    });

    private ActivityResultLauncher<Intent> ObtenerImagenCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                ActualizarImagenBD(imagen_uri);
                progressDialog.setTitle("Procesando");
                progressDialog.setMessage("La imagen se esta actualizando, espere por favor.");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imagen_uri = data.getData();
                        ActualizarImagenBD(imagen_uri);
                        progressDialog.setTitle("Procesando");
                        progressDialog.setMessage("La imagen se esta actualizando, espere por favor.");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    } else {
                        Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void ActualizarImagenBD(Uri uri) {
        String Ruta_de_Archivo_y_nombre = rutaDeAlmacenamiento + "" + imagen_perfil + "-" + user.getUid();
        StorageReference storageReference2 = storageReference.child(Ruta_de_Archivo_y_nombre);
        storageReference2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task <Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(imagen_perfil, downloadUri.toString());
                    BASE_DE_DATOS_ADMINISTRADORES.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            getActivity().finish();
                            Toast.makeText(getActivity(), "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}