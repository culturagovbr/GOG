/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbSubClassificacao;

/**
 *
 * @author renato
 */
@Stateless
public class SubClassificacaoDAO extends AbstractDAO<TbSubClassificacao> {
    public SubClassificacaoDAO() {
        super(TbSubClassificacao.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Subclassificação";
    }
}
