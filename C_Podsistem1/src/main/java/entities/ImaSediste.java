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
@Table(name = "ima_sediste")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImaSediste.findAll", query = "SELECT i FROM ImaSediste i"),
    @NamedQuery(name = "ImaSediste.findByIdKomitenta", query = "SELECT i FROM ImaSediste i WHERE i.idKomitenta = :idKomitenta")})
public class ImaSediste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKomitenta")
    private Integer idKomitenta;
    @JoinColumn(name = "IdKomitenta", referencedColumnName = "IdK", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Komitent komitent;
    @JoinColumn(name = "IdMesta", referencedColumnName = "IdM")
    @ManyToOne
    private Mesto idMesta;

    public ImaSediste() {
    }

    public ImaSediste(Integer idKomitenta) {
        this.idKomitenta = idKomitenta;
    }

    public Integer getIdKomitenta() {
        return idKomitenta;
    }

    public void setIdKomitenta(Integer idKomitenta) {
        this.idKomitenta = idKomitenta;
    }

    public Komitent getKomitent() {
        return komitent;
    }

    public void setKomitent(Komitent komitent) {
        this.komitent = komitent;
    }

    public Mesto getIdMesta() {
        return idMesta;
    }

    public void setIdMesta(Mesto idMesta) {
        this.idMesta = idMesta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKomitenta != null ? idKomitenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImaSediste)) {
            return false;
        }
        ImaSediste other = (ImaSediste) object;
        if ((this.idKomitenta == null && other.idKomitenta != null) || (this.idKomitenta != null && !this.idKomitenta.equals(other.idKomitenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ImaSediste[ idKomitenta=" + idKomitenta + " ]";
    }
    
}
