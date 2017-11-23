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
import javax.persistence.Table;

/**
 *
 * @author Samuel Correia Guimarães
 */
@Entity
@Table(name = "tbPerguntaQuestionario")
public class TbPerguntaQuestionario implements Serializable{
        
    private static final long serialVersionUID = 1L;
    
    //Colunas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPergunta")
    private Integer idPergunta;
    
    @Basic(optional = false)
    @Column(name = "dsPergunta")
    private String dsPergunta;
    
    @Basic(optional = false)
    @Column(name = "posicaoPergunta")
    private Integer posicaoPergunta;
    
    @ManyToOne
    @JoinColumn(name="idQuestionario", referencedColumnName = "idQuestionario")
    private TbQuestionario questionario;
    
    
    
    //Getters e Setters
    public Integer getIdPergunta() {
    	return idPergunta;
    }
    
    public void setIdPergunta(Integer idPergunta) {
    	this.idPergunta = idPergunta;
    }
    
    public String getDsPergunta() {
    	return dsPergunta;
    }
    
    public void setDsPergunta(String dsPergunta) {
    	this.dsPergunta = dsPergunta;
    }
    
    public Integer getPosicaoPergunta() {
    	return posicaoPergunta;
    }
    
    public void setPosicaoPergunta(Integer posicaoPergunta) {
    	this.posicaoPergunta = posicaoPergunta;
    }
    
    public TbQuestionario getQuestionario() {
    	return questionario;
    }
    
    public void setQuestionario(TbQuestionario questionario) {
    	this.questionario = questionario;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPergunta != null ? idPergunta.hashCode() : 0);
        return hash;
    }

	@Override
    public boolean equals(Object object) {
        if (!(object instanceof TbPerguntaQuestionario)) {
            return false;
        }
        TbPerguntaQuestionario other = (TbPerguntaQuestionario) object;
        if ((this.idPergunta == null && other.idPergunta != null) || (this.idPergunta != null && !this.idPergunta.equals(other.idPergunta))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%s > %s",getEntidade(),getDescricao());
    }
    
    private String getEntidade(){
        return "Pergunta Questionário";
    }
    private String getDescricao(){
        return dsPergunta;
    }
    
}
    
