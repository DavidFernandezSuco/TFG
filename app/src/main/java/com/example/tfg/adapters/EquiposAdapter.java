package com.example.tfg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Equipo;
import com.example.tfg.ui.team.TeamDetailActivity;

import java.util.List;

/**
 * Adaptador para mostrar la lista de equipos en RecyclerView
 * Se utiliza en el Dashboard y en la pantalla de Equipos
 */
public class EquiposAdapter extends RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder> {

    private final List<Equipo> listaEquipos;
    private final Context context;

    /**
     * Constructor del adaptador
     * @param equipos Lista de equipos a mostrar
     */
    public EquiposAdapter(List<Equipo> equipos) {
        this.listaEquipos = equipos;
        this.context = null;
    }

    /**
     * Constructor con contexto
     * @param context Contexto de la aplicación
     * @param equipos Lista de equipos a mostrar
     */
    public EquiposAdapter(Context context, List<Equipo> equipos) {
        this.listaEquipos = equipos;
        this.context = context;
    }

    @NonNull
    @Override
    public EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contexto = context != null ? context : parent.getContext();
        View view = LayoutInflater.from(contexto).inflate(R.layout.item_team_preview, parent, false);
        return new EquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipoViewHolder holder, int position) {
        Equipo equipo = listaEquipos.get(position);

        // Establecer los datos en la vista
        holder.tvTeamName.setText(equipo.getNombre());
        holder.tvTeamCategory.setText(equipo.getCategoria() + " | " + equipo.getTemporada());

        // Establecer imagen del equipo (aquí podrías usar Glide o Picasso si tienes URLs)
        // holder.ivTeamLogo.setImageResource(R.drawable.ic_teams);

        // Configurar clic en el elemento
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TeamDetailActivity.class);
            intent.putExtra("EQUIPO_ID", equipo.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaEquipos != null ? listaEquipos.size() : 0;
    }

    /**
     * Actualiza los datos del adaptador
     * @param nuevaLista Nueva lista de equipos
     */
    public void actualizarDatos(List<Equipo> nuevaLista) {
        this.listaEquipos.clear();
        this.listaEquipos.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para las vistas de equipo
     */
    static class EquipoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvTeamCategory;
        ImageView ivTeamLogo;
        CardView cardView;

        public EquipoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvTeamCategory = itemView.findViewById(R.id.tvTeamCategory);
            ivTeamLogo = itemView.findViewById(R.id.ivTeamLogo);
            cardView = (CardView) itemView;
        }
    }
}