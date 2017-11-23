/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.HashMap;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbPais;

/**
 *
 * @author renato
 */
@Stateless
public class PaisDAO extends AbstractDAO<TbPais> {
    public PaisDAO() {
        super(TbPais.class);
    }

    @Override
    public String getNomeEntidade() {
        return "País";
    }
    
    public TbPais findNome(String nmPais) throws Exception {
        String select = "SELECT t FROM TbPais t WHERE t.nmPais= :nmPais";

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nmPais", nmPais);
        TbPais tbPais = selectObject(select, map);

        return tbPais;
    }
}
