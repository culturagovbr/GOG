/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.TbUsuarioxPerfil;

/**
 *
 * @author renato
 */
@Stateless
public class UsuarioxPerfilDAO extends AbstractDAO<TbUsuarioxPerfil> {
    public UsuarioxPerfilDAO() {
        super(TbUsuarioxPerfil.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Usuário x Perfil";
    }
    
    public void create(TbUsuario usuario, List<TbPerfil> perfis) throws Exception {
        // Cria uma lista de 'TbUsuarioxPerfil' com o mesmo tamanho da lista de 'TbPerfil' passada por parâmetro
        List<TbUsuarioxPerfil> listaUsuarioPerfis = new ArrayList<TbUsuarioxPerfil>(perfis.size());
        
        // Popula a lista recém criada com os perfis a serem cadastrados
        for (TbPerfil perfil : perfis) {
            TbUsuarioxPerfil tbUsuarioxPerfil = new TbUsuarioxPerfil();
            tbUsuarioxPerfil.setIdUsuario(usuario);
            tbUsuarioxPerfil.setIdPerfil(perfil);
            tbUsuarioxPerfil.setDtAtivacao(new Date());
            listaUsuarioPerfis.add(tbUsuarioxPerfil);
        }
        
        // Realiza a persistência dos dados no banco
        for (TbUsuarioxPerfil usuarioPerfil : listaUsuarioPerfis) {
            create(usuarioPerfil);
        }
    }
    
    public void remove(TbUsuario usuario, List<TbPerfil> perfis) throws Exception {
        List<TbUsuarioxPerfil> usuarioPerfis = getPerfisUsuarios(usuario.getIdUsuario(), getIds(perfis));
        
        for (TbUsuarioxPerfil p : usuarioPerfis) {
            p.setDtDesativacao(new Date());
        }
    }
    
    private List<TbUsuarioxPerfil> getPerfisUsuarios(Integer idUsuario, List<Integer> perfis) throws Exception {
        if(perfis.isEmpty() || perfis == null) {
            return new ArrayList<TbUsuarioxPerfil>();
        }
        
        String select = "SELECT p FROM TbUsuarioxPerfil p WHERE p.idUsuario.idUsuario = :idUsuario AND p.idPerfil.idPerfil IN :idPerfis";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idUsuario", idUsuario);
        params.put("idPerfis", perfis);
        
        return selectList(select, params);
    }
    
    private List<Integer> getIds(List<TbPerfil> perfis) {
        List<Integer> ids = new ArrayList<Integer>();
        for (TbPerfil p : perfis) {
            ids.add(p.getIdPerfil());
        }
        return ids;
    }
    
}
