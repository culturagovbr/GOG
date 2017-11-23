/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbPrioridade;

/**
 *
 * @author renato
 */
@Stateless
public class PrioridadeDAO extends AbstractDAO<TbPrioridade> {

    public PrioridadeDAO() {
        super(TbPrioridade.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Prioridade";
    }
}
