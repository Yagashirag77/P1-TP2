package com.example.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.jpa.entities.Utilisateur;

/**
 * Porte les recherches propres aux utilisateurs et a leur salle associee.
 */
public class UtilisateurDao extends GenericDao<Utilisateur> {

    public UtilisateurDao(EntityManager em) {
        super(Utilisateur.class, em);
    }

    public List<Utilisateur> findBySalleNom(String nomSalle) {
        return em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.salle.nom = :nomSalle",
                Utilisateur.class)
                .setParameter("nomSalle", nomSalle)
                .getResultList();
    }

    public List<Utilisateur> findByNomContenant(String chaine) {
        return em.createQuery(
                "SELECT u FROM Utilisateur u WHERE LOWER(u.nom) LIKE LOWER(:pattern)",
                Utilisateur.class)
                .setParameter("pattern", "%" + chaine + "%")
                .getResultList();
    }

    public List<Utilisateur> findByEmail(String email) {
        return em.createQuery(
                "SELECT u FROM Utilisateur u WHERE LOWER(u.email) = LOWER(:email)",
                Utilisateur.class)
                .setParameter("email", email)
                .getResultList();
    }
}
