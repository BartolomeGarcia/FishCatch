package com.example.fishcatch.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;

public class MainActivityAgregarCaptura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button botonAnnadirImagen;
    private ImageView imagenCaptura;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agregar_captura);

        //Asociaciones
        botonAnnadirImagen=findViewById(R.id.buttonAnnadirImagen);
        imagenCaptura=findViewById(R.id.imageViewCaptura);

        //Botón Añadir Imagen
        botonAnnadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Para la imagen de la Captura
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imagenCaptura.setImageURI(imageUri);
        }
    }

    //Para el Spinner de Especies
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
