package com.example.tfg.model;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String telefono;

    // Constructor vacio
    public Usuario() {
    }

    //cosntructor con todos los atributos
    public Usuario(String id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    //getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método para convertir el objeto a un Map para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nombre", nombre);
        result.put("email", email);
        result.put("telefono", telefono);
        return result;
    }

    // Método para crear un objeto desde un Map de Firebase
    public static Usuario fromMap(Map<String, Object> map) {
        String id = (String) map.get("id");
        String nombre = (String) map.get("nombre");
        String email = (String) map.get("email");
        String telefono = (String) map.get("telefono");

        return new Usuario(id, nombre, email, telefono);
    }
}
