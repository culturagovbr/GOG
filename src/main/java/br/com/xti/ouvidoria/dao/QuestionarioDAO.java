/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbQuestionario;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;

/**
 *
 * @author Samuel Correia Guimarães
 */
@Stateless
public class QuestionarioDAO extends AbstractDAO<TbQuestionario> {
	
	private static final String QUERY_SETAR_TODOS_QUESTIONARIOS_INATIVOS = "UPDATE TbQuestionario SET stQuestionario = :stQuestionario";
	private static final String QUERY_QUESTIONARIO_ATIVO = "SELECT q FROM TbQuestionario q WHERE q.stQuestionario = :stQuestionario";
	
    public QuestionarioDAO() {
        super(TbQuestionario.class);
    }
    
    public TbQuestionario getQuestionarioAtivo() {
    	List<TbQuestionario> res = new ArrayList<TbQuestionario>();
    	TbQuestionario questionario = null;
		
    	try {
    		TypedQuery<TbQuestionario> query = getEntityManager().createQuery(QUERY_QUESTIONARIO_ATIVO, TbQuestionario.class);
    		query.setParameter("stQuestionario", BooleanEnum.SIM.getId());
    		res.addAll(query.getResultList());
    	} catch (Exception e) {
    		// Do nothing
    		e.printStackTrace();
    	}
    	
    	if(ValidacaoHelper.isNotEmpty(res))
    		questionario = res.get(0);
		
        return questionario;
    }
    
    private void inativarTodosQuestionarios() {
    	try {
			Query query = getEntityManager().createQuery(QUERY_SETAR_TODOS_QUESTIONARIOS_INATIVOS);
			query.setParameter("stQuestionario", BooleanEnum.NAO.getId());
			query.executeUpdate();
    	} catch (Exception e) {
    		// Do nothing
    	}
    }
    
    @Override
    public void create(TbQuestionario questionario) throws InfrastructureException {
    	if(BooleanEnum.SIM.getId().equals(questionario.getStQuestionario()))
    		inativarTodosQuestionarios();
    	
    	super.create(questionario);
    }
    
    @Override
    public void edit(TbQuestionario questionario) throws InfrastructureException {
    	if(BooleanEnum.SIM.getId().equals(questionario.getStQuestionario()))
    		inativarTodosQuestionarios();
    	
    	super.edit(questionario);
    }
    
     @Override
    public String getNomeEntidade() {
        return "Questionario";
    }
}
