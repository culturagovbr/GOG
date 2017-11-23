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
@Table(name = "tbTipoManifestacao")
@NamedQueries({
    @NamedQuery(name = "TbTipoManifestacao.findAll", query = "SELECT t FROM TbTipoManifestacao t"),
    @NamedQuery(name = "TbTipoManifestacao.findByIdTipoManifestacao", query = "SELECT t FROM TbTipoManifestacao t WHERE t.idTipoManifestacao = :idTipoManifestacao"),
    @NamedQuery(name = "TbTipoManifestacao.findByNmTipoManifestacao", query = "SELECT t FROM TbTipoManifestacao t WHERE t.nmTipoManifestacao = :nmTipoManifestacao"),
    @NamedQuery(name = "TbTipoManifestacao.findByDsTipoManifestacao", query = "SELECT t FROM TbTipoManifestacao t WHERE t.dsTipoManifestacao = :dsTipoManifestacao")})
public class TbTipoManifestacao implements Serializable, Comparable<TbTipoManifestacao> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTipoManifestacao")
    private Integer idTipoManifestacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nmTipoManifestacao")
    private String nmTipoManifestacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "dsTipoManifestacao")
    private String dsTipoManifestacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoEntrada")
    private int prazoEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoAreaSolucionadora")
    private int prazoAreaSolucionadora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoRespostaCidadao")
    private int prazoRespostaCidadao;
    
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoManifestacao")*/
    @OneToMany(mappedBy = "idTipoManifestacao")
    private Collection<TbManifestacao> tbManifestacaoCollection= new ArrayList<>();

    public TbTipoManifestacao() {
    }

    public TbTipoManifestacao(Integer idTipoManifestacao) {
        this.idTipoManifestacao = idTipoManifestacao;
    }

    public TbTipoManifestacao(Integer idTipoManifestacao, String nmTipoManifestacao, String dsTipoManifestacao) {
        this.idTipoManifestacao = idTipoManifestacao;
        this.nmTipoManifestacao = nmTipoManifestacao;
        this.dsTipoManifestacao = dsTipoManifestacao;
    }

    public Integer getIdTipoManifestacao() {
        return idTipoManifestacao;
    }

    public void setIdTipoManifestacao(Integer idTipoManifestacao) {
        this.idTipoManifestacao = idTipoManifestacao;
    }

    public String getNmTipoManifestacao() {
        return nmTipoManifestacao;
    }

    public void setNmTipoManifestacao(String nmTipoManifestacao) {
        this.nmTipoManifestacao = nmTipoManifestacao;
    }

    public String getDsTipoManifestacao() {
        return dsTipoManifestacao;
    }

    public void setDsTipoManifestacao(String dsTipoManifestacao) {
        this.dsTipoManifestacao = dsTipoManifestacao;
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
        hash += (idTipoManifestacao != null ? idTipoManifestacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbTipoManifestacao)) {
            return false;
        }
        TbTipoManifestacao other = (TbTipoManifestacao) object;
        if ((this.idTipoManifestacao == null && other.idTipoManifestacao != null) || (this.idTipoManifestacao != null && !this.idTipoManifestacao.equals(other.idTipoManifestacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Tipo de Manifestação";
    }

    private String getDescricao() {
        return dsTipoManifestacao;
    }

    /**
     * @return the prazoEntrada
     */
    public int getPrazoEntrada() {
        return prazoEntrada;
    }

    /**
     * @param prazoEntrada the prazoEntrada to set
     */
    public void setPrazoEntrada(int prazoEntrada) {
        this.prazoEntrada = prazoEntrada;
    }

    /**
     * @return the prazoAreaSolucionadora
     */
    public int getPrazoAreaSolucionadora() {
        return prazoAreaSolucionadora;
    }

    /**
     * @param prazoAreaSolucionadora the prazoAreaSolucionadora to set
     */
    public void setPrazoAreaSolucionadora(int prazoAreaSolucionadora) {
        this.prazoAreaSolucionadora = prazoAreaSolucionadora;
    }

    /**
     * @return the prazoRespostaCidadao
     */
    public int getPrazoRespostaCidadao() {
        return prazoRespostaCidadao;
    }

    /**
     * @param prazoRespostaCidadao the prazoRespostaCidadao to set
     */
    public void setPrazoRespostaCidadao(int prazoRespostaCidadao) {
        this.prazoRespostaCidadao = prazoRespostaCidadao;
    }

	@Override
	public int compareTo(TbTipoManifestacao other) {
		return getNmTipoManifestacao().toUpperCase().compareTo(other.getNmTipoManifestacao().toUpperCase());
	}
    
    
}
