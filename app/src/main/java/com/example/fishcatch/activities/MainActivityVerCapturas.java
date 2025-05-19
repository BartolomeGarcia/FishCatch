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

import java.util.ArrayList;
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

        spinnerFechasCapturas = findViewById(R.id.spinnerFechasCapturas);
        recyclerViewCapturasPorFecha = findViewById(R.id.recyclerViewCapturasPorFecha);
        recyclerViewCapturasPorFecha.setLayoutManager(new LinearLayoutManager(this));

        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);
        listaFechas = adaptadorBaseDeDatos.obtenerFechasCapturasUnicas();

        if (listaFechas != null && !listaFechas.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    listaFechas
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFechasCapturas.setAdapter(adapter);

            spinnerFechasCapturas.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cargarCapturasParaFecha(listaFechas.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    }
            );
        } else {
            Toast.makeText(this, "No hay capturas registradas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualiza spinner de fechas
        listaFechas = adaptadorBaseDeDatos.obtenerFechasCapturasUnicas();
        if (listaFechas != null && !listaFechas.isEmpty()) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerFechasCapturas.getAdapter();
            adapter.clear();
            adapter.addAll(listaFechas);
            adapter.notifyDataSetChanged();

            int pos = spinnerFechasCapturas.getSelectedItemPosition();
            if (pos >= 0 && pos < listaFechas.size()) {
                cargarCapturasParaFecha(listaFechas.get(pos));
            }
        }
    }

    private void cargarCapturasParaFecha(String fecha) {
        listaCapturas = adaptadorBaseDeDatos.obtenerCapturasPorFecha(fecha);
        if (listaCapturas != null && !listaCapturas.isEmpty()) {
            if (capturaAdapter == null) {
                capturaAdapter = new CapturaAdapter(listaCapturas, this);
                recyclerViewCapturasPorFecha.setAdapter(capturaAdapter);
            } else {
                capturaAdapter.setListaCapturas(listaCapturas);
            }
        } else {
            Toast.makeText(this, "No hay capturas para esta fecha", Toast.LENGTH_SHORT).show();
            if (capturaAdapter != null) {
                capturaAdapter.setListaCapturas(new ArrayList<>());
            }
        }
    }

    //Método para que el adapter actualice spinner y lista al borrar
    public void actualizarSpinnerFechas() {
        listaFechas = adaptadorBaseDeDatos.obtenerFechasCapturasUnicas();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerFechasCapturas.getAdapter();
        adapter.clear();
        adapter.addAll(listaFechas);
        adapter.notifyDataSetChanged();

        int pos = spinnerFechasCapturas.getSelectedItemPosition();
        if (pos >= 0 && pos < listaFechas.size()) {
            cargarCapturasParaFecha(listaFechas.get(pos));
        }
    }
}
