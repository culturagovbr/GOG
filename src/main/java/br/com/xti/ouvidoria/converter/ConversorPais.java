package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.PaisDAO;
import br.com.xti.ouvidoria.model.TbPais;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorPais extends ConversorBase {
	
	@EJB
	private PaisDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbPais pais = null;
        if(value == null || value.isEmpty()) {
            return pais;
        }
        
        try {
            pais = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return pais;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbPais) {
                r = ((TbPais) value).getIdPais().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
