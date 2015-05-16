/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelDB;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Agnesss
 */
@Entity
@Table(name = "quantity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuantityDB.findAll", query = "SELECT q FROM QuantityDB q"),
    @NamedQuery(name = "QuantityDB.findById", query = "SELECT q FROM QuantityDB q WHERE q.id = :id"),
    @NamedQuery(name = "QuantityDB.findByValue", query = "SELECT q FROM QuantityDB q WHERE q.value = :value"),
    @NamedQuery(name = "QuantityDB.findByUnits", query = "SELECT q FROM QuantityDB q WHERE q.units = :units")})
public class QuantityDB implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private Double value;
    @Column(name = "units")
    private String units;
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IngredientDB ingredientId;

    public QuantityDB() {
    }

    public QuantityDB(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public IngredientDB getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(IngredientDB ingredientId) {
        this.ingredientId = ingredientId;
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
        if (!(object instanceof QuantityDB)) {
            return false;
        }
        QuantityDB other = (QuantityDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelDB.QuantityDB[ id=" + id + " ]";
    }
    
}
