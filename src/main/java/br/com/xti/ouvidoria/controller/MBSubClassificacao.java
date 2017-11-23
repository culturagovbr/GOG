package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBSubClassificacao")
@ViewScoped
public class MBSubClassificacao implements Serializable {

    @EJB
    private SubClassificacaoDAO dao;
    @EJB
    private ClassificacaoDAO classificacaoDAO;
    private TbSubClassificacao subclassificacao = new TbSubClassificacao();
    private TbSubClassificacao subclassificacaoNovo = new TbSubClassificacao();
    private Integer idClassificacao;
    private Integer idSubClassificacao;
    private DualListModel<TbClassificacao> listaClassificacao = new DualListModel<>();

    @PostConstruct
    public void init() {
        limpar();
    }

    public void setListaClassificacao(DualListModel<TbClassificacao> listaClassificacao) {
        this.listaClassificacao = listaClassificacao;
    }

    public DualListModel<TbClassificacao> getListaClassificacao() {
        return listaClassificacao;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void limpar() {
        subclassificacao = new TbSubClassificacao();
        subclassificacaoNovo = new TbSubClassificacao();
        idClassificacao = null;
        idClassificacao = null;
        List<TbClassificacao> disponiveis = classificacaoDAO.findAll();
        Collections.sort(disponiveis);
        listaClassificacao = new DualListModel<>(disponiveis, new ArrayList());
    }

    public List<TbSubClassificacao> getTodos() {
    	List<TbSubClassificacao> list = dao.findAll();
    	Collections.sort(list);
        return list;
    }

    public void setIdClassificacao(Integer idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    public Integer getIdClassificacao() {
        return idClassificacao;
    }

    public List<TbClassificacao> getTodasClassificacoes() {
    	List<TbClassificacao> list = classificacaoDAO.findAll();
    	Collections.sort(list);
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            subclassificacaoNovo.setTbClassificacaoCollection(listaClassificacao.getTarget());
            getDao().create(subclassificacaoNovo);
            
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        } finally {
            limpar();
        }
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getSubclassificacao() != null && getSubclassificacao().getIdSubClassificacao() != null) {
                subclassificacao.setTbClassificacaoCollection(listaClassificacao.getTarget());
                getDao().edit(subclassificacao);
                
                MensagemFaceUtil.info("Alteração realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        } finally {
            limpar();
        }
    }

    public void remover(ActionEvent actionEvent) {
        try {
            if (getSubclassificacao() != null && getSubclassificacao().getIdSubClassificacao() != null) {

                for (TbClassificacao classific : subclassificacao.getTbClassificacaoCollection()) {
                    TbClassificacao classificacao = classificacaoDAO.find(classific.getIdClassificacao());
                    classificacao.getTbSubClassificacaoCollection().remove(subclassificacao);
                    classificacaoDAO.edit(classificacao);
                }
                getDao().remove(getSubclassificacao());

                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "O registro não pode ser excluído porque a tabela inclui registros relacionados.");
        } finally {
            limpar();
        }
    }
    
    public List<TbClassificacao> ordenarClassificacoes(Collection<TbClassificacao> itens) {
    	if(itens != null) {
    		List<TbClassificacao> list = new ArrayList<TbClassificacao>(itens);
    		Collections.sort(list);
    		return list;
    	}
    	return null;
    }

    /**
     * @return the dao
     */
    public SubClassificacaoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(SubClassificacaoDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the subclassificacao
     */
    public TbSubClassificacao getSubclassificacao() {
        return subclassificacao;
    }

    /**
     * @param subclassificacao the subclassificacao to set
     */
    public void setSubclassificacao(TbSubClassificacao subclassificacao) {
        this.subclassificacao = subclassificacao;

        List<TbClassificacao> todas = classificacaoDAO.findAll();
    	Collections.sort(todas);

        ArrayList<TbClassificacao> todasClassificacoes = new ArrayList<>();
        for (TbClassificacao tbClassificacao : todas) {
            todasClassificacoes.add(tbClassificacao);
        }

        todasClassificacoes.removeAll(subclassificacao.getTbClassificacaoCollection());
        listaClassificacao = new DualListModel<>((List<TbClassificacao>) todasClassificacoes, (List<TbClassificacao>) subclassificacao.getTbClassificacaoCollection());

    }

    /**
     * @return the subclassificacaoNovo
     */
    public TbSubClassificacao getSubclassificacaoNovo() {
        return subclassificacaoNovo;
    }

    /**
     * @param subclassificacaoNovo the subclassificacaoNovo to set
     */
    public void setSubclassificacaoNovo(TbSubClassificacao subclassificacaoNovo) {
        this.subclassificacaoNovo = subclassificacaoNovo;
    }

    /**
     * @return the idSubClassificacao
     */
    public Integer getIdSubClassificacao() {
        return idSubClassificacao;
    }

    /**
     * @param idSubClassificacao the idSubClassificacao to set
     */
    public void setIdSubClassificacao(Integer idSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
    }
}