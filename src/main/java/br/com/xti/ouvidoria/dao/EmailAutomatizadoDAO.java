/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;

/**
 *
 * @author renato
 */
@Stateless
public class EmailAutomatizadoDAO extends AbstractDAO<TbEmailAutomatizado> {
    public EmailAutomatizadoDAO() {
        super(TbEmailAutomatizado.class);
    }
    
    public TbEmailAutomatizado findByTipo(EmailAutomatizadoEnum tipoEmail) {
    	try {
	    	EntityManager em = getEntityManager();
	    	String select = "select e from TbEmailAutomatizado e where e.tpEmail = :tipo";
	    	TypedQuery<TbEmailAutomatizado> query = em.createQuery(select, TbEmailAutomatizado.class);
	    	query.setParameter("tipo", String.valueOf(tipoEmail.getId()));
	    	return query.getSingleResult();
    	} catch (Exception e) {
    		return null;
    	}
    }
    
      @Override
    public String getNomeEntidade() {
        return "Email Notificação";
    }
}
