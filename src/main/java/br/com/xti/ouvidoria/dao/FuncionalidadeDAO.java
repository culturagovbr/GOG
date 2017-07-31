/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbFuncionalidade;
import br.com.xti.ouvidoria.model.TbPerfil;

/**
 *
 * @author renato
 */
@Stateless
public class FuncionalidadeDAO extends AbstractDAO<TbFuncionalidade> {
    
    @EJB
    private PerfilxFuncionalidadeDAO perfilFuncionalidadeDAO;
    
    public FuncionalidadeDAO() {
        super(TbFuncionalidade.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Funcionalidade";
    }
    
    public void atualizarFuncionalidades(TbPerfil perfil, List<TbFuncionalidade> funcionalidades) throws Exception {
        List<TbFuncionalidade> funcionalidadesAntigas = getFuncionalidadesAtivas(perfil);
        List<TbFuncionalidade> funcionalidadesAux = new ArrayList<TbFuncionalidade>(funcionalidadesAntigas);
        
        // Removendo as Funcionalidades que foram desmarcados
        funcionalidadesAux.removeAll(funcionalidades);
        perfilFuncionalidadeDAO.remove(perfil, funcionalidadesAux);
        
        // Adicionando as novas Funcionaldades
        funcionalidadesAux = new ArrayList<TbFuncionalidade>(funcionalidades);
        funcionalidadesAux.removeAll(funcionalidadesAntigas);
        perfilFuncionalidadeDAO.create(perfil, funcionalidadesAux);
    }
    
    public List<TbFuncionalidade> getFuncionalidadesAtivas(Integer idPerfil) throws Exception {
        if(idPerfil == null || idPerfil == 0) {
            return null;
        }
        
        String select = "SELECT f "
                + "FROM TbFuncionalidade f "
                + "WHERE f.idFuncionalidade IN ("
                + "  SELECT pf.idFuncionalidade.idFuncionalidade "
                + "  FROM TbPerfilxFuncionalidade pf "
                + "  WHERE pf.idPerfil.idPerfil = :idPerfil"
                + ")";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idPerfil", idPerfil);
        
        return selectList(select, params);
    }
    
    public List<TbFuncionalidade> getFuncionalidadesAtivas(TbPerfil perfil) throws Exception {
        if(perfil == null || perfil.getIdPerfil() == null) {
            return null;
        }
        return getFuncionalidadesAtivas(perfil.getIdPerfil());
    }
    
}
