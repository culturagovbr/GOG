package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.MeioEntradaDAO;
import br.com.xti.ouvidoria.model.TbMeioEntrada;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBMeioEntrada")
@ViewScoped
public class MBMeioEntrada implements Serializable {

    @EJB
    private MeioEntradaDAO dao;
    private TbMeioEntrada meioEntrada = new TbMeioEntrada();
    private TbMeioEntrada meioEntradaNovo = new TbMeioEntrada();

    public List<TbMeioEntrada> getTodos() {
    	List<TbMeioEntrada> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getMeioEntradaNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();

    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getMeioEntrada() != null && getMeioEntrada().getIdMeioEntrada() != null) {
                getDao().edit(getMeioEntrada());
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
            if (getMeioEntrada() != null && getMeioEntrada().getIdMeioEntrada() != null) {
                getDao().remove(getMeioEntrada());
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro não pode ser excluído porque a tabela inclui registros relacionados.");
        }
        limpar();
    }

    private void limpar() {
//        mBDominio.resetTodosMeioEntrada();
        meioEntrada = new TbMeioEntrada();
        meioEntradaNovo = new TbMeioEntrada();
    }

    /**
     * @return the dao
     */
    public MeioEntradaDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(MeioEntradaDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the meioEntrada
     */
    public TbMeioEntrada getMeioEntrada() {
        return meioEntrada;
    }

    /**
     * @param meioEntrada the meioEntrada to set
     */
    public void setMeioEntrada(TbMeioEntrada meioEntrada) {
        this.meioEntrada = meioEntrada;
    }

    /**
     * @return the meioEntradaNovo
     */
    public TbMeioEntrada getMeioEntradaNovo() {
        return meioEntradaNovo;
    }

    /**
     * @param meioEntradaNovo the meioEntradaNovo to set
     */
    public void setMeioEntradaNovo(TbMeioEntrada meioEntradaNovo) {
        this.meioEntradaNovo = meioEntradaNovo;
    }
}