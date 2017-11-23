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

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbEncaminhamento")
@NamedQueries({
    @NamedQuery(name = "TbEncaminhamento.findAll", query = "SELECT t FROM TbEncaminhamento t"),
    @NamedQuery(name = "TbEncaminhamento.findByIdEncaminhamento", query = "SELECT t FROM TbEncaminhamento t WHERE t.idEncaminhamento = :idEncaminhamento"),
    @NamedQuery(name = "TbEncaminhamento.findByDtEnvioTramite", query = "SELECT t FROM TbEncaminhamento t WHERE t.dtEnvioTramite = :dtEnvioTramite"),
    @NamedQuery(name = "TbEncaminhamento.findByDtRespostaTramite", query = "SELECT t FROM TbEncaminhamento t WHERE t.dtRespostaTramite = :dtRespostaTramite"),
    @NamedQuery(name = "TbEncaminhamento.findByStEncaminhamento", query = "SELECT t FROM TbEncaminhamento t WHERE t.stEncaminhamento = :stEncaminhamento"),
    @NamedQuery(name = "TbEncaminhamento.findByDtCriacaoEncaminhamento", query = "SELECT t FROM TbEncaminhamento t WHERE t.dtCriacaoEncaminhamento = :dtCriacaoEncaminhamento")})
public class TbEncaminhamento implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEncaminhamento")
    private Integer idEncaminhamento;
    
    @Column(name = "dtEnvioTramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEnvioTramite;
    
    @Column(name = "dtRespostaTramite")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtRespostaTramite;
    
    @Column(name = "dsDescricao")
    private String dsDescricao;
    
    @Column(name = "stEncaminhamento")
    private String stEncaminhamento;
    
    @Column(name = "dtCriacaoEncaminhamento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacaoEncaminhamento;
    
    @OneToMany(mappedBy = "idEncaminhamento")
    private Collection<TbTramite> tbTramiteCollection = new ArrayList<>();
    
    @OneToMany(mappedBy = "idEncaminhamento", cascade=CascadeType.MERGE)
    private Collection<TbEncaminhamentoxAnexo> tbEncaminhamentoxAnexoCollection = new ArrayList<>();
    
    @JoinColumn(name = "idUsuarioEnviou", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuarioEnviou;
    
    @JoinColumn(name = "idUnidadeRecebeu", referencedColumnName = "idUnidade")
    @ManyToOne(optional = false)
    private TbUnidade idUnidadeRecebeu;
    
    @JoinColumn(name = "idUnidadeEnviou", referencedColumnName = "idUnidade")
    @ManyToOne(optional = false)
    private TbUnidade idUnidadeEnviou;
    
    @JoinColumn(name = "idManifestacao", referencedColumnName = "idManifestacao")
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private TbManifestacao idManifestacao;

    public TbEncaminhamento() {
    }

    public TbEncaminhamento(Integer idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public Integer getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(Integer idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public Date getDtEnvioTramite() {
        return dtEnvioTramite;
    }

    public void setDtEnvioTramite(Date dtEnvioTramite) {
        this.dtEnvioTramite = dtEnvioTramite;
    }

    public Date getDtRespostaTramite() {
        return dtRespostaTramite;
    }

    public void setDtRespostaTramite(Date dtRespostaTramite) {
        this.dtRespostaTramite = dtRespostaTramite;
    }

    public String getDsDescricao() {
        return dsDescricao;
    }

    public void setDsDescricao(String dsDescricao) {
        this.dsDescricao = dsDescricao;
    }

    public String getStEncaminhamento() {
        return stEncaminhamento;
    }

    public void setStEncaminhamento(String stEncaminhamento) {
        this.stEncaminhamento = stEncaminhamento;
    }

    public Date getDtCriacaoEncaminhamento() {
        return dtCriacaoEncaminhamento;
    }

    public void setDtCriacaoEncaminhamento(Date dtCriacaoEncaminhamento) {
        this.dtCriacaoEncaminhamento = dtCriacaoEncaminhamento;
    }

    public Collection<TbTramite> getTbTramiteCollection() {
        return tbTramiteCollection;
    }

    public void setTbTramiteCollection(Collection<TbTramite> tbTramiteCollection) {
        this.tbTramiteCollection = tbTramiteCollection;
    }

    public Collection<TbEncaminhamentoxAnexo> getTbEncaminhamentoxAnexoCollection() {
        return tbEncaminhamentoxAnexoCollection;
    }

    public void setTbEncaminhamentoxAnexoCollection(Collection<TbEncaminhamentoxAnexo> tbEncaminhamentoxAnexoCollection) {
        this.tbEncaminhamentoxAnexoCollection = tbEncaminhamentoxAnexoCollection;
    }

    public TbUsuario getIdUsuarioEnviou() {
        return idUsuarioEnviou;
    }

    public void setIdUsuarioEnviou(TbUsuario idUsuarioEnviou) {
        this.idUsuarioEnviou = idUsuarioEnviou;
    }

    public TbUnidade getIdUnidadeRecebeu() {
        return idUnidadeRecebeu;
    }

    public void setIdUnidadeRecebeu(TbUnidade idUnidadeRecebeu) {
        this.idUnidadeRecebeu = idUnidadeRecebeu;
    }

    public TbManifestacao getIdManifestacao() {
        return idManifestacao;
    }

	public void setIdManifestacao(TbManifestacao idManifestacao) {
		this.idManifestacao = idManifestacao;
	}

	public TbUnidade getIdUnidadeEnviou() {
		return idUnidadeEnviou;
	}

	public void setIdUnidadeEnviou(TbUnidade idUnidadeEnviou) {
		this.idUnidadeEnviou = idUnidadeEnviou;
	}
	
	public boolean temTramitePublico() {
		for (TbTramite tramite : tbTramiteCollection) {
			if(tramite.isTramitePublico()) {
				return true;
			}
		}
		return false;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idEncaminhamento != null ? idEncaminhamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbEncaminhamento)) {
            return false;
        }
        TbEncaminhamento other = (TbEncaminhamento) object;
        if ((this.idEncaminhamento == null && other.idEncaminhamento != null) || (this.idEncaminhamento != null && !this.idEncaminhamento.equals(other.idEncaminhamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Encaminhamento";
    }

    private String getDescricao() {
        return dsDescricao;
    }
}
