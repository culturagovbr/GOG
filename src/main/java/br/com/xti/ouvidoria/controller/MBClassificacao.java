package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;

import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.SubClassificacaoDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbUnidade;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBClassificacao")
@ViewScoped
public class MBClassificacao implements Serializable {

    @EJB private ClassificacaoDAO dao;
    @EJB private SubClassificacaoDAO subClassificacaoDAO;
    @EJB private UnidadeDAO unidadeDAO;
    
    private TbClassificacao classificacao = new TbClassificacao();
    private TbClassificacao classificacaoNovo = new TbClassificacao();
    private DualListModel<TbUnidade> listaUnidades;
    private DualListModel<TbSubClassificacao> listaSubclassificacao;

    @PostConstruct
    public void init() {
        limpar();
    }

    public DualListModel<TbSubClassificacao> getListaSubclassificacao() {
        return listaSubclassificacao;
    }

    public void setListaSubclassificacao(DualListModel<TbSubClassificacao> listaSubclassificacao) {
        this.listaSubclassificacao = listaSubclassificacao;
    }

    public DualListModel<TbUnidade> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(DualListModel<TbUnidade> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void limpar() {
        classificacao = new TbClassificacao();
        classificacaoNovo = new TbClassificacao();
        List<TbUnidade> listaTodasUnidades = unidadeDAO.findAll();
    	Collections.sort(listaTodasUnidades);
        listaUnidades = new DualListModel<>(listaTodasUnidades, new ArrayList());
        List<TbSubClassificacao> listaTodasSubclassificacoes = subClassificacaoDAO.findAll();
        Collections.sort(listaTodasSubclassificacoes);
        listaSubclassificacao = new DualListModel<>(listaTodasSubclassificacoes, new ArrayList());
    }

    public List<TbClassificacao> getTodos() {
    	List<TbClassificacao> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            classificacaoNovo.setTbUnidadeCollection(listaUnidades.getTarget());
            classificacaoNovo.setTbSubClassificacaoCollection(listaSubclassificacao.getTarget());
            getDao().create(classificacaoNovo);

            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();

    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getClassificacao() != null && getClassificacao().getIdClassificacao() != null) {
                classificacao.setTbUnidadeCollection(listaUnidades.getTarget());
                
                // Adiciona a clasificação nas Unidades escolhidas, se necessário
                for (TbUnidade u : listaUnidades.getTarget()) {
					if(!(u.getTbClassificacaoCollection().contains(getClassificacao()))) {
						u.getTbClassificacaoCollection().add(getClassificacao());
						unidadeDAO.edit(u);
					}
				}
                // Remove a classificação das demais unidades (não escolhidas)
                for (TbUnidade u : listaUnidades.getSource()) {
                	if(u.getTbClassificacaoCollection().contains(getClassificacao())) {
                		u.getTbClassificacaoCollection().remove(getClassificacao());
                		unidadeDAO.edit(u);
                	}
                }

                for (TbSubClassificacao sub : classificacao.getTbSubClassificacaoCollection()) {
                    TbSubClassificacao tbSub = subClassificacaoDAO.find(sub.getIdSubClassificacao());
                    tbSub.getTbClassificacaoCollection().remove(classificacao);
                    subClassificacaoDAO.edit(tbSub);
                }

                classificacao.setTbSubClassificacaoCollection(listaSubclassificacao.getTarget());
                for (TbSubClassificacao sub : classificacao.getTbSubClassificacaoCollection()) {
                    TbSubClassificacao tbSub = subClassificacaoDAO.find(sub.getIdSubClassificacao());
                    tbSub.getTbClassificacaoCollection().add(classificacao);
                    subClassificacaoDAO.edit(tbSub);
                }
                
                getDao().edit(getClassificacao());
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
            if (getClassificacao() != null && getClassificacao().getIdClassificacao() != null) {
                
                for (TbSubClassificacao subclassif : classificacao.getTbSubClassificacaoCollection()) {
                    TbSubClassificacao subclassificacao = subClassificacaoDAO.find(subclassif.getIdSubClassificacao());
                    subclassificacao.getTbClassificacaoCollection().remove(getClassificacao());
                    subClassificacaoDAO.edit(subclassificacao);
                }
                getClassificacao().setTbSubClassificacaoCollection(null);
                getDao().remove(getClassificacao());
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
    public ClassificacaoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(ClassificacaoDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the classificacao
     */
    public TbClassificacao getClassificacao() {
        return classificacao;
    }

    /**
     * @param classificacao the classificacao to set
     */
    public void setClassificacao(TbClassificacao classificacao) {
        this.classificacao = classificacao;

        List<TbUnidade> todasUnidades = unidadeDAO.findAll();
        todasUnidades.removeAll(classificacao.getTbUnidadeCollection());
        listaUnidades = new DualListModel<>(todasUnidades, (List<TbUnidade>) classificacao.getTbUnidadeCollection());

        List<TbSubClassificacao> todasSubClassificacao = subClassificacaoDAO.findAll();
        todasSubClassificacao.removeAll(classificacao.getTbSubClassificacaoCollection());
        listaSubclassificacao = new DualListModel<>(todasSubClassificacao, (List<TbSubClassificacao>) classificacao.getTbSubClassificacaoCollection());
    }

    /**
     * @return the classificacaoNovo
     */
    public TbClassificacao getClassificacaoNovo() {
        return classificacaoNovo;
    }

    /**
     * @param classificacaoNovo the classificacaoNovo to set
     */
    public void setClassificacaoNovo(TbClassificacao classificacaoNovo) {
        this.classificacaoNovo = classificacaoNovo;
    }
}