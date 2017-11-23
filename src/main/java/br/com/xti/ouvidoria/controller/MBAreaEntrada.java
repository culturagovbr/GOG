package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.AreaEntradaDAO;
import br.com.xti.ouvidoria.model.TbAreaEntrada;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBAreaEntrada")
@ViewScoped
public class MBAreaEntrada implements Serializable {

    @EJB
    private AreaEntradaDAO dao;
    private TbAreaEntrada areaEntrada = new TbAreaEntrada();
    private TbAreaEntrada areaNovo = new TbAreaEntrada();
    private List<TbAreaEntrada> areasFiltradas;

    public List<TbAreaEntrada> getAreasFiltradas() {
        return areasFiltradas;
    }

    public void setAreasFiltradas(List<TbAreaEntrada> areasFiltradas) {
        this.areasFiltradas = areasFiltradas;
    }
    
    public TbAreaEntrada getAreaNovo() {
        return areaNovo;
    }

    public void setAreaNovo(TbAreaEntrada areaNovo) {
        this.areaNovo = areaNovo;
    }

    public MBAreaEntrada() {
        areaEntrada = new TbAreaEntrada();
    }

    public TbAreaEntrada getAreaEntrada() {
        return areaEntrada;
    }

    public void setAreaEntrada(TbAreaEntrada areaEntrada) {
        this.areaEntrada = areaEntrada;
    }

    public List<TbAreaEntrada> getTodos() {
    	List<TbAreaEntrada> list = dao.findAll();
    	Collections.sort(list);
    	return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            dao.create(areaNovo);
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");

        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    private void limpar() {
        areaEntrada = new TbAreaEntrada();
        areaNovo = new TbAreaEntrada();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (areaEntrada != null && areaEntrada.getIdAreaEntrada() != null) {
                dao.edit(areaEntrada);
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
            if (areaEntrada != null && areaEntrada.getIdAreaEntrada() != null) {
                dao.remove(areaEntrada);
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro não pode ser excluído porque a tabela inclui registros relacionados.");
        }
        limpar();
    }
}