package com.example.fishcatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.fishcatch.R;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private Button btnGuardar;
    private Uri nuevaImagenUri;  // URI de la imagen seleccionada o guardada

    private int capturaId;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;
    private FrameLayout mapContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detalle_captura);

        // 1) Asociación de vistas
        imagenDetalle = findViewById(R.id.imagenDetalle);
        especieDetalle = findViewById(R.id.especieDetalle);
        pesoDetalle = findViewById(R.id.pesoDetalle);
        tamannoDetalle = findViewById(R.id.tamannoDetalle);
        fechaDetalle = findViewById(R.id.fechaDetalle);
        horaDetalle = findViewById(R.id.horaDetalle);
        comentariosDetalle = findViewById(R.id.comentariosDetalle);
        temperaturaDetalle = findViewById(R.id.temperaturaDetalle);
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

        //Botón guardar
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDetallesCaptura(int id) {
        Captura captura = adaptadorBaseDeDatos.obtenerCapturaPorId(id);
        if (captura == null) return;

        //Imagen (try/catch para evitar crash)
        String fotoUri = captura.getFotoUri();
        if (fotoUri != null && !fotoUri.isEmpty()) {
            try {
                imagenDetalle.setImageURI(Uri.parse(fotoUri));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }

        especieDetalle.setText(captura.getNombreEspecie());
        pesoDetalle.setText(String.valueOf(captura.getPeso()));
        tamannoDetalle.setText(String.valueOf(captura.getTamanno()));
        fechaDetalle.setText(captura.getFecha());
        horaDetalle.setText(captura.getHora());
        comentariosDetalle.setText(captura.getComentario());
        temperaturaDetalle.setText(String.valueOf(captura.getTemperatura()));
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
                            // Persistir permiso sobre la URI
                            int takeFlags = result.getData().getFlags()
                                    & Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(
                                    uriOriginal, takeFlags
                            );

                            // Usar la misma URI (no es necesario copiar)
                            imagenDetalle.setImageURI(uriOriginal);
                            nuevaImagenUri = uriOriginal;
                        }
                    }
            );
}
