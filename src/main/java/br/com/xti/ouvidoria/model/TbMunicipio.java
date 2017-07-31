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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbMunicipio")
@NamedQueries({
    @NamedQuery(name = "TbMunicipio.findAll", query = "SELECT t FROM TbMunicipio t"),
    @NamedQuery(name = "TbMunicipio.findByIdMunicipio", query = "SELECT t FROM TbMunicipio t WHERE t.idMunicipio = :idMunicipio"),
    @NamedQuery(name = "TbMunicipio.findByNmMunicipio", query = "SELECT t FROM TbMunicipio t WHERE t.nmMunicipio = :nmMunicipio")})
public class TbMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idMunicipio")
    private Integer idMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nmMunicipio")
    private String nmMunicipio;
    @JoinColumn(name = "idUF", referencedColumnName = "idUF")
    @ManyToOne(optional = false)
    private TbUF idUF;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idMunicipio")*/
   /* @OneToMany(mappedBy = "idMunicipio")
    private Collection<TbManifestacao> tbManifestacaoCollection;*/

    public TbMunicipio() {
    }

    public TbMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public TbMunicipio(Integer idMunicipio, String nmMunicipio) {
        this.idMunicipio = idMunicipio;
        this.nmMunicipio = nmMunicipio;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNmMunicipio() {
        return nmMunicipio;
    }

    public void setNmMunicipio(String nmMunicipio) {
        this.nmMunicipio = nmMunicipio;
    }

    public TbUF getIdUF() {
        return idUF;
    }

    public void setIdUF(TbUF idUF) {
        this.idUF = idUF;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMunicipio != null ? idMunicipio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbMunicipio)) {
            return false;
        }
        TbMunicipio other = (TbMunicipio) object;
        if ((this.idMunicipio == null && other.idMunicipio != null) || (this.idMunicipio != null && !this.idMunicipio.equals(other.idMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.xti.ouvidoria.model.TbMunicipio[ idMunicipio=" + idMunicipio + " ]";
    }
    
}
