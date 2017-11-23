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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tbLogOperacao")
@NamedQueries({
    @NamedQuery(name = "TbLogOperacao.findAll", query = "SELECT t FROM TbLogOperacao t"),
    @NamedQuery(name = "TbLogOperacao.findByIdLogOperacoes", query = "SELECT t FROM TbLogOperacao t WHERE t.idLogOperacoes = :idLogOperacoes"),
    @NamedQuery(name = "TbLogOperacao.findByDsOperacao", query = "SELECT t FROM TbLogOperacao t WHERE t.dsOperacao = :dsOperacao"),
    @NamedQuery(name = "TbLogOperacao.findByDtDataLog", query = "SELECT t FROM TbLogOperacao t WHERE t.dtDataLog = :dtDataLog"),
    @NamedQuery(name = "TbLogOperacao.findByTpOperacao", query = "SELECT t FROM TbLogOperacao t WHERE t.tpOperacao = :tpOperacao")})
public class TbLogOperacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLogOperacoes")
    private Integer idLogOperacoes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "dsOperacao")
    private String dsOperacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtDataLog")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtDataLog;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tpOperacao")
    private String tpOperacao;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuario;

    public TbLogOperacao() {
    }

    public TbLogOperacao(Integer idLogOperacoes) {
        this.idLogOperacoes = idLogOperacoes;
    }

    public TbLogOperacao(Integer idLogOperacoes, String dsOperacao, Date dtDataLog, String tpOperacao) {
        this.idLogOperacoes = idLogOperacoes;
        this.dsOperacao = dsOperacao;
        this.dtDataLog = dtDataLog;
        this.tpOperacao = tpOperacao;
    }

    public Integer getIdLogOperacoes() {
        return idLogOperacoes;
    }

    public void setIdLogOperacoes(Integer idLogOperacoes) {
        this.idLogOperacoes = idLogOperacoes;
    }

    public String getDsOperacao() {
        return dsOperacao;
    }

    public void setDsOperacao(String dsOperacao) {
        this.dsOperacao = dsOperacao;
    }

    public Date getDtDataLog() {
        return dtDataLog;
    }

    public void setDtDataLog(Date dtDataLog) {
        this.dtDataLog = dtDataLog;
    }

    public String getTpOperacao() {
        return tpOperacao;
    }

    public void setTpOperacao(String tpOperacao) {
        this.tpOperacao = tpOperacao;
    }

    public TbUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(TbUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLogOperacoes != null ? idLogOperacoes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbLogOperacao)) {
            return false;
        }
        TbLogOperacao other = (TbLogOperacao) object;
        if ((this.idLogOperacoes == null && other.idLogOperacoes != null) || (this.idLogOperacoes != null && !this.idLogOperacoes.equals(other.idLogOperacoes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.TbLogOperacao[ idLogOperacoes=" + idLogOperacoes + " ]";
    }
    
}
