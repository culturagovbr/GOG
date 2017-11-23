package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.model.TbUnidade;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorUnidade extends ConversorBase {
	
	@EJB
	private UnidadeDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbUnidade unidade = null;
        if(value == null || value.isEmpty()) {
            return unidade;
        }
        
        try {
            unidade = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return unidade;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbUnidade) {
                r = ((TbUnidade) value).getIdUnidade().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
