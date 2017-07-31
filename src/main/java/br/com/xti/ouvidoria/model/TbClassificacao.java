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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "tbClassificacao")
@NamedQueries({
    @NamedQuery(name = "TbClassificacao.findAll", query = "SELECT t FROM TbClassificacao t"),
    @NamedQuery(name = "TbClassificacao.findByIdClassificacao", query = "SELECT t FROM TbClassificacao t WHERE t.idClassificacao = :idClassificacao"),
    @NamedQuery(name = "TbClassificacao.findByUnidade", query = "SELECT t FROM TbClassificacao t WHERE :idUnidade MEMBER OF t.tbUnidadeCollection"),
    @NamedQuery(name = "TbClassificacao.findByDsClassificacao", query = "SELECT t FROM TbClassificacao t WHERE t.dsClassificacao = :dsClassificacao")})
public class TbClassificacao implements Serializable, Comparable<TbClassificacao> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idClassificacao")
    private Integer idClassificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 180)
    @Column(name = "dsClassificacao")
    private String dsClassificacao;
    @OneToMany
    private Collection<TbUnidade> tbUnidadeCollection = new ArrayList<>();
    @OneToMany
    @JoinTable(
    		name="tbClassificacao_tbSubClassificacao",
    		joinColumns={@JoinColumn(name="TbClassificacao_idClassificacao", referencedColumnName="idClassificacao")},
    		inverseJoinColumns={@JoinColumn(name="tbSubClassificacaoCollection_idSubClassificacao", referencedColumnName="idSubClassificacao")})
    private Collection<TbSubClassificacao> tbSubClassificacaoCollection = new ArrayList<>();
    @OneToMany
    @JoinTable(
    		name="tbManifestacao_tbClassificacao",
    		joinColumns={@JoinColumn(name="tbClassificacao_idClassificacao", referencedColumnName="idClassificacao")},
    		inverseJoinColumns={@JoinColumn(name="TbManifestacao_idManifestacao", referencedColumnName="idManifestacao")})
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();
    
    
    public Collection<TbUnidade> getTbUnidadeCollection() {
        return tbUnidadeCollection;
    }

    public Collection<TbManifestacao> getTbManifestacaoCollection() {
        return tbManifestacaoCollection;
    }

    public void setTbManifestacaoCollection(Collection<TbManifestacao> tbManifestacaoCollection) {
        this.tbManifestacaoCollection = tbManifestacaoCollection;
    }

    public void setTbUnidadeCollection(Collection<TbUnidade> tbUnidadeCollection) {
        this.tbUnidadeCollection = tbUnidadeCollection;
    }

    public TbClassificacao() {
    }

    public TbClassificacao(Integer idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    public TbClassificacao(Integer idClassificacao, String dsClassificacao) {
        this.idClassificacao = idClassificacao;
        this.dsClassificacao = dsClassificacao;
    }

    public Integer getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(Integer idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    public String getDsClassificacao() {
        return dsClassificacao;
    }

    public void setDsClassificacao(String dsClassificacao) {
        this.dsClassificacao = dsClassificacao;
    }

    public Collection<TbSubClassificacao> getTbSubClassificacaoCollection() {
        return tbSubClassificacaoCollection;
    }

    public void setTbSubClassificacaoCollection(Collection<TbSubClassificacao> tbSubClassificacaoCollection) {
        this.tbSubClassificacaoCollection = tbSubClassificacaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClassificacao != null ? idClassificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbClassificacao)) {
            return false;
        }
        TbClassificacao other = (TbClassificacao) object;
        if ((this.idClassificacao == null && other.idClassificacao != null) || (this.idClassificacao != null && !this.idClassificacao.equals(other.idClassificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Classificação";
    }

    private String getDescricao() {
        return dsClassificacao;
    }

	@Override
	public int compareTo(TbClassificacao o) {
		return getDsClassificacao().toUpperCase().compareTo(o.getDsClassificacao().toUpperCase());
	}
}
