package com.example.carws.model.annonce;

import com.example.carws.model.primaire.Lieu;
import com.example.carws.model.users.Users;
import com.example.carws.model.voiture.Voiture;
import com.fasterxml.jackson.annotation.*;

import java.sql.Timestamp;
import java.util.List;


import com.example.carws.utility.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table( name = "annonce" )
// @JsonIdentityInfo(
//  generator = ObjectIdGenerators.PropertyGenerator.class, 
//  property = "id")
public class Annonce{
	@Id
	@Column( name = "id_annonce" )
	@GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name = "prefix" , value = "ANC"), @org.hibernate.annotations.Parameter( name = "sequence", value = "seq_annonce" ), @org.hibernate.annotations.Parameter( name = "max_length", value = "7" ) }  )
    @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;

    @Column( name = "date_annonce")
	Timestamp date;

    @Column( name = "description")
    String description;

	@Column( name = "prix" )
	double prix;

	@OneToOne
    @JoinColumn(name = "id_lieu")
	Lieu lieu;

	@ManyToOne
	@JoinColumn(name = "id_utilisateur")
	// @JsonBackReference("annonces-user")
	Users user;

	@Column( name = "valeur" )
	Integer valeur;

	@ManyToOne
    @JoinColumn(name = "id_voiture")
    // @JsonBackReference("voiture-annonce")
	Voiture voiture;

	@OneToOne(mappedBy = "annonce")
	DetailsAnnonce details;

	@OneToOne(mappedBy = "annonce")
	@JsonBackReference("validate-annonce")
	ValidateAnnonce validate;

	@OneToMany(mappedBy = "annonce")
    @JsonIgnore
    List<AnnonceFavories> favories;

	@OneToMany(mappedBy="annonce")
	@JsonManagedReference("annonce")
	List<AnnoncePhoto> photos;

	// @OneToOne(mappedBy = "annonce")
	// @JsonIgnore
	// AnnonceVendus vendu;

	// @OneToOne(mappedBy = "annonce")
	// // @JsonIgnore
	// Historique historique;

	// public Historique getHistorique(){
	// 	return this.historique;
	// }

	// public void setHistorique(Historique historique){
	// 	this.historique = historique;
	// }
	// 
	
	public void setPhotos(List<AnnoncePhoto> pics){
		this.photos = pics;
	}

	public List<AnnoncePhoto> getPhotos(){
		return this.photos;
	}

	public List<AnnonceFavories> getFavories(){
		return this.favories;
	}

	public void setFavories(List<AnnonceFavories> fav){
		this.favories = fav;
	}

	public ValidateAnnonce getValidate(){
		return this.validate;
	}

	public void setValidate(ValidateAnnonce validate){
		this.validate = validate;
	}

	public DetailsAnnonce getDetails(){
        return this.details;
    }

    public void setDetails(DetailsAnnonce details){
        this.details = details;
    }

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public void setDate(Timestamp date){
		this.date = date;
	}

	public Timestamp getDate(){
		return this.date;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}

	public void setPrix(double prix){
		this.prix = prix;
	}

	public double getPrix(){
		return this.prix;
	}

	public void setLieu(Lieu lieu){
		this.lieu = lieu;
	}

	public Lieu getLieu(){
		return this.lieu;
	}

	public void setUser(Users user){
		this.user = user;
	}

	public Users getUser(){
		return this.user;
	}

	public void setValeur(Integer valeur){
		this.valeur = valeur;
	}

	public Integer getValeur(){
		return this.valeur;
	}

	public void setVoiture(Voiture voiture){
		this.voiture = voiture;
	}
	
	public Voiture getVoiture(){
		return this.voiture;
	}

	public Annonce(){
		
	}
}