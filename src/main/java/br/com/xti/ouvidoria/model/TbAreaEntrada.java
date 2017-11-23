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
@Table(name = "tbAreaEntrada")
@NamedQueries({
    @NamedQuery(name = "TbAreaEntrada.findAll", query = "SELECT t FROM TbAreaEntrada t"),
    @NamedQuery(name = "TbAreaEntrada.findByIdAreaEntrada", query = "SELECT t FROM TbAreaEntrada t WHERE t.idAreaEntrada = :idAreaEntrada"),
    @NamedQuery(name = "TbAreaEntrada.findByNmAreaEntrada", query = "SELECT t FROM TbAreaEntrada t WHERE t.nmAreaEntrada = :nmAreaEntrada")})
public class TbAreaEntrada implements Serializable, Comparable<TbAreaEntrada> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAreaEntrada")
    private Integer idAreaEntrada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nmAreaEntrada")
    private String nmAreaEntrada;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idAreaEntrada")*/
    @OneToMany(mappedBy = "idAreaEntrada")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbAreaEntrada() {
    }

    public TbAreaEntrada(Integer idAreaEntrada) {
        this.idAreaEntrada = idAreaEntrada;
    }

    public TbAreaEntrada(Integer idAreaEntrada, String nmAreaEntrada) {
        this.idAreaEntrada = idAreaEntrada;
        this.nmAreaEntrada = nmAreaEntrada;
    }

    public Integer getIdAreaEntrada() {
        return idAreaEntrada;
    }

    public void setIdAreaEntrada(Integer idAreaEntrada) {
        this.idAreaEntrada = idAreaEntrada;
    }

    public String getNmAreaEntrada() {
        return nmAreaEntrada;
    }

    public void setNmAreaEntrada(String nmAreaEntrada) {
        this.nmAreaEntrada = nmAreaEntrada;
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
        hash += (idAreaEntrada != null ? idAreaEntrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbAreaEntrada)) {
            return false;
        }
        TbAreaEntrada other = (TbAreaEntrada) object;
        if ((this.idAreaEntrada == null && other.idAreaEntrada != null) || (this.idAreaEntrada != null && !this.idAreaEntrada.equals(other.idAreaEntrada))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return String.format("%s > %s",getEntidade(),getDescricao());
    }
    
    private String getEntidade(){
        return "Area Entrada";
    }
    private String getDescricao(){
        return nmAreaEntrada;
    }

	@Override
	public int compareTo(TbAreaEntrada other) {
		return getNmAreaEntrada().toUpperCase().compareTo(other.getNmAreaEntrada().toUpperCase());
	}  
    
}
