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
import modelDB.IngredientDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelDB.RecipeDB;

/**
 *
 * @author Agnesss
 */
public class RecipeDBJpaController implements Serializable {

    public RecipeDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RecipeDB recipeDB) {
        if (recipeDB.getIngredientDBCollection() == null) {
            recipeDB.setIngredientDBCollection(new ArrayList<IngredientDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UseriDB userId = recipeDB.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                recipeDB.setUserId(userId);
            }
            Collection<IngredientDB> attachedIngredientDBCollection = new ArrayList<IngredientDB>();
            for (IngredientDB ingredientDBCollectionIngredientDBToAttach : recipeDB.getIngredientDBCollection()) {
                ingredientDBCollectionIngredientDBToAttach = em.getReference(ingredientDBCollectionIngredientDBToAttach.getClass(), ingredientDBCollectionIngredientDBToAttach.getId());
                attachedIngredientDBCollection.add(ingredientDBCollectionIngredientDBToAttach);
            }
            recipeDB.setIngredientDBCollection(attachedIngredientDBCollection);
            em.persist(recipeDB);
            if (userId != null) {
                userId.getRecipeDBCollection().add(recipeDB);
                userId = em.merge(userId);
            }
            for (IngredientDB ingredientDBCollectionIngredientDB : recipeDB.getIngredientDBCollection()) {
                RecipeDB oldRecipeIdOfIngredientDBCollectionIngredientDB = ingredientDBCollectionIngredientDB.getRecipeId();
                ingredientDBCollectionIngredientDB.setRecipeId(recipeDB);
                ingredientDBCollectionIngredientDB = em.merge(ingredientDBCollectionIngredientDB);
                if (oldRecipeIdOfIngredientDBCollectionIngredientDB != null) {
                    oldRecipeIdOfIngredientDBCollectionIngredientDB.getIngredientDBCollection().remove(ingredientDBCollectionIngredientDB);
                    oldRecipeIdOfIngredientDBCollectionIngredientDB = em.merge(oldRecipeIdOfIngredientDBCollectionIngredientDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RecipeDB recipeDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecipeDB persistentRecipeDB = em.find(RecipeDB.class, recipeDB.getId());
            UseriDB userIdOld = persistentRecipeDB.getUserId();
            UseriDB userIdNew = recipeDB.getUserId();
            Collection<IngredientDB> ingredientDBCollectionOld = persistentRecipeDB.getIngredientDBCollection();
            Collection<IngredientDB> ingredientDBCollectionNew = recipeDB.getIngredientDBCollection();
            List<String> illegalOrphanMessages = null;
            for (IngredientDB ingredientDBCollectionOldIngredientDB : ingredientDBCollectionOld) {
                if (!ingredientDBCollectionNew.contains(ingredientDBCollectionOldIngredientDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain IngredientDB " + ingredientDBCollectionOldIngredientDB + " since its recipeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                recipeDB.setUserId(userIdNew);
            }
            Collection<IngredientDB> attachedIngredientDBCollectionNew = new ArrayList<IngredientDB>();
            for (IngredientDB ingredientDBCollectionNewIngredientDBToAttach : ingredientDBCollectionNew) {
                ingredientDBCollectionNewIngredientDBToAttach = em.getReference(ingredientDBCollectionNewIngredientDBToAttach.getClass(), ingredientDBCollectionNewIngredientDBToAttach.getId());
                attachedIngredientDBCollectionNew.add(ingredientDBCollectionNewIngredientDBToAttach);
            }
            ingredientDBCollectionNew = attachedIngredientDBCollectionNew;
            recipeDB.setIngredientDBCollection(ingredientDBCollectionNew);
            recipeDB = em.merge(recipeDB);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getRecipeDBCollection().remove(recipeDB);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getRecipeDBCollection().add(recipeDB);
                userIdNew = em.merge(userIdNew);
            }
            for (IngredientDB ingredientDBCollectionNewIngredientDB : ingredientDBCollectionNew) {
                if (!ingredientDBCollectionOld.contains(ingredientDBCollectionNewIngredientDB)) {
                    RecipeDB oldRecipeIdOfIngredientDBCollectionNewIngredientDB = ingredientDBCollectionNewIngredientDB.getRecipeId();
                    ingredientDBCollectionNewIngredientDB.setRecipeId(recipeDB);
                    ingredientDBCollectionNewIngredientDB = em.merge(ingredientDBCollectionNewIngredientDB);
                    if (oldRecipeIdOfIngredientDBCollectionNewIngredientDB != null && !oldRecipeIdOfIngredientDBCollectionNewIngredientDB.equals(recipeDB)) {
                        oldRecipeIdOfIngredientDBCollectionNewIngredientDB.getIngredientDBCollection().remove(ingredientDBCollectionNewIngredientDB);
                        oldRecipeIdOfIngredientDBCollectionNewIngredientDB = em.merge(oldRecipeIdOfIngredientDBCollectionNewIngredientDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recipeDB.getId();
                if (findRecipeDB(id) == null) {
                    throw new NonexistentEntityException("The recipeDB with id " + id + " no longer exists.");
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
            RecipeDB recipeDB;
            try {
                recipeDB = em.getReference(RecipeDB.class, id);
                recipeDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recipeDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<IngredientDB> ingredientDBCollectionOrphanCheck = recipeDB.getIngredientDBCollection();
            for (IngredientDB ingredientDBCollectionOrphanCheckIngredientDB : ingredientDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RecipeDB (" + recipeDB + ") cannot be destroyed since the IngredientDB " + ingredientDBCollectionOrphanCheckIngredientDB + " in its ingredientDBCollection field has a non-nullable recipeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            UseriDB userId = recipeDB.getUserId();
            if (userId != null) {
                userId.getRecipeDBCollection().remove(recipeDB);
                userId = em.merge(userId);
            }
            em.remove(recipeDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RecipeDB> findRecipeDBEntities() {
        return findRecipeDBEntities(true, -1, -1);
    }

    public List<RecipeDB> findRecipeDBEntities(int maxResults, int firstResult) {
        return findRecipeDBEntities(false, maxResults, firstResult);
    }

    private List<RecipeDB> findRecipeDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RecipeDB.class));
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

    public RecipeDB findRecipeDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RecipeDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecipeDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RecipeDB> rt = cq.from(RecipeDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
