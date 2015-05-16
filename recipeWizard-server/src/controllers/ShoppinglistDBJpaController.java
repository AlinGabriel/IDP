/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelDB.UseriDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelDB.ShoppinglistDB;

/**
 *
 * @author Agnesss
 */
public class ShoppinglistDBJpaController implements Serializable {

    public ShoppinglistDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ShoppinglistDB shoppinglistDB) {
        if (shoppinglistDB.getUseriDBCollection() == null) {
            shoppinglistDB.setUseriDBCollection(new ArrayList<UseriDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UseriDB> attachedUseriDBCollection = new ArrayList<UseriDB>();
            for (UseriDB useriDBCollectionUseriDBToAttach : shoppinglistDB.getUseriDBCollection()) {
                useriDBCollectionUseriDBToAttach = em.getReference(useriDBCollectionUseriDBToAttach.getClass(), useriDBCollectionUseriDBToAttach.getId());
                attachedUseriDBCollection.add(useriDBCollectionUseriDBToAttach);
            }
            shoppinglistDB.setUseriDBCollection(attachedUseriDBCollection);
            em.persist(shoppinglistDB);
            for (UseriDB useriDBCollectionUseriDB : shoppinglistDB.getUseriDBCollection()) {
                ShoppinglistDB oldShoppinglistIdOfUseriDBCollectionUseriDB = useriDBCollectionUseriDB.getShoppinglistId();
                useriDBCollectionUseriDB.setShoppinglistId(shoppinglistDB);
                useriDBCollectionUseriDB = em.merge(useriDBCollectionUseriDB);
                if (oldShoppinglistIdOfUseriDBCollectionUseriDB != null) {
                    oldShoppinglistIdOfUseriDBCollectionUseriDB.getUseriDBCollection().remove(useriDBCollectionUseriDB);
                    oldShoppinglistIdOfUseriDBCollectionUseriDB = em.merge(oldShoppinglistIdOfUseriDBCollectionUseriDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ShoppinglistDB shoppinglistDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ShoppinglistDB persistentShoppinglistDB = em.find(ShoppinglistDB.class, shoppinglistDB.getId());
            Collection<UseriDB> useriDBCollectionOld = persistentShoppinglistDB.getUseriDBCollection();
            Collection<UseriDB> useriDBCollectionNew = shoppinglistDB.getUseriDBCollection();
            List<String> illegalOrphanMessages = null;
            for (UseriDB useriDBCollectionOldUseriDB : useriDBCollectionOld) {
                if (!useriDBCollectionNew.contains(useriDBCollectionOldUseriDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UseriDB " + useriDBCollectionOldUseriDB + " since its shoppinglistId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UseriDB> attachedUseriDBCollectionNew = new ArrayList<UseriDB>();
            for (UseriDB useriDBCollectionNewUseriDBToAttach : useriDBCollectionNew) {
                useriDBCollectionNewUseriDBToAttach = em.getReference(useriDBCollectionNewUseriDBToAttach.getClass(), useriDBCollectionNewUseriDBToAttach.getId());
                attachedUseriDBCollectionNew.add(useriDBCollectionNewUseriDBToAttach);
            }
            useriDBCollectionNew = attachedUseriDBCollectionNew;
            shoppinglistDB.setUseriDBCollection(useriDBCollectionNew);
            shoppinglistDB = em.merge(shoppinglistDB);
            for (UseriDB useriDBCollectionNewUseriDB : useriDBCollectionNew) {
                if (!useriDBCollectionOld.contains(useriDBCollectionNewUseriDB)) {
                    ShoppinglistDB oldShoppinglistIdOfUseriDBCollectionNewUseriDB = useriDBCollectionNewUseriDB.getShoppinglistId();
                    useriDBCollectionNewUseriDB.setShoppinglistId(shoppinglistDB);
                    useriDBCollectionNewUseriDB = em.merge(useriDBCollectionNewUseriDB);
                    if (oldShoppinglistIdOfUseriDBCollectionNewUseriDB != null && !oldShoppinglistIdOfUseriDBCollectionNewUseriDB.equals(shoppinglistDB)) {
                        oldShoppinglistIdOfUseriDBCollectionNewUseriDB.getUseriDBCollection().remove(useriDBCollectionNewUseriDB);
                        oldShoppinglistIdOfUseriDBCollectionNewUseriDB = em.merge(oldShoppinglistIdOfUseriDBCollectionNewUseriDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = shoppinglistDB.getId();
                if (findShoppinglistDB(id) == null) {
                    throw new NonexistentEntityException("The shoppinglistDB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ShoppinglistDB shoppinglistDB;
            try {
                shoppinglistDB = em.getReference(ShoppinglistDB.class, id);
                shoppinglistDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shoppinglistDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UseriDB> useriDBCollectionOrphanCheck = shoppinglistDB.getUseriDBCollection();
            for (UseriDB useriDBCollectionOrphanCheckUseriDB : useriDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ShoppinglistDB (" + shoppinglistDB + ") cannot be destroyed since the UseriDB " + useriDBCollectionOrphanCheckUseriDB + " in its useriDBCollection field has a non-nullable shoppinglistId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(shoppinglistDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ShoppinglistDB> findShoppinglistDBEntities() {
        return findShoppinglistDBEntities(true, -1, -1);
    }

    public List<ShoppinglistDB> findShoppinglistDBEntities(int maxResults, int firstResult) {
        return findShoppinglistDBEntities(false, maxResults, firstResult);
    }

    private List<ShoppinglistDB> findShoppinglistDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ShoppinglistDB.class));
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

    public ShoppinglistDB findShoppinglistDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ShoppinglistDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getShoppinglistDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ShoppinglistDB> rt = cq.from(ShoppinglistDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
