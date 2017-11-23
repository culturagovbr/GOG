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

import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbUsuario;

/**
 * @author renato
 */
@Stateless
public class PerfilDAO extends AbstractDAO<TbPerfil> {
    
    @EJB
    private UsuarioxPerfilDAO usuarioPerfilDAO;
    
    public PerfilDAO() {
        super(TbPerfil.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Perfil";
    }
    
    public void atualizarPerfis(TbUsuario usuario, List<TbPerfil> perfis) throws Exception {
        List<TbPerfil> perfisAntigos = getPerfisAtivos(usuario);
        List<TbPerfil> perfisAux = new ArrayList<>(perfisAntigos);
        
        // Removendo os Perfis que foram desmarcados
        perfisAux.removeAll(perfis);
        usuarioPerfilDAO.remove(usuario, perfisAux);
        
        // Adicionando os novos Perfis
        perfisAux = new ArrayList<>(perfis);
        perfisAux.removeAll(perfisAntigos);
        usuarioPerfilDAO.create(usuario, perfisAux);
    }
    
    public List<TbPerfil> getPerfisAtivos(TbUsuario usuario) throws Exception {
        if(usuario == null || usuario.getIdUsuario() == null) {
            return null;
        }
        
        String select = "SELECT p "
                + "FROM TbPerfil p "
                + "WHERE p.idPerfil IN ("
                + "  SELECT up.idPerfil.idPerfil "
                + "  FROM TbUsuarioxPerfil up "
                + "  WHERE up.idUsuario.idUsuario = :idUsuario"
                + "  AND up.dtDesativacao IS NULL"
                + ")";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idUsuario", usuario.getIdUsuario());
        
        return selectList(select, params);
    }
    
}

