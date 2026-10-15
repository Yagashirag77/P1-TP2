package com.example.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Gere l'unique EntityManagerFactory partagee par l'application.
 */
public final class JpaUtil {

    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("gestionPU");

    private JpaUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (!EMF.isOpen()) {
            throw new IllegalStateException("L'EntityManagerFactory est fermee");
        }
        return EMF;
    }

    public static EntityManager creerEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void fermer() {
        if (EMF.isOpen()) {
            EMF.close();
        }
    }
}
