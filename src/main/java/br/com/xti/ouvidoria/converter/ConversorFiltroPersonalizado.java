package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.FiltroPersonalizadoDAO;
import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorFiltroPersonalizado extends ConversorBase {
	
	@EJB
	private FiltroPersonalizadoDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbFiltroPersonalizado filtroPersonalizado = null;
        if(value == null || value.isEmpty()) {
            return filtroPersonalizado;
        }
        
        try {
            filtroPersonalizado = dao.find(Integer.parseInt(value));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return filtroPersonalizado;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbFiltroPersonalizado) {
                r = ((TbFiltroPersonalizado) value).getIdFiltroPersonalizado().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
