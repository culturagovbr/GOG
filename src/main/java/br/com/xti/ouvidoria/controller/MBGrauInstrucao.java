package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.GrauInstrucaoDAO;
import br.com.xti.ouvidoria.model.TbGrauInstrucao;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBGrauInstrucao")
@ViewScoped
public class MBGrauInstrucao implements Serializable {

    @EJB
    private GrauInstrucaoDAO dao;
    private TbGrauInstrucao grauInstrucao = new TbGrauInstrucao();
    private TbGrauInstrucao grauInstrucaoNovo = new TbGrauInstrucao();
    
    private void limpar() {
        grauInstrucao = new TbGrauInstrucao();
        grauInstrucaoNovo = new TbGrauInstrucao();
    }

    public List<TbGrauInstrucao> getTodos() {
    	List<TbGrauInstrucao> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getGrauInstrucaoNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getGrauInstrucao() != null && getGrauInstrucao().getIdGrauInstrucao() != null) {
                getDao().edit(getGrauInstrucao());
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
            if (getGrauInstrucao() != null && getGrauInstrucao().getIdGrauInstrucao() != null) {
                getDao().remove(getGrauInstrucao());
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
    public GrauInstrucaoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(GrauInstrucaoDAO dao) {
        this.dao = dao;
    }

    public void setGrauInstrucao(TbGrauInstrucao grauInstrucao) {
        this.grauInstrucao = grauInstrucao;
    }

    public void setGrauInstrucaoNovo(TbGrauInstrucao grauInstrucaoNovo) {
        this.grauInstrucaoNovo = grauInstrucaoNovo;
    }

    public TbGrauInstrucao getGrauInstrucao() {
        return grauInstrucao;
    }

    public TbGrauInstrucao getGrauInstrucaoNovo() {
        return grauInstrucaoNovo;
    }
}