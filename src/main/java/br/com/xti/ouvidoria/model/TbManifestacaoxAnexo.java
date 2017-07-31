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
@Table(name = "tbManifestacaoxAnexo")
@NamedQueries({
    @NamedQuery(name = "TbManifestacaoxAnexo.findAll", query = "SELECT t FROM TbManifestacaoxAnexo t"),
    @NamedQuery(name = "TbManifestacaoxAnexo.findByIdManifestacaoxAnexo", query = "SELECT t FROM TbManifestacaoxAnexo t WHERE t.idManifestacaoxAnexo = :idManifestacaoxAnexo")})
public class TbManifestacaoxAnexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idManifestacaoxAnexo")
    private Integer idManifestacaoxAnexo;
    @JoinColumn(name = "idManifestacao", referencedColumnName = "idManifestacao")
    @ManyToOne(optional = false)
    private TbManifestacao idManifestacao;
    @JoinColumn(name = "idAnexo", referencedColumnName = "idAnexo")
    @ManyToOne(optional = false)
    private TbAnexo idAnexo;

    public TbManifestacaoxAnexo() {
    }

    public TbManifestacaoxAnexo(Integer idManifestacaoxAnexo) {
        this.idManifestacaoxAnexo = idManifestacaoxAnexo;
    }

    public Integer getIdManifestacaoxAnexo() {
        return idManifestacaoxAnexo;
    }

    public void setIdManifestacaoxAnexo(Integer idManifestacaoxAnexo) {
        this.idManifestacaoxAnexo = idManifestacaoxAnexo;
    }

    public TbManifestacao getIdManifestacao() {
        return idManifestacao;
    }

    public void setIdManifestacao(TbManifestacao idManifestacao) {
        this.idManifestacao = idManifestacao;
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
        hash += (idManifestacaoxAnexo != null ? idManifestacaoxAnexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbManifestacaoxAnexo)) {
            return false;
        }
        TbManifestacaoxAnexo other = (TbManifestacaoxAnexo) object;
        if ((this.idManifestacaoxAnexo == null && other.idManifestacaoxAnexo != null) || (this.idManifestacaoxAnexo != null && !this.idManifestacaoxAnexo.equals(other.idManifestacaoxAnexo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Manifestação x Anexo";
    }

    private String getDescricao() {
        return "Manifestação: " + getIdManifestacao().getNrManifestacao() + "\tAnexo: " + idAnexo.getNmAnexo();
    }
}
