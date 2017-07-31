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
 * @author marcos.ribeiro
 */
@Entity
@Table(name = "tbEmailAutomatizado")
@NamedQueries({
    @NamedQuery(name = "TbEmailAutomatizado.findAll", query = "SELECT t FROM TbEmailAutomatizado t"),
    @NamedQuery(name = "TbEmailAutomatizado.findByIdEmailAutomatizado", query = "SELECT t FROM TbEmailAutomatizado t WHERE t.idEmailAutomatizado = :idEmailAutomatizado"),
    @NamedQuery(name = "TbEmailAutomatizado.findByTpEmail", query = "SELECT t FROM TbEmailAutomatizado t WHERE t.tpEmail = :tpEmail"),
    @NamedQuery(name = "TbEmailAutomatizado.findByNmTituloEmail", query = "SELECT t FROM TbEmailAutomatizado t WHERE t.nmTituloEmail = :nmTituloEmail")})
public class TbEmailAutomatizado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEmailAutomatizado", nullable = false)
    private Integer idEmailAutomatizado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tpEmail", nullable = false)
    private String tpEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nmTituloEmail", nullable = false, length = 200)
    private String nmTituloEmail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dsEmail", nullable = false)
    private String dsEmail;

    public TbEmailAutomatizado() {
    }

    public TbEmailAutomatizado(Integer idEmailAutomatizado) {
        this.idEmailAutomatizado = idEmailAutomatizado;
    }

    public TbEmailAutomatizado(Integer idEmailAutomatizado, String tpEmail, String nmTituloEmail, String dsEmail) {
        this.idEmailAutomatizado = idEmailAutomatizado;
        this.tpEmail = tpEmail;
        this.nmTituloEmail = nmTituloEmail;
        this.dsEmail = dsEmail;
    }

    public Integer getIdEmailAutomatizado() {
        return idEmailAutomatizado;
    }

    public void setIdEmailAutomatizado(Integer idEmailAutomatizado) {
        this.idEmailAutomatizado = idEmailAutomatizado;
    }

    public String getTpEmail() {
        return tpEmail;
    }

    public void setTpEmail(String tpEmail) {
        this.tpEmail = tpEmail;
    }
    
    public void setTpEmail(Integer tpEmail) {
    	this.tpEmail = String.valueOf(tpEmail);
    }

    public String getNmTituloEmail() {
        return nmTituloEmail;
    }

    public void setNmTituloEmail(String nmTituloEmail) {
        this.nmTituloEmail = nmTituloEmail;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmailAutomatizado != null ? idEmailAutomatizado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbEmailAutomatizado)) {
            return false;
        }
        TbEmailAutomatizado other = (TbEmailAutomatizado) object;
        if ((this.idEmailAutomatizado == null && other.idEmailAutomatizado != null) || (this.idEmailAutomatizado != null && !this.idEmailAutomatizado.equals(other.idEmailAutomatizado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Email Automatizado";
    }

    private String getDescricao() {
        return dsEmail;
    }
}
