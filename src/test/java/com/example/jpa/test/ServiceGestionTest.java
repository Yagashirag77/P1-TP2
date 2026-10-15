package com.example.jpa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;

public class ServiceGestionTest {

    private ServiceGestion service;

    @Before
    public void setUp() {
        Map<String, Object> proprietesJpa = new HashMap<String, Object>();
        proprietesJpa.put(
                "javax.persistence.jdbc.url",
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        proprietesJpa.put("hibernate.hbm2ddl.auto", "create-drop");
        proprietesJpa.put("hibernate.show_sql", "false");
        service = new ServiceGestion(proprietesJpa);
    }

    @After
    public void tearDown() {
        service.fermer();
    }

    @Test
    public void testCreationEtRechercheSalle() {
        Salle salle = new Salle("Rif", 20, "Tanger", true);
        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();
        assertFalse(salles.isEmpty());
        assertEquals("Rif", salles.get(0).getNom());
        assertEquals("Tanger", salles.get(0).getLocalisation());
        assertTrue(salles.get(0).isDisponible());
    }

    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Souss", 15, "Agadir", true);
        service.ajouterSalle(salle);

        Utilisateur utilisateur = new Utilisateur(
                "Salma Idrissi",
                "salma.idrissi@example.ma",
                "+212611223344",
                salle);
        service.ajouterUtilisateur(utilisateur);

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();
        assertFalse(utilisateurs.isEmpty());
        assertEquals("Salma Idrissi", utilisateurs.get(0).getNom());
        assertEquals("+212611223344", utilisateurs.get(0).getTelephone());
        assertEquals("Souss", utilisateurs.get(0).getSalle().getNom());
    }

    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Draa", 30, "Ouarzazate", true);
        service.ajouterSalle(salle);

        Utilisateur u1 = new Utilisateur("Omar Alaoui", "omar@example.ma", salle);
        Utilisateur u2 = new Utilisateur("Imane Zahra", "imane@example.ma", salle);
        service.ajouterUtilisateur(u1);
        service.ajouterUtilisateur(u2);

        List<Utilisateur> utilisateurs = service.utilisateursParSalle("Draa");
        assertEquals(2, utilisateurs.size());
    }

    @Test
    public void testRechercheSallesParCapaciteSuperieure() {
        service.ajouterSalle(new Salle("Cedre", 100, "Rabat", true));
        service.ajouterSalle(new Salle("Arganier", 200, "Marrakech", true));

        List<Salle> salles = service.sallesAvecCapaciteSuperieureA(150);

        assertEquals(1, salles.size());
        assertEquals("Arganier", salles.get(0).getNom());
    }

    @Test
    public void testRechercheUtilisateursParNomPartiel() {
        Salle salle = new Salle("Toubkal", 12, "Casablanca", true);
        service.ajouterSalle(salle);
        service.ajouterUtilisateur(
                new Utilisateur("Mehdi Bennani", "mehdi.bennani@example.ma", salle));
        service.ajouterUtilisateur(
                new Utilisateur("Nora Amrani", "nora.amrani@example.ma", salle));

        List<Utilisateur> utilisateurs = service.utilisateursDontNomContient("BENNANI");

        assertEquals(1, utilisateurs.size());
        assertEquals("Mehdi Bennani", utilisateurs.get(0).getNom());
    }

    @Test
    public void testRechercheParLocalisationEtDisponibilite() {
        service.ajouterSalle(new Salle("Ocean", 18, "Casablanca", true));
        service.ajouterSalle(new Salle("Medina", 10, "Casablanca", false));

        List<Salle> sallesCasablanca = service.sallesParLocalisation("casablanca");
        List<Salle> sallesDisponibles = service.sallesDisponibles();

        assertEquals(2, sallesCasablanca.size());
        assertEquals(1, sallesDisponibles.size());
        assertEquals("Ocean", sallesDisponibles.get(0).getNom());
    }

    @Test
    public void testRechercheUtilisateurParEmail() {
        Salle salle = new Salle("Andalous", 14, "Fes", true);
        service.ajouterSalle(salle);
        service.ajouterUtilisateur(new Utilisateur(
                "Lina Mansouri",
                "lina.mansouri@example.ma",
                "+212622334455",
                salle));

        List<Utilisateur> utilisateurs =
                service.utilisateursParEmail("LINA.MANSOURI@EXAMPLE.MA");

        assertEquals(1, utilisateurs.size());
        assertEquals("Lina Mansouri", utilisateurs.get(0).getNom());
    }

    @Test
    public void testChangementDisponibiliteSalle() {
        Salle salle = new Salle("Menara", 40, "Marrakech", true);
        service.ajouterSalle(salle);

        Salle salleModifiee = service.changerDisponibiliteSalle(salle, false);

        assertFalse(salleModifiee.isDisponible());
        assertTrue(service.sallesDisponibles().isEmpty());
    }
}
