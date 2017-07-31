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
@Table(name = "tbMeioResposta")
@NamedQueries({
    @NamedQuery(name = "TbMeioResposta.findAll", query = "SELECT t FROM TbMeioResposta t"),
    @NamedQuery(name = "TbMeioResposta.findByIdMeioResposta", query = "SELECT t FROM TbMeioResposta t WHERE t.idMeioResposta = :idMeioResposta"),
    @NamedQuery(name = "TbMeioResposta.findByNmMeioResposta", query = "SELECT t FROM TbMeioResposta t WHERE t.nmMeioResposta = :nmMeioResposta")})
public class TbMeioResposta implements Serializable,Comparable<TbMeioResposta> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMeioResposta")
    private Integer idMeioResposta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nmMeioResposta")
    private String nmMeioResposta;
    @OneToMany(mappedBy = "idMeioResposta")
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();

    public TbMeioResposta() {
    }

    public TbMeioResposta(Integer idMeioResposta) {
        this.idMeioResposta = idMeioResposta;
    }

    public TbMeioResposta(Integer idMeioResposta, String nmMeioResposta) {
        this.idMeioResposta = idMeioResposta;
        this.nmMeioResposta = nmMeioResposta;
    }

    public Integer getIdMeioResposta() {
        return idMeioResposta;
    }

    public void setIdMeioResposta(Integer idMeioResposta) {
        this.idMeioResposta = idMeioResposta;
    }

    public String getNmMeioResposta() {
        return nmMeioResposta;
    }

    public void setNmMeioResposta(String nmMeioResposta) {
        this.nmMeioResposta = nmMeioResposta;
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
        hash += (idMeioResposta != null ? idMeioResposta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbMeioResposta)) {
            return false;
        }
        TbMeioResposta other = (TbMeioResposta) object;
        if ((this.idMeioResposta == null && other.idMeioResposta != null) || (this.idMeioResposta != null && !this.idMeioResposta.equals(other.idMeioResposta))) {
            return false;
        }
        return true;
    }

   
   @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Meio Resposta";
    }

    private String getDescricao() {
        return nmMeioResposta;
    }

	@Override
	public int compareTo(TbMeioResposta other) {
		return getNmMeioResposta().toUpperCase().compareTo(other.getNmMeioResposta().toUpperCase());
	}
    
}
