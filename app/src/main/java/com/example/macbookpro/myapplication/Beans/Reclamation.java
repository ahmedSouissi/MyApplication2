package com.example.macbookpro.myapplication.Beans;

public class Reclamation {
    private String rue ;
    private String categorie ;
    private String message;
    private String type ;

    public Reclamation() {

    }

    public Reclamation(String rue, String categorie, String message, String type) {
        this.rue = rue;
        this.categorie = categorie;
        this.message = message;
        this.type = type;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

