/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;
import br.com.xti.ouvidoria.model.TbUsuario;

/**
 *
 * @author renato
 */
@Stateless
public class FiltroPersonalizadoDAO extends AbstractDAO<TbFiltroPersonalizado> {
    
    public FiltroPersonalizadoDAO() {
        super(TbFiltroPersonalizado.class);
    }
    
    @Override
    public String getNomeEntidade() {
        return "Filtro Personalizado";
    }
    
    public List<TbFiltroPersonalizado> findByUsuario(TbUsuario usuario) {
    	List<TbFiltroPersonalizado> lista = null;
    	try {
    		HashMap<String, Object> map = new HashMap<>();
    		map.put("idUsuario", usuario.getIdUsuario());
    		lista = selectList("select f from TbFiltroPersonalizado f where f.idUsuario.idUsuario = :idUsuario", map);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
        if(lista == null) {
            lista = new ArrayList<>();
        }
        return lista;
    }
}
