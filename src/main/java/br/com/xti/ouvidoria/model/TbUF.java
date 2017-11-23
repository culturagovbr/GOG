/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbUF")
@NamedQueries({
    @NamedQuery(name = "TbUF.findAll", query = "SELECT t FROM TbUF t"),
    @NamedQuery(name = "TbUF.findByIdUF", query = "SELECT t FROM TbUF t WHERE t.idUF = :idUF"),
    @NamedQuery(name = "TbUF.findBySgUF", query = "SELECT t FROM TbUF t WHERE t.sgUF = :sgUF"),
    @NamedQuery(name = "TbUF.findByNmUF", query = "SELECT t FROM TbUF t WHERE t.nmUF = :nmUF")})
public class TbUF implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUF")
    private Integer idUF;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sgUF")
    private String sgUF;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nmUF")
    private String nmUF;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idUF")*/
    @OneToMany(mappedBy = "idUF")
    private Collection<TbMunicipio> tbMunicipioCollection  = new ArrayList<>();

    public TbUF() {
    }

    public TbUF(Integer idUF) {
        this.idUF = idUF;
    }

    public TbUF(Integer idUF, String sgUF, String nmUF) {
        this.idUF = idUF;
        this.sgUF = sgUF;
        this.nmUF = nmUF;
    }

    public Integer getIdUF() {
        return idUF;
    }

    public void setIdUF(Integer idUF) {
        this.idUF = idUF;
    }

    public String getSgUF() {
        return sgUF;
    }

    public void setSgUF(String sgUF) {
        this.sgUF = sgUF;
    }

    public String getNmUF() {
        return nmUF;
    }

    public void setNmUF(String nmUF) {
        this.nmUF = nmUF;
    }

    public Collection<TbMunicipio> getTbMunicipioCollection() {
        return tbMunicipioCollection;
    }

    public void setTbMunicipioCollection(Collection<TbMunicipio> tbMunicipioCollection) {
        this.tbMunicipioCollection = tbMunicipioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUF != null ? idUF.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbUF)) {
            return false;
        }
        TbUF other = (TbUF) object;
        if ((this.idUF == null && other.idUF != null) || (this.idUF != null && !this.idUF.equals(other.idUF))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.TbUF[ idUF=" + idUF + " ]";
    }

}
