/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbEncaminhamentoPadronizado;

/**
 *
 * @author renato
 */
@Stateless
public class EncaminhamentoPadronizadoDAO extends AbstractDAO<TbEncaminhamentoPadronizado> {
    public EncaminhamentoPadronizadoDAO() {
        super(TbEncaminhamentoPadronizado.class);
    }
    
      @Override
    public String getNomeEntidade() {
        return "Encaminhameno Padronizado";
    }
}
