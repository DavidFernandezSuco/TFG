package com.example.tfg.model;

import java.util.HashMap;
import java.util.Map;

public class Estadistica {
    private String id;
    private String jugadorId;
    private String partidoId;
    private int puntos;
    private int rebotes;
    private int asistencias;
    private int faltas;
    private int minutos;

    // Constructor vacio
    public Estadistica() {
    }

    //constructor con todos los atributos
    public Estadistica(String id, String jugadorId, String partidoId, int puntos, int rebotes, int asistencias, int faltas, int minutos) {
        this.id = id;
        this.jugadorId = jugadorId;
        this.partidoId = partidoId;
        this.puntos = puntos;
        this.rebotes = rebotes;
        this.asistencias = asistencias;
        this.faltas = faltas;
        this.minutos = minutos;
    }

    //getters y setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getRebotes() {
        return rebotes;
    }

    public void setRebotes(int rebotes) {
        this.rebotes = rebotes;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(String partidoId) {
        this.partidoId = partidoId;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    // Método para convertir el objeto a un Map para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("jugadorId", jugadorId);
        result.put("partidoId", partidoId);
        result.put("puntos", puntos);
        result.put("rebotes", rebotes);
        result.put("asistencias", asistencias);
        result.put("faltas", faltas);
        result.put("minutos", minutos);
        return result;
    }

    // Método para crear un objeto desde un Map de Firebase
    public static Estadistica fromMap(Map<String, Object> map) {
        // Manejo seguro de valores nulos
        String id = (String) map.get("id");
        String jugadorId = (String) map.get("jugadorId");
        String partidoId = (String) map.get("partidoId");

        // Manejo seguro de valores numéricos
        int puntos = map.get("puntos") != null ? ((Long) map.get("puntos")).intValue() : 0;
        int rebotes = map.get("rebotes") != null ? ((Long) map.get("rebotes")).intValue() : 0;
        int asistencias = map.get("asistencias") != null ? ((Long) map.get("asistencias")).intValue() : 0;
        int faltas = map.get("faltas") != null ? ((Long) map.get("faltas")).intValue() : 0;
        int minutos = map.get("minutos") != null ? ((Long) map.get("minutos")).intValue() : 0;

        return new Estadistica(id, jugadorId, partidoId, puntos, rebotes, asistencias, faltas, minutos);
    }
}
