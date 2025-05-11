package com.example.fishcatch.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishcatch.R;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.recyclerviewadapter.CapturaAdapter;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import java.util.List;

public class MainActivityVerCapturas extends AppCompatActivity {
    private Spinner spinnerFechasCapturas;
    private RecyclerView recyclerViewCapturasPorFecha;
    private List<String> listaFechas;
    private List<Captura> listaCapturas;
    private CapturaAdapter capturaAdapter;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ver_capturas);

        //Asociaciones
        spinnerFechasCapturas = findViewById(R.id.spinnerFechasCapturas);
        recyclerViewCapturasPorFecha = findViewById(R.id.recyclerViewCapturasPorFecha);

        recyclerViewCapturasPorFecha.setLayoutManager(new LinearLayoutManager(this));

        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);

        listaFechas = adaptadorBaseDeDatos.obtenerFechasCapturasUnicas();

        if (listaFechas != null && !listaFechas.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaFechas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFechasCapturas.setAdapter(adapter);

            spinnerFechasCapturas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < listaFechas.size()) {
                        String fechaSeleccionada = listaFechas.get(position);

                        //Capturas para esa fecha
                        listaCapturas = adaptadorBaseDeDatos.obtenerCapturasPorFecha(fechaSeleccionada);

                        //Si hay capturas para esa fecha, actualiza el RecyclerView
                        if (listaCapturas != null && !listaCapturas.isEmpty()) {
                            //Si tenemos un Adapter asignado, actualiza los datos
                            if (capturaAdapter != null) {
                                capturaAdapter.setListaCapturas(listaCapturas);
                                capturaAdapter.notifyDataSetChanged();
                            } else {
                                // Si aún no has asignado el Adapter, asigna uno nuevo
                                capturaAdapter = new CapturaAdapter(listaCapturas, MainActivityVerCapturas.this);
                                recyclerViewCapturasPorFecha.setAdapter(capturaAdapter);
                            }
                        } else {
                            Toast.makeText(MainActivityVerCapturas.this, "No hay capturas para esta fecha", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            Toast.makeText(this, "No hay capturas registradas", Toast.LENGTH_SHORT).show(); //// Si no hay fechas disponibles
        }
    }

    public void actualizarSpinnerFechas() {
        listaFechas = adaptadorBaseDeDatos.obtenerFechasCapturasUnicas();
        if (listaFechas != null && !listaFechas.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaFechas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFechasCapturas.setAdapter(adapter);
        } else {
            Toast.makeText(MainActivityVerCapturas.this, "No hay fechas disponibles", Toast.LENGTH_SHORT).show();
        }
    }
}
