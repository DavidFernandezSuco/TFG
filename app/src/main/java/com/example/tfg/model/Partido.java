package com.example.tfg.model;

import java.util.HashMap;
import java.util.Map;

public class Partido {
    private String id;
    private String equipoLocalId;
    private String equipoVisitanteId;
    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private long fecha; // timestamp
    private String lugar;
    private int puntosLocal;
    private int puntosVisitante;
    private String estado; // "pendiente" o "jugado"
    private String entrenadorId;

    // Constructor vacio
    public Partido() {
    }

    //constructor con todos los atributos
    public Partido(String id, String equipoLocalId, String equipoVisitanteId, String nombreEquipoLocal, String nombreEquipoVisitante, long fecha, String lugar, int puntosLocal, int puntosVisitante, String estado, String entrenadorId) {
        this.id = id;
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.nombreEquipoLocal = nombreEquipoLocal;
        this.nombreEquipoVisitante = nombreEquipoVisitante;
        this.fecha = fecha;
        this.lugar = lugar;
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
        this.estado = estado;
        this.entrenadorId = entrenadorId;
    }

    //getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEquipoLocalId() {
        return equipoLocalId;
    }
    public void setEquipoLocalId(String equipoLocalId) {
        this.equipoLocalId = equipoLocalId;
    }
    public String getEquipoVisitanteId() {
        return equipoVisitanteId;
    }
    public void setEquipoVisitanteId(String equipoVisitanteId) {
        this.equipoVisitanteId = equipoVisitanteId;
    }
    public String getNombreEquipoLocal() {
        return nombreEquipoLocal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para convertir el objeto a un Map para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("equipoLocalId", equipoLocalId);
        result.put("equipoVisitanteId", equipoVisitanteId);
        result.put("nombreEquipoLocal", nombreEquipoLocal);
        result.put("nombreEquipoVisitante", nombreEquipoVisitante);
        result.put("fecha", fecha);
        result.put("lugar", lugar);
        result.put("puntosLocal", puntosLocal);
        result.put("puntosVisitante", puntosVisitante);
        result.put("estado", estado);
        result.put("entrenadorId", entrenadorId);
        return result;
    }

    // Método para crear un objeto desde un Map de Firebase
    public static Partido fromMap(Map<String, Object> map) {
        // Manejo seguro de valores nulos
        String id = (String) map.get("id");
        String equipoLocalId = (String) map.get("equipoLocalId");
        String equipoVisitanteId = (String) map.get("equipoVisitanteId");
        String nombreEquipoLocal = (String) map.get("nombreEquipoLocal");
        String nombreEquipoVisitante = (String) map.get("nombreEquipoVisitante");

        // Manejo seguro de Long
        long fecha = map.get("fecha") != null ? (Long) map.get("fecha") : 0L;

        String lugar = (String) map.get("lugar");

        // Manejo seguro de valores numéricos
        int puntosLocal = map.get("puntosLocal") != null ? ((Long) map.get("puntosLocal")).intValue() : 0;
        int puntosVisitante = map.get("puntosVisitante") != null ? ((Long) map.get("puntosVisitante")).intValue() : 0;

        String estado = (String) map.get("estado");
        String entrenadorId = (String) map.get("entrenadorId");

        return new Partido(id, equipoLocalId, equipoVisitanteId, nombreEquipoLocal,
                nombreEquipoVisitante, fecha, lugar, puntosLocal,
                puntosVisitante, estado, entrenadorId);
    }
}
