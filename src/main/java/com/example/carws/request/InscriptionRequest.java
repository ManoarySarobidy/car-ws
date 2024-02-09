package com.example.carws.request;

import com.example.carws.model.users.Users;
import java.sql.Date;

public class InscriptionRequest {
    String id;
    String nom;
    String prenom;
    String contact;
    Date dateDeNaissance;
    String mail;
    String password;
        
    public Users toUser() throws Exception{
        Users user = new Users();
        user.setNom(this.getNom());
        user.setMail(this.getMail());
        if(this.getDateDeNaissance() != null) {
            user.setContact(this.getContact());
            user.setDateDeNaissance(this.getDateDeNaissance() );
            user.setPassword(this.getPassword());
            user.setPrenom(this.getPrenom());
        } else {
            user.setId(id);
        }
        return user;
    }
        
    public Users toLoginUser() throws Exception{
            Users u = new Users();
            u.setMail(this.getMail());
            u.setPassword(this.getPassword());
            return u;
    }

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

    public String getPrenom() {
            return prenom;
    }

    public void setPrenom(String prenom) {
            this.prenom = prenom;
    }

    public String getContact() {
            return contact;
    }

    public void setContact(String contact) {
            this.contact = contact;
    }

    public Date getDateDeNaissance() {
            return dateDeNaissance;
    }

    public void setDateDeNaissance(Date naissance) {
            this.dateDeNaissance = naissance;
    }

    public String getMail() {
            return mail;
    }

    public void setMail(String mail) {
            this.mail = mail;
    }

    public String getPassword() {
            return password;
    }

    public void setPassword(String password) {
            this.password = password;
    }
    
}