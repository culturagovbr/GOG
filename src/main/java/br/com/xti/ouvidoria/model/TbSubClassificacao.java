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
@Table(name = "tbSubClassificacao")
@NamedQueries({
    @NamedQuery(name = "TbSubClassificacao.findAll", query = "SELECT t FROM TbSubClassificacao t"),
    @NamedQuery(name = "TbSubClassificacao.findByIdSubClassificacao", query = "SELECT t FROM TbSubClassificacao t WHERE t.idSubClassificacao = :idSubClassificacao"),
    @NamedQuery(name = "TbSubClassificacao.findByDsSubClassificacao", query = "SELECT t FROM TbSubClassificacao t WHERE t.dsSubClassificacao = :dsSubClassificacao")})
public class TbSubClassificacao implements Serializable, Comparable<TbSubClassificacao> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSubClassificacao")
    private Integer idSubClassificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 180)
    @Column(name = "dsSubClassificacao")
    private String dsSubClassificacao;
    @OneToMany
    @JoinTable(
    		name="tbClassificacao_tbSubClassificacao",
    		joinColumns={@JoinColumn(name="tbSubClassificacaoCollection_idSubClassificacao", referencedColumnName="idSubClassificacao")},
    		inverseJoinColumns={@JoinColumn(name="TbClassificacao_idClassificacao", referencedColumnName="idClassificacao")})
    private Collection<TbClassificacao> tbClassificacaoCollection = new ArrayList<>();
    @OneToMany
    @JoinTable(
    		name="tbManifestacao_tbSubClassificacao",
    		joinColumns={@JoinColumn(name="tbSubClassificacao_idSubClassificacao", referencedColumnName="idSubClassificacao")},
    		inverseJoinColumns={@JoinColumn(name="TbManifestacao_idManifestacao", referencedColumnName="idManifestacao")})
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();
    

    public Collection<TbClassificacao> getTbClassificacaoCollection() {
        return tbClassificacaoCollection;
    }

    public void setTbClassificacaoCollection(Collection<TbClassificacao> tbClassificacaoCollection) {
        this.tbClassificacaoCollection = tbClassificacaoCollection;
    }

    public TbSubClassificacao() {
    }

    public TbSubClassificacao(Integer idSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
    }

    public TbSubClassificacao(Integer idSubClassificacao, String dsSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
        this.dsSubClassificacao = dsSubClassificacao;
    }

    public Integer getIdSubClassificacao() {
        return idSubClassificacao;
    }

    public void setIdSubClassificacao(Integer idSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
    }

    public String getDsSubClassificacao() {
        return dsSubClassificacao;
    }

    public void setDsSubClassificacao(String dsSubClassificacao) {
        this.dsSubClassificacao = dsSubClassificacao;
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
        hash += (idSubClassificacao != null ? idSubClassificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbSubClassificacao)) {
            return false;
        }
        TbSubClassificacao other = (TbSubClassificacao) object;
        if ((this.idSubClassificacao == null && other.idSubClassificacao != null) || (this.idSubClassificacao != null && !this.idSubClassificacao.equals(other.idSubClassificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "SubClassificação";
    }

    private String getDescricao() {
        return dsSubClassificacao;
    }

    public String getClassificacoes() {
        StringBuilder bf = new StringBuilder();

        for (TbClassificacao classificacao : tbClassificacaoCollection) {
            bf.append(classificacao.getDsClassificacao());
            bf.append("\n");
        }

        return bf.toString();
    }

	@Override
	public int compareTo(TbSubClassificacao o) {
		return getDsSubClassificacao().toUpperCase().compareTo(o.getDsSubClassificacao().toUpperCase());
	}
}
