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
import com.example.tfg.model.Usuario;
import com.example.tfg.ui.main.DashboardActivity;

public class RegisterActivity extends AppCompatActivity {

    // Declaración de variables
    private EditText etNombre, etEmail, etPassword, etConfirmPassword, etTelefono;
    private Button btnRegistrar;
    private TextView tvLogin;
    private ProgressBar progressBar;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmailRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etTelefono = findViewById(R.id.etTelefono);
        btnRegistrar = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);

        // Obtener instancia del gestor de Firebase
        firebaseManager = FirebaseManager.getInstance();

        // Configurar botones
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volver a pantalla anterior (login)
            }
        });
    }

    // Método para registrar un nuevo usuario
    private void registrarUsuario() {
        // Obtener datos de los campos
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        // Validación básica
        if (nombre.isEmpty()) {
            etNombre.setError("Introduce un nombre");
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Introduce un email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Introduce una contraseña");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);

        // Registrar en Firebase
        firebaseManager.registerUser(email, password, task -> {
            if (task.isSuccessful()) {
                // Crear objeto usuario
                String userId = firebaseManager.getCurrentUser().getUid();
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setId(userId);
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setEmail(email);
                nuevoUsuario.setTelefono(telefono);

                // Guardar datos adicionales en la base de datos
                guardarDatosUsuario(nuevoUsuario);
            } else {
                // Error en el registro
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para guardar los datos del usuario en Firebase
    private void guardarDatosUsuario(Usuario usuario) {
        // Guardar en la base de datos
        firebaseManager.getDatabaseReference("usuarios").child(usuario.getId())
                .setValue(usuario.toMap())
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registro completado", Toast.LENGTH_SHORT).show();
                        irAlDashboard();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error al guardar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para ir a la pantalla principal
    private void irAlDashboard() {
        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
