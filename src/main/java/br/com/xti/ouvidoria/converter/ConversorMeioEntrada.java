package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.MeioEntradaDAO;
import br.com.xti.ouvidoria.model.TbMeioEntrada;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorMeioEntrada extends ConversorBase {
	
	@EJB
	private MeioEntradaDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbMeioEntrada meioEntrada = null;
        if(value == null || value.isEmpty()) {
            return meioEntrada;
        }
        
        try {
            meioEntrada = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return meioEntrada;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbMeioEntrada) {
                r = ((TbMeioEntrada) value).getIdMeioEntrada().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
