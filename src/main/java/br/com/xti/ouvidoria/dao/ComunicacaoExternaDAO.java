/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.Collection;
import java.util.HashMap;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbManifestacao;

/**
 *
 * @author renato
 */
@Stateless
public class ComunicacaoExternaDAO extends AbstractDAO<TbComunicacaoExterna> {

    public ComunicacaoExternaDAO() {
        super(TbComunicacaoExterna.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Comunicacao Externa";
    }
    
    public Collection<TbComunicacaoExterna> getPorManifestacao(TbManifestacao idManifestacao) {
        Collection<TbComunicacaoExterna> retorno = null;
        try {
        	HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("idManifestacao", idManifestacao);
            retorno = selectList("SELECT t FROM TbComunicacaoExterna t WHERE t.idManifestacao = :idManifestacao", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
    
    
    public Collection<TbComunicacaoExterna> getPorIdManifestacao(Integer idManifestacao) {
        Collection<TbComunicacaoExterna> retorno = null;
        try {
        	HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("idManifestacao", idManifestacao);
            retorno = selectList("SELECT t FROM TbComunicacaoExterna t WHERE t.idManifestacao.idManifestacao = :idManifestacao", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

}
