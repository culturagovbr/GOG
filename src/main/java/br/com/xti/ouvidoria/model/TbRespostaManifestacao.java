/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbRespostaManifestacao")
@NamedQueries({
    @NamedQuery(name = "TbRespostaManifestacao.findAll", query = "SELECT t FROM TbRespostaManifestacao t"),
    @NamedQuery(name = "TbRespostaManifestacao.findByIdRespostaManifestacao", query = "SELECT t FROM TbRespostaManifestacao t WHERE t.idRespostaManifestacao = :idRespostaManifestacao"),
    @NamedQuery(name = "TbRespostaManifestacao.findByDsTituloResposta", query = "SELECT t FROM TbRespostaManifestacao t WHERE t.dsTituloResposta = :dsTituloResposta"),
    @NamedQuery(name = "TbRespostaManifestacao.findByDsResposta", query = "SELECT t FROM TbRespostaManifestacao t WHERE t.dsResposta = :dsResposta"),
    @NamedQuery(name = "TbRespostaManifestacao.findByDtCadastroRespostaManifestação", query = "SELECT t FROM TbRespostaManifestacao t WHERE t.dtCadastroRespostaManifestacao = :dtCadastroRespostaManifestacao")})
public class TbRespostaManifestacao implements Serializable, Comparable<TbRespostaManifestacao> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRespostaManifestacao")
    private Integer idRespostaManifestacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "dsTituloResposta")
    private String dsTituloResposta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dsResposta")
    private String dsResposta;
    @Column(name = "dtCadastroRespostaManifestacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCadastroRespostaManifestacao;

    public TbRespostaManifestacao() {
    }

    public TbRespostaManifestacao(Integer idRespostaManifestacao) {
        this.idRespostaManifestacao = idRespostaManifestacao;
    }

    public TbRespostaManifestacao(Integer idRespostaManifestacao, String dsTituloResposta, String dsResposta) {
        this.idRespostaManifestacao = idRespostaManifestacao;
        this.dsTituloResposta = dsTituloResposta;
        this.dsResposta = dsResposta;
    }

    public Integer getIdRespostaManifestacao() {
        return idRespostaManifestacao;
    }

    public void setIdRespostaManifestacao(Integer idRespostaManifestacao) {
        this.idRespostaManifestacao = idRespostaManifestacao;
    }

    public String getDsTituloResposta() {
        return dsTituloResposta;
    }

    public void setDsTituloResposta(String dsTituloResposta) {
        this.dsTituloResposta = dsTituloResposta;
    }

    public String getDsResposta() {
        return dsResposta;
    }

    public void setDsResposta(String dsResposta) {
        this.dsResposta = dsResposta;
    }

    public Date getDtCadastroRespostaManifestacao() {
        return dtCadastroRespostaManifestacao;
    }

    public void setDtCadastroRespostaManifestacao(Date dtCadastroRespostaManifestacao) {
        this.dtCadastroRespostaManifestacao = dtCadastroRespostaManifestacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRespostaManifestacao != null ? idRespostaManifestacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbRespostaManifestacao)) {
            return false;
        }
        TbRespostaManifestacao other = (TbRespostaManifestacao) object;
        if ((this.idRespostaManifestacao == null && other.idRespostaManifestacao != null) || (this.idRespostaManifestacao != null && !this.idRespostaManifestacao.equals(other.idRespostaManifestacao))) {
            return false;
        }
        return true;
    }

     @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Resposta Manifestação";
    }

    private String getDescricao() {
        return dsTituloResposta;
    }

	@Override
	public int compareTo(TbRespostaManifestacao other) {
		return getDsResposta().compareToIgnoreCase(other.getDescricao());
	}
    
    
}
