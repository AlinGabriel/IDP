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
import modelDB.ShoppinglistDB;
import modelDB.RecipeDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelDB.UseriDB;

/**
 *
 * @author Agnesss
 */
public class UseriDBJpaController implements Serializable {

    public UseriDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UseriDB useriDB) {
        if (useriDB.getRecipeDBCollection() == null) {
            useriDB.setRecipeDBCollection(new ArrayList<RecipeDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ShoppinglistDB shoppinglistId = useriDB.getShoppinglistId();
            if (shoppinglistId != null) {
                shoppinglistId = em.getReference(shoppinglistId.getClass(), shoppinglistId.getId());
                useriDB.setShoppinglistId(shoppinglistId);
            }
            Collection<RecipeDB> attachedRecipeDBCollection = new ArrayList<RecipeDB>();
            for (RecipeDB recipeDBCollectionRecipeDBToAttach : useriDB.getRecipeDBCollection()) {
                recipeDBCollectionRecipeDBToAttach = em.getReference(recipeDBCollectionRecipeDBToAttach.getClass(), recipeDBCollectionRecipeDBToAttach.getId());
                attachedRecipeDBCollection.add(recipeDBCollectionRecipeDBToAttach);
            }
            useriDB.setRecipeDBCollection(attachedRecipeDBCollection);
            em.persist(useriDB);
            if (shoppinglistId != null) {
                shoppinglistId.getUseriDBCollection().add(useriDB);
                shoppinglistId = em.merge(shoppinglistId);
            }
            for (RecipeDB recipeDBCollectionRecipeDB : useriDB.getRecipeDBCollection()) {
                UseriDB oldUserIdOfRecipeDBCollectionRecipeDB = recipeDBCollectionRecipeDB.getUserId();
                recipeDBCollectionRecipeDB.setUserId(useriDB);
                recipeDBCollectionRecipeDB = em.merge(recipeDBCollectionRecipeDB);
                if (oldUserIdOfRecipeDBCollectionRecipeDB != null) {
                    oldUserIdOfRecipeDBCollectionRecipeDB.getRecipeDBCollection().remove(recipeDBCollectionRecipeDB);
                    oldUserIdOfRecipeDBCollectionRecipeDB = em.merge(oldUserIdOfRecipeDBCollectionRecipeDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UseriDB useriDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UseriDB persistentUseriDB = em.find(UseriDB.class, useriDB.getId());
            ShoppinglistDB shoppinglistIdOld = persistentUseriDB.getShoppinglistId();
            ShoppinglistDB shoppinglistIdNew = useriDB.getShoppinglistId();
            Collection<RecipeDB> recipeDBCollectionOld = persistentUseriDB.getRecipeDBCollection();
            Collection<RecipeDB> recipeDBCollectionNew = useriDB.getRecipeDBCollection();
            List<String> illegalOrphanMessages = null;
            for (RecipeDB recipeDBCollectionOldRecipeDB : recipeDBCollectionOld) {
                if (!recipeDBCollectionNew.contains(recipeDBCollectionOldRecipeDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RecipeDB " + recipeDBCollectionOldRecipeDB + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (shoppinglistIdNew != null) {
                shoppinglistIdNew = em.getReference(shoppinglistIdNew.getClass(), shoppinglistIdNew.getId());
                useriDB.setShoppinglistId(shoppinglistIdNew);
            }
            Collection<RecipeDB> attachedRecipeDBCollectionNew = new ArrayList<RecipeDB>();
            for (RecipeDB recipeDBCollectionNewRecipeDBToAttach : recipeDBCollectionNew) {
                recipeDBCollectionNewRecipeDBToAttach = em.getReference(recipeDBCollectionNewRecipeDBToAttach.getClass(), recipeDBCollectionNewRecipeDBToAttach.getId());
                attachedRecipeDBCollectionNew.add(recipeDBCollectionNewRecipeDBToAttach);
            }
            recipeDBCollectionNew = attachedRecipeDBCollectionNew;
            useriDB.setRecipeDBCollection(recipeDBCollectionNew);
            useriDB = em.merge(useriDB);
            if (shoppinglistIdOld != null && !shoppinglistIdOld.equals(shoppinglistIdNew)) {
                shoppinglistIdOld.getUseriDBCollection().remove(useriDB);
                shoppinglistIdOld = em.merge(shoppinglistIdOld);
            }
            if (shoppinglistIdNew != null && !shoppinglistIdNew.equals(shoppinglistIdOld)) {
                shoppinglistIdNew.getUseriDBCollection().add(useriDB);
                shoppinglistIdNew = em.merge(shoppinglistIdNew);
            }
            for (RecipeDB recipeDBCollectionNewRecipeDB : recipeDBCollectionNew) {
                if (!recipeDBCollectionOld.contains(recipeDBCollectionNewRecipeDB)) {
                    UseriDB oldUserIdOfRecipeDBCollectionNewRecipeDB = recipeDBCollectionNewRecipeDB.getUserId();
                    recipeDBCollectionNewRecipeDB.setUserId(useriDB);
                    recipeDBCollectionNewRecipeDB = em.merge(recipeDBCollectionNewRecipeDB);
                    if (oldUserIdOfRecipeDBCollectionNewRecipeDB != null && !oldUserIdOfRecipeDBCollectionNewRecipeDB.equals(useriDB)) {
                        oldUserIdOfRecipeDBCollectionNewRecipeDB.getRecipeDBCollection().remove(recipeDBCollectionNewRecipeDB);
                        oldUserIdOfRecipeDBCollectionNewRecipeDB = em.merge(oldUserIdOfRecipeDBCollectionNewRecipeDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = useriDB.getId();
                if (findUseriDB(id) == null) {
                    throw new NonexistentEntityException("The useriDB with id " + id + " no longer exists.");
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
            UseriDB useriDB;
            try {
                useriDB = em.getReference(UseriDB.class, id);
                useriDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The useriDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RecipeDB> recipeDBCollectionOrphanCheck = useriDB.getRecipeDBCollection();
            for (RecipeDB recipeDBCollectionOrphanCheckRecipeDB : recipeDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This UseriDB (" + useriDB + ") cannot be destroyed since the RecipeDB " + recipeDBCollectionOrphanCheckRecipeDB + " in its recipeDBCollection field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ShoppinglistDB shoppinglistId = useriDB.getShoppinglistId();
            if (shoppinglistId != null) {
                shoppinglistId.getUseriDBCollection().remove(useriDB);
                shoppinglistId = em.merge(shoppinglistId);
            }
            em.remove(useriDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UseriDB> findUseriDBEntities() {
        return findUseriDBEntities(true, -1, -1);
    }

    public List<UseriDB> findUseriDBEntities(int maxResults, int firstResult) {
        return findUseriDBEntities(false, maxResults, firstResult);
    }

    private List<UseriDB> findUseriDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UseriDB.class));
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

    public UseriDB findUseriDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UseriDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getUseriDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UseriDB> rt = cq.from(UseriDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public UseriDB getUserByUsername(String username){
       EntityManager em= getEntityManager();
       Query query =  em.createNamedQuery("UserDB.findByUsername");
       query.setParameter("username", username);
        try{
        return  (UseriDB)query.getSingleResult();
        }catch(Exception e){   }
        return null;
    }
}
