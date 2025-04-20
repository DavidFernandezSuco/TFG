package com.example.tfg.ui.auth;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.R;
import com.example.tfg.firebase.FirebaseManager;
import com.example.tfg.ui.main.DashboardActivity;

public class LoginActivity extends AppCompatActivity {

    // Declaración de variables
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);

        // Obtener instancia del gestor de Firebase
        firebaseManager = FirebaseManager.getInstance();

        // Comprobar si ya hay sesión iniciada
        if (firebaseManager.isUserLoggedIn()) {
            irAlDashboard();
        }

        // Configurar botones
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irARegistro();
            }
        });
    }

    // Método para iniciar sesión
    private void iniciarSesion() {
        // Obtener datos de los campos
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación básica
        if (email.isEmpty()) {
            etEmail.setError("Introduce un email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Introduce una contraseña");
            return;
        }

        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);

        // Iniciar sesión con Firebase
        firebaseManager.loginUser(email, password, task -> {
            // Ocultar loading
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                irAlDashboard();
            } else {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para ir a la pantalla principal
    private void irAlDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    // Método para ir a la pantalla de registro
    private void irARegistro() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}