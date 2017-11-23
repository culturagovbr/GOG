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
@Table(name = "tbPais")
@NamedQueries({
    @NamedQuery(name = "TbPais.findAll", query = "SELECT t FROM TbPais t"),
    @NamedQuery(name = "TbPais.findByIdPais", query = "SELECT t FROM TbPais t WHERE t.idPais = :idPais"),
    @NamedQuery(name = "TbPais.findByNmPais", query = "SELECT t FROM TbPais t WHERE t.nmPais = :nmPais")})
public class TbPais implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPais")
    private Integer idPais;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nmPais")
    private String nmPais;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPais")*/
    @OneToMany(mappedBy = "idPais")
    private Collection<TbManifestacao> tbManifestacaoCollection= new ArrayList<>();

    public TbPais() {
    }

    public TbPais(Integer idPais) {
        this.idPais = idPais;
    }

    public TbPais(Integer idPais, String nmPais) {
        this.idPais = idPais;
        this.nmPais = nmPais;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getNmPais() {
        return nmPais;
    }

    public void setNmPais(String nmPais) {
        this.nmPais = nmPais;
    }

    public Collection<TbManifestacao> getTbManifestacaoCollection() {
        return tbManifestacaoCollection;
    }

    public void setTbManifestacaoCollection(Collection<TbManifestacao> tbManifestacaoCollection) {
        this.tbManifestacaoCollection = tbManifestacaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPais)) {
            return false;
        }
        TbPais other = (TbPais) object;
        if ((this.idPais == null && other.idPais != null) || (this.idPais != null && !this.idPais.equals(other.idPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.TbPais[ idPais=" + idPais + " ]";
    }
    
}
