package com.example.tfg.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.adapters.EquiposAdapter;
import com.example.tfg.adapters.PartidosAdapter;
import com.example.tfg.firebase.FirebaseManager;
import com.example.tfg.model.Equipo;
import com.example.tfg.model.Partido;
import com.example.tfg.ui.match.MatchFormActivity;
import com.example.tfg.ui.match.MatchesActivity;
import com.example.tfg.ui.player.PlayerFormActivity;
import com.example.tfg.ui.profile.ProfileActivity;
import com.example.tfg.ui.stats.StatsActivity;
import com.example.tfg.ui.team.TeamFormActivity;
import com.example.tfg.ui.team.TeamsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    // Vistas
    private ProgressBar progressBar;
    private RecyclerView rvUpcomingMatches, rvMyTeams;
    private TextView tvNoUpcomingMatches, tvNoTeams;
    private Button btnAddPlayer, btnAddTeam, btnAddMatch, btnViewAllStats;
    private ImageView ivProfile;
    private BottomNavigationView bottomNavigation;

    // Adaptadores
    private PartidosAdapter partidosAdapter;
    private EquiposAdapter equiposAdapter;

    // FirebaseManager
    private FirebaseManager firebaseManager;

    // Listas
    private List<Partido> proximosPartidos;
    private List<Equipo> misEquipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inicializar FirebaseManager
        firebaseManager = FirebaseManager.getInstance();

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializar vistas
        inicializarVistas();

        // Configurar listeners
        configurarListeners();

        // Configurar navegación
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        // Cargar datos
        cargarDatos();
    }

    private void inicializarVistas() {
        progressBar = findViewById(R.id.progressBar);
        rvUpcomingMatches = findViewById(R.id.rvUpcomingMatches);
        rvMyTeams = findViewById(R.id.rvMyTeams);
        tvNoUpcomingMatches = findViewById(R.id.tvNoUpcomingMatches);
        tvNoTeams = findViewById(R.id.tvNoTeams);
        btnAddPlayer = findViewById(R.id.btnAddPlayer);
        btnAddTeam = findViewById(R.id.btnAddTeam);
        btnAddMatch = findViewById(R.id.btnAddMatch);
        btnViewAllStats = findViewById(R.id.btnViewAllStats);
        ivProfile = findViewById(R.id.ivProfile);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Configurar RecyclerView de partidos
        rvUpcomingMatches.setLayoutManager(new LinearLayoutManager(this));
        proximosPartidos = new ArrayList<>();
        partidosAdapter = new PartidosAdapter(proximosPartidos);
        rvUpcomingMatches.setAdapter(partidosAdapter);

        // Configurar RecyclerView de equipos
        rvMyTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        misEquipos = new ArrayList<>();
        equiposAdapter = new EquiposAdapter(misEquipos);
        rvMyTeams.setAdapter(equiposAdapter);
    }

    private void configurarListeners() {
        // Botón de perfil
        ivProfile.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Botones de acciones rápidas
        btnAddPlayer.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, PlayerFormActivity.class);
            startActivity(intent);
        });

        btnAddTeam.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, TeamFormActivity.class);
            startActivity(intent);
        });

        btnAddMatch.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, MatchFormActivity.class);
            startActivity(intent);
        });

        // Botón ver todas las estadísticas
        btnViewAllStats.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, StatsActivity.class);
            startActivity(intent);
        });
    }

    private void cargarDatos() {
        // Mostrar indicador de carga
        progressBar.setVisibility(View.VISIBLE);

        // Cargar próximos partidos
        cargarProximosPartidos();

        // Cargar mis equipos
        cargarMisEquipos();
    }

    private void cargarProximosPartidos() {
        String userId = firebaseManager.getCurrentUser().getUid();

        firebaseManager.getDatabaseReference("partidos")
                .orderByChild("entrenadorId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        proximosPartidos.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                            if (map != null) {
                                Partido partido = Partido.fromMap(map);

                                if ("pendiente".equals(partido.getEstado())) {
                                    proximosPartidos.add(partido);
                                }
                            }
                        }

                        partidosAdapter.notifyDataSetChanged();

                        // Mostrar mensaje si no hay partidos
                        if (proximosPartidos.isEmpty()) {
                            tvNoUpcomingMatches.setVisibility(View.VISIBLE);
                            rvUpcomingMatches.setVisibility(View.GONE);
                        } else {
                            tvNoUpcomingMatches.setVisibility(View.GONE);
                            rvUpcomingMatches.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void cargarMisEquipos() {
        String userId = firebaseManager.getCurrentUser().getUid();

        firebaseManager.getDatabaseReference("equipos")
                .orderByChild("entrenadorId")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        misEquipos.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                            if (map != null) {
                                Equipo equipo = Equipo.fromMap(map);
                                misEquipos.add(equipo);
                            }
                        }

                        equiposAdapter.notifyDataSetChanged();

                        // Mostrar mensaje si no hay equipos
                        if (misEquipos.isEmpty()) {
                            tvNoTeams.setVisibility(View.VISIBLE);
                            rvMyTeams.setVisibility(View.GONE);
                        } else {
                            tvNoTeams.setVisibility(View.GONE);
                            rvMyTeams.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // No hacer nada
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            return true; // Ya estamos en el dashboard
        } else if (item.getItemId() == R.id.nav_teams) {
            startActivity(new Intent(this, TeamsActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_matches) {
            startActivity(new Intent(this, MatchesActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_stats) {
            startActivity(new Intent(this, StatsActivity.class));
            return true;
        }
        return false;
    }
}