package com.example.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Personne autorisee a utiliser une salle et joignable par email ou telephone.
 */
@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20)
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String email, Salle salle) {
        this.nom = nom;
        this.email = email;
        this.salle = salle;
    }

    public Utilisateur(String nom, String email, String telephone, Salle salle) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.salle = salle;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", nom=" + nom + ", email=" + email
                + ", telephone=" + telephone
                + ", salle=" + (salle != null ? salle.getNom() : "aucune") + "]";
    }
}
