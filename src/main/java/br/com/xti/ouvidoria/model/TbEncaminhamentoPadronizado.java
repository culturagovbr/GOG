/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbEncaminhamentoPadronizado")
@NamedQueries({
    @NamedQuery(name = "TbEncaminhamentoPadronizado.findAll", query = "SELECT t FROM TbEncaminhamentoPadronizado t"),
    @NamedQuery(name = "TbEncaminhamentoPadronizado.findByIdEncaminhamentoPadronizado", query = "SELECT t FROM TbEncaminhamentoPadronizado t WHERE t.idEncaminhamentoPadronizado = :idEncaminhamentoPadronizado"),
    @NamedQuery(name = "TbEncaminhamentoPadronizado.findByDsTitulo", query = "SELECT t FROM TbEncaminhamentoPadronizado t WHERE t.dsTitulo = :dsTitulo"),
    @NamedQuery(name = "TbEncaminhamentoPadronizado.findByDsConteudo", query = "SELECT t FROM TbEncaminhamentoPadronizado t WHERE t.dsConteudo = :dsConteudo"),
    @NamedQuery(name = "TbEncaminhamentoPadronizado.findByDtCadastro", query = "SELECT t FROM TbEncaminhamentoPadronizado t WHERE t.dtCadastro = :dtCadastro")})
public class TbEncaminhamentoPadronizado implements Serializable, Comparable<TbEncaminhamentoPadronizado> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idEncaminhamentoPadronizado")
    private Integer idEncaminhamentoPadronizado;
    @Size(max = 50)
    @Column(name = "dsTitulo")
    private String dsTitulo;
    @Size(min = 1)
    @Column(name = "dsConteudo")
    private String dsConteudo;
    @Column(name = "dtCadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCadastro;

    public TbEncaminhamentoPadronizado() {
    }

    public TbEncaminhamentoPadronizado(Integer idEncaminhamentoPadronizado) {
        this.idEncaminhamentoPadronizado = idEncaminhamentoPadronizado;
    }

    public Integer getIdEncaminhamentoPadronizado() {
        return idEncaminhamentoPadronizado;
    }

    public void setIdEncaminhamentoPadronizado(Integer idEncaminhamentoPadronizado) {
        this.idEncaminhamentoPadronizado = idEncaminhamentoPadronizado;
    }

    public String getDsTitulo() {
        return dsTitulo;
    }

    public void setDsTitulo(String dsTitulo) {
        this.dsTitulo = dsTitulo;
    }

    public String getDsConteudo() {
        return dsConteudo;
    }

    public void setDsConteudo(String dsConteudo) {
        this.dsConteudo = dsConteudo;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEncaminhamentoPadronizado != null ? idEncaminhamentoPadronizado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbEncaminhamentoPadronizado)) {
            return false;
        }
        TbEncaminhamentoPadronizado other = (TbEncaminhamentoPadronizado) object;
        if ((this.idEncaminhamentoPadronizado == null && other.idEncaminhamentoPadronizado != null) || (this.idEncaminhamentoPadronizado != null && !this.idEncaminhamentoPadronizado.equals(other.idEncaminhamentoPadronizado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s > %s", getEntidade(), getDescricao());
    }

    private String getEntidade() {
        return "Encaminhamento Padronizado";
    }

    private String getDescricao() {
        return dsTitulo;
    }

	@Override
	public int compareTo(TbEncaminhamentoPadronizado other) {
		return getDsTitulo().compareToIgnoreCase(other.getDsTitulo());
	}
    
}
