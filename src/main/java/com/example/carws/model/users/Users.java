package com.example.carws.model.users;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.Collection;
import java.util.List;

import com.example.carws.model.annonce.AnnonceFavories;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "utilisateur")
public class Users implements Serializable {
    @Id
    @Column(name = "id_utilisateur")
    String id;
    @Column(name = "nom")
    String nom;
    @Column(nullable = true)
    String prenom;
    @Column(nullable = true)
    String contact;
    @Column(name = "date_naissance", nullable = true)
    Date dateDeNaissance;
    @Column(name = "email")
    String mail;
    @Column(name = "mot_de_passe", nullable = true)
    String password;

    @OneToMany
    @JoinTable(
        name = "roles_user",  
        joinColumns = @JoinColumn(name = "id_user")
    )
    Set<Role> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    Set<AnnonceFavories> favories;

    public Users() {
    }

    public Users(String id, String nom, String prenom, String contact, Date dateDeNaissance, String email, Set<Role> roles) throws Exception {
        this.setId(id);
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setContact(contact);
        this.setDateDeNaissance(dateDeNaissance);
        this.setMail(email);
        this.setRoles(roles);
    }

    public Users(String id) {
        this.setId(id);
    }

    public Users(String id, String email) {
        this.setId(id);
        this.setMail(email);
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

    public void setDateDeNaissance(Date date_de_naissance) throws Exception {
        if(date_de_naissance != null) {
            java.util.Date utilDate = new java.util.Date(date_de_naissance.getTime());
    
            Calendar calendarNaissance = Calendar.getInstance();
            calendarNaissance.setTime(utilDate);
    
            int anneeNaissance = calendarNaissance.get(Calendar.YEAR);
    
            Calendar calendarMaintenant = Calendar.getInstance();
            int anneeActuelle = calendarMaintenant.get(Calendar.YEAR);
    
            int age = anneeActuelle - anneeNaissance;
            if (age < 18)
                throw new Exception("Désolé, vous devez avoir au moins 18 ans pour ouvrir un compte.");
            this.dateDeNaissance = date_de_naissance;
        }
    }

    public void setDateDeNaissance(String date_de_naissance) throws Exception {
        this.setDateDeNaissance(Date.valueOf(date_de_naissance));
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

    public void setFavories(Set<AnnonceFavories> listes) {
        this.favories = listes;
    }

    public Set<AnnonceFavories> getFavories() {
        return this.favories;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> role) {
        this.roles = role;
    }

    public boolean isRole(String role) {
        if(this.getRoles() == null)
            return false;
        if(this.getRoles().size() == 0)
            return false;
        int count = 0;
        for(Role _role : this.getRoles()) {
            if(_role.getId().equals(role)) {
                count++;
                break;
            }
        }
        if(count == 0)
            return false;
        return true;
    }

    public Collection<GrantedAuthority> getAuthorities() throws Exception {
        Collection<GrantedAuthority> authorisation = new ArrayList<GrantedAuthority>();
        for(Role _role : this.getRoles()) {
            System.out.println("role: "+ _role.getId());
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + _role.getId());
            authorisation.add(authority);
        }
        return authorisation;
    }

}