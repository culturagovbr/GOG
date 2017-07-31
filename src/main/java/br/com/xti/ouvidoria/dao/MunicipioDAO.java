/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbMunicipio;

/**
 *
 * @author renato
 */
@Stateless
public class MunicipioDAO extends AbstractDAO<TbMunicipio> {
    public MunicipioDAO() {
        super(TbMunicipio.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Município";
    }
}
