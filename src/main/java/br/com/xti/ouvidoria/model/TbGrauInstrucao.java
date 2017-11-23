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
@Table(name = "tbGrauInstrucao")
@NamedQueries({
    @NamedQuery(name = "TbGrauInstrucao.findAll", query = "SELECT t FROM TbGrauInstrucao t"),
    @NamedQuery(name = "TbGrauInstrucao.findByIdGrauInstrucao", query = "SELECT t FROM TbGrauInstrucao t WHERE t.idGrauInstrucao = :idGrauInstrucao"),
    @NamedQuery(name = "TbGrauInstrucao.findByNmGrauInstrucao", query = "SELECT t FROM TbGrauInstrucao t WHERE t.nmGrauInstrucao = :nmGrauInstrucao")})
public class TbGrauInstrucao implements Serializable, Comparable<TbGrauInstrucao> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGrauInstrucao")
    private Integer idGrauInstrucao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nmGrauInstrucao")
    private String nmGrauInstrucao;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idGrauInstrucao")*/
    @OneToMany(mappedBy = "idGrauInstrucao")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbGrauInstrucao() {
    }

    public TbGrauInstrucao(Integer idGrauInstrucao) {
        this.idGrauInstrucao = idGrauInstrucao;
    }

    public TbGrauInstrucao(Integer idGrauInstrucao, String nmGrauInstrucao) {
        this.idGrauInstrucao = idGrauInstrucao;
        this.nmGrauInstrucao = nmGrauInstrucao;
    }

    public Integer getIdGrauInstrucao() {
        return idGrauInstrucao;
    }

    public void setIdGrauInstrucao(Integer idGrauInstrucao) {
        this.idGrauInstrucao = idGrauInstrucao;
    }

    public String getNmGrauInstrucao() {
        return nmGrauInstrucao;
    }

    public void setNmGrauInstrucao(String nmGrauInstrucao) {
        this.nmGrauInstrucao = nmGrauInstrucao;
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
        hash += (idGrauInstrucao != null ? idGrauInstrucao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbGrauInstrucao)) {
            return false;
        }
        TbGrauInstrucao other = (TbGrauInstrucao) object;
        if ((this.idGrauInstrucao == null && other.idGrauInstrucao != null) || (this.idGrauInstrucao != null && !this.idGrauInstrucao.equals(other.idGrauInstrucao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Grau de Instrução";
    }

    private String getDescricao() {
        return nmGrauInstrucao;
    }

	@Override
	public int compareTo(TbGrauInstrucao other) {
		return getNmGrauInstrucao().toUpperCase().compareTo(other.getNmGrauInstrucao().toUpperCase());
	}
}
