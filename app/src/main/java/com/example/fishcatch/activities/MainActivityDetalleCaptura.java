package com.example.fishcatch.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fishcatch.R;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

public class MainActivityDetalleCaptura extends AppCompatActivity {
    private ImageView imagenDetalle;
    private TextView especieDetalle;
    private EditText pesoDetalle;
    private EditText tamannoDetalle;
    private EditText fechaDetalle;
    private EditText horaDetalle;
    private EditText comentariosDetalle;
    private TextView temperaturaDetalle;

    private int capturaId;
    private AdaptadorBaseDeDatos adaptadorBaseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detalle_captura);

        //Inicializamos el AdaptadorBaseDeDatos
        adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(this);

        //Obtenemos el ID de la captura del Intent
        capturaId = getIntent().getIntExtra("captura_id", -1);
        // Si se recibe un ID válido, cargar los detalles
        if (capturaId != -1) {
            cargarDetallesCaptura(capturaId);
        }
    }

    //Métodos
    private void cargarDetallesCaptura(int capturaId) {
        // Usamos el adaptador para obtener los detalles de la captura
        Captura captura = adaptadorBaseDeDatos.obtenerCapturaPorId(capturaId);

        // Obtenemos las vistas del layout
        ImageView imagenDetalle = findViewById(R.id.imagenDetalle);
        TextView especieDetalle = findViewById(R.id.especieDetalle);
        EditText pesoDetalle = findViewById(R.id.pesoDetalle);
        EditText tamannoDetalle = findViewById(R.id.tamannoDetalle);
        EditText fechaDetalle = findViewById(R.id.fechaDetalle);
        EditText horaDetalle = findViewById(R.id.horaDetalle);
        EditText comentariosDetalle = findViewById(R.id.comentariosDetalle);
        TextView temperaturaDetalle = findViewById(R.id.temperaturaDetalle);

        // Cargamos los detalles en las vistas
        especieDetalle.setText(captura.getNombreEspecie());
        pesoDetalle.setText(String.valueOf(captura.getPeso()));
        tamannoDetalle.setText(String.valueOf(captura.getTamanno()));
        fechaDetalle.setText(captura.getFecha());
        horaDetalle.setText(captura.getHora());
        comentariosDetalle.setText(captura.getComentario());
        temperaturaDetalle.setText("Temperatura: " + captura.getTemperatura());

        // Si la foto URI no es nula ni vacía, cargamos la imagen
        if (captura.getFotoUri() != null && !captura.getFotoUri().isEmpty()) {
            imagenDetalle.setImageURI(Uri.parse(captura.getFotoUri()));
        } else {
            imagenDetalle.setImageResource(R.drawable.logoapp); // Imagen por defecto si no hay foto
        }

        // Aquí puedes cargar más detalles como el mapa o la ubicación, si los tienes
    }
}
