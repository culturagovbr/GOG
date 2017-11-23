package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;

/**
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBUnidade")
@ViewScoped
public class MBUnidade implements Serializable {

    @EJB
    private UnidadeDAO dao;
    
    @EJB
    private ClassificacaoDAO classificacaoDAO;
    
    private TbUnidade unidade = new TbUnidade();
    private TbUnidade unidadeNovo = new TbUnidade();
    
    public List<TbUnidade> getTodos() {
    	List<TbUnidade> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getUnidadeNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE")) {
                MensagemFaceUtil.erro("Erro", "Já existe uma unidade com este nome ou sigla");
            } else {
                MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
            }
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getUnidade() != null && getUnidade().getIdUnidade() != null) {
                getDao().edit(getUnidade());
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE")) {
                MensagemFaceUtil.erro("Erro", "Já existe uma unidade com este nome ou sigla");
            } else {
                MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
            }
        }
        limpar();
    }

    public void remover(ActionEvent actionEvent) {
        try {
            if (getUnidade() != null && getUnidade().getIdUnidade() != null) {
                getDao().remove(getUnidade());
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public boolean mostrarBotaoExcluir(TbUnidade u) {
    	if(u.getIdUnidade() == UnidadeEnum.OUVIDORIA.getId()) {
    		return false;
    	}
    	
    	if(ValidacaoHelper.isNotEmpty(u.getTbEncaminhamentoCollection().size())) {
    		return false;
    	}
    	
    	return true;
    }

    private void limpar() {
        unidade = new TbUnidade();
        unidadeNovo = new TbUnidade();
    }
    
    public UnidadeDAO getDao() {
        return dao;
    }

    public TbUnidade getUnidade() {
        return unidade;
    }

    public void setUnidade(TbUnidade unidade) {
        this.unidade = unidade;
    }

    public TbUnidade getUnidadeNovo() {
        return unidadeNovo;
    }

    public void setUnidadeNovo(TbUnidade unidadeNovo) {
        this.unidadeNovo = unidadeNovo;
    }
    
}