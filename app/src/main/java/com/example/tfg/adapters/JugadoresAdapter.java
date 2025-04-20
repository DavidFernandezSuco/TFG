package com.example.tfg.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.model.Jugador;
import com.example.tfg.ui.player.PlayerDetailActivity;

import java.util.List;

/**
 * Adaptador para mostrar la lista de jugadores en RecyclerView
 * Se utiliza en la pantalla de Jugadores y Detalle de Equipo
 */
public class JugadoresAdapter extends RecyclerView.Adapter<JugadoresAdapter.JugadorViewHolder> {

    private final List<Jugador> listaJugadores;
    private boolean esModificable;
    private OnJugadorListener listener;

    /**
     * Constructor del adaptador
     * @param jugadores Lista de jugadores a mostrar
     */
    public JugadoresAdapter(List<Jugador> jugadores) {
        this.listaJugadores = jugadores;
        this.esModificable = false;
    }

    /**
     * Constructor con opción de modificación (para pantalla de estadísticas)
     * @param jugadores Lista de jugadores a mostrar
     * @param esModificable Si las estadísticas del jugador se pueden modificar
     * @param listener Listener para eventos de modificación
     */
    public JugadoresAdapter(List<Jugador> jugadores, boolean esModificable, OnJugadorListener listener) {
        this.listaJugadores = jugadores;
        this.esModificable = esModificable;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new JugadorViewHolder(view, esModificable, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        Jugador jugador = listaJugadores.get(position);

        // Establecer los datos en la vista
        holder.tvPlayerName.setText(jugador.getNombre() + " " + jugador.getApellidos());
        holder.tvPlayerInfo.setText("#" + jugador.getNumero() + " | " + jugador.getPosicion());

        // Si es modificable, configuramos visibilidad y valores iniciales
        if (esModificable) {
            configureModifiableView(holder);
        } else {
            configureReadOnlyView(holder);

            // Click para ver detalles del jugador
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PlayerDetailActivity.class);
                intent.putExtra("JUGADOR_ID", jugador.getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    /**
     * Configura la vista como modificable (para estadísticas)
     */
    private void configureModifiableView(JugadorViewHolder holder) {
        // Hacer visibles botones de modificación
        holder.showModificationButtons(true);

        // Establecer valores iniciales
        holder.tvMinutes.setText("0");
        holder.tvPoints.setText("0");
        holder.tvRebounds.setText("0");
        holder.tvAssists.setText("0");
        holder.tvFouls.setText("0");

        // Configurar listeners de botones
        configureStatButtons(holder);
    }

    /**
     * Configura la vista como de solo lectura
     */
    private void configureReadOnlyView(JugadorViewHolder holder) {
        holder.showModificationButtons(false);
    }

    /**
     * Configura los botones para modificar estadísticas
     */
    private void configureStatButtons(JugadorViewHolder holder) {
        // Minutos
        holder.btnIncreaseMin.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvMinutes.getText().toString()) + 1;
            holder.tvMinutes.setText(String.valueOf(valor));
            if (listener != null) {
                listener.onStatChanged(holder.getAdapterPosition(), "minutos", valor);
            }
        });

        holder.btnDecreaseMin.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvMinutes.getText().toString());
            if (valor > 0) {
                valor--;
                holder.tvMinutes.setText(String.valueOf(valor));
                if (listener != null) {
                    listener.onStatChanged(holder.getAdapterPosition(), "minutos", valor);
                }
            }
        });

        // Puntos
        holder.btnIncreasePts.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvPoints.getText().toString()) + 1;
            holder.tvPoints.setText(String.valueOf(valor));
            if (listener != null) {
                listener.onStatChanged(holder.getAdapterPosition(), "puntos", valor);
            }
        });

        holder.btnDecreasePts.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvPoints.getText().toString());
            if (valor > 0) {
                valor--;
                holder.tvPoints.setText(String.valueOf(valor));
                if (listener != null) {
                    listener.onStatChanged(holder.getAdapterPosition(), "puntos", valor);
                }
            }
        });

        // Rebotes
        holder.btnIncreaseReb.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvRebounds.getText().toString()) + 1;
            holder.tvRebounds.setText(String.valueOf(valor));
            if (listener != null) {
                listener.onStatChanged(holder.getAdapterPosition(), "rebotes", valor);
            }
        });

        holder.btnDecreaseReb.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvRebounds.getText().toString());
            if (valor > 0) {
                valor--;
                holder.tvRebounds.setText(String.valueOf(valor));
                if (listener != null) {
                    listener.onStatChanged(holder.getAdapterPosition(), "rebotes", valor);
                }
            }
        });

        // Asistencias
        holder.btnIncreaseAst.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvAssists.getText().toString()) + 1;
            holder.tvAssists.setText(String.valueOf(valor));
            if (listener != null) {
                listener.onStatChanged(holder.getAdapterPosition(), "asistencias", valor);
            }
        });

        holder.btnDecreaseAst.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvAssists.getText().toString());
            if (valor > 0) {
                valor--;
                holder.tvAssists.setText(String.valueOf(valor));
                if (listener != null) {
                    listener.onStatChanged(holder.getAdapterPosition(), "asistencias", valor);
                }
            }
        });

        // Faltas
        holder.btnIncreaseFlt.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvFouls.getText().toString()) + 1;
            holder.tvFouls.setText(String.valueOf(valor));
            if (listener != null) {
                listener.onStatChanged(holder.getAdapterPosition(), "faltas", valor);
            }
        });

        holder.btnDecreaseFlt.setOnClickListener(v -> {
            int valor = Integer.parseInt(holder.tvFouls.getText().toString());
            if (valor > 0) {
                valor--;
                holder.tvFouls.setText(String.valueOf(valor));
                if (listener != null) {
                    listener.onStatChanged(holder.getAdapterPosition(), "faltas", valor);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJugadores != null ? listaJugadores.size() : 0;
    }

    /**
     * Actualiza los datos del adaptador
     * @param nuevaLista Nueva lista de jugadores
     */
    public void actualizarDatos(List<Jugador> nuevaLista) {
        this.listaJugadores.clear();
        this.listaJugadores.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    /**
     * Interface para manejar eventos de cambio de estadísticas
     */
    public interface OnJugadorListener {
        void onStatChanged(int position, String statName, int newValue);
    }

    /**
     * ViewHolder para las vistas de jugador
     */
    static class JugadorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvPlayerName, tvPlayerInfo, tvMinutes, tvPoints, tvRebounds, tvAssists, tvFouls;
        ImageView ivPlayerPhoto;
        ImageButton btnIncreaseMin, btnDecreaseMin, btnIncreasePts, btnDecreasePts,
                btnIncreaseReb, btnDecreaseReb, btnIncreaseAst, btnDecreaseAst,
                btnIncreaseFlt, btnDecreaseFlt;

        public JugadorViewHolder(@NonNull View itemView, boolean esModificable, OnJugadorListener listener) {
            super(itemView);

            // Inicializar vistas comunes
            cardView = (CardView) itemView;
            ivPlayerPhoto = itemView.findViewById(R.id.ivPlayerPhoto);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerInfo = itemView.findViewById(R.id.tvPlayerInfo);

            // Referencias a textos de estadísticas
            tvMinutes = itemView.findViewById(R.id.tvMinutes);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvRebounds = itemView.findViewById(R.id.tvRebounds);
            tvAssists = itemView.findViewById(R.id.tvAssists);
            tvFouls = itemView.findViewById(R.id.tvFouls);

            // Referencias a botones de incremento/decremento
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

            // Configurar visibilidad inicial
            showModificationButtons(esModificable);
        }

        /**
         * Controla la visibilidad de los botones de modificación
         */
        public void showModificationButtons(boolean mostrar) {
            int visibility = mostrar ? View.VISIBLE : View.GONE;

            btnIncreaseMin.setVisibility(visibility);
            btnDecreaseMin.setVisibility(visibility);
            btnIncreasePts.setVisibility(visibility);
            btnDecreasePts.setVisibility(visibility);
            btnIncreaseReb.setVisibility(visibility);
            btnDecreaseReb.setVisibility(visibility);
            btnIncreaseAst.setVisibility(visibility);
            btnDecreaseAst.setVisibility(visibility);
            btnIncreaseFlt.setVisibility(visibility);
            btnDecreaseFlt.setVisibility(visibility);
        }
    }
}