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

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbUsuarioxPerfil")
@NamedQueries({
    @NamedQuery(name = "TbUsuarioxPerfil.findAll", query = "SELECT t FROM TbUsuarioxPerfil t"),
    @NamedQuery(name = "TbUsuarioxPerfil.findByIdUsuarioPerfil", query = "SELECT t FROM TbUsuarioxPerfil t WHERE t.idUsuarioPerfil = :idUsuarioPerfil"),
    @NamedQuery(name = "TbUsuarioxPerfil.findByDtAtivacao", query = "SELECT t FROM TbUsuarioxPerfil t WHERE t.dtAtivacao = :dtAtivacao"),
    @NamedQuery(name = "TbUsuarioxPerfil.findByDtDesativacao", query = "SELECT t FROM TbUsuarioxPerfil t WHERE t.dtDesativacao = :dtDesativacao")})
public class TbUsuarioxPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuarioPerfil")
    private Integer idUsuarioPerfil;
    @Column(name = "dtAtivacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAtivacao;
    @Column(name = "dtDesativacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtDesativacao;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne
    private TbUsuario idUsuario;
    @JoinColumn(name = "idPerfil", referencedColumnName = "idPerfil")
    @ManyToOne
    private TbPerfil idPerfil;

    public TbUsuarioxPerfil() {
    }

    public TbUsuarioxPerfil(Integer idUsuarioPerfil) {
        this.idUsuarioPerfil = idUsuarioPerfil;
    }

    public Integer getIdUsuarioPerfil() {
        return idUsuarioPerfil;
    }

    public void setIdUsuarioPerfil(Integer idUsuarioPerfil) {
        this.idUsuarioPerfil = idUsuarioPerfil;
    }

    public Date getDtAtivacao() {
        return dtAtivacao;
    }

    public void setDtAtivacao(Date dtAtivacao) {
        this.dtAtivacao = dtAtivacao;
    }

    public Date getDtDesativacao() {
        return dtDesativacao;
    }

    public void setDtDesativacao(Date dtDesativacao) {
        this.dtDesativacao = dtDesativacao;
    }

    public TbUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(TbUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TbPerfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(TbPerfil idPerfil) {
        this.idPerfil = idPerfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuarioPerfil != null ? idUsuarioPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbUsuarioxPerfil)) {
            return false;
        }
        TbUsuarioxPerfil other = (TbUsuarioxPerfil) object;
        if ((this.idUsuarioPerfil == null && other.idUsuarioPerfil != null) || (this.idUsuarioPerfil != null && !this.idUsuarioPerfil.equals(other.idUsuarioPerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Usuário x Perfil";
    }

    private String getDescricao() {
        return "Usuário: " + getIdUsuario().getNmUsuario() + "\tPerfil: " + getIdPerfil().getNmPerfil();
    }
}
