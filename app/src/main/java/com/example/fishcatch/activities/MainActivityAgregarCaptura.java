package com.example.fishcatch.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fishcatch.R;
import com.example.fishcatch.model.WeatherResponse;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;
import com.example.fishcatch.interfaces.WeatherApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import retrofit2.*;

import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityAgregarCaptura extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button botonAnnadirImagen, botonObtenerUbicacion, botonObtenerTemperatura, botonGuardarCaptura;
    private ImageView imagenCaptura;
    private Spinner spinnerEspecies;
    private EditText peso, tamanno, fechaHora, comentariosAdicionales;
    private TextView ubicacion, temperatura;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private Calendar calendar;
    private int idEspecieSeleccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_agregar_captura);

        // Asociaciones
        botonAnnadirImagen = findViewById(R.id.buttonAnnadirImagen);
        imagenCaptura = findViewById(R.id.imageViewCaptura);
        spinnerEspecies = findViewById(R.id.spinnerEspecies);
        peso = findViewById(R.id.editTextNumberDecimalPeso);
        tamanno = findViewById(R.id.editTextNumberTamanno);
        fechaHora = findViewById(R.id.editTextDateFechaHora);
        ubicacion = findViewById(R.id.textViewUbicacion);
        temperatura = findViewById(R.id.textViewTemperatura);
        comentariosAdicionales = findViewById(R.id.editTextTextComentariosAdicionales);
        botonObtenerUbicacion = findViewById(R.id.buttonObtenerUbicacion);
        botonObtenerTemperatura = findViewById(R.id.buttonObtenerTemperatura);
        botonGuardarCaptura = findViewById(R.id.buttonGuardarCaptura);

        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);
        cargarEspeciesEnSpinner();

        // Imagen
        botonAnnadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        // Fecha y hora
        calendar = Calendar.getInstance();
        fechaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDateTimePicker();
            }
        });

        // Ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        botonObtenerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerUbicacion();
            }
        });

        
        
        // Temperatura
        botonObtenerTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ubicacion.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivityAgregarCaptura.this, "Se necesita una ubicación para obtener la temperatura", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] partes = ubicacion.getText().toString().split(",");
                double lat = Double.parseDouble(partes[0]);
                double lon = Double.parseDouble(partes[1]);
                obtenerTemperatura(lat, lon);
            }
        });

        // Guardar captura
        botonGuardarCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCaptura();
            }
        });
    }

    //PARA LA IMAGEN
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri sourceUri = data.getData();
            try {
                String fileName = "captura_" + System.currentTimeMillis() + ".jpg";
                InputStream inputStream = getContentResolver().openInputStream(sourceUri);
                File file = new File(getFilesDir(), fileName);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();

                Uri savedImageUri = Uri.fromFile(file);
                imagenCaptura.setImageURI(savedImageUri);
                imagenCaptura.setTag(savedImageUri.toString());

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al copiar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //CALENDARIO
    private void mostrarDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view1, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                fechaHora.setText(sdf.format(calendar.getTime()));
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
    }

    //SPINNER ESPECIES
    private void cargarEspeciesEnSpinner() {
        Cursor cursor = adaptadorBaseDeDatos.consultarEspecies();
        ArrayList<String> arrayListEspecies = new ArrayList<>();
        final ArrayList<Integer> idsEspecies = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nombreEspecie = cursor.getString(1);
            arrayListEspecies.add(nombreEspecie);
            idsEspecies.add(id);
        }

        ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListEspecies);
        spinnerEspecies.setAdapter(miAdaptador);
        spinnerEspecies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEspecieSeleccionada = idsEspecies.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idEspecieSeleccionada = -1;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void guardarCaptura() {
        if (idEspecieSeleccionada == -1) {
            Toast.makeText(this, "Selecciona una especie", Toast.LENGTH_SHORT).show();
            return;
        }

        double pesoVal = Double.parseDouble(peso.getText().toString());
        double tamannoValor = Double.parseDouble(tamanno.getText().toString());
        String fechaHoraValor = fechaHora.getText().toString();
        String[] partesFechaHora = fechaHoraValor.split(" ");
        String fechaValor = "";
        String horaValor = "";
        if (partesFechaHora.length > 0) {
            fechaValor = partesFechaHora[0];
        }
        if (partesFechaHora.length > 1) {
            horaValor = partesFechaHora[1];
        }
        
        String comentario = comentariosAdicionales.getText().toString();
        String fotoUri = imagenCaptura.getTag() != null ? imagenCaptura.getTag().toString() : "";

        // Insertar captura
        long idCaptura = adaptadorBaseDeDatos.insertarCaptura(idEspecieSeleccionada, pesoVal, tamannoValor, fechaValor, horaValor, comentario, fotoUri);

        // Insertar ubicación
        if (!ubicacion.getText().toString().isEmpty()) {
            String[] coords = ubicacion.getText().toString().split(",");
            double latitud = Double.parseDouble(coords[0]);
            double longitud = Double.parseDouble(coords[1]);
            adaptadorBaseDeDatos.insertarUbicacion((int) idCaptura, latitud, longitud);
        }

        // Insertar temperatura
        if (!temperatura.getText().toString().isEmpty() && temperatura.getText().toString().contains("°C")) {
            double temp = Double.parseDouble(temperatura.getText().toString().replace(" °C", ""));
            adaptadorBaseDeDatos.insertarCondiciones((int) idCaptura, temp);
        }

        Toast.makeText(this, "Captura guardada", Toast.LENGTH_LONG).show();
        finish();
    }

    //UBICACIÓN
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permiso no concedido", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        ubicacion.setText(lat + "," + lon);
                    } else {
                        Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //TEMPERATURA
    private void obtenerTemperatura(double lat, double lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService apiService = retrofit.create(WeatherApiService.class);
        String apiKey = "944b0efe31fd40ae1a21eff562550ecf";

        Call<WeatherResponse> call = apiService.getCurrentWeather(lat, lon, apiKey, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double temp = response.body().main.temp;
                    temperatura.setText(temp + " °C");
                } else {
                    temperatura.setText("No se pudo obtener");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                temperatura.setText("Error: " + t.getMessage());
            }
        });
    }
}
