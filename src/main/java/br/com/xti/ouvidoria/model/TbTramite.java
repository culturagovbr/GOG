/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.com.xti.ouvidoria.model.enums.BooleanEnum;

/**
 * @author Samuel Correia Guimarães
 */
@Entity
@Table(name = "tbTramite")
@NamedQueries({
    @NamedQuery(name = "TbTramite.findAll", query = "SELECT t FROM TbTramite t"),
    @NamedQuery(name = "TbTramite.findByIdTramite", query = "SELECT t FROM TbTramite t WHERE t.idTramite = :idTramite"),
    @NamedQuery(name = "TbTramite.findByDtTramite", query = "SELECT t FROM TbTramite t WHERE t.dtTramite = :dtTramite")})
public class TbTramite implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTramite")
    private Integer idTramite;
    
    @Column(name = "dsDescricao")
    private String dsDescricao;
    
    @Column(name = "dtTramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtTramite;
    
    @JoinColumn(name = "idUsuarioReceptor", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuarioReceptor;
    
    @JoinColumn(name = "idUsuarioEmissor", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuarioEmissor;
    
    @JoinColumn(name = "idUnidadeEnvio", referencedColumnName = "idUnidade")
    @ManyToOne(optional = false)
    private TbUnidade idUnidadeEnvio;
    
    @JoinColumn(name = "idEncaminhamento", referencedColumnName = "idEncaminhamento")
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private TbEncaminhamento idEncaminhamento;

    @OneToMany(mappedBy = "idTramite", cascade=CascadeType.MERGE)
    private Collection<TbTramitexAnexo> tbTramitexAnexoCollection = new ArrayList<>();
    
    @Size(max = 1)
    @Column(name = "stRetornada")
    private String stRetornada;
    
    @Size(max = 1)
    @Column(name = "stNotificacao")
    private String stNotificacao;
    
    @Size(max = 1)
    @Column(name = "stTramitePublico")
    private String stTramitePublico;

    public TbTramite() {
    }

    public TbTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public String getDsDescricao() {
        return dsDescricao;
    }

    public void setDsDescricao(String dsDescricao) {
        this.dsDescricao = dsDescricao;
    }

    public Date getDtTramite() {
        return dtTramite;
    }

    public void setDtTramite(Date dtTramite) {
        this.dtTramite = dtTramite;
    }

    public TbUsuario getIdUsuarioReceptor() {
        return idUsuarioReceptor;
    }

    public void setIdUsuarioReceptor(TbUsuario idUsuarioReceptor) {
        this.idUsuarioReceptor = idUsuarioReceptor;
    }

    public TbUsuario getIdUsuarioEmissor() {
        return idUsuarioEmissor;
    }

    public void setIdUsuarioEmissor(TbUsuario idUsuarioEmissor) {
        this.idUsuarioEmissor = idUsuarioEmissor;
    }

    public TbUnidade getIdUnidadeEnvio() {
        return idUnidadeEnvio;
    }

    public void setIdUnidadeEnvio(TbUnidade idUnidadeEnvio) {
        this.idUnidadeEnvio = idUnidadeEnvio;
    }

    public TbEncaminhamento getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(TbEncaminhamento idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public Collection<TbTramitexAnexo> getTbTramitexAnexoCollection() {
        return tbTramitexAnexoCollection;
    }

    public void setTbTramitexAnexoCollection(Collection<TbTramitexAnexo> tbTramitexAnexoCollection) {
        this.tbTramitexAnexoCollection = tbTramitexAnexoCollection;
    }
    
    private String getEntidade() {
        return "Trâmite";
    }

    private String getDescricao() {
        return dsDescricao;
    }
    
    public String getStRetornada() {
		return stRetornada;
	}

	public void setStRetornada(String stRetornada) {
		this.stRetornada = stRetornada;
	}
	
	public String getStNotificacao() {
		return stNotificacao;
	}

	public void setStNotificacao(String stNotificacao) {
		this.stNotificacao = stNotificacao;
	}
	
	public boolean isNotificacao() {
		return BooleanEnum.SIM.getId().equals(stNotificacao);
	}
	
    public String getStTramitePublico() {
		return stTramitePublico;
	}

	public void setStTramitePublico(String stTramitePublico) {
		this.stTramitePublico = stTramitePublico;
	}
	
	public boolean isTramitePublico() {
		return BooleanEnum.SIM.getId().equals(stTramitePublico);
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramite != null ? idTramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbTramite)) {
            return false;
        }
        TbTramite other = (TbTramite) object;
        if ((this.idTramite == null && other.idTramite != null) || (this.idTramite != null && !this.idTramite.equals(other.idTramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }
    
	// Atributo para marcar o trâmite como selecionado na tela de administração,
	// para qye o seu conteúdo seja copiado na resposta
	@Transient
    private boolean selecionado = true;
	
	public boolean isSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

}
