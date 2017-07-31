/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.comparator.PerguntaQuestionarioPosicaoComparator;
import br.com.xti.ouvidoria.dao.PerguntaQuestionarioDAO;
import br.com.xti.ouvidoria.dao.QuestionarioDAO;
import br.com.xti.ouvidoria.model.TbPerguntaQuestionario;
import br.com.xti.ouvidoria.model.TbQuestionario;

/**
 *
 * @author Emanuel Melo
 */

@SuppressWarnings("serial")
@ManagedBean(name = "mBPergunta")
@ViewScoped
public class MBPergunta implements Serializable{
    
    
    @EJB private PerguntaQuestionarioDAO dao;
    @EJB private QuestionarioDAO questionarioDao;
    
    private TbPerguntaQuestionario pergunta = new TbPerguntaQuestionario();
    private TbPerguntaQuestionario perguntaNova = new TbPerguntaQuestionario();
    private List<TbPerguntaQuestionario> perguntasFiltradas = new ArrayList<>();
    private TbQuestionario questionario;
    
    
    
    public void cadastrar(ActionEvent actionEvent) {
        try {
            perguntaNova.setQuestionario(questionario);
            dao.create(perguntaNova);
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public void alterar(ActionEvent actionEvent) {
        try {
            if (pergunta != null && pergunta.getIdPergunta()!= null) {
                dao.edit(pergunta);
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
            if (pergunta != null && pergunta.getIdPergunta()!= null) {
                dao.remove(pergunta);
                
                perguntasFiltradas.remove(pergunta);
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro n�o pode ser excluído porque a tabela inclui registros relacionados.");
        }
    }
    
    private void limpar() {
    	pergunta = new TbPerguntaQuestionario();
    	perguntaNova = new TbPerguntaQuestionario();
    }
    
    public void carregarPerguntas(TbQuestionario questionario) {
    	this.questionario = questionarioDao.find(questionario.getIdQuestionario());
    	perguntasFiltradas = new ArrayList<>(this.questionario.getPerguntas());
    	Collections.sort(perguntasFiltradas, new PerguntaQuestionarioPosicaoComparator());
    }
    
    
    public List<TbPerguntaQuestionario> getPerguntasFiltradas(){
    	Collections.sort(perguntasFiltradas, new PerguntaQuestionarioPosicaoComparator());
        return perguntasFiltradas;
    }
    
    public void setPerguntasFiltradas(List<TbPerguntaQuestionario> perguntasFiltradas) {
    	if(!(perguntasFiltradas == null))
    		this.perguntasFiltradas = perguntasFiltradas;
    }
    
    public TbPerguntaQuestionario getPerguntaNova() {
         if (perguntaNova == null) {
            perguntaNova = new TbPerguntaQuestionario();
        }
        return perguntaNova;
    }

    public void setPerguntaNova(TbPerguntaQuestionario perguntaNova) {
        this.perguntaNova = perguntaNova;
    }

    public MBPergunta() {
        pergunta = new TbPerguntaQuestionario();
    }

    public TbPerguntaQuestionario getPergunta() {
        return pergunta;
    }

    public void setPergunta(TbPerguntaQuestionario pergunta) {
        this.pergunta = pergunta;
    }

    public TbQuestionario getQuestionario() {
    	if(questionario == null)
    		questionario = new TbQuestionario();
    	
        return questionario;
    }

    public void setQuestionario(TbQuestionario questionario) {
        this.questionario = questionario;
    }
    
}
