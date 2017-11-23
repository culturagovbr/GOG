package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.AreaEntradaDAO;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbAreaEntrada;

/**
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorAreaEntrada extends ConversorBase {
	
	@EJB
	private AreaEntradaDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbAreaEntrada areaEntrada = null;
        try {
        	if(ValidacaoHelper.isNotEmpty(value)) {
        		areaEntrada = dao.find(Integer.parseInt(value));
        	}
        } catch (NumberFormatException e) {
        	e.printStackTrace();
        }
        return areaEntrada;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String areaEntrada = null;
        try {
            if (value instanceof TbAreaEntrada) {
                areaEntrada = ((TbAreaEntrada) value).getIdAreaEntrada().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return areaEntrada;
    }
}
