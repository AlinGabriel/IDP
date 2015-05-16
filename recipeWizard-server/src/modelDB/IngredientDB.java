/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelDB;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Agnesss
 */
@Entity
@Table(name = "ingredient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IngredientDB.findAll", query = "SELECT i FROM IngredientDB i"),
    @NamedQuery(name = "IngredientDB.findById", query = "SELECT i FROM IngredientDB i WHERE i.id = :id"),
    @NamedQuery(name = "IngredientDB.findByName", query = "SELECT i FROM IngredientDB i WHERE i.name = :name"),
    @NamedQuery(name = "IngredientDB.findByStatus", query = "SELECT i FROM IngredientDB i WHERE i.status = :status")})
public class IngredientDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RecipeDB recipeId;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CategoryDB categoryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingredientId")
    private Collection<QuantityDB> quantityDBCollection;

    public IngredientDB() {
    }

    public IngredientDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RecipeDB getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(RecipeDB recipeId) {
        this.recipeId = recipeId;
    }

    public CategoryDB getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryDB categoryId) {
        this.categoryId = categoryId;
    }

    @XmlTransient
    public Collection<QuantityDB> getQuantityDBCollection() {
        return quantityDBCollection;
    }

    public void setQuantityDBCollection(Collection<QuantityDB> quantityDBCollection) {
        this.quantityDBCollection = quantityDBCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngredientDB)) {
            return false;
        }
        IngredientDB other = (IngredientDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelDB.IngredientDB[ id=" + id + " ]";
    }
    
}
