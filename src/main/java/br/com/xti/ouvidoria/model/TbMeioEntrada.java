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
@Table(name = "tbMeioEntrada")
@NamedQueries({
    @NamedQuery(name = "TbMeioEntrada.findAll", query = "SELECT t FROM TbMeioEntrada t"),
    @NamedQuery(name = "TbMeioEntrada.findByIdMeioEntrada", query = "SELECT t FROM TbMeioEntrada t WHERE t.idMeioEntrada = :idMeioEntrada"),
    @NamedQuery(name = "TbMeioEntrada.findByNmMeioEntrada", query = "SELECT t FROM TbMeioEntrada t WHERE t.nmMeioEntrada = :nmMeioEntrada")})
public class TbMeioEntrada implements Serializable, Comparable<TbMeioEntrada> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMeioEntrada")
    private Integer idMeioEntrada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nmMeioEntrada")
    private String nmMeioEntrada;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMeioEntrada")*/
    @OneToMany(mappedBy = "idMeioEntrada")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbMeioEntrada() {
    }

    public TbMeioEntrada(Integer idMeioEntrada) {
        this.idMeioEntrada = idMeioEntrada;
    }

    public TbMeioEntrada(Integer idMeioEntrada, String nmMeioEntrada) {
        this.idMeioEntrada = idMeioEntrada;
        this.nmMeioEntrada = nmMeioEntrada;
    }

    public Integer getIdMeioEntrada() {
        return idMeioEntrada;
    }

    public void setIdMeioEntrada(Integer idMeioEntrada) {
        this.idMeioEntrada = idMeioEntrada;
    }

    public String getNmMeioEntrada() {
        return nmMeioEntrada;
    }

    public void setNmMeioEntrada(String nmMeioEntrada) {
        this.nmMeioEntrada = nmMeioEntrada;
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
        hash += (idMeioEntrada != null ? idMeioEntrada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbMeioEntrada)) {
            return false;
        }
        TbMeioEntrada other = (TbMeioEntrada) object;
        if ((this.idMeioEntrada == null && other.idMeioEntrada != null) || (this.idMeioEntrada != null && !this.idMeioEntrada.equals(other.idMeioEntrada))) {
            return false;
        }
        return true;
    }

   @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Meio Entrada";
    }

    private String getDescricao() {
        return nmMeioEntrada;
    }

	@Override
	public int compareTo(TbMeioEntrada other) {
		return getNmMeioEntrada().toUpperCase().compareTo(other.getNmMeioEntrada().toUpperCase());
	}
    
}
