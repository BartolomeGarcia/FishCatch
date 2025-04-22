package com.example.fishcatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

public class MainActivity extends AppCompatActivity {
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private Button botonAgregarCaptura;
    private Button botonVerCapturas;
    private Button botonEstadisticasPersonales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asociaciones
        botonAgregarCaptura=findViewById(R.id.buttonAgregarCaptura);
        botonVerCapturas=findViewById(R.id.buttonVerCapturas);
        botonEstadisticasPersonales=findViewById(R.id.buttonEstadisticasPersonales);

        //Instanciación AdaptadorBaseDeDatos
        adaptadorBaseDeDatos=new AdaptadorBaseDeDatos(this);

        //Botón Agregar Captura
        botonAgregarCaptura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAgregarCaptura=new Intent(MainActivity.this,MainActivityAgregarCaptura.class);
                intentAgregarCaptura.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAgregarCaptura);
            }
        });

        //Botón Ver Capturas
        botonVerCapturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVerCapturas=new Intent(MainActivity.this,MainActivityVerCapturas.class);
                intentVerCapturas.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentVerCapturas);
            }
        });
        //Botón Estadísticas Personales
        botonEstadisticasPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEstadisticasPersonales=new Intent(MainActivity.this,MainActivityEstadisticasPersonales.class);
                intentEstadisticasPersonales.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentEstadisticasPersonales);
            }
        });
    }
}
