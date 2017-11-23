/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.util;

import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.primefaces.context.RequestContext;

/**
 * @author marcos.ribeiro
 * @author Samuel Correia Guimarães
 */
public class JSFUtils {

    public static void updateComponent(String... componentes) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.update(Arrays.asList(componentes));
    }

    public static void closeDialog(String dialog) {
        executeJavaScript(dialog + ".hide()");
    }
    
    public static void goToTopOfPage() {
    	executeJavaScript("jQuery('html, body').animate({ scrollTop:0 },'slow')");
    }

    public static void executeJavaScript(String... acoes) {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        for (String acao : acoes) {
            requestContext.execute(acao);
        }
    }

    public static void showGrowl() {
        updateComponent("formInfo:growl");
    }

    public static void redirect(String page) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + page);
    }
    
    public static Flash getFlash() {
    	return FacesContext.getCurrentInstance().getExternalContext().getFlash();
    }
    
    public static Object setFlashAttribute(String key, Object value) {
    	return FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, value);
    }
    
    public static void setFlashAttributeNow(String key, Object value) {
    	FacesContext.getCurrentInstance().getExternalContext().getFlash().putNow(key, value);
    }
    
    public static Object getFlashAttribute(String key) {
    	return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
    }
    
    public static Object setRequestAttribute(String key, Object value) {
    	return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);
    }
    
    public static Object getRequestAttribute(String key) {
    	return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);
    }
    
    public static Object setSessionAttribute(String key, Object value) {
    	return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }
    
    public static Object getSessionAttribute(String key) {
    	return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }
    
}
