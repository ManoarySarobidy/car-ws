package com.example.carws.model.annonce;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import com.example.carws.model.users.Users;
import com.example.carws.utility.IdGenerator;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "valide" )
@JsonIdentityInfo(
 generator = ObjectIdGenerators.PropertyGenerator.class, 
 property = "id")
public class ValidateAnnonce{
	@Id
	@Column( name = "id_valide" )
	@GenericGenerator( name = "custom-id", type = IdGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name = "prefix" , value = "VAL"), @org.hibernate.annotations.Parameter( name = "sequence", value = "seq_valide" ), @org.hibernate.annotations.Parameter( name = "max_length", value = "7" ) }  )
    @GeneratedValue(generator = "custom-id" , strategy = GenerationType.IDENTITY)
	String id;

    @OneToOne
    @JoinColumn(name = "id_annonce")
    @JsonManagedReference("validate-annonce")
    Annonce annonce;

    @OneToOne
    @JoinColumn(name = "id_utilisateur")
    Users user;

    @Column( name = "date_valide" )
	Date datetimevalidation;

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

    public void setDatetimevalidation(Date dateheure){
        this.datetimevalidation = dateheure;
    }

    public Date getDatetime(){
        return this.datetimevalidation;
    }

    public Users getUser(){
        return this.user;
    }

    public void setUser(Users user){
        this.user = user;
    }

	public ValidateAnnonce(){

	}

	public ValidateAnnonce( Date dateheure ) throws Exception{
        this.setDatetimevalidation(dateheure);
	}

}