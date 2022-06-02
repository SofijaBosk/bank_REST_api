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
@Table(name = "uplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uplata.findAll", query = "SELECT u FROM Uplata u"),
    @NamedQuery(name = "Uplata.findByIdUplata", query = "SELECT u FROM Uplata u WHERE u.idUplata = :idUplata")})
public class Uplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdUplata")
    private Integer idUplata;
    @JoinColumn(name = "IdFilijala", referencedColumnName = "IdF")
    @ManyToOne
    private Filijala idFilijala;
    @JoinColumn(name = "IdUplata", referencedColumnName = "IdT", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Uplata() {
    }

    public Uplata(Integer idUplata) {
        this.idUplata = idUplata;
    }

    public Integer getIdUplata() {
        return idUplata;
    }

    public void setIdUplata(Integer idUplata) {
        this.idUplata = idUplata;
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
        hash += (idUplata != null ? idUplata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uplata)) {
            return false;
        }
        Uplata other = (Uplata) object;
        if ((this.idUplata == null && other.idUplata != null) || (this.idUplata != null && !this.idUplata.equals(other.idUplata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Uplata[ idUplata=" + idUplata + " ]";
    }
    
}
