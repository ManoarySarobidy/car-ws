package com.example.carws.model.users;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

public class Discussions {
    @Id
    private String id;
    private String idEnvoyeur;
    private LocalDateTime dateHeureEnvoie;
    private String dernierMessage;
    private int dernierStatus;
    private int nombreMessagesNonLu;

    
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdEnvoyeur() {
        return this.idEnvoyeur;
    }
    public void setIdEnvoyeur(String idEnvoyeur) {
        this.idEnvoyeur = idEnvoyeur;
    }
    public LocalDateTime getDateHeureEnvoie() {
        return this.dateHeureEnvoie;
    }
    public void setDateHeureEnvoie(LocalDateTime dateHeureEnvoie) {
        this.dateHeureEnvoie = dateHeureEnvoie;
    }
    public String getDernierMessage() {
        return this.dernierMessage;
    }
    public void setDernierMessage(String dernierMessage) {
        this.dernierMessage = dernierMessage;
    }
    public int getDernierStatus() {
        return this.dernierStatus;
    }
    public void setDernierStatus(int dernierStatus) {
        this.dernierStatus = dernierStatus;
    }
    public int getNombreMessagesNonLu() {
        return this.nombreMessagesNonLu;
    }
    public void setNombreMessagesNonLu(int nombreMessagesNonLu) {
        this.nombreMessagesNonLu = nombreMessagesNonLu;
    }

    

}
