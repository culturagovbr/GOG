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
@Table(name = "tbUnidadexManifestacao")
@NamedQueries({
    @NamedQuery(name = "TbUnidadexManifestacao.findAll", query = "SELECT t FROM TbUnidadexManifestacao t"),
    @NamedQuery(name = "TbUnidadexManifestacao.findByIdUnidadeManifestacao", query = "SELECT t FROM TbUnidadexManifestacao t WHERE t.idUnidadeManifestacao = :idUnidadeManifestacao")})
public class TbUnidadexManifestacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUnidadeManifestacao")
    private Integer idUnidadeManifestacao;
    @JoinColumn(name = "idUnidade", referencedColumnName = "idUnidade")
    @ManyToOne(optional = false)
    private TbUnidade idUnidade;
    @JoinColumn(name = "idManifestacao", referencedColumnName = "idManifestacao")
    @ManyToOne(optional = false)
    private TbManifestacao idManifestacao;

    public TbUnidadexManifestacao() {
    }

    public TbUnidadexManifestacao(Integer idUnidadeManifestacao) {
        this.idUnidadeManifestacao = idUnidadeManifestacao;
    }

    public Integer getIdUnidadeManifestacao() {
        return idUnidadeManifestacao;
    }

    public void setIdUnidadeManifestacao(Integer idUnidadeManifestacao) {
        this.idUnidadeManifestacao = idUnidadeManifestacao;
    }

    public TbUnidade getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(TbUnidade idUnidade) {
        this.idUnidade = idUnidade;
    }

    public TbManifestacao getIdManifestacao() {
        return idManifestacao;
    }

    public void setIdManifestacao(TbManifestacao idManifestacao) {
        this.idManifestacao = idManifestacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUnidadeManifestacao != null ? idUnidadeManifestacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbUnidadexManifestacao)) {
            return false;
        }
        TbUnidadexManifestacao other = (TbUnidadexManifestacao) object;
        if ((this.idUnidadeManifestacao == null && other.idUnidadeManifestacao != null) || (this.idUnidadeManifestacao != null && !this.idUnidadeManifestacao.equals(other.idUnidadeManifestacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Unidade x Manifestação";
    }

    private String getDescricao() {
        return "Unidade: " + getIdUnidade().getNmUnidade() + "\tManifestação: " + getIdManifestacao().getNrManifestacao();
    }
}
