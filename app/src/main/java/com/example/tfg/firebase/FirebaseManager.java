package com.example.tfg.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    // Instancia única (patrón Singleton)
    private static FirebaseManager instance;

    // Referencias a los servicios de Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;


    // Constructor privado para evitar instanciación directa
    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    /**
     * Obtiene la instancia única de FirebaseManager
     */
    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    /**
     * Registra un nuevo usuario con email y contraseña
     */
    public void registerUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    /**
     * Inicia sesión con email y contraseña
     */
    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public void logoutUser() {
        auth.signOut();
    }

    /**
     * Envía un correo para restablecer la contraseña
     */
    public void sendPasswordResetEmail(String email, OnCompleteListener<Void> listener) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(listener);
    }

    /**
     * Obtiene una referencia a la base de datos
     */
    public DatabaseReference getDatabaseReference(String path) {
        return database.getReference(path);
    }

}

