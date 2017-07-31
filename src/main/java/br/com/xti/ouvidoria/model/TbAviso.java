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
@Table(name = "tbAviso")
@NamedQueries({
    @NamedQuery(name = "TbAviso.findAll", query = "SELECT t FROM TbAviso t"),
    @NamedQuery(name = "TbAviso.findByIdAvisos", query = "SELECT t FROM TbAviso t WHERE t.idAvisos = :idAvisos"),
    @NamedQuery(name = "TbAviso.findByDtInicioAviso", query = "SELECT t FROM TbAviso t WHERE t.dtInicioAviso = :dtInicioAviso"),
    @NamedQuery(name = "TbAviso.findByDtFimAviso", query = "SELECT t FROM TbAviso t WHERE t.dtFimAviso = :dtFimAviso"),
    @NamedQuery(name = "TbAviso.findByDsTitulo", query = "SELECT t FROM TbAviso t WHERE t.dsTitulo = :dsTitulo"),
    @NamedQuery(name = "TbAviso.findByDsConteudo", query = "SELECT t FROM TbAviso t WHERE t.dsConteudo = :dsConteudo")})
public class TbAviso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idAvisos")
    private Integer idAvisos;
    @Column(name = "dtInicioAviso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInicioAviso;
    @Column(name = "dtFimAviso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFimAviso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsTitulo")
    private String dsTitulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "dsConteudo")
    private String dsConteudo;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuario;

    public TbAviso() {
    }

    public TbAviso(Integer idAvisos) {
        this.idAvisos = idAvisos;
    }

    public TbAviso(Integer idAvisos, String dsTitulo, String dsConteudo) {
        this.idAvisos = idAvisos;
        this.dsTitulo = dsTitulo;
        this.dsConteudo = dsConteudo;
    }

    public Integer getIdAvisos() {
        return idAvisos;
    }

    public void setIdAvisos(Integer idAvisos) {
        this.idAvisos = idAvisos;
    }

    public Date getDtInicioAviso() {
        return dtInicioAviso;
    }

    public void setDtInicioAviso(Date dtInicioAviso) {
        this.dtInicioAviso = dtInicioAviso;
    }

    public Date getDtFimAviso() {
        return dtFimAviso;
    }

    public void setDtFimAviso(Date dtFimAviso) {
        this.dtFimAviso = dtFimAviso;
    }

    public String getDsTitulo() {
        return dsTitulo;
    }

    public void setDsTitulo(String dsTitulo) {
        this.dsTitulo = dsTitulo;
    }

    public String getDsConteudo() {
        return dsConteudo;
    }

    public void setDsConteudo(String dsConteudo) {
        this.dsConteudo = dsConteudo;
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
        hash += (idAvisos != null ? idAvisos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbAviso)) {
            return false;
        }
        TbAviso other = (TbAviso) object;
        if ((this.idAvisos == null && other.idAvisos != null) || (this.idAvisos != null && !this.idAvisos.equals(other.idAvisos))) {
            return false;
        }
        return true;
    }

   
       @Override
    public String toString() {
        return String.format("%s > %s",getEntidade(),getDescricao());
    }
    
    private String getEntidade(){
        return "Aviso";
    }
    private String getDescricao(){
        return dsTitulo;
    }  
    
}
