package com.example.carws.request;

import java.util.HashMap;

import com.example.carws.model.primaire.Categorie;
import com.example.carws.model.primaire.Modele;
import com.example.carws.model.primaire.Moteur;
import com.example.carws.model.primaire.Vitesse;

public class SearchedElements {
    String idCategorie;
    String idBoite;
    String idMoteur;
    String idModele;

    double minIntervalle;
    double maxIntervalle;

    double kilometrageMin;
    double kilometrageMax;

    Integer anneeMin = -1;
    Integer anneeMax = -1;

    String idCouleur;

    String description;

    public String getIdCouleur(){
        return this.idCouleur;
    }

    public void setIdCouleur(String idCouleur){
        this.idCouleur = idCouleur;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setKilometrageMin(double kilometrage){
        this.kilometrageMin = kilometrage;
    }

    public double getKilometrageMin(){
        return this.kilometrageMin;
    }

    public void setKilometrageMax(double kilometrage){
        this.kilometrageMax = kilometrage;
    }

    public double getKilometrageMax(){
        return this.kilometrageMax;
    }

    public void setAnneeMin(Integer annee){
        this.anneeMin = annee;
    }

    public Integer getAnneeMin(){
        return this.anneeMin;
    }

    public void setAnneeMax(Integer annee){
        this.anneeMax = annee;
    }

    public Integer getAnneeMax(){
        return this.anneeMax;
    }

    public String getIdCategorie(){
        return this.idCategorie;
    }

    public void setIdCategorie(String idcategorie){
        this.idCategorie = idcategorie;
    }

    public String getIdBoite(){
        return this.idBoite;
    }

    public void setIdBoite(String idBoite){
        this.idBoite = idBoite;
    }

    public String getIdMoteur(){
        return this.idMoteur;
    }

    public void setIdMoteur(String idMoteur){
        this.idMoteur = idMoteur;
    }

    public String getIdModele(){
        return this.idModele;
    }

    public void setIdModele(String idModele){
        this.idModele = idModele;
    }

    public double getMinIntervalle(){
        return this.minIntervalle;
    }

    public void setMinIntervalle(double min){
        this.minIntervalle = min;
    }

    public double getMaxIntervalle(){
        return this.maxIntervalle;
    }

    public void setMaxIntervalle(double max){
        this.maxIntervalle = max;
    }

    public SearchedElements(){

    }

    public HashMap<String, Object> allConditions(){
        HashMap<String, Object> conditions = new HashMap<>();

        conditions.put("categorie", this.getIdCategorie());
        conditions.put("vitesse", this.getIdBoite());
        conditions.put("moteur", this.getIdMoteur() );

        // Vitesse vitesse = new Vitesse();
        // vitesse.setId(this.getIdBoite());
        // conditions.put("vitesse", vitesse);

        // Moteur moteur = new Moteur();
        // moteur.setId(this.getIdMoteur());
        // conditions.put("moteur", moteur);

        // Modele modele = new Modele();
        // modele.setId(this.getIdModele());
        // conditions.put("modele", modele);
        // conditions.put("annee", this.getAnnee());

        return conditions;
    }

}
