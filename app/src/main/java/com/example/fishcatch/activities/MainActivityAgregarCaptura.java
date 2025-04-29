package com.example.fishcatch.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivityAgregarCaptura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button botonAnnadirImagen;
    private ImageView imagenCaptura;
    private static final int PICK_IMAGE_REQUEST = 1;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private Spinner spinnerEspecies;
    private EditText peso;
    private EditText tamanno;
    private EditText fechaHora;
    private TextView ubicacion;
    private TextView temperatura;
    private EditText comentariosAdicionales;
    private Button botonObtenerUbicacion;
    private Button botonObtenerTemperatura;
    private Calendar calendar;  //Para la fecha y hora

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
        fechaHora=findViewById(R.id.editTextDateFechaHora);
        ubicacion=findViewById(R.id.textViewUbicacion);
        temperatura=findViewById(R.id.textViewTemperatura);
        comentariosAdicionales=findViewById(R.id.editTextTextComentariosAdicionales);
        botonObtenerUbicacion=findViewById(R.id.buttonObtenerUbicacion);
        botonObtenerTemperatura=findViewById(R.id.buttonObtenerTemperatura);

        //Inicializamos el AdaptadorBaseDeDatos
        adaptadorBaseDeDatos=new AdaptadorBaseDeDatos(this);
        Cursor cursor = adaptadorBaseDeDatos.consultarEspecies();
        ArrayList<String> arrayListEspecies=new ArrayList<>();
        //Recorremos el Cursor y llenamos el ArrayList de Especies
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombreEspecie = cursor.getString(1);
            arrayListEspecies.add(nombreEspecie);
        }
        //Creamos el Adaptador del Spinner
        ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListEspecies);
        spinnerEspecies.setAdapter(miAdaptador);
        spinnerEspecies.setOnItemSelectedListener(this);

        //Botón Añadir Imagen
        botonAnnadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        //Fecha y Hora
        calendar=Calendar.getInstance();
        fechaHora.setOnClickListener(view -> {
            // Mostrar selector de fecha
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivityAgregarCaptura.this,
                    (DatePicker view1, int year, int month, int dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Luego mostrar selector de hora
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                MainActivityAgregarCaptura.this,
                                (TimePicker timeView, int hourOfDay, int minute) -> {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);

                                    // Formatear y mostrar
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                    fechaHora.setText(sdf.format(calendar.getTime()));

                                    // Guardar la fecha/hora en una variable
                                    // Por ejemplo: Date fechaSeleccionada = calendar.getTime();
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );
                        timePickerDialog.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
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