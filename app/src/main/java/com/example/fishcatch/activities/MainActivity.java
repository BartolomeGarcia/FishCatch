package com.example.fishcatch.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

public class MainActivity extends AppCompatActivity {
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private Button botonAgregarCaptura;
    private Button botonVerCapturas;
    private Button botonEstadisticasPersonales;
    private Button botonEnviarSugerencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asociaciones
        botonAgregarCaptura=findViewById(R.id.buttonAgregarCaptura);
        botonVerCapturas=findViewById(R.id.buttonVerCapturas);
        botonEstadisticasPersonales=findViewById(R.id.buttonEstadisticasPersonales);
        botonEnviarSugerencia = findViewById(R.id.buttonEnviarSugerencia);

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

        //Botón Enviar Sugerencia
        botonEnviarSugerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear el EditText dentro del diálogo
                final EditText input = new EditText(MainActivity.this);
                input.setHint("Escribe tu sugerencia aquí");
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                input.setMinLines(3);
                input.setPadding(40, 30, 40, 30);

                // Crear el diálogo
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Enviar sugerencia")
                        .setMessage("¿Qué te gustaría mejorar o sugerir?")
                        .setView(input)
                        .setPositiveButton("Enviar", (dialog, which) -> {
                            String sugerencia = input.getText().toString().trim();
                            if (!sugerencia.isEmpty()) {
                                Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                                intentEmail.setData(Uri.parse("mailto:bgarciar11@iesarroyoharnina.es")); // Reemplaza con tu correo
                                intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia para FishCatch");
                                intentEmail.putExtra(Intent.EXTRA_TEXT, sugerencia);

                                if (intentEmail.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intentEmail);
                                } else {
                                    Toast.makeText(MainActivity.this, "No se encontró una app de correo instalada.", Toast.LENGTH_SHORT).show();
                                }

                                if (intentEmail.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intentEmail);
                                } else {
                                    Toast.makeText(MainActivity.this, "No hay app de correo instalada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "La sugerencia no puede estar vacía", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }
}
