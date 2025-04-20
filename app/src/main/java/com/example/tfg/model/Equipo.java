package com.example.tfg.model;

import java.util.HashMap;
import java.util.Map;

public class Equipo {
    private String id;
    private String nombre;
    private String categoria;
    private String temporada;
    private String entrenadorId;

    // Constructor vacio
    public Equipo() {
    }

    //constructor con todos los atributos
    public Equipo(String id, String nombre, String categoria, String temporada, String entrenadorId) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.temporada = temporada;
        this.entrenadorId = entrenadorId;
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
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getTemporada() {
        return temporada;
    }
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }
    public String getEntrenadorId() {
        return entrenadorId;
    }
    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }

    // Método para convertir el objeto a un Map para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nombre", nombre);
        result.put("categoria", categoria);
        result.put("temporada", temporada);
        result.put("entrenadorId", entrenadorId);
        return result;
    }

    // Método para crear un objeto desde un Map de Firebase
    public static Equipo fromMap(Map<String, Object> map) {
        String id = (String) map.get("id");
        String nombre = (String) map.get("nombre");
        String categoria = (String) map.get("categoria");
        String temporada = (String) map.get("temporada");
        String entrenadorId = (String) map.get("entrenadorId");

        return new Equipo(id, nombre, categoria, temporada, entrenadorId);
    }
}
