package br.com.xti.ouvidoria.controller;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.util.JSFUtils;

/**
 * @author Samuel Correia Guimarães
 */
public class MensagemFaceUtil {

    public static void info(String titulo, String mensagem) {
        show(FacesMessage.SEVERITY_INFO, titulo, mensagem, null);
        JSFUtils.showGrowl();
    }
    
    public static void info(String titulo, String mensagem, String clientId) {
    	show(FacesMessage.SEVERITY_INFO, titulo, mensagem, clientId);
    	JSFUtils.showGrowl();
    }

    public static void infoSemGrowl(String titulo, String mensagem) {
        show(FacesMessage.SEVERITY_INFO, titulo, mensagem, null);
    }

    public static void alerta(String titulo, String mensagem) {
        show(FacesMessage.SEVERITY_WARN, titulo, mensagem, null);
        JSFUtils.showGrowl();
    }
    
    public static void alerta(String titulo, String mensagem, String clientId) {
    	show(FacesMessage.SEVERITY_WARN, titulo, mensagem, clientId);
    }

    public static void erro(String titulo, String mensagem) {
        show(FacesMessage.SEVERITY_ERROR, titulo, mensagem, null);
        JSFUtils.showGrowl();
    }

    public static void fatal(String titulo, String mensagem) {
        show(FacesMessage.SEVERITY_FATAL, titulo, mensagem, null);
        JSFUtils.showGrowl();
    }

    private static void show(Severity severity, String titulo, String mensagem, String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.addMessage(clientId, new FacesMessage(severity, titulo, mensagem));
    }
}
