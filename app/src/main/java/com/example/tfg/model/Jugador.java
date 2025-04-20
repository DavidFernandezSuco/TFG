package com.example.tfg.model;

import java.util.HashMap;
import java.util.Map;

public class Jugador {
    private String id;
    private String nombre;
    private String apellidos;
    private int numero;
    private String posicion;
    private String equipoId;
    private int altura; // en cm
    private int edad;

    // Constructor vacio
    public Jugador() {
    }

    //constructor con todos los atributos
    public Jugador(String id, String nombre, String apellidos, int numero, String posicion, String equipoId, int altura, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numero = numero;
        this.posicion = posicion;
        this.equipoId = equipoId;
        this.altura = altura;
        this.edad = edad;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(String equipoId) {
        this.equipoId = equipoId;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    // Método para convertir el objeto a un Map para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nombre", nombre);
        result.put("apellidos", apellidos);
        result.put("numero", numero);
        result.put("posicion", posicion);
        result.put("equipoId", equipoId);
        result.put("altura", altura);
        result.put("edad", edad);
        return result;
    }

    // Método para crear un objeto desde un Map de Firebase
    public static Jugador fromMap(Map<String, Object> map) {
        // Manejo seguro de valores nulos
        String id = (String) map.get("id");
        String nombre = (String) map.get("nombre");
        String apellidos = (String) map.get("apellidos");

        // Manejo de tipos numéricos
        // (Firebase almacena números como Long, hay que convertirlos a int)
        int numero = 0;
        if (map.get("numero") != null) {
            numero = ((Long) map.get("numero")).intValue();
        }

        String posicion = (String) map.get("posicion");
        String equipoId = (String) map.get("equipoId");

        int altura = 0;
        if (map.get("altura") != null) {
            altura = ((Long) map.get("altura")).intValue();
        }

        int edad = 0;
        if (map.get("edad") != null) {
            edad = ((Long) map.get("edad")).intValue();
        }

        return new Jugador(id, nombre, apellidos, numero, posicion, equipoId, altura, edad);
    }
}

