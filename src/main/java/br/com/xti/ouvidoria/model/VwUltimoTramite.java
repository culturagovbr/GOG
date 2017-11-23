/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "vwUltimoTramite")
@NamedQueries({
    @NamedQuery(name = "VwUltimoTramite.findAll", query = "SELECT v FROM VwUltimoTramite v"),
    @NamedQuery(name = "VwUltimoTramite.findByIdManifestacao", query = "SELECT v FROM VwUltimoTramite v WHERE v.idManifestacao = :idManifestacao"),
    @NamedQuery(name = "VwUltimoTramite.findByIdEncaminhamento", query = "SELECT v FROM VwUltimoTramite v WHERE v.idEncaminhamento = :idEncaminhamento"),
    @NamedQuery(name = "VwUltimoTramite.findByIdTramite", query = "SELECT v FROM VwUltimoTramite v WHERE v.idTramite = :idTramite")})
public class VwUltimoTramite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idManifestacao")
    private int idManifestacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEncaminhamento")
    private int idEncaminhamento;
    
    @Column(name = "idTramite")
    private Integer idTramite;
    
    @Column(name = "idUsuarioReceptor")
    private Integer idUsuarioReceptor;

    public VwUltimoTramite() {
    }

    public VwUltimoTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public int getIdManifestacao() {
        return idManifestacao;
    }

    public void setIdManifestacao(int idManifestacao) {
        this.idManifestacao = idManifestacao;
    }

    public int getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(int idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
    }

    public Integer getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

    public Integer getIdUsuarioReceptor() {
        return idUsuarioReceptor;
    }

    public void setIdUsuarioReceptor(Integer idUsuarioReceptor) {
        this.idUsuarioReceptor = idUsuarioReceptor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramite != null ? idTramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VwUltimoTramite)) {
            return false;
        }
        VwUltimoTramite other = (VwUltimoTramite) object;
        if ((this.idTramite == null && other.idTramite != null) || (this.idTramite != null && !this.idTramite.equals(other.idTramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.VwUltimoTramite[ idTramite=" + idTramite + " ]";
    }
    
}
