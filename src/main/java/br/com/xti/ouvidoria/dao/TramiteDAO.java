/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.Collection;
import java.util.HashMap;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbTramite;

/**
 *
 * @author renato
 */
@Stateless
public class TramiteDAO extends AbstractDAO<TbTramite> {

    public TramiteDAO() {
        super(TbTramite.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Trâmite";
    }
    
    public Collection<TbTramite> getPorEncaminhamento(TbEncaminhamento idEncaminhamento) {
        Collection<TbTramite> retorno = null;
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("idEncaminhamento", idEncaminhamento);
            retorno = selectList("SELECT t FROM TbTramite t WHERE t.idEncaminhamento = :idEncaminhamento order by t.dtTramite asc", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
