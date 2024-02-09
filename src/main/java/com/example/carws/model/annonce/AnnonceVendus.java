package com.example.carws.model.annonce;

import java.sql.Date;

import com.example.carws.model.users.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.example.carws.utility.IdGenerator;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table( name = "vendu" )
@JsonIdentityInfo(
 generator = ObjectIdGenerators.PropertyGenerator.class, 
 property = "id")
public class AnnonceVendus{
	@Id
	@Column( name = "id_vendu" )
	@GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name = "prefix" , value = "VEN"), @org.hibernate.annotations.Parameter( name = "sequence", value = "seq_vendu" ), @org.hibernate.annotations.Parameter( name = "max_length", value = "7" ) }  )
    @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;

    @ManyToOne
    @JoinColumn(name = "id_annonce")
    Annonce annonce;

    @ManyToOne
	@JoinColumn(name = "id_utilisateur")
    Users user;

    @Column( name = "date_vendu" )
	Date date;

    @Column( name = "commission" )
    Integer commission;

    public Integer getCommission(){
        return this.commission;
    }

    public void setCommission(Integer commission){
        this.commission = commission;
    }

	public void setId( String id ){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

    public void setAnnonce(Annonce annonce){
        this.annonce = annonce;
    }

    public Annonce getAnnonce(){
        return this.annonce;
    }

    public void setUser(Users user){
        this.user = user;
    }

    public Users getUser(){
        return this.user;
    }

    public void setDate(Date dateheure){
        this.date = dateheure;
    }

    public Date getDatetime(){
        return this.date;
    }

	public AnnonceVendus(){

	}

	public AnnonceVendus( Date date ) throws Exception{
        this.setDate(date);
	}

}