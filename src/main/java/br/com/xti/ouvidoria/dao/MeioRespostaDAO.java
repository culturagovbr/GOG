/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbMeioResposta;

/**
 *
 * @author renato
 */
@Stateless
public class MeioRespostaDAO extends AbstractDAO<TbMeioResposta> {

    public MeioRespostaDAO() {
        super(TbMeioResposta.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Meio de Resposta";
    }
}
