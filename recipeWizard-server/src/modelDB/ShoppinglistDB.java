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
@Table(name = "shoppinglist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShoppinglistDB.findAll", query = "SELECT s FROM ShoppinglistDB s"),
    @NamedQuery(name = "ShoppinglistDB.findById", query = "SELECT s FROM ShoppinglistDB s WHERE s.id = :id"),
    @NamedQuery(name = "ShoppinglistDB.findByName", query = "SELECT s FROM ShoppinglistDB s WHERE s.name = :name")})
public class ShoppinglistDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppinglistId")
    private Collection<UseriDB> useriDBCollection;

    public ShoppinglistDB() {
    }

    public ShoppinglistDB(Integer id) {
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

    @XmlTransient
    public Collection<UseriDB> getUseriDBCollection() {
        return useriDBCollection;
    }

    public void setUseriDBCollection(Collection<UseriDB> useriDBCollection) {
        this.useriDBCollection = useriDBCollection;
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
        if (!(object instanceof ShoppinglistDB)) {
            return false;
        }
        ShoppinglistDB other = (ShoppinglistDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelDB.ShoppinglistDB[ id=" + id + " ]";
    }
    
}
