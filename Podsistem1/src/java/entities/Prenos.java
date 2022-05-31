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
@Table(name = "prenos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prenos.findAll", query = "SELECT p FROM Prenos p"),
    @NamedQuery(name = "Prenos.findByIdPrenos", query = "SELECT p FROM Prenos p WHERE p.idPrenos = :idPrenos")})
public class Prenos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdPrenos")
    private Integer idPrenos;
    @JoinColumn(name = "IdRacunNa", referencedColumnName = "IdR")
    @ManyToOne
    private Racun idRacunNa;
    @JoinColumn(name = "IdPrenos", referencedColumnName = "IdT", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Prenos() {
    }

    public Prenos(Integer idPrenos) {
        this.idPrenos = idPrenos;
    }

    public Integer getIdPrenos() {
        return idPrenos;
    }

    public void setIdPrenos(Integer idPrenos) {
        this.idPrenos = idPrenos;
    }

    public Racun getIdRacunNa() {
        return idRacunNa;
    }

    public void setIdRacunNa(Racun idRacunNa) {
        this.idRacunNa = idRacunNa;
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
        hash += (idPrenos != null ? idPrenos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prenos)) {
            return false;
        }
        Prenos other = (Prenos) object;
        if ((this.idPrenos == null && other.idPrenos != null) || (this.idPrenos != null && !this.idPrenos.equals(other.idPrenos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Prenos[ idPrenos=" + idPrenos + " ]";
    }
    
}
