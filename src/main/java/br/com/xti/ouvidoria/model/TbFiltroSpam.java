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
@Table(name = "tbFiltroSpam")
@NamedQueries({
    @NamedQuery(name = "TbFiltroSpam.findAll", query = "SELECT t FROM TbFiltroSpam t"),
    @NamedQuery(name = "TbFiltroSpam.findByIdFiltrosSpam", query = "SELECT t FROM TbFiltroSpam t WHERE t.idFiltrosSpam = :idFiltrosSpam"),
    @NamedQuery(name = "TbFiltroSpam.findByTpFiltro", query = "SELECT t FROM TbFiltroSpam t WHERE t.tpFiltro = :tpFiltro"),
    @NamedQuery(name = "TbFiltroSpam.findByDsFiltro", query = "SELECT t FROM TbFiltroSpam t WHERE t.dsFiltro = :dsFiltro")})
public class TbFiltroSpam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFiltrosSpam")
    private Integer idFiltrosSpam;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tpFiltro")
    private String tpFiltro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "dsFiltro")
    private String dsFiltro;

    public TbFiltroSpam() {
    }

    public TbFiltroSpam(Integer idFiltrosSpam) {
        this.idFiltrosSpam = idFiltrosSpam;
    }

    public TbFiltroSpam(Integer idFiltrosSpam, String tpFiltro, String dsFiltro) {
        this.idFiltrosSpam = idFiltrosSpam;
        this.tpFiltro = tpFiltro;
        this.dsFiltro = dsFiltro;
    }

    public Integer getIdFiltrosSpam() {
        return idFiltrosSpam;
    }

    public void setIdFiltrosSpam(Integer idFiltrosSpam) {
        this.idFiltrosSpam = idFiltrosSpam;
    }

    public String getTpFiltro() {
        return tpFiltro;
    }

    public void setTpFiltro(String tpFiltro) {
        this.tpFiltro = tpFiltro;
    }

    public String getDsFiltro() {
        return dsFiltro;
    }

    public void setDsFiltro(String dsFiltro) {
        this.dsFiltro = dsFiltro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFiltrosSpam != null ? idFiltrosSpam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbFiltroSpam)) {
            return false;
        }
        TbFiltroSpam other = (TbFiltroSpam) object;
        if ((this.idFiltrosSpam == null && other.idFiltrosSpam != null) || (this.idFiltrosSpam != null && !this.idFiltrosSpam.equals(other.idFiltrosSpam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Filtro Spam";
    }

    private String getDescricao() {
        return dsFiltro;
    }
}
