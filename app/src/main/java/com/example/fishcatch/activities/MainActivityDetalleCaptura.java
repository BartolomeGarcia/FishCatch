package com.example.fishcatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fishcatch.R;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivityDetalleCaptura extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imagenDetalle;
    private TextView especieDetalle;
    private TextView pesoDetalle;
    private TextView tamannoDetalle;
    private TextView fechaDetalle;
    private TextView horaDetalle;
    private EditText comentariosDetalle;
    private TextView temperaturaDetalle;
    private TextView temperaturaTitulo;
    private Button btnGuardar;
    private Uri nuevaImagenUri;  // URI de la imagen seleccionada o guardada

    private int capturaId;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private FrameLayout mapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detalle_captura);

        //Asociaciones
        imagenDetalle = findViewById(R.id.imagenDetalle);
        especieDetalle = findViewById(R.id.especieDetalle);
        pesoDetalle = findViewById(R.id.pesoDetalle);
        tamannoDetalle = findViewById(R.id.tamannoDetalle);
        fechaDetalle = findViewById(R.id.fechaDetalle);
        horaDetalle = findViewById(R.id.horaDetalle);
        comentariosDetalle = findViewById(R.id.comentariosDetalle);
        temperaturaDetalle = findViewById(R.id.temperaturaDetalle);
        temperaturaTitulo=findViewById(R.id.textViewTituloTemperatura);
        mapContainer = findViewById(R.id.mapContainer);
        btnGuardar = findViewById(R.id.btnGuardar);

        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);

        //Leer ID de la captura
        capturaId = getIntent().getIntExtra("captura_id", -1);

        //Cargar datos existentes
        cargarDetallesCaptura(capturaId);

        //Botón para cambiar imagen
        Button btnAgregarImagen = findViewById(R.id.btnAgregarImagen);
        btnAgregarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            );
            selectImageLauncher.launch(intent);
        });

        // Para mostrar el Mapa con OSMdroid
        Configuration.getInstance().load(
                getApplicationContext(),
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        );

        FrameLayout mapContainer = findViewById(R.id.mapContainer);
        Captura captura = adaptadorBaseDeDatos.obtenerCapturaPorId(capturaId);

        if (captura != null && captura.tieneUbicacion()) {
            mapContainer.setVisibility(View.VISIBLE);

            MapView map = new MapView(this);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setMultiTouchControls(true);
            map.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));
            mapContainer.addView(map);

            GeoPoint punto = new GeoPoint(captura.getLatitud(), captura.getLongitud());
            map.getController().setZoom(15.0);
            map.getController().setCenter(punto);

            Marker marker = new Marker(map);
            marker.setPosition(punto);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Lugar de captura");
            map.getOverlays().add(marker);

        } else {
            // Ocultar el contenedor si no hay ubicación
            mapContainer.setVisibility(View.GONE);
        }


        //Botón guardar
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDetallesCaptura(int id) {
        Captura captura = adaptadorBaseDeDatos.obtenerCapturaPorId(id);
        if (captura == null) return;

        // Imagen
        String fotoUri = captura.getFotoUri();
        if (fotoUri != null && !fotoUri.isEmpty()) {
            imagenDetalle.setVisibility(View.VISIBLE);
            try {
                imagenDetalle.setImageURI(Uri.parse(fotoUri));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        } else {
            imagenDetalle.setVisibility(View.GONE);
        }

        especieDetalle.setText(captura.getNombreEspecie());
        pesoDetalle.setText(String.valueOf(captura.getPeso()));
        tamannoDetalle.setText(String.valueOf(captura.getTamanno()));
        fechaDetalle.setText(captura.getFecha());
        horaDetalle.setText(captura.getHora());
        comentariosDetalle.setText(captura.getComentario());

        // Temperatura
        if (captura.getTemperatura() == null) {
            temperaturaTitulo.setVisibility(View.GONE);
            temperaturaDetalle.setVisibility(View.GONE);
        } else {
            temperaturaDetalle.setVisibility(View.VISIBLE);
            temperaturaDetalle.setText(String.valueOf(captura.getTemperatura()));
        }
    }


    private void guardarCambios() {
        // Obtenemos el texto de comentarios
        String comentarios = comentariosDetalle.getText().toString();

        // Construimos fotoString sin ternario
        String fotoString = null;
        if (nuevaImagenUri != null) {
            fotoString = nuevaImagenUri.toString();
        }

        // Actualizamos en la base de datos
        adaptadorBaseDeDatos.actualizarCaptura(
                capturaId,
                comentarios,
                fotoString
        );

        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    //Lanzador para ACTION_OPEN_DOCUMENT
    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK &&
                                result.getData() != null &&
                                result.getData().getData() != null) {

                            Uri uriOriginal = result.getData().getData();
                            int takeFlags = result.getData().getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uriOriginal, takeFlags);

                            // Guardar URI para guardar en la base
                            nuevaImagenUri = uriOriginal;

                            // Actualizar ImageView y hacerlo visible
                            imagenDetalle.setVisibility(View.VISIBLE);
                            imagenDetalle.setImageURI(uriOriginal);
                        }
                    }
            );
}
