/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelDB.IngredientDB;
import modelDB.QuantityDB;

/**
 *
 * @author Agnesss
 */
public class QuantityDBJpaController implements Serializable {

    public QuantityDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(QuantityDB quantityDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IngredientDB ingredientId = quantityDB.getIngredientId();
            if (ingredientId != null) {
                ingredientId = em.getReference(ingredientId.getClass(), ingredientId.getId());
                quantityDB.setIngredientId(ingredientId);
            }
            em.persist(quantityDB);
            if (ingredientId != null) {
                ingredientId.getQuantityDBCollection().add(quantityDB);
                ingredientId = em.merge(ingredientId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(QuantityDB quantityDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            QuantityDB persistentQuantityDB = em.find(QuantityDB.class, quantityDB.getId());
            IngredientDB ingredientIdOld = persistentQuantityDB.getIngredientId();
            IngredientDB ingredientIdNew = quantityDB.getIngredientId();
            if (ingredientIdNew != null) {
                ingredientIdNew = em.getReference(ingredientIdNew.getClass(), ingredientIdNew.getId());
                quantityDB.setIngredientId(ingredientIdNew);
            }
            quantityDB = em.merge(quantityDB);
            if (ingredientIdOld != null && !ingredientIdOld.equals(ingredientIdNew)) {
                ingredientIdOld.getQuantityDBCollection().remove(quantityDB);
                ingredientIdOld = em.merge(ingredientIdOld);
            }
            if (ingredientIdNew != null && !ingredientIdNew.equals(ingredientIdOld)) {
                ingredientIdNew.getQuantityDBCollection().add(quantityDB);
                ingredientIdNew = em.merge(ingredientIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = quantityDB.getId();
                if (findQuantityDB(id) == null) {
                    throw new NonexistentEntityException("The quantityDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            QuantityDB quantityDB;
            try {
                quantityDB = em.getReference(QuantityDB.class, id);
                quantityDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The quantityDB with id " + id + " no longer exists.", enfe);
            }
            IngredientDB ingredientId = quantityDB.getIngredientId();
            if (ingredientId != null) {
                ingredientId.getQuantityDBCollection().remove(quantityDB);
                ingredientId = em.merge(ingredientId);
            }
            em.remove(quantityDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<QuantityDB> findQuantityDBEntities() {
        return findQuantityDBEntities(true, -1, -1);
    }

    public List<QuantityDB> findQuantityDBEntities(int maxResults, int firstResult) {
        return findQuantityDBEntities(false, maxResults, firstResult);
    }

    private List<QuantityDB> findQuantityDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(QuantityDB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public QuantityDB findQuantityDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(QuantityDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuantityDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<QuantityDB> rt = cq.from(QuantityDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
