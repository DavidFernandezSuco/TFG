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
import com.example.tfg.model.Partido;
import com.example.tfg.ui.match.MatchDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PartidosAdapter extends RecyclerView.Adapter<PartidosAdapter.ViewHolder> {

    private List<Partido> partidos;
    private Context context;
    private SimpleDateFormat dateFormat;

    public PartidosAdapter(List<Partido> partidos) {
        this.partidos = partidos;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_match_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Partido partido = partidos.get(position);

        // Formatear fecha
        String fechaFormateada = dateFormat.format(new Date(partido.getFecha()));

        // Mostrar datos del partido
        holder.tvEquipoLocal.setText(partido.getNombreEquipoLocal());
        holder.tvEquipoVisitante.setText(partido.getNombreEquipoVisitante());
        holder.tvFechaPartido.setText(fechaFormateada);
        holder.tvLugarPartido.setText(partido.getLugar());

        // Si el partido ya se ha jugado, mostrar resultado
        if ("jugado".equals(partido.getEstado())) {
            String resultado = partido.getPuntosLocal() + " - " + partido.getPuntosVisitante();
            holder.tvResultadoPartido.setText(resultado);
            holder.tvResultadoPartido.setVisibility(View.VISIBLE);
        } else {
            holder.tvResultadoPartido.setVisibility(View.GONE);
        }

        // Configurar click en la tarjeta
        holder.cardPartido.setOnClickListener(view -> {
            Intent intent = new Intent(context, MatchDetailActivity.class);
            intent.putExtra("PARTIDO_ID", partido.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardPartido;
        TextView tvEquipoLocal, tvEquipoVisitante, tvFechaPartido, tvLugarPartido, tvResultadoPartido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPartido = itemView.findViewById(R.id.cardPartido);
            tvEquipoLocal = itemView.findViewById(R.id.tvEquipoLocal);
            tvEquipoVisitante = itemView.findViewById(R.id.tvEquipoVisitante);
            tvFechaPartido = itemView.findViewById(R.id.tvFechaPartido);
            tvLugarPartido = itemView.findViewById(R.id.tvLugarPartido);
            tvResultadoPartido = itemView.findViewById(R.id.tvResultadoPartido);
        }
    }
}
