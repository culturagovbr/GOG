/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbFuncionalidade;
import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbPerfilxFuncionalidade;

/**
 *
 * @author renato
 */
@Stateless
public class PerfilxFuncionalidadeDAO extends AbstractDAO<TbPerfilxFuncionalidade> {
    public PerfilxFuncionalidadeDAO() {
        super(TbPerfilxFuncionalidade.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Perfil x Funcionalidade";
    }
    
    public void create(TbPerfil perfil, List<TbFuncionalidade> funcionalidades) throws Exception {
        // Cria uma lista de 'TbPerfilxFuncionalidade' com o mesmo tamanho da lista de 'TbFuncionalidade' passada por parâmetro
        List<TbPerfilxFuncionalidade> listaPerfilFuncionalidades = new ArrayList<TbPerfilxFuncionalidade>(funcionalidades.size());
        
        // Popula a lista recém criada com as funcionalidades a serem cadastradas
        for (TbFuncionalidade func : funcionalidades) {
            TbPerfilxFuncionalidade tbPerfilxFuncionalidade = new TbPerfilxFuncionalidade();
            tbPerfilxFuncionalidade.setIdPerfil(perfil);
            tbPerfilxFuncionalidade.setIdFuncionalidade(func);
            listaPerfilFuncionalidades.add(tbPerfilxFuncionalidade);
        }
        
        // Realiza a persistência dos dados no banco
        for (TbPerfilxFuncionalidade perfilFuncionalidade : listaPerfilFuncionalidades) {
            create(perfilFuncionalidade);
        }
    }
    
    public void remove(TbPerfil perfil, List<TbFuncionalidade> funcionalidades) throws Exception {
        List<TbPerfilxFuncionalidade> perfilFuncionalidades = getFuncionalidadesPerfil(perfil.getIdPerfil(), getIds(funcionalidades));
        
        for (TbPerfilxFuncionalidade p : perfilFuncionalidades) {
            remove(p);
        }
    }
    
    private List<TbPerfilxFuncionalidade> getFuncionalidadesPerfil(Integer idPerfil, List<Integer> idFuncionalidades) throws Exception {
        if(idFuncionalidades.isEmpty() || idFuncionalidades == null) {
            return new ArrayList<TbPerfilxFuncionalidade>();
        }
        
        String select = "SELECT p FROM TbPerfilxFuncionalidade p WHERE p.idPerfil.idPerfil = :idPerfil AND p.idFuncionalidade.idFuncionalidade IN :idFuncionalidades";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idPerfil", idPerfil);
        params.put("idFuncionalidades", idFuncionalidades);
        
        return selectList(select, params);
    }
    
    private List<Integer> getIds(List<TbFuncionalidade> funcionalidades) {
        List<Integer> ids = new ArrayList<Integer>();
        for (TbFuncionalidade f : funcionalidades) {
            ids.add(f.getIdFuncionalidade());
        }
        return ids;
    }
    
}
