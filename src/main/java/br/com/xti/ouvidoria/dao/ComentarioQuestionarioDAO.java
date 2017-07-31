package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbComentarioQuestionario;


/**
*
* @author Emanuel Melo
*/

@Stateless
public class ComentarioQuestionarioDAO extends AbstractDAO<TbComentarioQuestionario>{
	public ComentarioQuestionarioDAO() {
        super(TbComentarioQuestionario.class);
    }
    
     @Override
    public String getNomeEntidade() {
        return "Comentario Questionario";
    }
}

