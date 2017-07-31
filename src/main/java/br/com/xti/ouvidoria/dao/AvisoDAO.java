/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbAviso;

/**
 * 
 * @author renato
 */
@Stateless
public class AvisoDAO extends AbstractDAO<TbAviso> {
	public AvisoDAO() {
		super(TbAviso.class);
	}

	@Override
	public String getNomeEntidade() {
		return "Aviso";
	}
	
	public TbAviso getAvisoAtual() {
		List<TbAviso> res = new ArrayList<TbAviso>();
		TbAviso aviso = null;
		
		try {
			String select = "SELECT a FROM TbAviso a WHERE a.dtInicioAviso <= :dtHoje and a.dtFimAviso >= :dtHoje order by a.idAvisos desc";
			TypedQuery<TbAviso> query = getEntityManager().createQuery(select, TbAviso.class);
			query.setParameter("dtHoje", new Date());
			
			res.addAll(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(ValidacaoHelper.isNotEmpty(res))
			aviso = res.get(0);
		
        return aviso;
    }

}
