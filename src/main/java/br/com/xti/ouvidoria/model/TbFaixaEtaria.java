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
@Table(name = "tbFaixaEtaria")
@NamedQueries({
    @NamedQuery(name = "TbFaixaEtaria.findAll", query = "SELECT t FROM TbFaixaEtaria t"),
    @NamedQuery(name = "TbFaixaEtaria.findByIdFaixaEtaria", query = "SELECT t FROM TbFaixaEtaria t WHERE t.idFaixaEtaria = :idFaixaEtaria"),
    @NamedQuery(name = "TbFaixaEtaria.findByNmFaixaEtaria", query = "SELECT t FROM TbFaixaEtaria t WHERE t.nmFaixaEtaria = :nmFaixaEtaria")})
public class TbFaixaEtaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFaixaEtaria")
    private Integer idFaixaEtaria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nmFaixaEtaria")
    private String nmFaixaEtaria;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idFaixaEtaria")*/
    @OneToMany(mappedBy = "idFaixaEtaria")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbFaixaEtaria() {
    }

    public TbFaixaEtaria(Integer idFaixaEtaria) {
        this.idFaixaEtaria = idFaixaEtaria;
    }

    public TbFaixaEtaria(Integer idFaixaEtaria, String nmFaixaEtaria) {
        this.idFaixaEtaria = idFaixaEtaria;
        this.nmFaixaEtaria = nmFaixaEtaria;
    }

    public Integer getIdFaixaEtaria() {
        return idFaixaEtaria;
    }

    public void setIdFaixaEtaria(Integer idFaixaEtaria) {
        this.idFaixaEtaria = idFaixaEtaria;
    }

    public String getNmFaixaEtaria() {
        return nmFaixaEtaria;
    }

    public void setNmFaixaEtaria(String nmFaixaEtaria) {
        this.nmFaixaEtaria = nmFaixaEtaria;
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
        hash += (idFaixaEtaria != null ? idFaixaEtaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbFaixaEtaria)) {
            return false;
        }
        TbFaixaEtaria other = (TbFaixaEtaria) object;
        if ((this.idFaixaEtaria == null && other.idFaixaEtaria != null) || (this.idFaixaEtaria != null && !this.idFaixaEtaria.equals(other.idFaixaEtaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Faixa Etária";
    }

    private String getDescricao() {
        return nmFaixaEtaria;
    }
}
