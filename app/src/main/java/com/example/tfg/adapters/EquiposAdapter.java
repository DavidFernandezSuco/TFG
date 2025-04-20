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
import com.example.tfg.model.Equipo;
import com.example.tfg.ui.team.TeamDetailActivity;

import java.util.List;

public class EquiposAdapter extends RecyclerView.Adapter<EquiposAdapter.ViewHolder> {

    private List<Equipo> equipos;
    private Context context;

    public EquiposAdapter(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Equipo equipo = equipos.get(position);

        // Mostrar datos del equipo
        holder.tvNombreEquipo.setText(equipo.getNombre());
        holder.tvCategoriaEquipo.setText(equipo.getCategoria());

        // Configurar click en la tarjeta
        holder.cardEquipo.setOnClickListener(view -> {
            Intent intent = new Intent(context, TeamDetailActivity.class);
            intent.putExtra("EQUIPO_ID", equipo.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardEquipo;
        TextView tvNombreEquipo, tvCategoriaEquipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardEquipo = itemView.findViewById(R.id.cardTeam);
            tvNombreEquipo = itemView.findViewById(R.id.tvTeamName);
            tvCategoriaEquipo = itemView.findViewById(R.id.tvTeamCategory);
        }
    }
}
