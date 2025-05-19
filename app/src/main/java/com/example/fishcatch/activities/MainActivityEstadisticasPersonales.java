package com.example.fishcatch.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivityEstadisticasPersonales extends AppCompatActivity {

    private TextView totalCapturas;
    private TextView especieMasCapturada;
    private TextView capturasPorEspecie;
    private TextView diaMasExitoso;
    private TextView mayorPesoConUbicacion;
    private TextView mayorPesoSinUbicacion;
    private Button botonAbrirUbicacionGoogleMaps;
    private Spinner spinnerAnios;
    private Button buttonGenerarPdf;

    private Captura capturaConUbicacion;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estadisticas_personales);

        // Enlazar vistas
        totalCapturas = findViewById(R.id.textViewTotalCapturas);
        especieMasCapturada = findViewById(R.id.textViewEspecieMasCapturada);
        capturasPorEspecie = findViewById(R.id.textViewCapturasPorEspecie);
        diaMasExitoso = findViewById(R.id.textViewDiaMasExitoso);
        mayorPesoConUbicacion = findViewById(R.id.textViewMayorPesoConUbicacion);
        mayorPesoSinUbicacion = findViewById(R.id.textViewMayorPesoSinUbicacion);
        botonAbrirUbicacionGoogleMaps = findViewById(R.id.buttonAbrirMapaUbicacion);
        spinnerAnios = findViewById(R.id.spinnerAnios);
        buttonGenerarPdf = findViewById(R.id.buttonGenerarPdf);

        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);

        // Cargar años disponibles en el spinner
        List<String> listaAnios = adaptadorBaseDeDatos.obtenerAnnosDisponibles();
        listaAnios.add(0, "Todos");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listaAnios);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnios.setAdapter(adapterSpinner);

        spinnerAnios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String anioSeleccionado = spinnerAnios.getSelectedItem().toString();
                if (anioSeleccionado.equals("Todos")) {
                    anioSeleccionado = null;
                }
                actualizarEstadisticas(anioSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        botonAbrirUbicacionGoogleMaps.setOnClickListener(v -> {
            if (capturaConUbicacion != null) {
                double lat = capturaConUbicacion.getLatitud();
                double lon = capturaConUbicacion.getLongitud();
                String uri = "geo:" + lat + "," + lon + "?q=" + lat + "," + lon;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });

        buttonGenerarPdf.setOnClickListener(v -> {
            String anioSeleccionado = spinnerAnios.getSelectedItem().toString();
            if (anioSeleccionado.equals("Todos")) {
                anioSeleccionado = null;
            }
            mostrarDialogoConfirmacion(anioSeleccionado);
        });
    }

    private void actualizarEstadisticas(String anio) {
        int total = adaptadorBaseDeDatos.obtenerNumeroTotalCapturas(anio);
        String especieMas = adaptadorBaseDeDatos.obtenerEspecieMasCapturada(anio);
        List<String> listaEspecies = adaptadorBaseDeDatos.obtenerCapturasPorEspecie(anio);
        String diaExitoso = adaptadorBaseDeDatos.obtenerDiaMasExitoso(anio);

        capturaConUbicacion = adaptadorBaseDeDatos.obtenerCapturaMayorPesoConUbicacion(anio);
        Captura capturaSinUbicacion = adaptadorBaseDeDatos.obtenerCapturaMayorPesoSinUbicacion(anio);

        // Actualizar textos como antes
        totalCapturas.setText("Total de capturas: " + total);
        if (especieMas != null) {
            especieMasCapturada.setText("Especie más capturada: " + especieMas);
        } else {
            especieMasCapturada.setText("Especie más capturada: N/A");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String especie : listaEspecies) {
            stringBuilder.append("• ").append(especie).append("\n");
        }
        capturasPorEspecie.setText("Capturas por especie:\n" + stringBuilder.toString().trim());

        if (diaExitoso != null) {
            diaMasExitoso.setText("Día más exitoso: " + diaExitoso);
        } else {
            diaMasExitoso.setText("Día más exitoso: N/A");
        }

        if (capturaConUbicacion != null) {
            String texto = "Captura con mayor peso (con ubicación):\n"
                    + "Especie: " + capturaConUbicacion.getNombreEspecie() + "\n"
                    + "Peso: " + capturaConUbicacion.getPeso() + " kg\n"
                    + "Fecha: " + capturaConUbicacion.getFecha() + "\n"
                    + "Ubicación: Lat " + capturaConUbicacion.getLatitud()
                    + ", Lon " + capturaConUbicacion.getLongitud();
            mayorPesoConUbicacion.setText(texto);
            botonAbrirUbicacionGoogleMaps.setEnabled(true);
        } else {
            mayorPesoConUbicacion.setText("No hay capturas con ubicación.");
            botonAbrirUbicacionGoogleMaps.setEnabled(false);
        }

        if (capturaSinUbicacion != null) {
            String texto = "Captura con mayor peso (sin ubicación):\n"
                    + "Especie: " + capturaSinUbicacion.getNombreEspecie() + "\n"
                    + "Peso: " + capturaSinUbicacion.getPeso() + " kg\n"
                    + "Fecha: " + capturaSinUbicacion.getFecha();
            mayorPesoSinUbicacion.setText(texto);
        } else {
            mayorPesoSinUbicacion.setText("No hay capturas sin ubicación.");
        }

        // Aquí deshabilitamos el botón si no hay datos
        // Puedes usar total para validar, si no hay capturas, total == 0
        if (total > 0) {
            buttonGenerarPdf.setEnabled(true);
        } else {
            buttonGenerarPdf.setEnabled(false);
        }
    }


    private void mostrarDialogoConfirmacion(String anio) {
        String mensaje;
        if (anio == null) {
            mensaje = "¿Quieres generar un PDF con las estadísticas de todos los años?";
        } else {
            mensaje = "¿Quieres generar un PDF con las estadísticas del año " + anio + "?";
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar generación de PDF")
                .setMessage(mensaje)
                .setPositiveButton("Sí", (dialog, which) -> generarPdf(anio))
                .setNegativeButton("No", null)
                .show();
    }

    private void generarPdf(String anio) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();
        int x = 50;
        int y = 50;

        String titulo = "Estadísticas de Capturas";
        if (anio != null) {
            titulo += " - Año " + anio;
        } else {
            titulo += " (Todos los años)";
        }

        page.getCanvas().drawText(titulo, x, y, paint); y += 30;
        page.getCanvas().drawText(totalCapturas.getText().toString(), x, y, paint); y += 20;
        page.getCanvas().drawText(especieMasCapturada.getText().toString(), x, y, paint); y += 20;
        page.getCanvas().drawText(diaMasExitoso.getText().toString(), x, y, paint); y += 30;

        page.getCanvas().drawText("Capturas por especie:", x, y, paint); y += 20;
        for (String linea : capturasPorEspecie.getText().toString().split("\n")) {
            page.getCanvas().drawText(linea, x + 20, y, paint);
            y += 20;
        }

        y += 20;
        for (String linea : mayorPesoConUbicacion.getText().toString().split("\n")) {
            page.getCanvas().drawText(linea, x + 20, y, paint);
            y += 20;
        }

        y += 20;
        for (String linea : mayorPesoSinUbicacion.getText().toString().split("\n")) {
            page.getCanvas().drawText(linea, x + 20, y, paint);
            y += 20;
        }

        pdfDocument.finishPage(page);

        // Guardar en la carpeta pública Downloads
        String nombreArchivo = "Estadisticas_" + (anio != null ? anio : "Todos") + ".pdf";
        File archivo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreArchivo);

        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            pdfDocument.writeTo(fos);
            Toast.makeText(this, "PDF generado en: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }
    }
}
