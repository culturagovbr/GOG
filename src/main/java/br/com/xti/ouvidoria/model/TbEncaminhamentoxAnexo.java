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
@Table(name = "tbEncaminhamentoxAnexo")
@NamedQueries({
    @NamedQuery(name = "TbEncaminhamentoxAnexo.findAll", query = "SELECT t FROM TbEncaminhamentoxAnexo t"),
    @NamedQuery(name = "TbEncaminhamentoxAnexo.findByIdEncaminhamentoxAnexo", query = "SELECT t FROM TbEncaminhamentoxAnexo t WHERE t.idEncaminhamentoxAnexo = :idEncaminhamentoxAnexo")})
public class TbEncaminhamentoxAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEncaminhamentoxAnexo")
    private Integer idEncaminhamentoxAnexo;
    @JoinColumn(name = "idEncaminhamento", referencedColumnName = "idEncaminhamento")
    @ManyToOne(optional = false)
    private TbEncaminhamento idEncaminhamento;
    @JoinColumn(name = "idAnexo", referencedColumnName = "idAnexo")
    @ManyToOne(optional = false)
    private TbAnexo idAnexo;

    public TbEncaminhamentoxAnexo() {
    }

    public TbEncaminhamentoxAnexo(Integer idEncaminhamentoxAnexo) {
        this.idEncaminhamentoxAnexo = idEncaminhamentoxAnexo;
    }

    public Integer getIdEncaminhamentoxAnexo() {
        return idEncaminhamentoxAnexo;
    }

    public void setIdEncaminhamentoxAnexo(Integer idEncaminhamentoxAnexo) {
        this.idEncaminhamentoxAnexo = idEncaminhamentoxAnexo;
    }

    public TbEncaminhamento getIdEncaminhamento() {
        return idEncaminhamento;
    }

    public void setIdEncaminhamento(TbEncaminhamento idEncaminhamento) {
        this.idEncaminhamento = idEncaminhamento;
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
        hash += (idEncaminhamentoxAnexo != null ? idEncaminhamentoxAnexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbEncaminhamentoxAnexo)) {
            return false;
        }
        TbEncaminhamentoxAnexo other = (TbEncaminhamentoxAnexo) object;
        if ((this.idEncaminhamentoxAnexo == null && other.idEncaminhamentoxAnexo != null) || (this.idEncaminhamentoxAnexo != null && !this.idEncaminhamentoxAnexo.equals(other.idEncaminhamentoxAnexo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Encaminhamento x Anexo";
    }

    private String getDescricao() {
        return "Encaminhamento: " + getIdEncaminhamento().getDsDescricao() + "\tAnexo: " + idAnexo.getNmAnexo();
    }
}
