/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbPerfil")
@NamedQueries({
    @NamedQuery(name = "TbPerfil.findAll", query = "SELECT t FROM TbPerfil t"),
    @NamedQuery(name = "TbPerfil.findByIdPerfil", query = "SELECT t FROM TbPerfil t WHERE t.idPerfil = :idPerfil"),
    @NamedQuery(name = "TbPerfil.findByTpPerfil", query = "SELECT t FROM TbPerfil t WHERE t.tpPerfil = :tpPerfil"),
    @NamedQuery(name = "TbPerfil.findByNmPerfil", query = "SELECT t FROM TbPerfil t WHERE t.nmPerfil = :nmPerfil")})
public class TbPerfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPerfil")
    private Integer idPerfil;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tpPerfil")
    private String tpPerfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nmPerfil")
    private String nmPerfil;
    @OneToMany(mappedBy = "idPerfil")
    private Collection<TbUsuarioxPerfil> tbUsuarioxPerfilCollection = new ArrayList<>();
    @OneToMany(mappedBy = "idPerfil")
    private Collection<TbPerfilxFuncionalidade> tbPerfilxFuncionalidadeCollection = new ArrayList<>();

    public TbPerfil() {
    }

    public TbPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public TbPerfil(Integer idPerfil, String tpPerfil, String nmPerfil) {
        this.idPerfil = idPerfil;
        this.tpPerfil = tpPerfil;
        this.nmPerfil = nmPerfil;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getTpPerfil() {
        return tpPerfil;
    }

    public void setTpPerfil(String tpPerfil) {
        this.tpPerfil = tpPerfil;
    }

    public String getNmPerfil() {
        return nmPerfil;
    }

    public void setNmPerfil(String nmPerfil) {
        this.nmPerfil = nmPerfil;
    }
    
    public Collection<TbUsuarioxPerfil> getTbUsuarioxPerfilCollection() {
        return tbUsuarioxPerfilCollection;
    }

    public void setTbUsuarioxPerfilCollection(Collection<TbUsuarioxPerfil> tbUsuarioxPerfilCollection) {
        this.tbUsuarioxPerfilCollection = tbUsuarioxPerfilCollection;
    }

    public Collection<TbPerfilxFuncionalidade> getTbPerfilxFuncionalidadeCollection() {
        return tbPerfilxFuncionalidadeCollection;
    }

    public void setTbPerfilxFuncionalidadeCollection(Collection<TbPerfilxFuncionalidade> tbPerfilxFuncionalidadeCollection) {
        this.tbPerfilxFuncionalidadeCollection = tbPerfilxFuncionalidadeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerfil != null ? idPerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPerfil)) {
            return false;
        }
        TbPerfil other = (TbPerfil) object;
        if ((this.idPerfil == null && other.idPerfil != null) || (this.idPerfil != null && !this.idPerfil.equals(other.idPerfil))) {
            return false;
        }
        return true;
    }

      
   @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Perfil";
    }

    private String getDescricao() {
        return nmPerfil;
    }
    
}
