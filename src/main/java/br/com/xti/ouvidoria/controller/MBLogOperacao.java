package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.xti.ouvidoria.dao.LogOperacaoDAO;
import br.com.xti.ouvidoria.model.TbLogOperacao;
import br.com.xti.ouvidoria.model.enums.LogOperacaoEnum;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class MBLogOperacao implements Serializable {

    private LogOperacaoEnum tipo;
    private Integer idUsuario;
    private Date dataInicial;
    private Date dataFinal;
    @EJB
    private LogOperacaoDAO dao;
    private List<TbLogOperacao> resultado = new ArrayList<TbLogOperacao>();

    public List<TbLogOperacao> getResultado() {
        return resultado;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void consultar() {
        try {
            resultado = dao.searchOperacoes(tipo, idUsuario, dataInicial, dataFinal);
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Erro ao consultar Logs", "Ocorreu um erro ao consultar os logs.");
        }
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LogOperacaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(LogOperacaoEnum tipo) {
        this.tipo = tipo;
    }

    public MBLogOperacao() {
    }

    public LogOperacaoEnum[] getTipos() {

        return LogOperacaoEnum.values();
    }
}
