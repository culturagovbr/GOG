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
@Table(name = "tbFuncionalidade")
@NamedQueries({
    @NamedQuery(name = "TbFuncionalidade.findAll", query = "SELECT t FROM TbFuncionalidade t"),
    @NamedQuery(name = "TbFuncionalidade.findByIdFuncionalidade", query = "SELECT t FROM TbFuncionalidade t WHERE t.idFuncionalidade = :idFuncionalidade"),
    @NamedQuery(name = "TbFuncionalidade.findByDsFuncionalidade", query = "SELECT t FROM TbFuncionalidade t WHERE t.dsFuncionalidade = :dsFuncionalidade")})
public class TbFuncionalidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idFuncionalidade")
    private Integer idFuncionalidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "dsFuncionalidade")
    private String dsFuncionalidade;
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncionalidade")*/
    @OneToMany(mappedBy = "idFuncionalidade")
    private Collection<TbPerfilxFuncionalidade> tbPerfilxFuncionalidadeCollection = new ArrayList<>();

    public TbFuncionalidade() {
    }

    public TbFuncionalidade(Integer idFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
    }

    public TbFuncionalidade(Integer idFuncionalidade, String dsFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
        this.dsFuncionalidade = dsFuncionalidade;
    }

    public Integer getIdFuncionalidade() {
        return idFuncionalidade;
    }

    public void setIdFuncionalidade(Integer idFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
    }

    public String getDsFuncionalidade() {
        return dsFuncionalidade;
    }

    public void setDsFuncionalidade(String dsFuncionalidade) {
        this.dsFuncionalidade = dsFuncionalidade;
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
        hash += (idFuncionalidade != null ? idFuncionalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbFuncionalidade)) {
            return false;
        }
        TbFuncionalidade other = (TbFuncionalidade) object;
        if ((this.idFuncionalidade == null && other.idFuncionalidade != null) || (this.idFuncionalidade != null && !this.idFuncionalidade.equals(other.idFuncionalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Funcionalidade";
    }

    private String getDescricao() {
        return dsFuncionalidade;
    }
}
