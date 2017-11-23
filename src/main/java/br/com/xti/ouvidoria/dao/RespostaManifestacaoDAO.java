/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbRespostaManifestacao;

/**
 *
 * @author renato
 */
@Stateless
public class RespostaManifestacaoDAO extends AbstractDAO<TbRespostaManifestacao> {
    public RespostaManifestacaoDAO() {
        super(TbRespostaManifestacao.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Resposta Manifestação";
    }
}
