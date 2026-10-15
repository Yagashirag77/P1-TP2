package com.example.jpa.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Fournit le socle CRUD commun. Chaque ecriture constitue une unite de travail
 * autonome et restaure la transaction si Hibernate signale une erreur.
 */
public class GenericDao<T> {

    private static final Logger LOGGER = Logger.getLogger(GenericDao.class.getName());

    private final Class<T> entityClass;
    protected EntityManager em;

    public GenericDao(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    public void create(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx);
            LOGGER.log(Level.SEVERE, "Erreur lors de la creation de l'entite", e);
            throw e;
        }
    }

    public T find(Object id) {
        return em.find(entityClass, id);
    }

    public T update(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            rollback(tx);
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise a jour de l'entite", e);
            throw e;
        }
    }

    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx);
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entite", e);
            throw e;
        }
    }

    public List<T> findAll() {
        return em.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e",
                entityClass)
                .getResultList();
    }

    private void rollback(EntityTransaction tx) {
        if (tx.isActive()) {
            try {
                tx.rollback();
            } catch (RuntimeException rollbackException) {
                LOGGER.log(Level.SEVERE, "Erreur pendant le rollback de la transaction", rollbackException);
            }
        }
    }
}
