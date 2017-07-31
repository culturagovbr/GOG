package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.FaixaEtariaDAO;
import br.com.xti.ouvidoria.model.TbFaixaEtaria;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorFaixaEtaria extends ConversorBase {
	
	@EJB
	private FaixaEtariaDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbFaixaEtaria faixaEtaria = null;
        if(value == null || value.isEmpty()) {
            return faixaEtaria;
        }
        
        try {
            faixaEtaria = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return faixaEtaria;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbFaixaEtaria) {
                r = ((TbFaixaEtaria) value).getIdFaixaEtaria().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
