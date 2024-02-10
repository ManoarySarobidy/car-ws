package com.example.carws.model.voiture;

import com.example.carws.model.annonce.Annonce;
import com.example.carws.model.primaire.Categorie;
import com.example.carws.model.primaire.Coloriage;
import com.example.carws.model.primaire.Modele;
import com.example.carws.model.primaire.Moteur;
import com.example.carws.model.primaire.Vitesse;
import com.example.carws.model.users.Users;
import com.fasterxml.jackson.annotation.*;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.example.carws.utility.IdGenerator;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table( name = "voiture" )
@JsonIdentityInfo(
 generator = ObjectIdGenerators.PropertyGenerator.class, 
 property = "id")
public class Voiture{
	@Id
	@Column( name = "id_voiture" )
	@GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name = "prefix" , value = "VOI"), @org.hibernate.annotations.Parameter( name = "sequence", value = "seq_voiture" ), @org.hibernate.annotations.Parameter( name = "max_length", value = "7" ) }  )
    @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;

    @Column( name = "kilometrage")
	double kilometrage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_utilisateur")
    Users user;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_categorie")
    Categorie categorie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_boite")
    @JsonBackReference("voitures")
    Vitesse vitesse;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_moteur")
    Moteur moteur;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_modele")
    Modele modele;

    @OneToMany(mappedBy = "voiture")
    // @JsonManagedReference("voiture-annonce")
    @JsonIgnore
    List<Annonce> annonce;

    @OneToMany(mappedBy = "voiture")
    @JsonBackReference("coloriages-voitures")
    List<Coloriage> coloriages;

    // @Transient
    // Coloriage couleurActuelle;

    // public Coloriage getCouleurActuelle(){
    //     return this.couleurActuelle;
    // }

    // public void setCouleurActuelle(Coloriage couleur){
    //     this.couleurActuelle = couleur;
    // }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setKilometrage(double kilometrage){
        this.kilometrage = kilometrage;
    }

    public double getKilometrage(){
        return this.kilometrage;
    }

    public void setUser(Users user){
        this.user = user;
    }

    public Users getUser(){
        return this.user;
    }

    public void setCategorie(Categorie categorie){
        this.categorie = categorie;
    }

    public Categorie getCategorie(){
        return this.categorie;
    }

    public void setVitesse(Vitesse vitesse){
        this.vitesse = vitesse;
    }

    public Vitesse getVitesse(){
        return this.vitesse;
    }

    public void setMoteur(Moteur moteur){
        this.moteur = moteur;
    }

    public Moteur getMoteur(){
        return this.moteur;
    }

    public void setModele(Modele modele){
        this.modele = modele;
    }

    public Modele getModele(){
        return this.modele;
    }

    public void setAnnonce(List<Annonce> annonce){
        this.annonce = annonce;
    }

    public List<Annonce> getAnnonce(){
        return this.annonce;
    }

    public List<Coloriage> getColoriages(){
        return this.coloriages;
    }

    public void setColoriages(List<Coloriage> coloriages){
        this.coloriages = coloriages;
    }

    public Voiture(){
        
    }
}