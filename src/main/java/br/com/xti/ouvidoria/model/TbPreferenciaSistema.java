/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbPreferenciaSistema")
@NamedQueries({
    @NamedQuery(name = "TbPreferenciaSistema.findAll", query = "SELECT t FROM TbPreferenciaSistema t"),
    @NamedQuery(name = "TbPreferenciaSistema.findByIdPreferenciaSistema", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.idPreferenciaSistema = :idPreferenciaSistema"),
    @NamedQuery(name = "TbPreferenciaSistema.findByNomeOuvidoria", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.nomeOuvidoria = :nomeOuvidoria"),
    @NamedQuery(name = "TbPreferenciaSistema.findByEmailOuvidoria", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.emailOuvidoria = :emailOuvidoria"),
    @NamedQuery(name = "TbPreferenciaSistema.findByHostEmail", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.hostEmail = :hostEmail"),
    @NamedQuery(name = "TbPreferenciaSistema.findByPortaEmail", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.portaEmail = :portaEmail"),
    @NamedQuery(name = "TbPreferenciaSistema.findByUsuarioEmail", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.usuarioEmail = :usuarioEmail"),
    @NamedQuery(name = "TbPreferenciaSistema.findBySenhaEmail", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.senhaEmail = :senhaEmail"),
    @NamedQuery(name = "TbPreferenciaSistema.findBySslEmail", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.sslEmail = :sslEmail"),
    @NamedQuery(name = "TbPreferenciaSistema.findByEncerrarTramiteEncaminhada", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.encerrarTramiteEncaminhada = :encerrarTramiteEncaminhada"),
    @NamedQuery(name = "TbPreferenciaSistema.findByRetornarTramiteOuvidoria", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.retornarTramiteOuvidoria = :retornarTramiteOuvidoria"),
    @NamedQuery(name = "TbPreferenciaSistema.findByCtlPrazoManifSoluc", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.ctlPrazoManifSoluc = :ctlPrazoManifSoluc"),
    @NamedQuery(name = "TbPreferenciaSistema.findByRespostasImediatas", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.respostasImediatas = :respostasImediatas"),
    @NamedQuery(name = "TbPreferenciaSistema.findByPrazoEntrada", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.prazoEntrada = :prazoEntrada"),
    @NamedQuery(name = "TbPreferenciaSistema.findByPrazoAreaSolucionadora", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.prazoAreaSolucionadora = :prazoAreaSolucionadora"),
    @NamedQuery(name = "TbPreferenciaSistema.findByPrazoRespostaCidadao", query = "SELECT t FROM TbPreferenciaSistema t WHERE t.prazoRespostaCidadao = :prazoRespostaCidadao")})
public class TbPreferenciaSistema implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPreferenciaSistema")
    private Integer idPreferenciaSistema;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nomeOuvidoria")
    private String nomeOuvidoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "emailOuvidoria")
    private String emailOuvidoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "hostEmail")
    private String hostEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "portaEmail")
    private int portaEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usuarioEmail")
    private String usuarioEmail;
    @Size(min = 0, max = 50)
    @Column(name = "senhaEmail")
    private String senhaEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sslEmail")
    private String sslEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "encerrarTramiteEncaminhada")
    private String encerrarTramiteEncaminhada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "retornarTramiteOuvidoria")
    private String retornarTramiteOuvidoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ctlPrazoManifSoluc")
    private String ctlPrazoManifSoluc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RespostasImediatas")
    private String respostasImediatas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoEntrada")
    private int prazoEntrada;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoAreaSolucionadora")
    private int prazoAreaSolucionadora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prazoRespostaCidadao")
    private int prazoRespostaCidadao;

    public TbPreferenciaSistema() {
    }

    public TbPreferenciaSistema(Integer idPreferenciaSistema) {
        this.idPreferenciaSistema = idPreferenciaSistema;
    }

    public TbPreferenciaSistema(Integer idPreferenciaSistema, String nomeOuvidoria, String emailOuvidoria, String hostEmail, int portaEmail, String usuarioEmail, String senhaEmail, String sslEmail, String encerrarTramiteEncaminhada, String retornarTramiteOuvidoria, String ctlPrazoManifSoluc, String respostasImediatas, int prazoEntrada, int prazoAreaSolucionadora, int prazoRespostaCidadao) {
        this.idPreferenciaSistema = idPreferenciaSistema;
        this.nomeOuvidoria = nomeOuvidoria;
        this.emailOuvidoria = emailOuvidoria;
        this.hostEmail = hostEmail;
        this.portaEmail = portaEmail;
        this.usuarioEmail = usuarioEmail;
        this.senhaEmail = senhaEmail;
        this.sslEmail = sslEmail;
        this.encerrarTramiteEncaminhada = encerrarTramiteEncaminhada;
        this.retornarTramiteOuvidoria = retornarTramiteOuvidoria;
        this.ctlPrazoManifSoluc = ctlPrazoManifSoluc;
        this.respostasImediatas = respostasImediatas;
        this.prazoEntrada = prazoEntrada;
        this.prazoAreaSolucionadora = prazoAreaSolucionadora;
        this.prazoRespostaCidadao = prazoRespostaCidadao;
    }

    public Integer getIdPreferenciaSistema() {
        return idPreferenciaSistema;
    }

    public void setIdPreferenciaSistema(Integer idPreferenciaSistema) {
        this.idPreferenciaSistema = idPreferenciaSistema;
    }

    public String getNomeOuvidoria() {
        return nomeOuvidoria;
    }

    public void setNomeOuvidoria(String nomeOuvidoria) {
        this.nomeOuvidoria = nomeOuvidoria;
    }

    public String getEmailOuvidoria() {
        return emailOuvidoria;
    }

    public void setEmailOuvidoria(String emailOuvidoria) {
        this.emailOuvidoria = emailOuvidoria;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public int getPortaEmail() {
        return portaEmail;
    }

    public void setPortaEmail(int portaEmail) {
        this.portaEmail = portaEmail;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
    }

    public String getSenhaEmail() {
        return senhaEmail;
    }

    public void setSenhaEmail(String senhaEmail) {
        this.senhaEmail = senhaEmail;
    }

    public String getSslEmail() {
        return sslEmail;
    }

    public void setSslEmail(String sslEmail) {
        this.sslEmail = sslEmail;
    }

    public String getEncerrarTramiteEncaminhada() {
        return encerrarTramiteEncaminhada;
    }

    public void setEncerrarTramiteEncaminhada(String encerrarTramiteEncaminhada) {
        this.encerrarTramiteEncaminhada = encerrarTramiteEncaminhada;
    }

    public String getRetornarTramiteOuvidoria() {
        return retornarTramiteOuvidoria;
    }

    public void setRetornarTramiteOuvidoria(String retornarTramiteOuvidoria) {
        this.retornarTramiteOuvidoria = retornarTramiteOuvidoria;
    }

    public String getCtlPrazoManifSoluc() {
        return ctlPrazoManifSoluc;
    }

    public void setCtlPrazoManifSoluc(String ctlPrazoManifSoluc) {
        this.ctlPrazoManifSoluc = ctlPrazoManifSoluc;
    }

    public String getRespostasImediatas() {
        return respostasImediatas;
    }

    public void setRespostasImediatas(String respostasImediatas) {
        this.respostasImediatas = respostasImediatas;
    }

    public int getPrazoEntrada() {
        return prazoEntrada;
    }

    public void setPrazoEntrada(int prazoEntrada) {
        this.prazoEntrada = prazoEntrada;
    }

    public int getPrazoAreaSolucionadora() {
        return prazoAreaSolucionadora;
    }

    public void setPrazoAreaSolucionadora(int prazoAreaSolucionadora) {
        this.prazoAreaSolucionadora = prazoAreaSolucionadora;
    }

    public int getPrazoRespostaCidadao() {
        return prazoRespostaCidadao;
    }

    public void setPrazoRespostaCidadao(int prazoRespostaCidadao) {
        this.prazoRespostaCidadao = prazoRespostaCidadao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPreferenciaSistema != null ? idPreferenciaSistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPreferenciaSistema)) {
            return false;
        }
        TbPreferenciaSistema other = (TbPreferenciaSistema) object;
        if ((this.idPreferenciaSistema == null && other.idPreferenciaSistema != null) || (this.idPreferenciaSistema != null && !this.idPreferenciaSistema.equals(other.idPreferenciaSistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Preferências do Sistema";
    }
    
}
