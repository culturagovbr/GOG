/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.VwEstatisticasManifestacao;

/**
 *
 * @author renato
 */
@Stateless
public class VwEstatisticasManifestacaoDAO extends AbstractDAO<VwEstatisticasManifestacao> {
    public VwEstatisticasManifestacaoDAO() {
        super(VwEstatisticasManifestacao.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Estatística Manifestação";
    }
}
