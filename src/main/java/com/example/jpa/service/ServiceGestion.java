package com.example.jpa.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.util.JpaUtil;

/**
 * Facade metier de l'application. Un contexte JPA court est ouvert pour chaque
 * operation afin de ne pas conserver d'entites gerees plus longtemps que necessaire.
 */
public class ServiceGestion implements AutoCloseable {

    private final EntityManagerFactory emf;
    private final boolean fermerEmf;

    public ServiceGestion() {
        this(JpaUtil.getEntityManagerFactory(), false);
    }

    public ServiceGestion(Map<String, Object> proprietesJpa) {
        this(Persistence.createEntityManagerFactory(
                "gestionPU",
                proprietesJpa == null
                        ? Collections.<String, Object>emptyMap()
                        : proprietesJpa), true);
    }

    private ServiceGestion(EntityManagerFactory emf, boolean fermerEmf) {
        this.emf = emf;
        this.fermerEmf = fermerEmf;
    }

    public void fermer() {
        if (fermerEmf && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public void close() {
        fermer();
    }

    public void ajouterSalle(Salle salle) {
        EntityManager em = emf.createEntityManager();
        try {
            new SalleDao(em).create(salle);
        } finally {
            fermer(em);
        }
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        EntityManager em = emf.createEntityManager();
        try {
            new UtilisateurDao(em).create(utilisateur);
        } finally {
            fermer(em);
        }
    }

    public List<Salle> listerSalles() {
        EntityManager em = emf.createEntityManager();
        try {
            return new SalleDao(em).findAll();
        } finally {
            fermer(em);
        }
    }

    public List<Utilisateur> listerUtilisateurs() {
        EntityManager em = emf.createEntityManager();
        try {
            return new UtilisateurDao(em).findAll();
        } finally {
            fermer(em);
        }
    }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        EntityManager em = emf.createEntityManager();
        try {
            return new UtilisateurDao(em).findBySalleNom(nomSalle);
        } finally {
            fermer(em);
        }
    }

    public List<Salle> sallesAvecCapaciteSuperieureA(int minCapacite) {
        EntityManager em = emf.createEntityManager();
        try {
            return new SalleDao(em).findByCapaciteSuperieureA(minCapacite);
        } finally {
            fermer(em);
        }
    }

    public List<Utilisateur> utilisateursDontNomContient(String chaine) {
        EntityManager em = emf.createEntityManager();
        try {
            return new UtilisateurDao(em).findByNomContenant(chaine);
        } finally {
            fermer(em);
        }
    }

    public List<Salle> sallesDisponibles() {
        EntityManager em = emf.createEntityManager();
        try {
            return new SalleDao(em).findSallesDisponibles();
        } finally {
            fermer(em);
        }
    }

    public List<Salle> sallesParLocalisation(String localisation) {
        EntityManager em = emf.createEntityManager();
        try {
            return new SalleDao(em).findByLocalisation(localisation);
        } finally {
            fermer(em);
        }
    }

    public List<Utilisateur> utilisateursParEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return new UtilisateurDao(em).findByEmail(email);
        } finally {
            fermer(em);
        }
    }

    public Salle changerDisponibiliteSalle(Salle salle, boolean disponible) {
        salle.setDisponible(disponible);
        EntityManager em = emf.createEntityManager();
        try {
            return new SalleDao(em).update(salle);
        } finally {
            fermer(em);
        }
    }

    private void fermer(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
