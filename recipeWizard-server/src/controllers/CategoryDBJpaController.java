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
import modelDB.IngredientDB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelDB.CategoryDB;

/**
 *
 * @author Agnesss
 */
public class CategoryDBJpaController implements Serializable {

    public CategoryDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoryDB categoryDB) {
        if (categoryDB.getIngredientDBCollection() == null) {
            categoryDB.setIngredientDBCollection(new ArrayList<IngredientDB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<IngredientDB> attachedIngredientDBCollection = new ArrayList<IngredientDB>();
            for (IngredientDB ingredientDBCollectionIngredientDBToAttach : categoryDB.getIngredientDBCollection()) {
                ingredientDBCollectionIngredientDBToAttach = em.getReference(ingredientDBCollectionIngredientDBToAttach.getClass(), ingredientDBCollectionIngredientDBToAttach.getId());
                attachedIngredientDBCollection.add(ingredientDBCollectionIngredientDBToAttach);
            }
            categoryDB.setIngredientDBCollection(attachedIngredientDBCollection);
            em.persist(categoryDB);
            for (IngredientDB ingredientDBCollectionIngredientDB : categoryDB.getIngredientDBCollection()) {
                CategoryDB oldCategoryIdOfIngredientDBCollectionIngredientDB = ingredientDBCollectionIngredientDB.getCategoryId();
                ingredientDBCollectionIngredientDB.setCategoryId(categoryDB);
                ingredientDBCollectionIngredientDB = em.merge(ingredientDBCollectionIngredientDB);
                if (oldCategoryIdOfIngredientDBCollectionIngredientDB != null) {
                    oldCategoryIdOfIngredientDBCollectionIngredientDB.getIngredientDBCollection().remove(ingredientDBCollectionIngredientDB);
                    oldCategoryIdOfIngredientDBCollectionIngredientDB = em.merge(oldCategoryIdOfIngredientDBCollectionIngredientDB);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoryDB categoryDB) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoryDB persistentCategoryDB = em.find(CategoryDB.class, categoryDB.getId());
            Collection<IngredientDB> ingredientDBCollectionOld = persistentCategoryDB.getIngredientDBCollection();
            Collection<IngredientDB> ingredientDBCollectionNew = categoryDB.getIngredientDBCollection();
            List<String> illegalOrphanMessages = null;
            for (IngredientDB ingredientDBCollectionOldIngredientDB : ingredientDBCollectionOld) {
                if (!ingredientDBCollectionNew.contains(ingredientDBCollectionOldIngredientDB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain IngredientDB " + ingredientDBCollectionOldIngredientDB + " since its categoryId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<IngredientDB> attachedIngredientDBCollectionNew = new ArrayList<IngredientDB>();
            for (IngredientDB ingredientDBCollectionNewIngredientDBToAttach : ingredientDBCollectionNew) {
                ingredientDBCollectionNewIngredientDBToAttach = em.getReference(ingredientDBCollectionNewIngredientDBToAttach.getClass(), ingredientDBCollectionNewIngredientDBToAttach.getId());
                attachedIngredientDBCollectionNew.add(ingredientDBCollectionNewIngredientDBToAttach);
            }
            ingredientDBCollectionNew = attachedIngredientDBCollectionNew;
            categoryDB.setIngredientDBCollection(ingredientDBCollectionNew);
            categoryDB = em.merge(categoryDB);
            for (IngredientDB ingredientDBCollectionNewIngredientDB : ingredientDBCollectionNew) {
                if (!ingredientDBCollectionOld.contains(ingredientDBCollectionNewIngredientDB)) {
                    CategoryDB oldCategoryIdOfIngredientDBCollectionNewIngredientDB = ingredientDBCollectionNewIngredientDB.getCategoryId();
                    ingredientDBCollectionNewIngredientDB.setCategoryId(categoryDB);
                    ingredientDBCollectionNewIngredientDB = em.merge(ingredientDBCollectionNewIngredientDB);
                    if (oldCategoryIdOfIngredientDBCollectionNewIngredientDB != null && !oldCategoryIdOfIngredientDBCollectionNewIngredientDB.equals(categoryDB)) {
                        oldCategoryIdOfIngredientDBCollectionNewIngredientDB.getIngredientDBCollection().remove(ingredientDBCollectionNewIngredientDB);
                        oldCategoryIdOfIngredientDBCollectionNewIngredientDB = em.merge(oldCategoryIdOfIngredientDBCollectionNewIngredientDB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoryDB.getId();
                if (findCategoryDB(id) == null) {
                    throw new NonexistentEntityException("The categoryDB with id " + id + " no longer exists.");
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
            CategoryDB categoryDB;
            try {
                categoryDB = em.getReference(CategoryDB.class, id);
                categoryDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoryDB with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<IngredientDB> ingredientDBCollectionOrphanCheck = categoryDB.getIngredientDBCollection();
            for (IngredientDB ingredientDBCollectionOrphanCheckIngredientDB : ingredientDBCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CategoryDB (" + categoryDB + ") cannot be destroyed since the IngredientDB " + ingredientDBCollectionOrphanCheckIngredientDB + " in its ingredientDBCollection field has a non-nullable categoryId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoryDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoryDB> findCategoryDBEntities() {
        return findCategoryDBEntities(true, -1, -1);
    }

    public List<CategoryDB> findCategoryDBEntities(int maxResults, int firstResult) {
        return findCategoryDBEntities(false, maxResults, firstResult);
    }

    private List<CategoryDB> findCategoryDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoryDB.class));
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

    public CategoryDB findCategoryDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoryDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoryDB> rt = cq.from(CategoryDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
