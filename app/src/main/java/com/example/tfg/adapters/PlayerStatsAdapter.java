package com.example.tfg.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Estadistica;
import com.example.tfg.model.Jugador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptador para mostrar jugadores con sus estadísticas durante un partido
 * Se utiliza en la pantalla de GameStatsActivity para registrar estadísticas en tiempo real
 */
public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.PlayerStatsViewHolder> {

    private final List<Jugador> listaJugadores;
    private final Map<String, Estadistica> mapEstadisticas;
    private OnStatsChangeListener listener;

    /**
     * Constructor del adaptador
     * @param jugadores Lista de jugadores del equipo
     * @param listener Listener para cambios en estadísticas
     */
    public PlayerStatsAdapter(List<Jugador> jugadores, OnStatsChangeListener listener) {
        this.listaJugadores = jugadores;
        this.mapEstadisticas = new HashMap<>();
        this.listener = listener;

        // Inicializar estadísticas en cero para cada jugador
        for (Jugador jugador : jugadores) {
            Estadistica estadistica = new Estadistica();
            estadistica.setJugadorId(jugador.getId());
            estadistica.setPuntos(0);
            estadistica.setRebotes(0);
            estadistica.setAsistencias(0);
            estadistica.setFaltas(0);
            estadistica.setMinutos(0);

            mapEstadisticas.put(jugador.getId(), estadistica);
        }
    }

    @NonNull
    @Override
    public PlayerStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerStatsViewHolder holder, int position) {
        Jugador jugador = listaJugadores.get(position);
        Estadistica estadistica = mapEstadisticas.get(jugador.getId());

        // Información del jugador
        holder.tvPlayerName.setText(jugador.getNombre() + " " + jugador.getApellidos());
        holder.tvPlayerInfo.setText("#" + jugador.getNumero() + " | " + jugador.getPosicion());

        // Mostrar estadísticas actuales
        holder.tvMinutes.setText(String.valueOf(estadistica.getMinutos()));
        holder.tvPoints.setText(String.valueOf(estadistica.getPuntos()));
        holder.tvRebounds.setText(String.valueOf(estadistica.getRebotes()));
        holder.tvAssists.setText(String.valueOf(estadistica.getAsistencias()));
        holder.tvFouls.setText(String.valueOf(estadistica.getFaltas()));

        // Botones de minutos
        holder.btnIncreaseMin.setOnClickListener(v -> {
            int nuevosMinutos = estadistica.getMinutos() + 1;
            estadistica.setMinutos(nuevosMinutos);
            holder.tvMinutes.setText(String.valueOf(nuevosMinutos));
            if (listener != null) {
                listener.onStatChanged(jugador.getId(), "minutos", nuevosMinutos);
            }
        });

        holder.btnDecreaseMin.setOnClickListener(v -> {
            if (estadistica.getMinutos() > 0) {
                int nuevosMinutos = estadistica.getMinutos() - 1;
                estadistica.setMinutos(nuevosMinutos);
                holder.tvMinutes.setText(String.valueOf(nuevosMinutos));
                if (listener != null) {
                    listener.onStatChanged(jugador.getId(), "minutos", nuevosMinutos);
                }
            }
        });

        // Botones de puntos
        holder.btnIncreasePts.setOnClickListener(v -> {
            int nuevosPuntos = estadistica.getPuntos() + 1;
            estadistica.setPuntos(nuevosPuntos);
            holder.tvPoints.setText(String.valueOf(nuevosPuntos));
            if (listener != null) {
                listener.onStatChanged(jugador.getId(), "puntos", nuevosPuntos);
            }
        });

        holder.btnDecreasePts.setOnClickListener(v -> {
            if (estadistica.getPuntos() > 0) {
                int nuevosPuntos = estadistica.getPuntos() - 1;
                estadistica.setPuntos(nuevosPuntos);
                holder.tvPoints.setText(String.valueOf(nuevosPuntos));
                if (listener != null) {
                    listener.onStatChanged(jugador.getId(), "puntos", nuevosPuntos);
                }
            }
        });

        // Botones de rebotes
        holder.btnIncreaseReb.setOnClickListener(v -> {
            int nuevosRebotes = estadistica.getRebotes() + 1;
            estadistica.setRebotes(nuevosRebotes);
            holder.tvRebounds.setText(String.valueOf(nuevosRebotes));
            if (listener != null) {
                listener.onStatChanged(jugador.getId(), "rebotes", nuevosRebotes);
            }
        });

        holder.btnDecreaseReb.setOnClickListener(v -> {
            if (estadistica.getRebotes() > 0) {
                int nuevosRebotes = estadistica.getRebotes() - 1;
                estadistica.setRebotes(nuevosRebotes);
                holder.tvRebounds.setText(String.valueOf(nuevosRebotes));
                if (listener != null) {
                    listener.onStatChanged(jugador.getId(), "rebotes", nuevosRebotes);
                }
            }
        });

        // Botones de asistencias
        holder.btnIncreaseAst.setOnClickListener(v -> {
            int nuevasAsistencias = estadistica.getAsistencias() + 1;
            estadistica.setAsistencias(nuevasAsistencias);
            holder.tvAssists.setText(String.valueOf(nuevasAsistencias));
            if (listener != null) {
                listener.onStatChanged(jugador.getId(), "asistencias", nuevasAsistencias);
            }
        });

        holder.btnDecreaseAst.setOnClickListener(v -> {
            if (estadistica.getAsistencias() > 0) {
                int nuevasAsistencias = estadistica.getAsistencias() - 1;
                estadistica.setAsistencias(nuevasAsistencias);
                holder.tvAssists.setText(String.valueOf(nuevasAsistencias));
                if (listener != null) {
                    listener.onStatChanged(jugador.getId(), "asistencias", nuevasAsistencias);
                }
            }
        });

        // Botones de faltas
        holder.btnIncreaseFlt.setOnClickListener(v -> {
            int nuevasFaltas = estadistica.getFaltas() + 1;
            estadistica.setFaltas(nuevasFaltas);
            holder.tvFouls.setText(String.valueOf(nuevasFaltas));
            if (listener != null) {
                listener.onStatChanged(jugador.getId(), "faltas", nuevasFaltas);
            }
        });

        holder.btnDecreaseFlt.setOnClickListener(v -> {
            if (estadistica.getFaltas() > 0) {
                int nuevasFaltas = estadistica.getFaltas() - 1;
                estadistica.setFaltas(nuevasFaltas);
                holder.tvFouls.setText(String.valueOf(nuevasFaltas));
                if (listener != null) {
                    listener.onStatChanged(jugador.getId(), "faltas", nuevasFaltas);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJugadores != null ? listaJugadores.size() : 0;
    }

    /**
     * Obtiene las estadísticas actuales de todos los jugadores
     * @return Mapa de estadísticas por ID de jugador
     */
    public Map<String, Estadistica> getEstadisticas() {
        return new HashMap<>(mapEstadisticas);
    }

    /**
     * Interfaz para notificar cambios en las estadísticas
     */
    public interface OnStatsChangeListener {
        void onStatChanged(String jugadorId, String statName, int newValue);
    }

    /**
     * ViewHolder para las vistas de estadísticas de jugador
     */
    static class PlayerStatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerName, tvPlayerInfo, tvMinutes, tvPoints, tvRebounds, tvAssists, tvFouls;
        ImageView ivPlayerPhoto;
        ImageButton btnIncreaseMin, btnDecreaseMin, btnIncreasePts, btnDecreasePts,
                btnIncreaseReb, btnDecreaseReb, btnIncreaseAst, btnDecreaseAst,
                btnIncreaseFlt, btnDecreaseFlt;

        public PlayerStatsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Información del jugador
            ivPlayerPhoto = itemView.findViewById(R.id.ivPlayerPhoto);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerInfo = itemView.findViewById(R.id.tvPlayerInfo);

            // Estadísticas
            tvMinutes = itemView.findViewById(R.id.tvMinutes);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvRebounds = itemView.findViewById(R.id.tvRebounds);
            tvAssists = itemView.findViewById(R.id.tvAssists);
            tvFouls = itemView.findViewById(R.id.tvFouls);

            // Botones de incremento/decremento
            btnIncreaseMin = itemView.findViewById(R.id.btnIncreaseMin);
            btnDecreaseMin = itemView.findViewById(R.id.btnDecreaseMin);
            btnIncreasePts = itemView.findViewById(R.id.btnIncreasePts);
            btnDecreasePts = itemView.findViewById(R.id.btnDecreasePts);
            btnIncreaseReb = itemView.findViewById(R.id.btnIncreaseReb);
            btnDecreaseReb = itemView.findViewById(R.id.btnDecreaseReb);
            btnIncreaseAst = itemView.findViewById(R.id.btnIncreaseAst);
            btnDecreaseAst = itemView.findViewById(R.id.btnDecreaseAst);
            btnIncreaseFlt = itemView.findViewById(R.id.btnIncreaseFlt);
            btnDecreaseFlt = itemView.findViewById(R.id.btnDecreaseFlt);
        }
    }
}
