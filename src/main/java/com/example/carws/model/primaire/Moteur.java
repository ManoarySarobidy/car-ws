package com.example.carws.model.primaire;

import com.example.carws.utility.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.*;
   

import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import java.util.Set;

@Entity
@Table( name = "moteur" )
public class Moteur{

         @Id
         @Column( name = "id_moteur" )
         @GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@Parameter(name = "prefix" , value = "MOT"), @Parameter( name = "sequence", value = "seq_moteur" ),             @Parameter( name = "max_length", value = "7" ) }  )
          @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;
         @Column
         String nom;
         @Column
         Integer cylindre;
         @Column
         Double puissance;
         @Column
         String configuration;
         @Column
         Double consommation;
         @Column
         Double capacite;
         
         
         @ManyToOne
          @JoinColumn( name = "id_marque" )
//          @Column
          @JsonManagedReference
         Marque marque;
         
         @ManyToOne
         @JoinColumn( name = "id_type" )
//          @Column
         TypeMoteur type;
         
         @ManyToOne
         @JoinColumn( name = "id_carburant" )
//          @Column
         Carburant carburant;

         @ManyToMany
         @JoinTable(
            name = "transmission",
            joinColumns = @JoinColumn( name = "id_moteur" ),
            inverseJoinColumns = @JoinColumn( name = "id_boite" )
         )
         Set<Vitesse> vitesses;

         public void setVitesses(Set<Vitesse> vitesses){
            this.vitesses = vitesses;
         }

         public Set<Vitesse> getVitesses(){
            return this.vitesses;
         }
         @Column( name = "deleted" )
	    boolean deleted;

          public String getId() {
                    return id;
          }

          public void setId(String id) {
                    this.id = id;
          }

          public String getNom() {
                    return nom;
          }

          public void setNom(String nom) {
                    this.nom = nom;
          }

          public Integer getCylindre() {
                    return cylindre;
          }

          public void setCylindre(Integer cylindre) {
                    this.cylindre = cylindre;
          }

          public Double getPuissance() {
                    return puissance;
          }

          public void setPuissance(Double puissance) {
                    this.puissance = puissance;
          }

          public String getConfiguration() {
                    return configuration;
          }

          public void setConfiguration(String configuration) {
                    this.configuration = configuration;
          }

          public Marque getMarque() {
                    return marque;
          }

          public void setMarque(Marque marque) {
                    this.marque = marque;
          }


	public TypeMoteur getType() {
			return type;
	}


          public void setType(TypeMoteur type) {
                    this.type = type;
          }

          public Carburant getCarburant() {
                    return carburant;
          }

          public void setCarburant(Carburant carburant) {
                    this.carburant = carburant;
          }

          public Double getConsommation() {
                    return consommation;
          }

          public void setConsommation(Double consommation) {
                    this.consommation = consommation;
          }

          public Double getCapacite() {
                    return capacite;
          }

          public void setCapacite(Double capacite) {
                    this.capacite = capacite;
          }
                    
        public boolean getDeleted(){
            return this.deleted;
        }
    
        public void setDeleted( boolean bool ){
            this.deleted = bool;
        }
}