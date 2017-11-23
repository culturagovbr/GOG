package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.PreferenciaSistemaDAO;
import br.com.xti.ouvidoria.dao.TipoManifestacaoDAO;
import br.com.xti.ouvidoria.model.TbPreferenciaSistema;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBTipoManifestacao")
@ViewScoped
public class MBTipoManifestacao implements Serializable {

    @EJB
    private TipoManifestacaoDAO dao;
    @EJB
    private PreferenciaSistemaDAO preferenciaSistemaDAO;
    private TbTipoManifestacao tipomanifestacao = new TbTipoManifestacao();
    private TbTipoManifestacao tipomanifestacaoNovo = new TbTipoManifestacao();
    private Boolean definirPrazo;
    private TbPreferenciaSistema preferenciaSistema;

    @PostConstruct
    public void init() {
        preferenciaSistema = preferenciaSistemaDAO.findAll().get(0);
    }

    public void ajustaPrazoPadraoNovo() {
        tipomanifestacaoNovo.setPrazoAreaSolucionadora(preferenciaSistema.getPrazoAreaSolucionadora());
        tipomanifestacaoNovo.setPrazoEntrada(preferenciaSistema.getPrazoEntrada());
        tipomanifestacaoNovo.setPrazoRespostaCidadao(preferenciaSistema.getPrazoRespostaCidadao());
        tipomanifestacao.setPrazoAreaSolucionadora(preferenciaSistema.getPrazoAreaSolucionadora());
        tipomanifestacao.setPrazoEntrada(preferenciaSistema.getPrazoEntrada());
        tipomanifestacao.setPrazoRespostaCidadao(preferenciaSistema.getPrazoRespostaCidadao());
    }

    public void zeraPrazoPadraoNovo() {
        tipomanifestacaoNovo.setPrazoAreaSolucionadora(0);
        tipomanifestacaoNovo.setPrazoEntrada(0);
        tipomanifestacaoNovo.setPrazoRespostaCidadao(0);
        tipomanifestacao.setPrazoAreaSolucionadora(0);
        tipomanifestacao.setPrazoEntrada(0);
        tipomanifestacao.setPrazoRespostaCidadao(0);
    }

    public Boolean getDefinirPrazo() {
        return definirPrazo;
    }

    public void setDefinirPrazo(Boolean definirPrazo) {
        this.definirPrazo = definirPrazo;
    }

    public TbPreferenciaSistema getPreferencias() {
        return preferenciaSistemaDAO.getPreferenciaSistema();
    }
    
    public List<TbTipoManifestacao> getTodos() {
    	List<TbTipoManifestacao> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    private void limpar() {
        tipomanifestacao = new TbTipoManifestacao();
        tipomanifestacaoNovo = new TbTipoManifestacao();
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            getDao().create(getTipomanifestacaoNovo());
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getTipomanifestacao() != null && getTipomanifestacao().getIdTipoManifestacao() != null) {
                getDao().edit(getTipomanifestacao());
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
            if (getTipomanifestacao() != null && getTipomanifestacao().getIdTipoManifestacao() != null) {
                getDao().remove(getTipomanifestacao());
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
    public TipoManifestacaoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(TipoManifestacaoDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the tipomanifestacao
     */
    public TbTipoManifestacao getTipomanifestacao() {
        return tipomanifestacao;
    }

    /**
     * @param tipomanifestacao the tipomanifestacao to set
     */
    public void setTipomanifestacao(TbTipoManifestacao tipomanifestacao) {
        this.tipomanifestacao = tipomanifestacao;
    }

    /**
     * @return the tipomanifestacaoNovo
     */
    public TbTipoManifestacao getTipomanifestacaoNovo() {
        return tipomanifestacaoNovo;
    }

    /**
     * @param tipomanifestacaoNovo the tipomanifestacaoNovo to set
     */
    public void setTipomanifestacaoNovo(TbTipoManifestacao tipomanifestacaoNovo) {
        this.tipomanifestacaoNovo = tipomanifestacaoNovo;
    }
}