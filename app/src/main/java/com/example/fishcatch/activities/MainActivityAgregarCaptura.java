package com.example.fishcatch.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

public class MainActivityAgregarCaptura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button botonAnnadirImagen;
    private ImageView imagenCaptura;
    private static final int PICK_IMAGE_REQUEST = 1;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private Spinner spinnerEspecies;
    private EditText peso;
    private EditText tamanno;
    private EditText fecha;
    private EditText hora;
    private TextView ubicacion;
    private TextView temperatura;
    private EditText comentariosAdicionales;
    private Button botonObtenerUbicacion;
    private Button botonObtenerTemperatura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agregar_captura);

        //Asociaciones
        botonAnnadirImagen=findViewById(R.id.buttonAnnadirImagen);
        imagenCaptura=findViewById(R.id.imageViewCaptura);
        spinnerEspecies=findViewById(R.id.spinnerEspecies);
        peso=findViewById(R.id.editTextNumberDecimalPeso);
        tamanno=findViewById(R.id.editTextNumberTamanno);
        fecha=findViewById(R.id.editTextDateFecha);
        hora=findViewById(R.id.editTextTimeHora);
        ubicacion=findViewById(R.id.textViewUbicacion);
        temperatura=findViewById(R.id.textViewTemperatura);
        comentariosAdicionales=findViewById(R.id.editTextTextComentariosAdicionales);
        botonObtenerUbicacion=findViewById(R.id.buttonObtenerUbicacion);
        botonObtenerTemperatura=findViewById(R.id.buttonObtenerTemperatura);

        //Inicializamos el AdaptadorBaseDeDatos
        adaptadorBaseDeDatos=new AdaptadorBaseDeDatos(this);
        Cursor cursor = adaptadorBaseDeDatos.consultarEspecies();

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