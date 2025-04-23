package com.example.mybestlocations;

public class Position {
    private int idposition;
    private String pseudo;
    private String numero;
    private String logitude;
    private String latitude;

    public Position(int idposition, String pseudo, String numero, String logitude, String latitude) {
        this.idposition = idposition;
        this.pseudo = pseudo;
        this.numero = numero;
        this.logitude = logitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ID: " + idposition + 
               "\nPseudo: " + pseudo + 
               "\nNumero: " + numero + 
               "\nPosition: " + logitude + ", " + latitude;
    }
}
