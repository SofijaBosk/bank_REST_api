/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sofij
 */
@Entity
@Table(name = "isplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Isplata.findAll", query = "SELECT i FROM Isplata i"),
    @NamedQuery(name = "Isplata.findByIdIsplata", query = "SELECT i FROM Isplata i WHERE i.idIsplata = :idIsplata")})
public class Isplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdIsplata")
    private Integer idIsplata;
    @JoinColumn(name = "IdFilijala", referencedColumnName = "IdF")
    @ManyToOne
    private Filijala idFilijala;
    @JoinColumn(name = "IdIsplata", referencedColumnName = "IdT", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Isplata() {
    }

    public Isplata(Integer idIsplata) {
        this.idIsplata = idIsplata;
    }

    public Integer getIdIsplata() {
        return idIsplata;
    }

    public void setIdIsplata(Integer idIsplata) {
        this.idIsplata = idIsplata;
    }

    public Filijala getIdFilijala() {
        return idFilijala;
    }

    public void setIdFilijala(Filijala idFilijala) {
        this.idFilijala = idFilijala;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIsplata != null ? idIsplata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Isplata)) {
            return false;
        }
        Isplata other = (Isplata) object;
        if ((this.idIsplata == null && other.idIsplata != null) || (this.idIsplata != null && !this.idIsplata.equals(other.idIsplata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Isplata[ idIsplata=" + idIsplata + " ]";
    }
    
}
