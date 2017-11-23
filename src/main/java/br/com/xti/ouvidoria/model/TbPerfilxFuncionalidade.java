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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbPerfilxFuncionalidade")
@NamedQueries({
    @NamedQuery(name = "TbPerfilxFuncionalidade.findAll", query = "SELECT t FROM TbPerfilxFuncionalidade t"),
    @NamedQuery(name = "TbPerfilxFuncionalidade.findByIdPerfilxFuncionalidade", query = "SELECT t FROM TbPerfilxFuncionalidade t WHERE t.idPerfilxFuncionalidade = :idPerfilxFuncionalidade")})
public class TbPerfilxFuncionalidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPerfilxFuncionalidade")
    private Integer idPerfilxFuncionalidade;
    @JoinColumn(name = "idPerfil", referencedColumnName = "idPerfil")
    @ManyToOne(optional = false)
    private TbPerfil idPerfil;
    @JoinColumn(name = "idFuncionalidade", referencedColumnName = "idFuncionalidade")
    @ManyToOne(optional = false)
    private TbFuncionalidade idFuncionalidade;

    public TbPerfilxFuncionalidade() {
    }

    public TbPerfilxFuncionalidade(Integer idPerfilxFuncionalidade) {
        this.idPerfilxFuncionalidade = idPerfilxFuncionalidade;
    }

    public Integer getIdPerfilxFuncionalidade() {
        return idPerfilxFuncionalidade;
    }

    public void setIdPerfilxFuncionalidade(Integer idPerfilxFuncionalidade) {
        this.idPerfilxFuncionalidade = idPerfilxFuncionalidade;
    }

    public TbPerfil getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(TbPerfil idPerfil) {
        this.idPerfil = idPerfil;
    }

    public TbFuncionalidade getIdFuncionalidade() {
        return idFuncionalidade;
    }

    public void setIdFuncionalidade(TbFuncionalidade idFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfilxFuncionalidade != null ? idPerfilxFuncionalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPerfilxFuncionalidade)) {
            return false;
        }
        TbPerfilxFuncionalidade other = (TbPerfilxFuncionalidade) object;
        if ((this.idPerfilxFuncionalidade == null && other.idPerfilxFuncionalidade != null) || (this.idPerfilxFuncionalidade != null && !this.idPerfilxFuncionalidade.equals(other.idPerfilxFuncionalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Perfil x Funcionalidade";
    }

    private String getDescricao() {
        return "Perfil: " + getIdPerfil().getNmPerfil() + "\tFuncionalidade: " + getIdFuncionalidade().getDsFuncionalidade();
    }
    
}
