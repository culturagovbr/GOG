package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;

import br.com.xti.ouvidoria.dao.PerfilDAO;
import br.com.xti.ouvidoria.dao.UsuarioxPerfilDAO;
import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbUsuario;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBUsuarioxPerfil")
@ViewScoped
public class MBUsuarioxPerfil implements Serializable {

    @EJB
    private UsuarioxPerfilDAO dao;
    @EJB
    private PerfilDAO perfilDAO;
    
    private DualListModel<TbPerfil> perfis = new DualListModel<TbPerfil>();
    private TbUsuario usuario = new TbUsuario();

	public void prepararAlterar(final int idUsuario) {
        try {
            // Recupera todos os perfis
            List<TbPerfil> disponiveis = perfilDAO.findAll();
            
            // Recupera os perfis ativos do usuário atual
            String select = "SELECT p FROM TbPerfil p WHERE p.idPerfil IN (SELECT up.idPerfil.idPerfil FROM TbUsuarioxPerfil up WHERE up.idUsuario.idUsuario = :idUsuario)";
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("idUsuario", idUsuario);
            List<TbPerfil> selecionados = perfilDAO.selectList(select, params);
            
            // Retira da lista 'todos' os perfis já ativos atualmente
            disponiveis.removeAll(selecionados);
            
            perfis.setSource(disponiveis);
            perfis.setTarget(selecionados);
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao alterar carregar os perfis desse usuário");
        }
    }
    
    public void alterar(ActionEvent actionEvent) {
//        try {
//            if (getPerfil() != null && getPerfil().getIdPerfil() != null) {
//                getDao().edit(getPerfil());
//                setPerfil(new TbPerfil());
//                MensagemFaceUtil.info("Sucesso", "Perfil alterada com sucesso");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao alterar a perfil");
//        }
    }

    public UsuarioxPerfilDAO getDao() {
        return dao;
    }

    public void setDao(UsuarioxPerfilDAO dao) {
        this.dao = dao;
    }

    public PerfilDAO getPerfilDAO() {
        return perfilDAO;
    }

    public void setPerfilDAO(PerfilDAO perfilDAO) {
        this.perfilDAO = perfilDAO;
    }

    public DualListModel<TbPerfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(DualListModel<TbPerfil> perfis) {
        this.perfis = perfis;
    }

    public TbUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TbUsuario usuario) {
        this.usuario = usuario;
    }
    
}