package com.example.carws.model.primaire;

import com.example.carws.utility.IdGenerator;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;
import java.util.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "marque" )

public class Marque{
	@Id
	@Column( name = "id_marque" )
	@GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name = "prefix" , value = "MAR"), @org.hibernate.annotations.Parameter( name = "sequence", value = "seq_marque" ), @org.hibernate.annotations.Parameter( name = "max_length", value = "7" ) }  )
    @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;
	@Column( name = "nom" )
	String nom;
	@Column( name = "deleted" )
	boolean deleted = false;
	
	@JsonManagedReference("modeles-marques")
	// @JsonBackReference("marque-modeles")
  @OneToMany( mappedBy ="marque", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
	List<Modele> modeles;

	public void setModeles(List<Modele> modeles){
		this.modeles = modeles;
	}
	public List<Modele> getModeles(){
		return this.modeles;
	}
	public boolean getDeleted(){
		return this.deleted;
	}
   
         public boolean isDeleted(){
                   return this.getDeleted();
         }

	public void setDeleted( boolean bool ){
		this.deleted = bool;
	}

	public void setId( String id ){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public void setNom( String nom ) throws Exception{
		if( nom == null || nom.isEmpty() ){
			throw new Exception("Le nom de categorie ne peut etre nulle");
		}
		this.nom = nom;
	}

	public Marque(){

	}

	public Marque( String nom ) throws Exception{
		this.setNom(nom);
	}

	public String getNom(){
		return this.nom;
	}
    
    @Override
    public String toString(){
              return " Marque = { id :  " + this.getId() + " , nom : " + this.getNom() + " }";
    }

	/*
	 * 
	 * Get static data for category
	 * 
	 */
	// public Categorie[] test() throws Exception{
	// 	Categorie[] categories = new Categorie[3];
	// 	categories[0] = new Categorie("Camion");
	// 	categories[1] = new Categorie("Plaisir");
	// 	categories[2] = new Categorie("Mini Bus");
	// 	return categories;
	// }

}