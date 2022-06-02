/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sofij
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdT", query = "SELECT t FROM Transakcija t WHERE t.idT = :idT"),
    @NamedQuery(name = "Transakcija.findByDatumObavljanja", query = "SELECT t FROM Transakcija t WHERE t.datumObavljanja = :datumObavljanja"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findByRedBr", query = "SELECT t FROM Transakcija t WHERE t.redBr = :redBr"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdT")
    private Integer idT;
    @Column(name = "DatumObavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumObavljanja;
    @Column(name = "iznos")
    private Integer iznos;
    @Column(name = "RedBr")
    private Integer redBr;
    @Size(max = 45)
    @Column(name = "Svrha")
    private String svrha;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Isplata isplata;
    @JoinColumn(name = "IdRacun", referencedColumnName = "IdR")
    @ManyToOne
    private Racun idRacun;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Prenos prenos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Uplata uplata;

    public Transakcija() {
    }

    public Transakcija(Integer idT) {
        this.idT = idT;
    }

    public Integer getIdT() {
        return idT;
    }

    public void setIdT(Integer idT) {
        this.idT = idT;
    }

    public Date getDatumObavljanja() {
        return datumObavljanja;
    }

    public void setDatumObavljanja(Date datumObavljanja) {
        this.datumObavljanja = datumObavljanja;
    }

    public Integer getIznos() {
        return iznos;
    }

    public void setIznos(Integer iznos) {
        this.iznos = iznos;
    }

    public Integer getRedBr() {
        return redBr;
    }

    public void setRedBr(Integer redBr) {
        this.redBr = redBr;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Isplata getIsplata() {
        return isplata;
    }

    public void setIsplata(Isplata isplata) {
        this.isplata = isplata;
    }

    public Racun getIdRacun() {
        return idRacun;
    }

    public void setIdRacun(Racun idRacun) {
        this.idRacun = idRacun;
    }

    public Prenos getPrenos() {
        return prenos;
    }

    public void setPrenos(Prenos prenos) {
        this.prenos = prenos;
    }

    public Uplata getUplata() {
        return uplata;
    }

    public void setUplata(Uplata uplata) {
        this.uplata = uplata;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idT != null ? idT.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idT == null && other.idT != null) || (this.idT != null && !this.idT.equals(other.idT))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transakcija[ idT=" + idT + " ]";
    }
    
}
