package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.PrioridadeDAO;
import br.com.xti.ouvidoria.model.TbPrioridade;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBPrioridade")
@ViewScoped
public class MBPrioridade implements Serializable {

    @EJB
    private PrioridadeDAO dao;
    private TbPrioridade prioridade = new TbPrioridade();
    private TbPrioridade prioridadeNovo = new TbPrioridade();
    
    private void limpar() {
        prioridade = new TbPrioridade();
        prioridadeNovo = new TbPrioridade();
    }

    public List<TbPrioridade> getTodos() {
    	List<TbPrioridade> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getPrioridadeNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getPrioridade() != null && getPrioridade().getIdPrioridade() != null) {
                getDao().edit(getPrioridade());
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
            if (getPrioridade() != null && getPrioridade().getIdPrioridade() != null) {
                getDao().remove(getPrioridade());
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
    public PrioridadeDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(PrioridadeDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the prioridade
     */
    public TbPrioridade getPrioridade() {
        return prioridade;
    }

    /**
     * @param prioridade the prioridade to set
     */
    public void setPrioridade(TbPrioridade prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * @return the prioridadeNovo
     */
    public TbPrioridade getPrioridadeNovo() {
        return prioridadeNovo;
    }

    /**
     * @param prioridadeNovo the prioridadeNovo to set
     */
    public void setPrioridadeNovo(TbPrioridade prioridadeNovo) {
        this.prioridadeNovo = prioridadeNovo;
    }
}