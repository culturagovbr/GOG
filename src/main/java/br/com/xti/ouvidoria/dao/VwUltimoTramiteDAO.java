/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.VwUltimoTramite;

/**
 *
 * @author samuel.guimaraes
 */
@Stateless
public class VwUltimoTramiteDAO extends AbstractDAO<VwUltimoTramite> {
    public VwUltimoTramiteDAO() {
        super(VwUltimoTramite.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Último Trâmite";
    }
    
    public List<VwUltimoTramite> findByUsuarioReceptor(List<Integer> listaIds) {
        if (listaIds == null || listaIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            String query = "SELECT ut FROM VwUltimoTramite ut WHERE ut.idUsuarioReceptor IN :listaIds";
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("listaIds", listaIds);

            return selectList(query, param);
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
}
