/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbUsuario")
@NamedQueries({
    @NamedQuery(name = "TbUsuario.findAll", query = "SELECT t FROM TbUsuario t"),
    @NamedQuery(name = "TbUsuario.findByIdUsuario", query = "SELECT t FROM TbUsuario t WHERE t.idUsuario = :idUsuario"),
    @NamedQuery(name = "TbUsuario.findByNmUsuario", query = "SELECT t FROM TbUsuario t WHERE t.nmUsuario = :nmUsuario"),
    @NamedQuery(name = "TbUsuario.findByStStatus", query = "SELECT t FROM TbUsuario t WHERE t.stStatus = :stStatus"),
    @NamedQuery(name = "TbUsuario.findByEeEmail", query = "SELECT t FROM TbUsuario t WHERE t.eeEmail = :eeEmail"),
    @NamedQuery(name = "TbUsuario.findByTpUsuario", query = "SELECT t FROM TbUsuario t WHERE t.tpUsuario = :tpUsuario"),
    @NamedQuery(name = "TbUsuario.findByNmLogin", query = "SELECT t FROM TbUsuario t WHERE t.nmLogin = :nmLogin"),
    @NamedQuery(name = "TbUsuario.findByNumTelefone", query = "SELECT t FROM TbUsuario t WHERE t.numTelefone = :numTelefone"),
    @NamedQuery(name = "TbUsuario.findByNmSenha", query = "SELECT t FROM TbUsuario t WHERE t.nmSenha = :nmSenha"),
    @NamedQuery(name = "TbUsuario.findByTpFuncao", query = "SELECT t FROM TbUsuario t WHERE t.tpFuncao = :tpFuncao")})
