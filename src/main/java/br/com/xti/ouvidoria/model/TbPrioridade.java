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
@Table(name = "tbPrioridade")
@NamedQueries({
    @NamedQuery(name = "TbPrioridade.findAll", query = "SELECT t FROM TbPrioridade t"),
    @NamedQuery(name = "TbPrioridade.findByIdPrioridade", query = "SELECT t FROM TbPrioridade t WHERE t.idPrioridade = :idPrioridade"),
    @NamedQuery(name = "TbPrioridade.findByNmPrioridade", query = "SELECT t FROM TbPrioridade t WHERE t.nmPrioridade = :nmPrioridade")})
public class TbPrioridade implements Serializable, Comparable<TbPrioridade> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPrioridade")
    private Integer idPrioridade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nmPrioridade")
    private String nmPrioridade;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idPrioridade")*/
    @OneToMany(mappedBy = "idPrioridade")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbPrioridade() {
    }

    public TbPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public TbPrioridade(Integer idPrioridade, String nmPrioridade) {
        this.idPrioridade = idPrioridade;
        this.nmPrioridade = nmPrioridade;
    }

    public Integer getIdPrioridade() {
        return idPrioridade;
    }

    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public String getNmPrioridade() {
        return nmPrioridade;
    }

    public void setNmPrioridade(String nmPrioridade) {
        this.nmPrioridade = nmPrioridade;
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
        hash += (idPrioridade != null ? idPrioridade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPrioridade)) {
            return false;
        }
        TbPrioridade other = (TbPrioridade) object;
        if ((this.idPrioridade == null && other.idPrioridade != null) || (this.idPrioridade != null && !this.idPrioridade.equals(other.idPrioridade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Prioridade";
    }

    private String getDescricao() {
        return nmPrioridade;
    }
    
    @Override
    public int compareTo(TbPrioridade other) {
    	return getNmPrioridade().compareToIgnoreCase(other.getNmPrioridade());
    }
    
}
