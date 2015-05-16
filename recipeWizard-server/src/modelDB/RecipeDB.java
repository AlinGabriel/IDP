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
@Table(name = "recipe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RecipeDB.findAll", query = "SELECT r FROM RecipeDB r"),
    @NamedQuery(name = "RecipeDB.findById", query = "SELECT r FROM RecipeDB r WHERE r.id = :id"),
    @NamedQuery(name = "RecipeDB.findByName", query = "SELECT r FROM RecipeDB r WHERE r.name = :name"),
    @NamedQuery(name = "RecipeDB.findByHowtocook", query = "SELECT r FROM RecipeDB r WHERE r.howtocook = :howtocook"),
    @NamedQuery(name = "RecipeDB.findByRating", query = "SELECT r FROM RecipeDB r WHERE r.rating = :rating"),
    @NamedQuery(name = "RecipeDB.findByPicture", query = "SELECT r FROM RecipeDB r WHERE r.picture = :picture")})
public class RecipeDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "howtocook")
    private String howtocook;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "picture")
    private String picture;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UseriDB userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipeId")
    private Collection<IngredientDB> ingredientDBCollection;

    public RecipeDB() {
    }

    public RecipeDB(Integer id) {
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

    public String getHowtocook() {
        return howtocook;
    }

    public void setHowtocook(String howtocook) {
        this.howtocook = howtocook;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UseriDB getUserId() {
        return userId;
    }

    public void setUserId(UseriDB userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<IngredientDB> getIngredientDBCollection() {
        return ingredientDBCollection;
    }

    public void setIngredientDBCollection(Collection<IngredientDB> ingredientDBCollection) {
        this.ingredientDBCollection = ingredientDBCollection;
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
        if (!(object instanceof RecipeDB)) {
            return false;
        }
        RecipeDB other = (RecipeDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelDB.RecipeDB[ id=" + id + " ]";
    }
    
}
