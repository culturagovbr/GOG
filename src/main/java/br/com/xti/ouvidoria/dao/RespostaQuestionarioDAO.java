/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbRespostaQuestionario;

/**
 *
 * @author Emanuel Melo
 */
@Stateless
public class RespostaQuestionarioDAO extends AbstractDAO<TbRespostaQuestionario> {
    public RespostaQuestionarioDAO() {
        super(TbRespostaQuestionario.class);
    }
    
     @Override
    public String getNomeEntidade() {
        return "Resposta Questionario";
    }

	public List<TbRespostaQuestionario> getByManifestacao(TbManifestacao manifestacao) {
		List<TbRespostaQuestionario> res = new ArrayList<>();
		
		try {
			String select = "select r from TbRespostaQuestionario r where r.manifestacao.idManifestacao = :idManifestacao";
			TypedQuery<TbRespostaQuestionario> query = getEntityManager().createQuery(select, TbRespostaQuestionario.class);
			query.setParameter("idManifestacao", manifestacao.getIdManifestacao());
			
			res.addAll(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
