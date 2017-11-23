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
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbParametro")
@NamedQueries({
    @NamedQuery(name = "TbParametro.findAll", query = "SELECT t FROM TbParametro t"),
    @NamedQuery(name = "TbParametro.findByIdParametro", query = "SELECT t FROM TbParametro t WHERE t.idParametro = :idParametro"),
    @NamedQuery(name = "TbParametro.findByNmParametro", query = "SELECT t FROM TbParametro t WHERE t.nmParametro = :nmParametro"),
    @NamedQuery(name = "TbParametro.findByVlrParametro", query = "SELECT t FROM TbParametro t WHERE t.vlrParametro = :vlrParametro")})
public class TbParametro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idParametro")
    private Integer idParametro;
    @Size(max = 250)
    @Column(name = "nmParametro")
    private String nmParametro;
    @Size(max = 250)
    @Column(name = "vlrParametro")
    private String vlrParametro;

    public TbParametro() {
    }

    public TbParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public Integer getIdParametro() {
        return idParametro;
    }

    public void setIdParametro(Integer idParametro) {
        this.idParametro = idParametro;
    }

    public String getNmParametro() {
        return nmParametro;
    }

    public void setNmParametro(String nmParametro) {
        this.nmParametro = nmParametro;
    }

    public String getVlrParametro() {
        return vlrParametro;
    }

    public void setVlrParametro(String vlrParametro) {
        this.vlrParametro = vlrParametro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParametro != null ? idParametro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbParametro)) {
            return false;
        }
        TbParametro other = (TbParametro) object;
        if ((this.idParametro == null && other.idParametro != null) || (this.idParametro != null && !this.idParametro.equals(other.idParametro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.TbParametro[ idParametro=" + idParametro + " ]";
    }
    
}
