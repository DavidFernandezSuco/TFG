package com.example.tfg.adapter;

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
import com.example.tfg.model.Partido;
import com.example.tfg.ui.match.MatchDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador para mostrar la lista de partidos en RecyclerView
 * Se utiliza en el Dashboard y en la pantalla de Partidos
 */
public class PartidosAdapter extends RecyclerView.Adapter<PartidosAdapter.PartidoViewHolder> {

    private final List<Partido> listaPartidos;
    private SimpleDateFormat dateFormat;

    /**
     * Constructor del adaptador
     * @param partidos Lista de partidos a mostrar
     */
    public PartidosAdapter(List<Partido> partidos) {
        this.listaPartidos = partidos;
        this.dateFormat = new SimpleDateFormat("dd MMMM yyyy | HH:mm'h'", new Locale("es", "ES"));
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_preview, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        Partido partido = listaPartidos.get(position);

        // Establecer los datos en la vista
        holder.tvHomeTeamName.setText(partido.getNombreEquipoLocal());
        holder.tvAwayTeamName.setText(partido.getNombreEquipoVisitante());
        holder.tvLocation.setText(partido.getLugar());

        // Formatear y mostrar la fecha
        Date fechaPartido = new Date(partido.getFecha());
        holder.tvMatchDate.setText(dateFormat.format(fechaPartido));

        // Configurar el estado del partido
        configureMatchStatus(holder, partido);

        // Configurar el botón de detalles
        holder.btnViewDetails.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MatchDetailActivity.class);
            intent.putExtra("PARTIDO_ID", partido.getId());
            v.getContext().startActivity(intent);
        });
    }

    /**
     * Configura la visualización según el estado del partido
     */
    private void configureMatchStatus(PartidoViewHolder holder, Partido partido) {
        if ("pendiente".equalsIgnoreCase(partido.getEstado())) {
            // Partido pendiente, mostrar "VS" en lugar del resultado
            holder.tvScore.setVisibility(View.GONE);
            holder.tvVs.setVisibility(View.VISIBLE);
            holder.tvMatchStatus.setText("Próximo");
            holder.tvMatchStatus.setBackgroundResource(R.color.gray_light);
            holder.tvMatchStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.blue_info));
        } else {
            // Partido jugado, mostrar resultado
            holder.tvScore.setVisibility(View.VISIBLE);
            holder.tvVs.setVisibility(View.GONE);
            holder.tvScore.setText(partido.getPuntosLocal() + " - " + partido.getPuntosVisitante());
            holder.tvMatchStatus.setText("Finalizado");
            holder.tvMatchStatus.setBackgroundResource(R.color.gray_light);
            holder.tvMatchStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green_success));
        }
    }

    @Override
    public int getItemCount() {
        return listaPartidos != null ? listaPartidos.size() : 0;
    }

    /**
     * Actualiza los datos del adaptador
     * @param nuevaLista Nueva lista de partidos
     */
    public void actualizarDatos(List<Partido> nuevaLista) {
        this.listaPartidos.clear();
        this.listaPartidos.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para las vistas de partido
     */
    static class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatchDate, tvMatchStatus, tvHomeTeamName, tvAwayTeamName, tvScore, tvVs, tvLocation;
        ImageView ivHomeTeamLogo, ivAwayTeamLogo;
        Button btnViewDetails;

        public PartidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatchDate = itemView.findViewById(R.id.tvMatchDate);
            tvMatchStatus = itemView.findViewById(R.id.tvMatchStatus);
            tvHomeTeamName = itemView.findViewById(R.id.tvHomeTeamName);
            tvAwayTeamName = itemView.findViewById(R.id.tvAwayTeamName);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvVs = itemView.findViewById(R.id.tvVs);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            ivHomeTeamLogo = itemView.findViewById(R.id.ivHomeTeamLogo);
            ivAwayTeamLogo = itemView.findViewById(R.id.ivAwayTeamLogo);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
