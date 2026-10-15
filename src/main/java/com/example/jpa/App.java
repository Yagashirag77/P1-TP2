package com.example.jpa;

import java.util.List;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;
import com.example.jpa.util.JpaUtil;

/**
 * Scenario de demonstration : creation d'une salle marocaine, affectation d'une
 * utilisatrice puis consultation a travers les methodes du service metier.
 */
public class App {
    public static void main(String[] args) {
        ServiceGestion service = null;

        try {
            service = new ServiceGestion();

            List<Salle> sallesAtlas = service.sallesParLocalisation("Etage 2");
            Salle atlas = sallesAtlas.stream()
                    .filter(salle -> "Atlas".equals(salle.getNom()))
                    .findFirst()
                    .orElse(null);

            if (atlas == null) {
                atlas = new Salle("Atlas", 24, "Etage 2", true);
                service.ajouterSalle(atlas);
            }

            if (service.utilisateursParEmail("yassine.gartite@example.ma").isEmpty()) {
                Utilisateur yasmine = new Utilisateur(
                        "Yassine Gartite",
                        "Yassine Gartite@example.ma",
                        "+212600112233",
                        atlas);
                service.ajouterUtilisateur(yasmine);
            }

            System.out.println("Salles disponibles a l'Etage 2 :");
            service.sallesParLocalisation("etage 2").forEach(System.out::println);

            System.out.println("Utilisateurs affectes a la salle Atlas :");
            service.utilisateursParSalle("Atlas").forEach(System.out::println);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (service != null) {
                service.fermer();
            }
            JpaUtil.fermer();
        }
    }
}
