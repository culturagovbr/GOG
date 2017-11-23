package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.MeioRespostaDAO;
import br.com.xti.ouvidoria.model.TbMeioResposta;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBMeioResposta")
@ViewScoped
public class MBMeioResposta implements Serializable {
    
    @EJB
    private MeioRespostaDAO dao;
    private TbMeioResposta meioResposta = new TbMeioResposta();
    private TbMeioResposta meioRespostaNovo = new TbMeioResposta();
    
    private void limpar() {
        meioResposta = new TbMeioResposta();
        meioRespostaNovo = new TbMeioResposta();
    }
    
    public List<TbMeioResposta> getTodos() {
    	List<TbMeioResposta> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }
    
    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getMeioRespostaNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public void alterar(ActionEvent actionEvent) {
        try {
            if (getMeioResposta() != null && getMeioResposta().getIdMeioResposta() != null) {
                getDao().edit(getMeioResposta());
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
            if (getMeioResposta() != null && getMeioResposta().getIdMeioResposta() != null) {
                getDao().remove(getMeioResposta());
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
    public MeioRespostaDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(MeioRespostaDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the prioridade
     */
    public TbMeioResposta getMeioResposta() {
        return meioResposta;
    }

    /**
     * @param prioridade the prioridade to set
     */
    public void setMeioResposta(TbMeioResposta prioridade) {
        this.meioResposta = prioridade;
    }

    /**
     * @return the prioridadeNovo
     */
    public TbMeioResposta getMeioRespostaNovo() {
        return meioRespostaNovo;
    }

    /**
     * @param prioridadeNovo the prioridadeNovo to set
     */
    public void setMeioRespostaNovo(TbMeioResposta prioridadeNovo) {
        this.meioRespostaNovo = prioridadeNovo;
    }
}