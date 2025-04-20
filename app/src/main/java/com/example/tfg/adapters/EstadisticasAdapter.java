package com.example.tfg.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Estadistica;
import com.example.tfg.model.Jugador;
import com.example.tfg.ui.player.PlayerDetailActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptador para mostrar resúmenes de estadísticas en RecyclerView
 * Se utiliza en la pantalla de Estadísticas
 */
public class EstadisticasAdapter extends RecyclerView.Adapter<EstadisticasAdapter.EstadisticaViewHolder> {

    private final List<Map<String, Object>> listaEstadisticasResumen;
    private final Map<String, Jugador> mapJugadores;
    private final Map<String, String> mapEquipos;
    private final DecimalFormat decimalFormat;

    /**
     * Constructor del adaptador
     * @param estadisticasResumen Lista de mapas con resúmenes de estadísticas por jugador
     * @param jugadores Mapa de jugadores por ID
     * @param equipos Mapa de nombres de equipo por ID
     */
    public EstadisticasAdapter(List<Map<String, Object>> estadisticasResumen,
                               Map<String, Jugador> jugadores,
                               Map<String, String> equipos) {
        this.listaEstadisticasResumen = estadisticasResumen;
        this.mapJugadores = jugadores;
        this.mapEquipos = equipos;
        this.decimalFormat = new DecimalFormat("#.0");
    }

    @NonNull
    @Override
    public EstadisticaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadistica_resumen, parent, false);
        return new EstadisticaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadisticaViewHolder holder, int position) {
        Map<String, Object> resumen = listaEstadisticasResumen.get(position);

        // Obtener el jugador asociado a estas estadísticas
        String jugadorId = (String) resumen.get("jugadorId");
        Jugador jugador = mapJugadores.get(jugadorId);

        if (jugador != null) {
            // Información del jugador
            holder.tvPlayerName.setText(jugador.getNombre() + " " + jugador.getApellidos());
            holder.tvPlayerInfo.setText("#" + jugador.getNumero() + " | " + jugador.getPosicion());

            // Nombre del equipo
            String nombreEquipo = mapEquipos.get(jugador.getEquipoId());
            holder.tvTeamName.setText(nombreEquipo != null ? nombreEquipo : "");

            // Datos de partidos jugados y minutos
            int partidosJugados = ((Long) resumen.get("partidosJugados")).intValue();
            holder.tvMatchCount.setText(partidosJugados + " partidos");

            double minutosPromedio = (double) resumen.get("minutosPromedio");
            holder.tvMinutesAvg.setText(decimalFormat.format(minutosPromedio) + " min/partido");

            // Promedios de estadísticas
            double puntosPromedio = (double) resumen.get("puntosPromedio");
            double rebotesPromedio = (double) resumen.get("rebotesPromedio");
            double asistenciasPromedio = (double) resumen.get("asistenciasPromedio");
            double faltasPromedio = (double) resumen.get("faltasPromedio");

            holder.tvPoints.setText(decimalFormat.format(puntosPromedio));
            holder.tvRebounds.setText(decimalFormat.format(rebotesPromedio));
            holder.tvAssists.setText(decimalFormat.format(asistenciasPromedio));
            holder.tvFouls.setText(decimalFormat.format(faltasPromedio));

            // Botón para ver detalle
            holder.btnViewDetail.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PlayerDetailActivity.class);
                intent.putExtra("JUGADOR_ID", jugadorId);
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaEstadisticasResumen != null ? listaEstadisticasResumen.size() : 0;
    }

    /**
     * Actualiza los datos del adaptador
     * @param nuevaLista Nueva lista de resúmenes de estadísticas
     */
    public void actualizarDatos(List<Map<String, Object>> nuevaLista) {
        this.listaEstadisticasResumen.clear();
        this.listaEstadisticasResumen.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    /**
     * Crea un resumen de estadísticas para un jugador a partir de una lista de estadísticas
     * @param estadisticas Lista de estadísticas de un jugador
     * @param jugadorId ID del jugador
     * @return Mapa con el resumen de estadísticas
     */
    public static Map<String, Object> crearResumenEstadisticas(List<Estadistica> estadisticas, String jugadorId) {
        Map<String, Object> resumen = new HashMap<>();
        int partidosJugados = estadisticas.size();

        if (partidosJugados == 0) {
            resumen.put("jugadorId", jugadorId);
            resumen.put("partidosJugados", 0L);
            resumen.put("puntosPromedio", 0.0);
            resumen.put("rebotesPromedio", 0.0);
            resumen.put("asistenciasPromedio", 0.0);
            resumen.put("faltasPromedio", 0.0);
            resumen.put("minutosPromedio", 0.0);
            return resumen;
        }

        int totalPuntos = 0;
        int totalRebotes = 0;
        int totalAsistencias = 0;
        int totalFaltas = 0;
        int totalMinutos = 0;

        for (Estadistica est : estadisticas) {
            totalPuntos += est.getPuntos();
            totalRebotes += est.getRebotes();
            totalAsistencias += est.getAsistencias();
            totalFaltas += est.getFaltas();
            totalMinutos += est.getMinutos();
        }

        double puntosPromedio = (double) totalPuntos / partidosJugados;
        double rebotesPromedio = (double) totalRebotes / partidosJugados;
        double asistenciasPromedio = (double) totalAsistencias / partidosJugados;
        double faltasPromedio = (double) totalFaltas / partidosJugados;
        double minutosPromedio = (double) totalMinutos / partidosJugados;

        resumen.put("jugadorId", jugadorId);
        resumen.put("partidosJugados", (long) partidosJugados);
        resumen.put("puntosPromedio", puntosPromedio);
        resumen.put("rebotesPromedio", rebotesPromedio);
        resumen.put("asistenciasPromedio", asistenciasPromedio);
        resumen.put("faltasPromedio", faltasPromedio);
        resumen.put("minutosPromedio", minutosPromedio);

        return resumen;
    }

    /**
     * ViewHolder para las vistas de estadísticas
     */
    static class EstadisticaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerName, tvPlayerInfo, tvTeamName, tvMatchCount, tvMinutesAvg;
        TextView tvPoints, tvRebounds, tvAssists, tvFouls;
        ImageView ivPlayerPhoto;
        Button btnViewDetail;

        public EstadisticaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Información del jugador
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerInfo = itemView.findViewById(R.id.tvPlayerInfo);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvMatchCount = itemView.findViewById(R.id.tvMatchCount);
            tvMinutesAvg = itemView.findViewById(R.id.tvMinutesAvg);

            // Estadísticas
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvRebounds = itemView.findViewById(R.id.tvRebounds);
            tvAssists = itemView.findViewById(R.id.tvAssists);
            tvFouls = itemView.findViewById(R.id.tvFouls);

            // Imagen y botón
            ivPlayerPhoto = itemView.findViewById(R.id.ivPlayerPhoto);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}
