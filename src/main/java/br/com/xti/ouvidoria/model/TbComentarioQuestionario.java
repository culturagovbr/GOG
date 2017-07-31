/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *
 * @author Samuel Correia Guimarães
 */
@Entity
@Table(name = "tbComentarioQuestionario")
public class TbComentarioQuestionario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "idComentario")
    private Integer idComentario;
    
    @Basic(optional = false)
    @Column(name = "dsComentario")
    private String dsComentario;
    
    //Construtores
    public TbComentarioQuestionario() {
    }

    public TbComentarioQuestionario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Integer getIdComentario() {
		return idComentario;
	}

	public void setIdComentario(Integer idComentario) {
		this.idComentario = idComentario;
	}

	public String getDsComentario() {
		return dsComentario;
	}

	public void setDsComentario(String dsComentario) {
		this.dsComentario = dsComentario;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idComentario != null ? idComentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbComentarioQuestionario)) {
            return false;
        }
        TbComentarioQuestionario other = (TbComentarioQuestionario) object;
        if ((this.idComentario == null && other.idComentario != null) || (this.idComentario != null && !this.idComentario.equals(other.idComentario))) {
            return false;
        }
        return true;
    }
    
	@Override
	public String toString() {
		return String.format("%s > %s", getEntidade(), getDescricao());
	}

	private String getEntidade() {
		return "Comentário Questionário";
	}

	private String getDescricao() {
		return idComentario + " - " + dsComentario;
	}

}