public class TbUsuario implements Serializable, Comparable<TbUsuario> {
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idUsuario")
	private Integer idUsuario;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 180)
	@Column(name = "nmUsuario")
	private String nmUsuario;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "stStatus")
	private Integer stStatus;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 200)
	@Column(name = "eeEmail")
	private String eeEmail;
	
	@Column(name = "tpUsuario")
	private String tpUsuario;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nmLogin")
	private String nmLogin;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 13)
	@Column(name = "numTelefone")
	private String numTelefone;
	
	@Size(max = 32)
	@Column(name = "nmSenha")
	private String nmSenha;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "tpFuncao")
	private String tpFuncao;
	
	@OneToMany(mappedBy = "idUsuario")
	private Collection<TbUsuarioxPerfil> tbUsuarioxPerfilCollection;
	
	@OneToMany(mappedBy = "idUsuario")
	private Collection<TbAviso> tbAvisoCollection;
	
	@OneToMany(mappedBy = "idUsuarioReceptor")
	private Collection<TbTramite> tbTramiteCollection;
	
	@OneToMany(mappedBy = "idUsuarioEmissor")
	private Collection<TbTramite> tbTramiteCollection1;
	
	@OneToMany(mappedBy = "idUsuarioAnalisador")
	private Collection<TbManifestacao> tbManifestacaoCollection;
	
	@OneToMany(mappedBy = "idUsuarioManifestante")
	private Collection<TbManifestacao> tbManifestacaoCollection1;
	
	@OneToMany(mappedBy = "idUsuarioCriador")
	private Collection<TbManifestacao> manifestacoesCriadas;
	
	@OneToMany(mappedBy = "idUsuario")
	private Collection<TbFiltroPersonalizado> tbFiltroPersonalizadoCollection;
	
	@JoinColumn(name = "idUnidade", referencedColumnName = "idUnidade")
	@ManyToOne
	private TbUnidade idUnidade;
	
	@OneToMany(mappedBy = "idUsuario")
	private Collection<TbComunicacaoExterna> tbComunicacaoExternaCollection;
	
	@OneToMany(mappedBy = "idUsuario")
	private Collection<TbLogOperacao> tbLogOperacaoCollection;
	
	@OneToMany(mappedBy = "idUsuarioEnviou")
	private Collection<TbEncaminhamento> tbEncaminhamentoCollection;

    public TbUsuario() {
    }

    public TbUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TbUsuario(Integer idUsuario, String nmUsuario, Integer stStatus, String eeEmail, String nmLogin, String numTelefone, String tpFuncao) {
        this.idUsuario = idUsuario;
        this.nmUsuario = nmUsuario;
        this.stStatus = stStatus;
        this.eeEmail = eeEmail;
        this.nmLogin = nmLogin;
        this.numTelefone = numTelefone;
        this.tpFuncao = tpFuncao;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public Integer getStStatus() {
        return stStatus;
    }

    public void setStStatus(Integer stStatus) {
        this.stStatus = stStatus;
    }

    public String getEeEmail() {
        return eeEmail;
    }

    public void setEeEmail(String eeEmail) {
        this.eeEmail = eeEmail;
    }

    public String getTpUsuario() {
        return tpUsuario;
    }

    public void setTpUsuario(String tpUsuario) {
        this.tpUsuario = tpUsuario;
    }

    public String getNmLogin() {
        return nmLogin;
    }

    public void setNmLogin(String nmLogin) {
        this.nmLogin = nmLogin;
    }

    public String getNumTelefone() {
        return numTelefone;
    }

    public void setNumTelefone(String numTelefone) {
        this.numTelefone = numTelefone;
    }

    public String getNmSenha() {
        return nmSenha;
    }

    public void setNmSenha(String nmSenha) {
        this.nmSenha = nmSenha;
    }

    public String getTpFuncao() {
        return tpFuncao;
    }

    public void setTpFuncao(String tpFuncao) {
        this.tpFuncao = tpFuncao;
    }

    public Collection<TbUsuarioxPerfil> getTbUsuarioxPerfilCollection() {
        return tbUsuarioxPerfilCollection;
    }

    public void setTbUsuarioxPerfilCollection(Collection<TbUsuarioxPerfil> tbUsuarioxPerfilCollection) {
        this.tbUsuarioxPerfilCollection = tbUsuarioxPerfilCollection;
    }

    public Collection<TbAviso> getTbAvisoCollection() {
        return tbAvisoCollection;
    }

    public void setTbAvisoCollection(Collection<TbAviso> tbAvisoCollection) {
        this.tbAvisoCollection = tbAvisoCollection;
    }

    public Collection<TbTramite> getTbTramiteCollection() {
        return tbTramiteCollection;
    }

    public void setTbTramiteCollection(Collection<TbTramite> tbTramiteCollection) {
        this.tbTramiteCollection = tbTramiteCollection;
    }

    public Collection<TbTramite> getTbTramiteCollection1() {
        return tbTramiteCollection1;
    }

    public void setTbTramiteCollection1(Collection<TbTramite> tbTramiteCollection1) {
        this.tbTramiteCollection1 = tbTramiteCollection1;
    }

    public Collection<TbManifestacao> getTbManifestacaoCollection() {
        return tbManifestacaoCollection;
    }

    public void setTbManifestacaoCollection(Collection<TbManifestacao> tbManifestacaoCollection) {
        this.tbManifestacaoCollection = tbManifestacaoCollection;
    }

    public Collection<TbManifestacao> getTbManifestacaoCollection1() {
        return tbManifestacaoCollection1;
    }

    public void setTbManifestacaoCollection1(Collection<TbManifestacao> tbManifestacaoCollection1) {
        this.tbManifestacaoCollection1 = tbManifestacaoCollection1;
    }

    public Collection<TbFiltroPersonalizado> getTbFiltroPersonalizadoCollection() {
        return tbFiltroPersonalizadoCollection;
    }

    public void setTbFiltroPersonalizadoCollection(Collection<TbFiltroPersonalizado> tbFiltroPersonalizadoCollection) {
        this.tbFiltroPersonalizadoCollection = tbFiltroPersonalizadoCollection;
    }
    
    public Collection<TbManifestacao> getManifestacoesCriadas() {
		return manifestacoesCriadas;
	}

	public void setManifestacoesCriadas(
			Collection<TbManifestacao> manifestacoesCriadas) {
		this.manifestacoesCriadas = manifestacoesCriadas;
	}

	public TbUnidade getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(TbUnidade idUnidade) {
        this.idUnidade = idUnidade;
    }

    public Collection<TbComunicacaoExterna> getTbComunicacaoExternaCollection() {
        return tbComunicacaoExternaCollection;
    }

    public void setTbComunicacaoExternaCollection(Collection<TbComunicacaoExterna> tbComunicacaoExternaCollection) {
        this.tbComunicacaoExternaCollection = tbComunicacaoExternaCollection;
    }

    public Collection<TbLogOperacao> getTbLogOperacaoCollection() {
        return tbLogOperacaoCollection;
    }

    public void setTbLogOperacaoCollection(Collection<TbLogOperacao> tbLogOperacaoCollection) {
        this.tbLogOperacaoCollection = tbLogOperacaoCollection;
    }

    public Collection<TbEncaminhamento> getTbEncaminhamentoCollection() {
        return tbEncaminhamentoCollection;
    }

    public void setTbEncaminhamentoCollection(Collection<TbEncaminhamento> tbEncaminhamentoCollection) {
        this.tbEncaminhamentoCollection = tbEncaminhamentoCollection;
    }

    public boolean isAtivoOuNovaSenha() {
		return getStStatus() == StatusUsuarioEnum.ATIVO.getId() || getStStatus() == StatusUsuarioEnum.NOVA_SENHA.getId();
    }
    
    public boolean isInativo() {
    	return getStStatus() == StatusUsuarioEnum.INATIVO.getId();
    }

    public void setAtivo(boolean ativo) {
        setStStatus(false == ativo ? StatusUsuarioEnum.INATIVO.getId() : StatusUsuarioEnum.ATIVO.getId());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbUsuario)) {
            return false;
        }
        TbUsuario other = (TbUsuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Usuário";
    }

    private String getDescricao() {
        return nmUsuario + " " + eeEmail;
    }

	@Override
	public int compareTo(TbUsuario o) {
		return getNmUsuario().compareToIgnoreCase(o.getNmUsuario());
	}
}
