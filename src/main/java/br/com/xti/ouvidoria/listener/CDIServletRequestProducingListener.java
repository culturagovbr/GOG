package br.com.xti.ouvidoria.listener;

import javax.enterprise.inject.Produces;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Listener responsável por produzir os objetos HttpServletRequest
 * para serem injetados utilizando CDI. Exemplo de como utilizar:</p>
 * 
 * <pre>
 * <code>@Inject
 * private HttpServletRequest request;</code>
 * </pre>
 * 
 * @author Samuel Correia Guimarães
 * @since v1.2.0 - 12/11/2014
 *
 */
@WebListener
public class CDIServletRequestProducingListener implements ServletRequestListener {

    private static ThreadLocal<ServletRequest> SERVLET_REQUESTS = new ThreadLocal<>();

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        SERVLET_REQUESTS.set(sre.getServletRequest());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        SERVLET_REQUESTS.remove();
    }

    @Produces
    private ServletRequest obtain() {
        return SERVLET_REQUESTS.get();
    }
    
    @Produces
    private HttpServletRequest obtainHttpServletRequest() {
    	return (HttpServletRequest) SERVLET_REQUESTS.get();
    }

}