package com.example.tfg.ui.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.adapters.JugadoresAdapter;
import com.example.tfg.firebase.FirebaseManager;
import com.example.tfg.model.Jugador;
import com.example.tfg.model.Partido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MatchDetailActivity extends AppCompatActivity {

    // Referencias a vistas
    private TextView tvDateTime, tvLocation, tvScore, tvMatchStatus;
    private TextView tvHomeTeamName, tvAwayTeamName;
    private ImageView ivHomeTeamLogo, ivAwayTeamLogo, ivShare;
    private Button btnUpdateScore;
    private RecyclerView rvHomeTeamPlayers, rvAwayTeamPlayers;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseManager firebaseManager;

    // Datos
    private String partidoId;
    private Partido partido;
    private SimpleDateFormat dateFormat;
    private JugadoresAdapter homeTeamAdapter, awayTeamAdapter;
    private List<Jugador> jugadoresLocal = new ArrayList<>();
    private List<Jugador> jugadoresVisitante = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

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
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle del partido");
        }

        // Cargar datos del partido
        cargarDatosPartido();
    }

    private void inicializarVistas() {
        tvDateTime = findViewById(R.id.tvDateTime);
        tvLocation = findViewById(R.id.tvLocation);
        tvScore = findViewById(R.id.tvScore);
        tvMatchStatus = findViewById(R.id.tvMatchStatus);
        tvHomeTeamName = findViewById(R.id.tvHomeTeamName);
        tvAwayTeamName = findViewById(R.id.tvAwayTeamName);
        ivHomeTeamLogo = findViewById(R.id.ivHomeTeamLogo);
        ivAwayTeamLogo = findViewById(R.id.ivAwayTeamLogo);
        ivShare = findViewById(R.id.ivShare);
        btnUpdateScore = findViewById(R.id.btnUpdateScore);
        rvHomeTeamPlayers = findViewById(R.id.rvHomeTeamPlayers);
        rvAwayTeamPlayers = findViewById(R.id.rvAwayTeamPlayers);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerViews
        rvHomeTeamPlayers.setLayoutManager(new LinearLayoutManager(this));
        rvAwayTeamPlayers.setLayoutManager(new LinearLayoutManager(this));

        // Adapters vacíos inicialmente
        homeTeamAdapter = new JugadoresAdapter(jugadoresLocal);
        awayTeamAdapter = new JugadoresAdapter(jugadoresVisitante);
        rvHomeTeamPlayers.setAdapter(homeTeamAdapter);
        rvAwayTeamPlayers.setAdapter(awayTeamAdapter);

        // Botón para actualizar marcador
        btnUpdateScore.setOnClickListener(v -> {
            Intent intent = new Intent(MatchDetailActivity.this, GameStatsActivity.class);
            intent.putExtra("PARTIDO_ID", partidoId);
            startActivity(intent);
        });

        // Botón de compartir
        ivShare.setOnClickListener(v -> compartirInfoPartido());
    }

    private void cargarDatosPartido() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtener datos del partido
        firebaseManager.getDatabaseReference("partidos").child(partidoId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> partidoMap = (Map<String, Object>) dataSnapshot.getValue();
                            partido = Partido.fromMap(partidoMap);

                            // Mostrar datos del partido
                            mostrarDatosPartido();

                            // Cargar jugadores de ambos equipos
                            cargarJugadoresEquipo(partido.getEquipoLocalId(), true);
                            cargarJugadoresEquipo(partido.getEquipoVisitanteId(), false);

                            // Actualizar botón según estado
                            actualizarEstadoBoton();
                        } else {
                            Toast.makeText(MatchDetailActivity.this, "Error: Partido no encontrado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MatchDetailActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarDatosPartido() {
        // Formatear y mostrar fecha y lugar
        Date fechaPartido = new Date(partido.getFecha());
        tvDateTime.setText(dateFormat.format(fechaPartido));
        tvLocation.setText(partido.getLugar());

        // Mostrar equipos
        tvHomeTeamName.setText(partido.getNombreEquipoLocal());
        tvAwayTeamName.setText(partido.getNombreEquipoVisitante());

        // Mostrar resultado
        tvScore.setText(partido.getPuntosLocal() + " - " + partido.getPuntosVisitante());

        // Mostrar estado
        if ("pendiente".equals(partido.getEstado())) {
            tvMatchStatus.setText("PENDIENTE");
        } else {
            tvMatchStatus.setText("FINAL");
        }
    }

    private void actualizarEstadoBoton() {
        if ("pendiente".equals(partido.getEstado())) {
            btnUpdateScore.setText("Registrar estadísticas");
        } else {
            btnUpdateScore.setText("Actualizar estadísticas");
        }
    }

    private void cargarJugadoresEquipo(String equipoId, boolean esLocal) {
        firebaseManager.getDatabaseReference("jugadores")
                .orderByChild("equipoId")
                .equalTo(equipoId)
                .addValueEventListener(new ValueEventListener() {
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
                            homeTeamAdapter.notifyDataSetChanged();
                        } else {
                            jugadoresVisitante.clear();
                            jugadoresVisitante.addAll(jugadores);
                            awayTeamAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MatchDetailActivity.this,
                                "Error al cargar jugadores",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void compartirInfoPartido() {
        String infoPartido = partido.getNombreEquipoLocal() + " " +
                partido.getPuntosLocal() + " - " +
                partido.getPuntosVisitante() + " " +
                partido.getNombreEquipoVisitante() + "\n" +
                dateFormat.format(new Date(partido.getFecha())) + "\n" +
                partido.getLugar();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, infoPartido);
        startActivity(Intent.createChooser(shareIntent, "Compartir información del partido"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Reemplaza onBackPressed() con finish() para cerrar la actividad
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
