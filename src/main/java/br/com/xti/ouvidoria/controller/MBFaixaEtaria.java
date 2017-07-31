package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.FaixaEtariaDAO;
import br.com.xti.ouvidoria.model.TbFaixaEtaria;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBFaixaEtaria")
@ViewScoped
public class MBFaixaEtaria implements Serializable {

    @EJB
    private FaixaEtariaDAO dao;
    private TbFaixaEtaria faixaetaria = new TbFaixaEtaria();
    private TbFaixaEtaria faixaetariaNovo = new TbFaixaEtaria();
    
    private void limpar() {
        faixaetaria = new TbFaixaEtaria();
        faixaetariaNovo = new TbFaixaEtaria();
    }

    public List<TbFaixaEtaria> getTodos() {
    	List<TbFaixaEtaria> list = dao.findAll();
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getFaixaetariaNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getFaixaetaria() != null && getFaixaetaria().getIdFaixaEtaria() != null) {
                getDao().edit(getFaixaetaria());
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();

    }

    public void remover(ActionEvent actionEvent) {
        try {
            if (getFaixaetaria() != null && getFaixaetaria().getIdFaixaEtaria() != null) {
                getDao().remove(getFaixaetaria());
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro não pode ser excluído porque a tabela inclui registros relacionados.");
        }
        limpar();

    }

    /**
     * @return the dao
     */
    public FaixaEtariaDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(FaixaEtariaDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the faixaetaria
     */
    public TbFaixaEtaria getFaixaetaria() {
        return faixaetaria;
    }

    /**
     * @param faixaetaria the faixaetaria to set
     */
    public void setFaixaetaria(TbFaixaEtaria faixaetaria) {
        this.faixaetaria = faixaetaria;
    }

    /**
     * @return the faixaetariaNovo
     */
    public TbFaixaEtaria getFaixaetariaNovo() {
        return faixaetariaNovo;
    }

    /**
     * @param faixaetariaNovo the faixaetariaNovo to set
     */
    public void setFaixaetariaNovo(TbFaixaEtaria faixaetariaNovo) {
        this.faixaetariaNovo = faixaetariaNovo;
    }
}