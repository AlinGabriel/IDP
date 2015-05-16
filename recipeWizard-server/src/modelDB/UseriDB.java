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
@Table(name = "useri")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UseriDB.findAll", query = "SELECT u FROM UseriDB u"),
    @NamedQuery(name = "UseriDB.findById", query = "SELECT u FROM UseriDB u WHERE u.id = :id"),
    @NamedQuery(name = "UseriDB.findByUsername", query = "SELECT u FROM UseriDB u WHERE u.username = :username"),
    @NamedQuery(name = "UseriDB.findByPassword", query = "SELECT u FROM UseriDB u WHERE u.password = :password"),
    @NamedQuery(name = "UseriDB.findByEmail", query = "SELECT u FROM UseriDB u WHERE u.email = :email")})
public class UseriDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<RecipeDB> recipeDBCollection;
    @JoinColumn(name = "shoppinglist_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ShoppinglistDB shoppinglistId;

    public UseriDB() {
    }

    public UseriDB(Integer id) {
        this.id = id;
    }

    public UseriDB(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public Collection<RecipeDB> getRecipeDBCollection() {
        return recipeDBCollection;
    }

    public void setRecipeDBCollection(Collection<RecipeDB> recipeDBCollection) {
        this.recipeDBCollection = recipeDBCollection;
    }

    public ShoppinglistDB getShoppinglistId() {
        return shoppinglistId;
    }

    public void setShoppinglistId(ShoppinglistDB shoppinglistId) {
        this.shoppinglistId = shoppinglistId;
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
        if (!(object instanceof UseriDB)) {
            return false;
        }
        UseriDB other = (UseriDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelDB.UseriDB[ id=" + id + " ]";
    }
    
}
