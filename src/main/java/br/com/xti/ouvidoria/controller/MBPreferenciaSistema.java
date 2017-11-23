package br.com.xti.ouvidoria.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import br.com.xti.ouvidoria.dao.PreferenciaSistemaDAO;
import br.com.xti.ouvidoria.model.TbPreferenciaSistema;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBPreferenciaSistema")
@ViewScoped
public class MBPreferenciaSistema implements Serializable {

    @EJB private PreferenciaSistemaDAO dao;
    private TbPreferenciaSistema preferencias;
    
    @PostConstruct
    public void init() {
        try {
            preferencias = dao.findAll().get(0);
        } catch (IndexOutOfBoundsException e) {
            preferencias = new TbPreferenciaSistema();
        }
    }

    public void atualizar(ActionEvent actionEvent) {
        try {
            if (preferencias.getIdPreferenciaSistema() == null) {
                dao.create(preferencias);
            } else {
                dao.edit(preferencias);
            }
            MensagemFaceUtil.info("Sucesso", "Prefêrencias do sistema atualizadas com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao atualizar as preferências do sistema");
        }
    }

    public TbPreferenciaSistema getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(TbPreferenciaSistema preferencias) {
        this.preferencias = preferencias;
    }

    public Boolean getSsl() {
        return (preferencias.getSslEmail() == null ? BooleanEnum.SIM.getId() == null : preferencias.getSslEmail().equals(BooleanEnum.SIM.getId()));
    }

    public void setSsl(Boolean ssl) {
        preferencias.setSslEmail(ssl ? BooleanEnum.SIM.getId() : BooleanEnum.NAO.getId());
    }

    public Boolean getEncerrarTramiteEncaminhada() {
        return (preferencias.getEncerrarTramiteEncaminhada() == null ? BooleanEnum.SIM.getId() == null : preferencias.getEncerrarTramiteEncaminhada().equals(BooleanEnum.SIM.getId()));
    }

    public void setEncerrarTramiteEncaminhada(Boolean encerrarTramiteEncaminhada) {
        preferencias.setEncerrarTramiteEncaminhada(encerrarTramiteEncaminhada ? BooleanEnum.SIM.getId() : BooleanEnum.NAO.getId());
    }

    public Boolean getRetornarTramiteOuvidoria() {
        return (preferencias.getRetornarTramiteOuvidoria() == null ? BooleanEnum.SIM.getId() == null : preferencias.getRetornarTramiteOuvidoria().equals(BooleanEnum.SIM.getId()));
    }

    public void setRetornarTramiteOuvidoria(Boolean retornarTramiteOuvidoria) {
        preferencias.setRetornarTramiteOuvidoria(retornarTramiteOuvidoria ? BooleanEnum.SIM.getId() : BooleanEnum.NAO.getId());
    }

    public Boolean getCtlPrazoManifSoluc() {
        return (preferencias.getCtlPrazoManifSoluc() == null ? BooleanEnum.SIM.getId() == null : preferencias.getCtlPrazoManifSoluc().equals(BooleanEnum.SIM.getId()));
    }

    public void setCtlPrazoManifSoluc(Boolean ctlPrazoManifSoluc) {
        preferencias.setCtlPrazoManifSoluc(ctlPrazoManifSoluc ? BooleanEnum.SIM.getId() : BooleanEnum.NAO.getId());
    }

    public Boolean getRespostasImediatas() {
        return (preferencias.getRespostasImediatas() == null ? BooleanEnum.SIM.getId() == null : preferencias.getRespostasImediatas().equals(BooleanEnum.SIM.getId()));
    }

    public void setRespostasImediatas(Boolean respostasImediatas) {
        preferencias.setRespostasImediatas(respostasImediatas ? BooleanEnum.SIM.getId() : BooleanEnum.NAO.getId());
    }
    
}