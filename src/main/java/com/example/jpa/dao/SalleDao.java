package com.example.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.example.jpa.entities.Salle;

/**
 * Regroupe les recherches liees aux caracteristiques metier des salles.
 */
public class SalleDao extends GenericDao<Salle> {

    public SalleDao(EntityManager em) {
        super(Salle.class, em);
    }

    public List<Salle> findByCapaciteMin(int capaciteMin) {
        return em.createQuery(
                "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin",
                Salle.class)
                .setParameter("capaciteMin", capaciteMin)
                .getResultList();
    }

    public List<Salle> findByCapaciteSuperieureA(int minCapacite) {
        return em.createQuery(
                "SELECT s FROM Salle s WHERE s.capacite > :minCapacite",
                Salle.class)
                .setParameter("minCapacite", minCapacite)
                .getResultList();
    }

    public List<Salle> findSallesDisponibles() {
        return em.createQuery(
                "SELECT s FROM Salle s WHERE s.disponible = true ORDER BY s.nom",
                Salle.class)
                .getResultList();
    }

    public List<Salle> findByLocalisation(String localisation) {
        return em.createQuery(
                "SELECT s FROM Salle s "
                        + "WHERE LOWER(s.localisation) = LOWER(:localisation) "
                        + "ORDER BY s.nom",
                Salle.class)
                .setParameter("localisation", localisation)
                .getResultList();
    }
}
