package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;

import br.com.xti.ouvidoria.dao.FuncionalidadeDAO;
import br.com.xti.ouvidoria.dao.PerfilDAO;
import br.com.xti.ouvidoria.model.TbFuncionalidade;
import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.enums.TipoPerfilEnum;

/**
 * @author Samuel Correia Guimarães
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBPerfil")
@ViewScoped
public class MBPerfil implements Serializable {

    @EJB
    private PerfilDAO perfilDAO;
    
    @EJB
    private FuncionalidadeDAO funcDAO;
    
    
    private TbPerfil perfil = new TbPerfil();
    private TbPerfil perfilNovo = new TbPerfil();
    private DualListModel<TbFuncionalidade> funcionalidades = new DualListModel<TbFuncionalidade>();
    
    
    public List<TbPerfil> getTodos() {
    	List<TbPerfil> list = perfilDAO.findAll();
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            perfilNovo.setTpPerfil(TipoPerfilEnum.INTERNO.getId());
            perfilDAO.create(perfilNovo);
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getPerfil() != null && getPerfil().getIdPerfil() != null) {
                perfil.setTpPerfil(TipoPerfilEnum.INTERNO.getId());
                perfilDAO.edit(perfil);
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void atualizarFuncionalidade(ActionEvent actionEvent) {
        try {
            if (getPerfil() != null && getPerfil().getIdPerfil() != null) {
                funcDAO.atualizarFuncionalidades(perfil, converterEmListaFuncionalidade(funcionalidades.getTarget()));
                MensagemFaceUtil.info("Associação realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public void remover(ActionEvent actionEvent) {
        try {
            if (getPerfil() != null && getPerfil().getIdPerfil() != null) {
                perfilDAO.remove(getPerfil());
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    private void limpar() {
        perfil = new TbPerfil();
        perfilNovo = new TbPerfil();
        funcionalidades = new DualListModel<>();
    }

    @SuppressWarnings("rawtypes")
	public List<TbFuncionalidade> converterEmListaFuncionalidade(List idFuncionalidades) {
        List<TbFuncionalidade> listaFuncionalidades = new ArrayList<>();
        for (Object idFuncionalidade : idFuncionalidades) {
            listaFuncionalidades.add(funcDAO.find(Integer.parseInt((String) idFuncionalidade)));
        }
        return listaFuncionalidades;
    }
    
    public void prepararAlterarFuncionalidade(TbPerfil perfil) {
        try {
            // Recupera todos as funcionalidades
            List<TbFuncionalidade> disponiveis = funcDAO.findAll();
            
            // Recupera as funcionalidades ativas do perfil atual
            List<TbFuncionalidade> selecionados = funcDAO.getFuncionalidadesAtivas(perfil);
            
            // Retira da lista 'disponiveis' as funcionalidades já ativas atualmente
            disponiveis.removeAll(selecionados);

            funcionalidades.setSource(disponiveis);
            funcionalidades.setTarget(selecionados);
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
    }
    
    
    // SETTERS e SETTERS
    public TbPerfil getPerfil() {
        return perfil;
    }

    public void setPerfil(TbPerfil perfil) {
        this.perfil = perfil;
    }

    public TbPerfil getPerfilNovo() {
        return perfilNovo;
    }

    public void setPerfilNovo(TbPerfil perfilNovo) {
        this.perfilNovo = perfilNovo;
    }

    public DualListModel<TbFuncionalidade> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(DualListModel<TbFuncionalidade> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

}
