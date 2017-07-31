/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbAreaEntrada;

/**
 * 
 * @author renato
 */
@Stateless
public class AreaEntradaDAO extends AbstractDAO<TbAreaEntrada> {
	public AreaEntradaDAO() {
		super(TbAreaEntrada.class);
	}
	
	@Override
	public String getNomeEntidade() {
		return "Área de Entrada";
	}

}
