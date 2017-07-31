/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbPerguntaQuestionario;

/**
 *
 * @author Emanuel Melo
 */
@Stateless
public class PerguntaQuestionarioDAO extends AbstractDAO<TbPerguntaQuestionario>{
    
    public PerguntaQuestionarioDAO(){
        super(TbPerguntaQuestionario.class);
    }
    
    @Override
    public String getNomeEntidade() {
        return "Pergunta Questionario";
    }
}

