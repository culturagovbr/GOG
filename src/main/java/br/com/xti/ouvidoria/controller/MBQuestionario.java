/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.QuestionarioDAO;
import br.com.xti.ouvidoria.model.TbQuestionario;

/**
 *
 * @author Samuel Correia Guimarães
 */

@SuppressWarnings("serial")
@ManagedBean(name = "mBQuestionario")
@ViewScoped
public class MBQuestionario implements Serializable{
    
    @EJB private QuestionarioDAO dao;
    private TbQuestionario questionario = new TbQuestionario();
    private TbQuestionario questionarioNovo = new TbQuestionario();
    private List<TbQuestionario> questionariosFiltrados;
    
    
    public void cadastrar(ActionEvent actionEvent) {
        try {
	        dao.create(questionarioNovo);
	        
	        MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
	
    public void alterar(ActionEvent actionEvent) {
        try {
            if (questionario != null && questionario.getIdQuestionario()!= null) {
                dao.edit(questionario);
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public void remover(ActionEvent actionEvent) {
        try {
            if (questionario != null && questionario.getIdQuestionario()!= null) {
                dao.remove(questionario);
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro não pode ser exclu�do porque a tabela inclui registros relacionados.");
        }
        limpar();
    }
    
    private void limpar() {
    	questionario = new TbQuestionario();
    	questionarioNovo = new TbQuestionario();
    }
    
    public void preAtivar(TbQuestionario questionario) {
		this.questionario = questionario;
    }
    
    public void ativar() {
    	try {
    		if (questionario != null && questionario.getIdQuestionario()!= null) {                
    			questionario.setStatus(true);
    			dao.edit(questionario);
    			MensagemFaceUtil.info("Questionário ativado com sucesso.", "");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		MensagemFaceUtil.erro("Erro", "");
    	}
    	limpar();
    }
    
    public List<TbQuestionario> getQuestionariosFiltrados(){
        return questionariosFiltrados;
    }
    
    public void setQuestionariosFiltrados(List<TbQuestionario> questionariosFiltrados) {
        this.questionariosFiltrados = questionariosFiltrados;
    }
    
    public TbQuestionario getQuestionarioNovo() {
        return questionarioNovo;
    }

    public void setQuestionarioNovo(TbQuestionario questionarioNovo) {
        this.questionarioNovo = questionarioNovo;
    }

    public TbQuestionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(TbQuestionario questionario) {
        this.questionario = questionario;
    }

    public List<TbQuestionario> getTodos() {
        return dao.findAll();
    }
    
}
