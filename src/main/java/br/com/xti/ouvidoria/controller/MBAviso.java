package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.AvisoDAO;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.model.TbAviso;
import br.com.xti.ouvidoria.util.JSFUtils;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBAviso")
@ViewScoped
public class MBAviso implements Serializable {

    @EJB
    private AvisoDAO dao;
    private TbAviso aviso = new TbAviso();
    private TbAviso avisoNovo = new TbAviso();
    private List<TbAviso> avisosFiltrados;
    private boolean temAviso;
    
    public void limpar() {
        aviso = new TbAviso();
        avisoNovo = new TbAviso();
    }

    public List<TbAviso> getTodos() {
    	List<TbAviso> list = dao.findAll();
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            if (avisoNovo.getDtInicioAviso() != null && avisoNovo.getDtFimAviso() != null) {
                if (avisoNovo.getDtFimAviso().before(avisoNovo.getDtInicioAviso())) {
                    MensagemFaceUtil.erro("Data Final não pode ser menor que a Data Inicial", "");
                    return;
                }
            }
            
            avisoNovo.setDtInicioAviso(DataHelper.getDataMin(avisoNovo.getDtInicioAviso()));
            avisoNovo.setDtFimAviso(DataHelper.getDataMax(avisoNovo.getDtFimAviso()));
            
            getDao().create(avisoNovo);
            JSFUtils.closeDialog("dlgNovoAviso");
            MensagemFaceUtil.info("Inclusão realizada com sucesso.", "");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();

    }

    public void alterar(ActionEvent actionEvent) {
        try {
            if (getAviso() != null && getAviso().getIdAvisos() != null) {
                if (aviso.getDtInicioAviso() != null && aviso.getDtFimAviso() != null) {
                    if (aviso.getDtFimAviso().before(aviso.getDtInicioAviso())) {
                        MensagemFaceUtil.erro("Data Final não pode ser menor que a Data Inicial", "");
                        return;
                    }
                }
                
                aviso.setDtInicioAviso(DataHelper.getDataMin(aviso.getDtInicioAviso()));
                aviso.setDtFimAviso(DataHelper.getDataMax(aviso.getDtFimAviso()));
                
                getDao().edit(getAviso());
                JSFUtils.closeDialog("dlgEditarAviso");
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
            if (getAviso() != null && getAviso().getIdAvisos() != null) {
                getDao().remove(getAviso());
                MensagemFaceUtil.info("Exclusão realizada com sucesso.", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }
    
    public void getAvisoAtual() {
    	TbAviso aviso = dao.getAvisoAtual();
    	
    	if(aviso == null)
    		temAviso = Boolean.FALSE;
    	else {
    		this.aviso = aviso;
    		temAviso = Boolean.TRUE;
    	}
    }

    /**
     * @return the dao
     */
    public AvisoDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(AvisoDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the aviso
     */
    public TbAviso getAviso() {
        return aviso;
    }

    /**
     * @param aviso the aviso to set
     */
    public void setAviso(TbAviso aviso) {
        this.aviso = aviso;
    }

    /**
     * @return the avisoNovo
     */
    public TbAviso getAvisoNovo() {
        return avisoNovo;
    }

    /**
     * @param avisoNovo the avisoNovo to set
     */
    public void setAvisoNovo(TbAviso avisoNovo) {
        this.avisoNovo = avisoNovo;
    }

	public List<TbAviso> getAvisosFiltrados() {
		return avisosFiltrados;
	}

	public void setAvisosFiltrados(List<TbAviso> avisosFiltrados) {
		this.avisosFiltrados = avisosFiltrados;
	}

	public boolean isTemAviso() {
		return temAviso;
	}

	public void setTemAviso(boolean temAviso) {
		this.temAviso = temAviso;
	}
	
}