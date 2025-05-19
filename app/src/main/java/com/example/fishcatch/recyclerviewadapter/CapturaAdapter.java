package com.example.fishcatch.recyclerviewadapter;

import android.app.AlertDialog;
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
    private MainActivityVerCapturas parentActivity;

    public CapturaAdapter(List<Captura> listaCapturas, MainActivityVerCapturas activity) {
        this.listaCapturas = listaCapturas;
        this.parentActivity = activity;
    }

    @NonNull
    @Override
    public CapturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_captura, parent, false);
        return new CapturaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CapturaViewHolder holder, int position) {
        Captura captura = listaCapturas.get(position);
        holder.textEspecie.setText(captura.getNombreEspecie());
        holder.textPeso.setText("Peso: " + captura.getPeso() + " kg");

        try {
            if (captura.getFotoUri() != null && !captura.getFotoUri().isEmpty()) {
                holder.imagenMiniatura.setImageURI(Uri.parse(captura.getFotoUri()));
            } else {
                holder.imagenMiniatura.setImageResource(R.drawable.logoapp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.imagenMiniatura.setImageResource(R.drawable.logoapp);
        }

        holder.itemView.setOnClickListener(v -> {
            parentActivity.startActivity(
                    new Intent(parentActivity,
                            MainActivityDetalleCaptura.class)
                            .putExtra("captura_id", captura.getId())
            );
        });

        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoBorrar(captura, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaCapturas.size();
    }

    public void setListaCapturas(List<Captura> nuevaListaCapturas) {
        this.listaCapturas = nuevaListaCapturas;
        notifyDataSetChanged();
    }

    private void mostrarDialogoBorrar(Captura captura, int position) {
        new AlertDialog.Builder(parentActivity)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres borrar esta captura?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    new AdaptadorBaseDeDatos(parentActivity)
                            .eliminarCaptura(captura.getId());

                    listaCapturas.remove(position);
                    notifyItemRemoved(position);

                    parentActivity.actualizarSpinnerFechas();
                })
                .setNegativeButton("No", (d, w) -> d.dismiss())
                .show();
    }

    static class CapturaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenMiniatura;
        TextView textEspecie, textPeso;

        CapturaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenMiniatura=itemView.findViewById(R.id.imageMiniatura);
            textEspecie=itemView.findViewById(R.id.textEspecie);
            textPeso=itemView.findViewById(R.id.textPeso);
        }
    }
}
