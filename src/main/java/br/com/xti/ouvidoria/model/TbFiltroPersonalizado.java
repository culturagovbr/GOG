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
@Table(name = "tbFiltroPersonalizado")
@NamedQueries({
    @NamedQuery(name = "TbFiltroPersonalizado.findAll", query = "SELECT t FROM TbFiltroPersonalizado t"),
    @NamedQuery(name = "TbFiltroPersonalizado.findByIdFiltroPersonalizado", query = "SELECT t FROM TbFiltroPersonalizado t WHERE t.idFiltroPersonalizado = :idFiltroPersonalizado"),
    @NamedQuery(name = "TbFiltroPersonalizado.findByNmFiltroPersonalizado", query = "SELECT t FROM TbFiltroPersonalizado t WHERE t.nmFiltroPersonalizado = :nmFiltroPersonalizado"),
    @NamedQuery(name = "TbFiltroPersonalizado.findByDsParticao", query = "SELECT t FROM TbFiltroPersonalizado t WHERE t.dsParticao = :dsParticao")})
public class TbFiltroPersonalizado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFiltroPersonalizado")
    private Integer idFiltroPersonalizado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nmFiltroPersonalizado")
    private String nmFiltroPersonalizado;
    @Size(min= 1)
    @Column(name = "dsParticao")
    private String dsParticao;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private TbUsuario idUsuario;

    public TbFiltroPersonalizado() {
    }

    public TbFiltroPersonalizado(Integer idFiltroPersonalizado) {
        this.idFiltroPersonalizado = idFiltroPersonalizado;
    }

    public TbFiltroPersonalizado(Integer idFiltroPersonalizado, String nmFiltroPersonalizado) {
        this.idFiltroPersonalizado = idFiltroPersonalizado;
        this.nmFiltroPersonalizado = nmFiltroPersonalizado;
    }

    public Integer getIdFiltroPersonalizado() {
        return idFiltroPersonalizado;
    }

    public void setIdFiltroPersonalizado(Integer idFiltroPersonalizado) {
        this.idFiltroPersonalizado = idFiltroPersonalizado;
    }

    public String getNmFiltroPersonalizado() {
        return nmFiltroPersonalizado;
    }

    public void setNmFiltroPersonalizado(String nmFiltroPersonalizado) {
        this.nmFiltroPersonalizado = nmFiltroPersonalizado;
    }

    public String getDsParticao() {
        return dsParticao;
    }

    public void setDsParticao(String dsParticao) {
        this.dsParticao = dsParticao;
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
        hash += (idFiltroPersonalizado != null ? idFiltroPersonalizado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbFiltroPersonalizado)) {
            return false;
        }
        TbFiltroPersonalizado other = (TbFiltroPersonalizado) object;
        if ((this.idFiltroPersonalizado == null && other.idFiltroPersonalizado != null) || (this.idFiltroPersonalizado != null && !this.idFiltroPersonalizado.equals(other.idFiltroPersonalizado))) {
            return false;
        }
        return true;
    }

   @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Filtro Personalizado";
    }

    private String getDescricao() {
        return nmFiltroPersonalizado;
    }
    
}
