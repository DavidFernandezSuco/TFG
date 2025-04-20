package com.example.tfg.ui.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.adapters.JugadoresAdapter;
import com.example.tfg.firebase.FirebaseManager;
import com.example.tfg.model.Equipo;
import com.example.tfg.model.Jugador;
import com.example.tfg.ui.player.PlayerFormActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamDetailActivity extends AppCompatActivity {

    // Vistas
    private TextView tvTeamName, tvTeamCategory, tvTeamSeason, tvNoPlayers;
    private ImageView ivTeamLogo;
    private RecyclerView rvPlayers;
    private FloatingActionButton fabAddPlayer;
    private ProgressBar progressBar;

    // Firebase y datos
    private FirebaseManager firebaseManager;
    private String equipoId;
    private Equipo equipo;
    private List<Jugador> jugadores = new ArrayList<>();
    private JugadoresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        // Inicializar Firebase
        firebaseManager = FirebaseManager.getInstance();

        // Obtener ID del equipo de los extras
        equipoId = getIntent().getStringExtra("EQUIPO_ID");
        if (equipoId == null) {
            Toast.makeText(this, "Error: ID de equipo no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        inicializarVistas();

        // Configurar Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Cargar datos
        cargarDatosEquipo();
    }

    private void inicializarVistas() {
        tvTeamName = findViewById(R.id.tvTeamName);
        tvTeamCategory = findViewById(R.id.tvTeamCategory);
        tvTeamSeason = findViewById(R.id.tvTeamSeason);
        tvNoPlayers = findViewById(R.id.tvNoPlayers);
        ivTeamLogo = findViewById(R.id.ivTeamLogo);
        rvPlayers = findViewById(R.id.rvPlayers);
        fabAddPlayer = findViewById(R.id.fabAddPlayer);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerView
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JugadoresAdapter(jugadores);
        rvPlayers.setAdapter(adapter);

        // Botón para añadir jugador
        fabAddPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(TeamDetailActivity.this, PlayerFormActivity.class);
            intent.putExtra("EQUIPO_ID", equipoId);
            startActivity(intent);
        });
    }

    private void cargarDatosEquipo() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseManager.getDatabaseReference("equipos").child(equipoId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> equipoMap = (Map<String, Object>) dataSnapshot.getValue();
                            equipo = Equipo.fromMap(equipoMap);
                            mostrarDatosEquipo();
                        } else {
                            Toast.makeText(TeamDetailActivity.this, "Equipo no encontrado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TeamDetailActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });

        cargarJugadoresEquipo();
    }

    private void mostrarDatosEquipo() {
        if (equipo != null) {
            tvTeamName.setText(equipo.getNombre());
            tvTeamCategory.setText(equipo.getCategoria());
            tvTeamSeason.setText(equipo.getTemporada());

            // Aquí podrías cargar la imagen del equipo si tuvieras una URL
            // Glide.with(this).load(equipo.getImagenUrl()).into(ivTeamLogo);
        }
    }

    private void cargarJugadoresEquipo() {
        firebaseManager.getDatabaseReference("jugadores")
                .orderByChild("equipoId")
                .equalTo(equipoId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        jugadores.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> jugadorMap = (Map<String, Object>) snapshot.getValue();
                            Jugador jugador = Jugador.fromMap(jugadorMap);
                            jugadores.add(jugador);
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                        // Mostrar mensaje si no hay jugadores
                        if (jugadores.isEmpty()) {
                            tvNoPlayers.setVisibility(View.VISIBLE);
                            rvPlayers.setVisibility(View.GONE);
                        } else {
                            tvNoPlayers.setVisibility(View.GONE);
                            rvPlayers.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TeamDetailActivity.this, "Error al cargar jugadores", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_edit_team) {
            Intent intent = new Intent(TeamDetailActivity.this, TeamFormActivity.class);
            intent.putExtra("EQUIPO_ID", equipoId);
            intent.putExtra("MODO_EDICION", true);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_delete_team) {
            confirmarEliminacion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar equipo")
                .setMessage("¿Estás seguro de que quieres eliminar este equipo?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarEquipo())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarEquipo() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseManager.getDatabaseReference("equipos").child(equipoId)
                .removeValue()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(TeamDetailActivity.this, "Equipo eliminado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TeamDetailActivity.this, "Error al eliminar equipo", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}