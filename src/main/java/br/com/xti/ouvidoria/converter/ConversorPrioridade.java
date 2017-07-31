package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.PrioridadeDAO;
import br.com.xti.ouvidoria.model.TbPrioridade;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorPrioridade extends ConversorBase {
	
	@EJB
	private PrioridadeDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbPrioridade prioridade = null;
        if(value == null || value.isEmpty()) {
            return prioridade;
        }
        
        try {
            prioridade = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return prioridade;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbPrioridade) {
                r = ((TbPrioridade) value).getIdPrioridade().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
