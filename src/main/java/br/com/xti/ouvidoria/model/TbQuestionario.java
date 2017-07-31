/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.xti.ouvidoria.model.enums.BooleanEnum;
/**
 *
 * @author Samuel Correia Guimarães
 */
@Entity
@Table(name = "tbQuestionario")
public class TbQuestionario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idQuestionario")
    private Integer idQuestionario;
    
    @Basic(optional = false)
    @Column(name = "nmQuestionario")
    private String nmQuestionario;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dtInicio")
    private Date dtInicio;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dtFinal")
    private Date dtFinal;
    
    @Basic(optional = false)
    @Column(name = "stQuestionario")
    private String stQuestionario;
    
    @OneToMany(mappedBy = "questionario")
    private Collection<TbPerguntaQuestionario> perguntas = new ArrayList<>();
    
    @OneToMany(mappedBy = "idQuestionario")
    private Collection<TbManifestacao> manifestacoes = new ArrayList<>();

    //Construtores
    public TbQuestionario() {
    }

    public TbQuestionario(Integer idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public TbQuestionario(Integer idQuestionario, String nmQuestionario) {
        this.idQuestionario = idQuestionario;
        this.nmQuestionario = nmQuestionario;
    }

    @OneToMany(mappedBy = "questionario")
    private Collection<TbPerguntaQuestionario> tbPerguntaCollection = new ArrayList<>();
    
    //Getters e Setters
    public Collection<TbPerguntaQuestionario> getTbPerguntaCollection() {
        return tbPerguntaCollection;
    }

    public void setTbPerguntaCollection(Collection<TbPerguntaQuestionario> tbPerguntaCollection) {
        this.tbPerguntaCollection = tbPerguntaCollection;
    }

    public Integer getIdQuestionario() {
        return idQuestionario;
    }

    public String getNmQuestionario() {
        return nmQuestionario == null ? "" : nmQuestionario;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public String getStQuestionario() {
        return stQuestionario;
    }

    public void setIdQuestionario(Integer idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public void setNmQuestionario(String nmQuestionario) {
        this.nmQuestionario = nmQuestionario;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }

    public void setStQuestionario(String stQuestionario) {
        this.stQuestionario = stQuestionario;
    }
    
    public Collection<TbPerguntaQuestionario> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(Collection<TbPerguntaQuestionario> perguntas) {
		this.perguntas = perguntas;
	}

	public Collection<TbManifestacao> getManifestacoes() {
		return manifestacoes;
	}

	public void setManifestacoes(Collection<TbManifestacao> manifestacoes) {
		this.manifestacoes = manifestacoes;
	}
	
	public boolean isStatus() {
		return BooleanEnum.SIM.getId().equals(stQuestionario);
	}
	
	public void setStatus(boolean status) {
		if(status)
			stQuestionario = BooleanEnum.SIM.getId();
		else
			stQuestionario = BooleanEnum.NAO.getId();
	}
	
	
	
	
	

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idQuestionario != null ? idQuestionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbQuestionario)) {
            return false;
        }
        TbQuestionario other = (TbQuestionario) object;
        if ((this.idQuestionario == null && other.idQuestionario != null) || (this.idQuestionario != null && !this.idQuestionario.equals(other.idQuestionario))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%s > %s",getEntidade(),getDescricao());
    }
    
    private String getEntidade(){
        return "Questionário";
    }
    private String getDescricao(){
        return nmQuestionario;
    }  
    
}
