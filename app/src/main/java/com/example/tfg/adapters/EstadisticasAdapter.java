package com.example.tfg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Estadistica;
import com.example.tfg.model.Jugador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.tfg.firebase.FirebaseManager;

import java.util.List;
import java.util.Map;

public class EstadisticasAdapter extends RecyclerView.Adapter<EstadisticasAdapter.ViewHolder> {

    private List<Estadistica> estadisticas;
    private Context context;
    private FirebaseManager firebaseManager;

    public EstadisticasAdapter(List<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
        this.firebaseManager = FirebaseManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_estadistica_resumen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estadistica estadistica = estadisticas.get(position);

        // Mostrar estadísticas
        holder.tvPuntos.setText(String.valueOf(estadistica.getPuntos()));
        holder.tvRebotes.setText(String.valueOf(estadistica.getRebotes()));
        holder.tvFaltas.setText(String.valueOf(estadistica.getFaltas()));
        holder.tvMinutos.setText(String.valueOf(estadistica.getMinutos()));

        // Cargar datos del jugador
        cargarDatosJugador(estadistica.getJugadorId(), holder);
    }

    private void cargarDatosJugador(String jugadorId, ViewHolder holder) {
        firebaseManager.getDatabaseReference("jugadores")
                .child(jugadorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if (map != null) {
                                Jugador jugador = Jugador.fromMap(map);
                                String nombreCompleto = jugador.getNombre() + " " + jugador.getApellidos();
                                holder.tvNombreJugador.setText(nombreCompleto);
                                holder.tvNumeroJugador.setText("#" + jugador.getNumero());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // No hacer nada
                    }
                });
    }

    @Override
    public int getItemCount() {
        return estadisticas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreJugador, tvNumeroJugador, tvPuntos, tvRebotes,
                tvAsistencias, tvFaltas, tvMinutos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreJugador = itemView.findViewById(R.id.tvPlayerName);
            tvNumeroJugador = itemView.findViewById(R.id.tvPlayerNumber);
            tvPuntos = itemView.findViewById(R.id.tvPoints);
            tvRebotes = itemView.findViewById(R.id.tvRebounds);
            tvAsistencias = itemView.findViewById(R.id.tvAssists);
            tvFaltas = itemView.findViewById(R.id.tvFouls);
            tvMinutos = itemView.findViewById(R.id.tvMinutes);
        }
    }
}