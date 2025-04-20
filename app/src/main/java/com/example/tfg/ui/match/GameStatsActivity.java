package com.example.tfg.ui.match;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.adapters.PlayerStatsAdapter;
import com.example.tfg.firebase.FirebaseManager;
import com.example.tfg.model.Equipo;
import com.example.tfg.model.Estadistica;
import com.example.tfg.model.Jugador;
import com.example.tfg.model.Partido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GameStatsActivity extends AppCompatActivity implements PlayerStatsAdapter.OnStatsChangeListener {

    // Referencias a vistas
    private TextView tvMatchDate, tvHomeTeam, tvAwayTeam, tvScore;
    private RadioGroup rgTeamSelector;
    private RadioButton rbHomeTeam, rbAwayTeam;
    private RecyclerView rvPlayerStats;
    private Button btnSaveStats;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    // Firebase
    private FirebaseManager firebaseManager;

    // Datos
    private String partidoId;
    private Partido partido;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private List<Jugador> jugadoresLocal = new ArrayList<>();
    private List<Jugador> jugadoresVisitante = new ArrayList<>();
    private PlayerStatsAdapter adapter;
    private boolean mostrandoLocal = true; // Por defecto muestra el equipo local

    // Formato de fecha
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stats);

        // Inicializar Firebase
        firebaseManager = FirebaseManager.getInstance();

        // Inicializar formato de fecha
        dateFormat = new SimpleDateFormat("dd MMMM yyyy | HH:mm'h'", new Locale("es", "ES"));

        // Obtener ID del partido de los extras
        partidoId = getIntent().getStringExtra("PARTIDO_ID");
        if (partidoId == null) {
            Toast.makeText(this, "Error: No se pudo obtener información del partido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        inicializarVistas();

        // Configurar Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Estadísticas del partido");
        }

        // Cargar datos del partido
        cargarDatosPartido();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        tvMatchDate = findViewById(R.id.tvMatchDate);
        tvHomeTeam = findViewById(R.id.tvHomeTeam);
        tvAwayTeam = findViewById(R.id.tvAwayTeam);
        tvScore = findViewById(R.id.tvScore);
        rgTeamSelector = findViewById(R.id.rgTeamSelector);
        rbHomeTeam = findViewById(R.id.rbHomeTeam);
        rbAwayTeam = findViewById(R.id.rbAwayTeam);
        rvPlayerStats = findViewById(R.id.rvPlayerStats);
        btnSaveStats = findViewById(R.id.btnSaveStats);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerView
        rvPlayerStats.setLayoutManager(new LinearLayoutManager(this));

        // Configurar RadioGroup para cambiar entre equipos
        rgTeamSelector.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbHomeTeam) {
                mostrandoLocal = true;
                mostrarJugadoresLocal();
            } else {
                mostrandoLocal = false;
                mostrarJugadoresVisitante();
            }
        });

        // Configurar botón de guardar estadísticas
        btnSaveStats.setOnClickListener(v -> guardarEstadisticas());
    }

    private void cargarDatosPartido() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener datos del partido
        firebaseManager.getDatabaseReference("partidos").child(partidoId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> partidoMap = (Map<String, Object>) dataSnapshot.getValue();
                            partido = Partido.fromMap(partidoMap);

                            // Mostrar datos del partido
                            mostrarDatosPartido();

                            // Cargar equipos y jugadores
                            cargarEquipos();
                        } else {
                            Toast.makeText(GameStatsActivity.this, "Error: Partido no encontrado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GameStatsActivity.this, "Error al cargar datos: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void mostrarDatosPartido() {
        // Formatear y mostrar fecha
        Date fechaPartido = new Date(partido.getFecha());
        tvMatchDate.setText(dateFormat.format(fechaPartido));

        // Mostrar equipos
        tvHomeTeam.setText(partido.getNombreEquipoLocal());
        tvAwayTeam.setText(partido.getNombreEquipoVisitante());

        // Mostrar marcador
        tvScore.setText(partido.getPuntosLocal() + " - " + partido.getPuntosVisitante());

        // Configurar nombres en los RadioButtons
        rbHomeTeam.setText(partido.getNombreEquipoLocal());
        rbAwayTeam.setText(partido.getNombreEquipoVisitante());
    }

    private void cargarEquipos() {
        // Cargar equipo local
        firebaseManager.getDatabaseReference("equipos").child(partido.getEquipoLocalId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> equipoMap = (Map<String, Object>) dataSnapshot.getValue();
                            equipoLocal = Equipo.fromMap(equipoMap);

                            // Cargar jugadores del equipo local
                            cargarJugadoresEquipo(partido.getEquipoLocalId(), true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GameStatsActivity.this, "Error al cargar equipo local", Toast.LENGTH_SHORT).show();
                    }
                });

        // Cargar equipo visitante
        firebaseManager.getDatabaseReference("equipos").child(partido.getEquipoVisitanteId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> equipoMap = (Map<String, Object>) dataSnapshot.getValue();
                            equipoVisitante = Equipo.fromMap(equipoMap);

                            // Cargar jugadores del equipo visitante
                            cargarJugadoresEquipo(partido.getEquipoVisitanteId(), false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GameStatsActivity.this, "Error al cargar equipo visitante", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarJugadoresEquipo(String equipoId, boolean esLocal) {
        firebaseManager.getDatabaseReference("jugadores")
                .orderByChild("equipoId")
                .equalTo(equipoId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Jugador> jugadores = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> jugadorMap = (Map<String, Object>) snapshot.getValue();
                            Jugador jugador = Jugador.fromMap(jugadorMap);
                            jugadores.add(jugador);
                        }

                        if (esLocal) {
                            jugadoresLocal.clear();
                            jugadoresLocal.addAll(jugadores);

                            // Si es el equipo local, mostrar sus jugadores por defecto
                            if (mostrandoLocal) {
                                mostrarJugadoresLocal();
                            }
                        } else {
                            jugadoresVisitante.clear();
                            jugadoresVisitante.addAll(jugadores);

                            // Si es el equipo visitante y estamos mostrando visitante, actualizar
                            if (!mostrandoLocal) {
                                mostrarJugadoresVisitante();
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GameStatsActivity.this,
                                "Error al cargar jugadores: " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarJugadoresLocal() {
        if (jugadoresLocal.isEmpty()) {
            Toast.makeText(this, "No hay jugadores para este equipo", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new PlayerStatsAdapter(jugadoresLocal, this);
        rvPlayerStats.setAdapter(adapter);
    }

    private void mostrarJugadoresVisitante() {
        if (jugadoresVisitante.isEmpty()) {
            Toast.makeText(this, "No hay jugadores para este equipo", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new PlayerStatsAdapter(jugadoresVisitante, this);
        rvPlayerStats.setAdapter(adapter);
    }

    private void guardarEstadisticas() {
        if (adapter == null) {
            Toast.makeText(this, "No hay datos para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Obtener estadísticas del adaptador
        Map<String, Estadistica> estadisticas = adapter.getEstadisticas();

        // Contador para controlar cuando se completan todas las operaciones
        final int[] contador = {estadisticas.size()};

        // Guardar cada estadística en Firebase
        for (Map.Entry<String, Estadistica> entry : estadisticas.entrySet()) {
            Estadistica estadistica = entry.getValue();

            // Generar ID para la estadística si no existe
            if (estadistica.getId() == null || estadistica.getId().isEmpty()) {
                estadistica.setId(UUID.randomUUID().toString());
            }

            // Asignar ID del partido
            estadistica.setPartidoId(partidoId);

            // Guardar en Firebase
            firebaseManager.getDatabaseReference("estadisticas")
                    .child(estadistica.getId())
                    .setValue(estadistica.toMap())
                    .addOnCompleteListener(task -> {
                        // Decrementar contador
                        contador[0]--;

                        // Si todas las operaciones están completas
                        if (contador[0] == 0) {
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                Toast.makeText(GameStatsActivity.this,
                                        "Estadísticas guardadas correctamente",
                                        Toast.LENGTH_SHORT).show();

                                // Marcar partido como jugado si no lo estaba
                                if (!"jugado".equals(partido.getEstado())) {
                                    actualizarEstadoPartido();
                                } else {
                                    finish();
                                }
                            } else {
                                Toast.makeText(GameStatsActivity.this,
                                        "Error al guardar algunas estadísticas",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void actualizarEstadoPartido() {
        // Actualizar estado del partido a "jugado"
        partido.setEstado("jugado");

        firebaseManager.getDatabaseReference("partidos")
                .child(partidoId)
                .child("estado")
                .setValue("jugado")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(GameStatsActivity.this,
                                "Error al actualizar estado del partido",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStatChanged(String jugadorId, String statName, int newValue) {
        // Este método se llama cuando cambia una estadística en el adaptador
        // Podría usarse para actualizar totales o realizar otras acciones
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
