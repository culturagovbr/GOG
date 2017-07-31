/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbMeioEntrada;

/**
 *
 * @author renato
 */
@Stateless
public class MeioEntradaDAO extends AbstractDAO<TbMeioEntrada> {
    public MeioEntradaDAO() {
        super(TbMeioEntrada.class);
    }
    
     @Override
    public String getNomeEntidade() {
        return "Meio de Entrada";
    }
}
