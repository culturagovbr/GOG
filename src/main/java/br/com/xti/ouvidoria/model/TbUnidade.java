/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import static br.com.xti.ouvidoria.model.enums.BooleanEnum.NAO;
import static br.com.xti.ouvidoria.model.enums.BooleanEnum.SIM;

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

import br.com.xti.ouvidoria.model.enums.UnidadeEnum;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbUnidade")
@NamedQueries({
    @NamedQuery(name = "TbUnidade.findAll", query = "SELECT t FROM TbUnidade t"),
    @NamedQuery(name = "TbUnidade.findByIdUnidade", query = "SELECT t FROM TbUnidade t WHERE t.idUnidade = :idUnidade"),
    @NamedQuery(name = "TbUnidade.findByNmUnidade", query = "SELECT t FROM TbUnidade t WHERE t.nmUnidade = :nmUnidade"),
    @NamedQuery(name = "TbUnidade.findBySgUnidade", query = "SELECT t FROM TbUnidade t WHERE t.sgUnidade = :sgUnidade"),
    @NamedQuery(name = "TbUnidade.findByEeEmail", query = "SELECT t FROM TbUnidade t WHERE t.eeEmail = :eeEmail")})
public class TbUnidade implements Serializable, Comparable<TbUnidade> {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUnidade")
    private Integer idUnidade;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nmUnidade")
    private String nmUnidade;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "sgUnidade")
    private String sgUnidade;
    
    @Size(max = 200)
    @Column(name = "eeEmail")
    private String eeEmail;
    
    @Basic(optional = true)
    @Size(min = 1, max = 1)
    @Column(name = "stRetornoOuvidoria")
    private String stRetornoOuvidoria;
    
    @Size(min = 1, max = 1)
    @Column(name = "stVinculada")
    private String stVinculada;
    
    @Size(min = 1, max = 1)
    @Column(name = "stEncaminharOutrasAreas")
    private String stEncaminharOutrasAreas;
   
    @OneToMany(mappedBy = "idUnidadeEnvio")
    private Collection<TbTramite> tbTramiteCollection  = new ArrayList<>();
    
    @OneToMany(mappedBy = "idUnidade")
    private Collection<TbUnidadexManifestacao> tbUnidadexManifestacaoCollection  = new ArrayList<>();
    
    @OneToMany(mappedBy = "idUnidade")
    private Collection<TbUsuario> tbUsuarioCollection = new ArrayList<>();
    
    @OneToMany(mappedBy = "idUnidadeRecebeu")
    private Collection<TbEncaminhamento> tbEncaminhamentoCollection  = new ArrayList<>();
    
    @OneToMany
    @JoinTable(
    		name="TbManifestacao_UnidadeAreaSolucionadora",
    		joinColumns={@JoinColumn(name="idUnidade", referencedColumnName="idUnidade")},
			inverseJoinColumns={@JoinColumn(name="idManifestacao", referencedColumnName="idManifestacao")})
    private Collection<TbManifestacao> tbManifestacaoCollection = new ArrayList<>();
    
    @OneToMany
    @JoinTable(
    		name="tbClassificacao_tbUnidade",
			joinColumns={@JoinColumn(name="tbUnidadeCollection_idUnidade", referencedColumnName="idUnidade")},
			inverseJoinColumns={@JoinColumn(name="TbClassificacao_idClassificacao", referencedColumnName="idClassificacao")})
    private Collection<TbClassificacao> tbClassificacaoCollection = new ArrayList<>();
    
    public TbUnidade() {
    }

    public TbUnidade(Integer idUnidade) {
        this.idUnidade = idUnidade;
    }

    public TbUnidade(Integer idUnidade, String nmUnidade, String sgUnidade) {
        this.idUnidade = idUnidade;
        this.nmUnidade = nmUnidade;
        this.sgUnidade = sgUnidade;
    }

    public Integer getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Integer idUnidade) {
        this.idUnidade = idUnidade;
    }

    public String getNmUnidade() {
        return nmUnidade;
    }

    public void setNmUnidade(String nmUnidade) {
        this.nmUnidade = nmUnidade;
    }

    public String getSgUnidade() {
        return sgUnidade;
    }

    public void setSgUnidade(String sgUnidade) {
        this.sgUnidade = sgUnidade;
    }

    public String getEeEmail() {
        return eeEmail;
    }

    public void setEeEmail(String eeEmail) {
        this.eeEmail = eeEmail;
    }

    public Collection<TbTramite> getTbTramiteCollection() {
        return tbTramiteCollection;
    }

    public void setTbTramiteCollection(Collection<TbTramite> tbTramiteCollection) {
        this.tbTramiteCollection = tbTramiteCollection;
    }

    public Collection<TbUnidadexManifestacao> getTbUnidadexManifestacaoCollection() {
        return tbUnidadexManifestacaoCollection;
    }

    public void setTbUnidadexManifestacaoCollection(Collection<TbUnidadexManifestacao> tbUnidadexManifestacaoCollection) {
        this.tbUnidadexManifestacaoCollection = tbUnidadexManifestacaoCollection;
    }

    public Collection<TbManifestacao> getTbManifestacaoCollection() {
        return tbManifestacaoCollection;
    }

    public void setTbManifestacaoCollection(Collection<TbManifestacao> tbManifestacaoCollection) {
        this.tbManifestacaoCollection = tbManifestacaoCollection;
    }

    public Collection<TbUsuario> getTbUsuarioCollection() {
        return tbUsuarioCollection;
    }

    public void setTbUsuarioCollection(Collection<TbUsuario> tbUsuarioCollection) {
        this.tbUsuarioCollection = tbUsuarioCollection;
    }

    public Collection<TbEncaminhamento> getTbEncaminhamentoCollection() {
        return tbEncaminhamentoCollection;
    }

    public void setTbEncaminhamentoCollection(Collection<TbEncaminhamento> tbEncaminhamentoCollection) {
        this.tbEncaminhamentoCollection = tbEncaminhamentoCollection;
    }
    
    public Collection<TbClassificacao> getTbClassificacaoCollection() {
		return tbClassificacaoCollection;
	}

	public void setTbClassificacaoCollection(
			Collection<TbClassificacao> tbClassificacaoCollection) {
		this.tbClassificacaoCollection = tbClassificacaoCollection;
	}
	
    private String getEntidade() {
        return "Unidade";
    }

    private String getDescricao() {
        return nmUnidade;
    }
    
	public String getStRetornoOuvidoria() {
		return stRetornoOuvidoria;
	}

	public void setStRetornoOuvidoria(String stRetornoOuvidoria) {
		this.stRetornoOuvidoria = stRetornoOuvidoria;
	}
	
	public boolean isRetornoOuvidoria() {
		return SIM.getId().equals(stRetornoOuvidoria);
	}
	
	public void setRetornoOuvidoria(boolean data) {
		this.stRetornoOuvidoria = data ? SIM.getId() : NAO.getId();
	}
	
	public String getStVinculada() {
		return stVinculada;
	}
	
	public void setStVinculada(String stVinculada) {
		this.stVinculada = stVinculada;
	}
	
	public boolean isVinculada() {
		return SIM.getId().equals(stVinculada);
	}
	
	public void setVinculada(boolean data) {
		this.stVinculada = data ? SIM.getId() : NAO.getId();
	}
	
	public String getStEncaminharOutrasAreas() {
		return stEncaminharOutrasAreas;
	}
	
	public void setStEncaminharOutrasAreas(String stEncaminharOutrasAreas) {
		this.stEncaminharOutrasAreas = stEncaminharOutrasAreas;
	}
	
	public boolean isEncaminharOutrasAreas() {
		return SIM.getId().equals(stEncaminharOutrasAreas);
	}
	
	public void setEncaminharOutrasAreas(boolean data) {
		this.stEncaminharOutrasAreas = data ? SIM.getId() : NAO.getId();
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idUnidade != null ? idUnidade.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TbUnidade)) {
			return false;
		}
		TbUnidade other = (TbUnidade) object;
		if ((this.idUnidade == null && other.idUnidade != null)
				|| (this.idUnidade != null && !this.idUnidade
				.equals(other.idUnidade))) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s > %s", getEntidade(), getDescricao());
	}
	
	@Override
	public int compareTo(TbUnidade o) {
		return getNmUnidade().toUpperCase().compareTo(o.getNmUnidade().toUpperCase());
	}
	
	public String getNomeFormatado() {
		String name = null;
		if(getIdUnidade() == UnidadeEnum.OUVIDORIA.getId()) {
			name = "Ouvidoria";
		} else {
			name = String.format("%s - %s", getSgUnidade(), getNmUnidade());
		}
		return name;
	}
    
}

