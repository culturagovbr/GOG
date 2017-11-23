package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.UfDAO;
import br.com.xti.ouvidoria.model.TbUF;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorEstado extends ConversorBase {
	
	@EJB
	private UfDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbUF estado = null;
        if(value == null || value.isEmpty()) {
            return estado;
        }
        
        try {
            estado = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return estado;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbUF) {
                r = ((TbUF) value).getIdUF().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
