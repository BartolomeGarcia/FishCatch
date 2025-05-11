package com.example.fishcatch.recyclerviewadapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fishcatch.R;
import com.example.fishcatch.activities.MainActivityDetalleCaptura;
import com.example.fishcatch.activities.MainActivityVerCapturas;
import com.example.fishcatch.model.Captura;
import com.example.fishcatch.repositories.AdaptadorBaseDeDatos;

import java.util.List;

public class CapturaAdapter extends RecyclerView.Adapter<CapturaAdapter.CapturaViewHolder> {

    private List<Captura> listaCapturas;
    private Context context;

    public CapturaAdapter(List<Captura> listaCapturas, Context context) {
        this.listaCapturas = listaCapturas;
        this.context = context;
    }

    @NonNull
    @Override
    public CapturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_captura, parent, false);
        return new CapturaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturaViewHolder holder, int position) {
        Captura captura = listaCapturas.get(position);
        holder.textEspecie.setText(captura.getNombreEspecie());
        holder.textPeso.setText("Peso: " + captura.getPeso() + " kg");

        try {
            //Verifica si la URI de la foto no es nula ni vacía
            if (captura.getFotoUri() != null && !captura.getFotoUri().isEmpty()) {
                //Carga la imagen desde la URI
                holder.imagenMiniatura.setImageURI(Uri.parse(captura.getFotoUri()));
            } else {
                //Si no hay URI válida, se pone una imagen predeterminada
                holder.imagenMiniatura.setImageResource(R.drawable.logoapp); // Imagen por defecto con el logotipo de la app
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Si ocurre un error al cargar la imagen, se coloca la imagen predeterminada
            holder.imagenMiniatura.setImageResource(R.drawable.logoapp); // Imagen por defecto
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivityDetalleCaptura.class);
            intent.putExtra("captura_id", captura.getId()); //Pasamos el id de la Captura a la Activity de Detalles de la Captura
            context.startActivity(intent);
        });

        // Detectar clic largo
        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoBorrar(captura, position);
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return listaCapturas.size();
    }

    public static class CapturaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenMiniatura;
        TextView textEspecie, textPeso;

        public CapturaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenMiniatura = itemView.findViewById(R.id.imageMiniatura);
            textEspecie = itemView.findViewById(R.id.textEspecie);
            textPeso = itemView.findViewById(R.id.textPeso);
        }
    }

    public void setListaCapturas(List<Captura> nuevaListaCapturas) {
        this.listaCapturas = nuevaListaCapturas;
        notifyDataSetChanged(); // Notificar que los datos han cambiado
    }

    private void mostrarDialogoBorrar(Captura captura, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres borrar esta captura?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    AdaptadorBaseDeDatos adaptadorBaseDeDatos = new AdaptadorBaseDeDatos(context);

                    // Eliminar la captura de la base de datos
                    adaptadorBaseDeDatos.eliminarCaptura(captura.getId());

                    // Eliminar de la lista local
                    listaCapturas.remove(position);
                    notifyItemRemoved(position);

                    // Actualizar el Spinner en MainActivityVerCapturas
                    if (context instanceof MainActivityVerCapturas) {
                        MainActivityVerCapturas activity = (MainActivityVerCapturas) context;
                        activity.actualizarSpinnerFechas();
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
