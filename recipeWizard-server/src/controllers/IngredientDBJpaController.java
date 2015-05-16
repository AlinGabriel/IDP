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
import modelDB.RecipeDB;
import modelDB.CategoryDB;
import modelDB.QuantityDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelDB.IngredientDB;

/**
 *
 * @author Agnesss
 */
public class IngredientDBJpaController implements Serializable {

    public IngredientDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(IngredientDB ingredientDB) {
        if (ingredientDB.getQuantityDBCollection() == null) {
            ingredientDB.setQuantityDBCollection(new ArrayList<QuantityDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RecipeDB recipeId = ingredientDB.getRecipeId();
            if (recipeId != null) {
                recipeId = em.getReference(recipeId.getClass(), recipeId.getId());
                ingredientDB.setRecipeId(recipeId);
            }
            CategoryDB categoryId = ingredientDB.getCategoryId();
            if (categoryId != null) {
                categoryId = em.getReference(categoryId.getClass(), categoryId.getId());
                ingredientDB.setCategoryId(categoryId);
            }
            Collection<QuantityDB> attachedQuantityDBCollection = new ArrayList<QuantityDB>();
            for (QuantityDB quantityDBCollectionQuantityDBToAttach : ingredientDB.getQuantityDBCollection()) {
                quantityDBCollectionQuantityDBToAttach = em.getReference(quantityDBCollectionQuantityDBToAttach.getClass(), quantityDBCollectionQuantityDBToAttach.getId());
                attachedQuantityDBCollection.add(quantityDBCollectionQuantityDBToAttach);
            }
            ingredientDB.setQuantityDBCollection(attachedQuantityDBCollection);
            em.persist(ingredientDB);
            if (recipeId != null) {
                recipeId.getIngredientDBCollection().add(ingredientDB);
                recipeId = em.merge(recipeId);
            }
            if (categoryId != null) {
                categoryId.getIngredientDBCollection().add(ingredientDB);
                categoryId = em.merge(categoryId);
            }
            for (QuantityDB quantityDBCollectionQuantityDB : ingredientDB.getQuantityDBCollection()) {
                IngredientDB oldIngredientIdOfQuantityDBCollectionQuantityDB = quantityDBCollectionQuantityDB.getIngredientId();
                quantityDBCollectionQuantityDB.setIngredientId(ingredientDB);
                quantityDBCollectionQuantityDB = em.merge(quantityDBCollectionQuantityDB);
                if (oldIngredientIdOfQuantityDBCollectionQuantityDB != null) {
                    oldIngredientIdOfQuantityDBCollectionQuantityDB.getQuantityDBCollection().remove(quantityDBCollectionQuantityDB);
                    oldIngredientIdOfQuantityDBCollectionQuantityDB = em.merge(oldIngredientIdOfQuantityDBCollectionQuantityDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(IngredientDB ingredientDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            IngredientDB persistentIngredientDB = em.find(IngredientDB.class, ingredientDB.getId());
            RecipeDB recipeIdOld = persistentIngredientDB.getRecipeId();
            RecipeDB recipeIdNew = ingredientDB.getRecipeId();
            CategoryDB categoryIdOld = persistentIngredientDB.getCategoryId();
            CategoryDB categoryIdNew = ingredientDB.getCategoryId();
            Collection<QuantityDB> quantityDBCollectionOld = persistentIngredientDB.getQuantityDBCollection();
            Collection<QuantityDB> quantityDBCollectionNew = ingredientDB.getQuantityDBCollection();
            List<String> illegalOrphanMessages = null;
            for (QuantityDB quantityDBCollectionOldQuantityDB : quantityDBCollectionOld) {
                if (!quantityDBCollectionNew.contains(quantityDBCollectionOldQuantityDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain QuantityDB " + quantityDBCollectionOldQuantityDB + " since its ingredientId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (recipeIdNew != null) {
                recipeIdNew = em.getReference(recipeIdNew.getClass(), recipeIdNew.getId());
                ingredientDB.setRecipeId(recipeIdNew);
            }
            if (categoryIdNew != null) {
                categoryIdNew = em.getReference(categoryIdNew.getClass(), categoryIdNew.getId());
                ingredientDB.setCategoryId(categoryIdNew);
            }
            Collection<QuantityDB> attachedQuantityDBCollectionNew = new ArrayList<QuantityDB>();
            for (QuantityDB quantityDBCollectionNewQuantityDBToAttach : quantityDBCollectionNew) {
                quantityDBCollectionNewQuantityDBToAttach = em.getReference(quantityDBCollectionNewQuantityDBToAttach.getClass(), quantityDBCollectionNewQuantityDBToAttach.getId());
                attachedQuantityDBCollectionNew.add(quantityDBCollectionNewQuantityDBToAttach);
            }
            quantityDBCollectionNew = attachedQuantityDBCollectionNew;
            ingredientDB.setQuantityDBCollection(quantityDBCollectionNew);
            ingredientDB = em.merge(ingredientDB);
            if (recipeIdOld != null && !recipeIdOld.equals(recipeIdNew)) {
                recipeIdOld.getIngredientDBCollection().remove(ingredientDB);
                recipeIdOld = em.merge(recipeIdOld);
            }
            if (recipeIdNew != null && !recipeIdNew.equals(recipeIdOld)) {
                recipeIdNew.getIngredientDBCollection().add(ingredientDB);
                recipeIdNew = em.merge(recipeIdNew);
            }
            if (categoryIdOld != null && !categoryIdOld.equals(categoryIdNew)) {
                categoryIdOld.getIngredientDBCollection().remove(ingredientDB);
                categoryIdOld = em.merge(categoryIdOld);
            }
            if (categoryIdNew != null && !categoryIdNew.equals(categoryIdOld)) {
                categoryIdNew.getIngredientDBCollection().add(ingredientDB);
                categoryIdNew = em.merge(categoryIdNew);
            }
            for (QuantityDB quantityDBCollectionNewQuantityDB : quantityDBCollectionNew) {
                if (!quantityDBCollectionOld.contains(quantityDBCollectionNewQuantityDB)) {
                    IngredientDB oldIngredientIdOfQuantityDBCollectionNewQuantityDB = quantityDBCollectionNewQuantityDB.getIngredientId();
                    quantityDBCollectionNewQuantityDB.setIngredientId(ingredientDB);
                    quantityDBCollectionNewQuantityDB = em.merge(quantityDBCollectionNewQuantityDB);
                    if (oldIngredientIdOfQuantityDBCollectionNewQuantityDB != null && !oldIngredientIdOfQuantityDBCollectionNewQuantityDB.equals(ingredientDB)) {
                        oldIngredientIdOfQuantityDBCollectionNewQuantityDB.getQuantityDBCollection().remove(quantityDBCollectionNewQuantityDB);
                        oldIngredientIdOfQuantityDBCollectionNewQuantityDB = em.merge(oldIngredientIdOfQuantityDBCollectionNewQuantityDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ingredientDB.getId();
                if (findIngredientDB(id) == null) {
                    throw new NonexistentEntityException("The ingredientDB with id " + id + " no longer exists.");
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
            IngredientDB ingredientDB;
            try {
                ingredientDB = em.getReference(IngredientDB.class, id);
                ingredientDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ingredientDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<QuantityDB> quantityDBCollectionOrphanCheck = ingredientDB.getQuantityDBCollection();
            for (QuantityDB quantityDBCollectionOrphanCheckQuantityDB : quantityDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This IngredientDB (" + ingredientDB + ") cannot be destroyed since the QuantityDB " + quantityDBCollectionOrphanCheckQuantityDB + " in its quantityDBCollection field has a non-nullable ingredientId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            RecipeDB recipeId = ingredientDB.getRecipeId();
            if (recipeId != null) {
                recipeId.getIngredientDBCollection().remove(ingredientDB);
                recipeId = em.merge(recipeId);
            }
            CategoryDB categoryId = ingredientDB.getCategoryId();
            if (categoryId != null) {
                categoryId.getIngredientDBCollection().remove(ingredientDB);
                categoryId = em.merge(categoryId);
            }
            em.remove(ingredientDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<IngredientDB> findIngredientDBEntities() {
        return findIngredientDBEntities(true, -1, -1);
    }

    public List<IngredientDB> findIngredientDBEntities(int maxResults, int firstResult) {
        return findIngredientDBEntities(false, maxResults, firstResult);
    }

    private List<IngredientDB> findIngredientDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(IngredientDB.class));
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

    public IngredientDB findIngredientDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(IngredientDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getIngredientDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<IngredientDB> rt = cq.from(IngredientDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
