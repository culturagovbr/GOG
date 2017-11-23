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
@Table(name = "tbTramitexAnexo")
@NamedQueries({
    @NamedQuery(name = "TbTramitexAnexo.findAll", query = "SELECT t FROM TbTramitexAnexo t"),
    @NamedQuery(name = "TbTramitexAnexo.findByIdTramitexAnexo", query = "SELECT t FROM TbTramitexAnexo t WHERE t.idTramitexAnexo = :idTramitexAnexo")})
public class TbTramitexAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTramitexAnexo")
    private Integer idTramitexAnexo;
    @JoinColumn(name = "idTramite", referencedColumnName = "idTramite")
    @ManyToOne(optional = false)
    private TbTramite idTramite;
    @JoinColumn(name = "idAnexo", referencedColumnName = "idAnexo")
    @ManyToOne(optional = false)
    private TbAnexo idAnexo;

    public TbTramitexAnexo() {
    }

    public TbTramitexAnexo(Integer idTramitexAnexo) {
        this.idTramitexAnexo = idTramitexAnexo;
    }

    public Integer getIdTramitexAnexo() {
        return idTramitexAnexo;
    }

    public void setIdTramitexAnexo(Integer idTramitexAnexo) {
        this.idTramitexAnexo = idTramitexAnexo;
    }

    public TbTramite getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(TbTramite idTramite) {
        this.idTramite = idTramite;
    }

    public TbAnexo getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(TbAnexo idAnexo) {
        this.idAnexo = idAnexo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramitexAnexo != null ? idTramitexAnexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbTramitexAnexo)) {
            return false;
        }
        TbTramitexAnexo other = (TbTramitexAnexo) object;
        if ((this.idTramitexAnexo == null && other.idTramitexAnexo != null) || (this.idTramitexAnexo != null && !this.idTramitexAnexo.equals(other.idTramitexAnexo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Trâmite x Anexo";
    }

    private String getDescricao() {
        return "Trâmite: " + getIdTramite().getIdTramite() + "\tAnexo: " + getIdAnexo().getNmAnexo();
    }
}
