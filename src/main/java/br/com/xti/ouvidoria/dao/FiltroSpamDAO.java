/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbFiltroSpam;

/**
 *
 * @author renato
 */
@Stateless
public class FiltroSpamDAO extends AbstractDAO<TbFiltroSpam> {
    public FiltroSpamDAO() {
        super(TbFiltroSpam.class);
    }
    
      @Override
    public String getNomeEntidade() {
        return "Filtro Spam";
    }
}
