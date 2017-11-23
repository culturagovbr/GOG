/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.TipoUsuarioEnum;
import br.com.xti.ouvidoria.util.PasswordUtils;

/**
 *
 * @author renato
 */
@Stateless
public class UsuarioDAO extends AbstractDAO<TbUsuario> {
    
    public UsuarioDAO() {
        super(TbUsuario.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Usuário";
    }

    public List<TbUsuario> getUsuariosInternos() {
        List<TbUsuario> listaUsuarios = null;
        try {
        	HashMap<String, Object> map = new HashMap<>();
            map.put("tpUsuario", TipoUsuarioEnum.INTERNO.getId());
            listaUsuarios = getList("findByTpUsuario", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    public List<TbUsuario> getUsuariosExternos() {
        List<TbUsuario> listaUsuarios = null;
        try {
        	HashMap<String, Object> map = new HashMap<>();
            map.put("tpUsuario", TipoUsuarioEnum.EXTERNO.getId());
            listaUsuarios = getList("findByTpUsuario", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    public TbUsuario login(String login, String password) throws Exception {
        String select = "SELECT t FROM TbUsuario t WHERE t.nmLogin = :nmLogin and t.nmSenha = :nmSenha";

        HashMap<String, Object> map = new HashMap<>();
        map.put("nmLogin", login);
        map.put("nmSenha", PasswordUtils.getMD5(password).toUpperCase());
        TbUsuario tbUsuario = selectObject(select, map);

        return tbUsuario;
    }

    public TbUsuario findByEmail(String eeEmail) throws NonUniqueResultException {
    	TbUsuario usuario = null;
    	try {
    		String select = "SELECT t FROM TbUsuario t WHERE t.eeEmail = :eeEmail";
    		HashMap<String, Object> map = new HashMap<>();
    		map.put("eeEmail", eeEmail);
    		usuario = selectObject(select, map);
    	} catch (NonUniqueResultException e) {
    		throw e;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return usuario;
    }

    public TbUsuario findByLogin(String login) throws Exception {
        String select = "SELECT t FROM TbUsuario t WHERE t.nmLogin = :nmLogin";

        HashMap<String, Object> map = new HashMap<>();
        map.put("nmLogin", login);
        TbUsuario tbUsuario = selectObject(select, map);

        return tbUsuario;
    }

    public Collection<TbUsuario> findOuvidorByUnidade(Integer unidade) throws Exception {
        String select = "SELECT t FROM TbUsuario t WHERE t.tpFuncao = :tpFuncao ";

        HashMap<String, Object> map = new HashMap<>();
        if (unidade != null && unidade!=0) {
            select += " and t.idUnidade.idUnidade = :idUnidade";
            map.put("idUnidade", unidade);
        }
        map.put("tpFuncao", FuncaoUsuarioEnum.OUVIDOR.getId());
        Collection<TbUsuario> tbUsuario = selectList(select, map);

        return tbUsuario;
    }
    
    public Collection<TbUsuario> findByUnidade(Integer unidade) {
        try {
            String select = "SELECT t FROM TbUsuario t ";

            HashMap<String, Object> map = new HashMap<>();
            if (unidade != null) {
                select += "WHERE t.idUnidade.idUnidade = :idUnidade";
                map.put("idUnidade", unidade);
            }
            Collection<TbUsuario> tbUsuario = selectList(select, map);

            return tbUsuario;
        } catch (Exception e) {
            return new ArrayList<TbUsuario>();
        }
    }
    
    public FuncaoUsuarioEnum getFuncaoUsuario(TbUsuario usuario) {
        return EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
    }
    
    public List<TbUsuario> getUsuarioPorFuncao(FuncaoUsuarioEnum... funcoesEscolhidas) {
        List<TbUsuario> listaUsuarios = new ArrayList<>();
        for (TbUsuario usuario : getUsuariosInternos()) {
            for(FuncaoUsuarioEnum funcao : funcoesEscolhidas) {
                if(funcao.getId().equals(usuario.getTpFuncao())) {
                    listaUsuarios.add(usuario);
                }
            }
        }
        
        return listaUsuarios;
    }
    
}
