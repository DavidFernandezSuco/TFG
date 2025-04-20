package com.example.tfg.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Jugador;
import com.example.tfg.ui.player.PlayerDetailActivity;

import java.util.List;

public class JugadoresAdapter extends RecyclerView.Adapter<JugadoresAdapter.ViewHolder> {

    private List<Jugador> jugadores;
    private Context context;

    public JugadoresAdapter(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_player_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Jugador jugador = jugadores.get(position);

        // Mostrar datos del jugador
        holder.tvNombreJugador.setText(jugador.getNombre() + " " + jugador.getApellidos());
        holder.tvNumeroJugador.setText(String.valueOf(jugador.getNumero()));
        holder.tvPosicionJugador.setText(jugador.getPosicion());

        // Configurar click en la tarjeta
        holder.cardJugador.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerDetailActivity.class);
            intent.putExtra("JUGADOR_ID", jugador.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardJugador;
        TextView tvNombreJugador, tvNumeroJugador, tvPosicionJugador;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardJugador = itemView.findViewById(R.id.cardJugador);
            tvNombreJugador = itemView.findViewById(R.id.tvNombreJugador);
            tvNumeroJugador = itemView.findViewById(R.id.tvNumeroJugador);
            tvPosicionJugador = itemView.findViewById(R.id.tvPosicionJugador);
        }
    }
}