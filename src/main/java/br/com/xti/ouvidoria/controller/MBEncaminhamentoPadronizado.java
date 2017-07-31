package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.EncaminhamentoPadronizadoDAO;
import br.com.xti.ouvidoria.model.TbEncaminhamentoPadronizado;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBEncaminhamentoPadronizado")
@ViewScoped
public class MBEncaminhamentoPadronizado implements Serializable {

    @EJB
    private EncaminhamentoPadronizadoDAO dao;
    private TbEncaminhamentoPadronizado encaminhamentoPadronizado = new TbEncaminhamentoPadronizado();
    private TbEncaminhamentoPadronizado encaminhamentoPadronizadoNovo = new TbEncaminhamentoPadronizado();
    
    private void limpar() {
        encaminhamentoPadronizado = new TbEncaminhamentoPadronizado();
        encaminhamentoPadronizadoNovo = new TbEncaminhamentoPadronizado();
    }

    public List<TbEncaminhamentoPadronizado> getTodos() {
    	List<TbEncaminhamentoPadronizado> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            encaminhamentoPadronizadoNovo.setDtCadastro(new Date());
            getDao().create(getEncaminhamentoPadronizadoNovo());
            limpar();
            MensagemFaceUtil.info("Sucesso", "Inclusão realizada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        setEncaminhamentoPadronizadoNovo(new TbEncaminhamentoPadronizado());
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getEncaminhamentoPadronizado() != null && getEncaminhamentoPadronizado().getIdEncaminhamentoPadronizado() != null) {
                getDao().edit(getEncaminhamentoPadronizado());
                limpar();
                MensagemFaceUtil.info("Sucesso", "Alteração realizada com sucesso");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
    }

    public void remover(ActionEvent actionEvent) {
        try {
            if (getEncaminhamentoPadronizado() != null && getEncaminhamentoPadronizado().getIdEncaminhamentoPadronizado() != null) {
                getDao().remove(getEncaminhamentoPadronizado());

                MensagemFaceUtil.info("Sucesso", "Exclusão realizada com sucesso");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
    }

    public Date getDataAtual() {

        return new Date();
    }

    /**
     * @return the dao
     */
    public EncaminhamentoPadronizadoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(EncaminhamentoPadronizadoDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the encaminhamentoPadronizado
     */
    public TbEncaminhamentoPadronizado getEncaminhamentoPadronizado() {
        return encaminhamentoPadronizado;
    }

    /**
     * @param encaminhamentoPadronizado the encaminhamentoPadronizado to set
     */
    public void setEncaminhamentoPadronizado(TbEncaminhamentoPadronizado encaminhamentoPadronizado) {
        this.encaminhamentoPadronizado = encaminhamentoPadronizado;
    }

    /**
     * @return the encaminhamentoPadronizadoNovo
     */
    public TbEncaminhamentoPadronizado getEncaminhamentoPadronizadoNovo() {
        return encaminhamentoPadronizadoNovo;
    }

    /**
     * @param encaminhamentoPadronizadoNovo the encaminhamentoPadronizadoNovo to
     * set
     */
    public void setEncaminhamentoPadronizadoNovo(TbEncaminhamentoPadronizado encaminhamentoPadronizadoNovo) {
        this.encaminhamentoPadronizadoNovo = encaminhamentoPadronizadoNovo;
    }
}