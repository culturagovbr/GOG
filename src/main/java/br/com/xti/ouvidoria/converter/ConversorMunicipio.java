package br.com.xti.ouvidoria.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.model.TbMunicipio;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorMunicipio extends ConversorBase {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbMunicipio municipio = null;
        if(value == null || value.isEmpty()) {
            return municipio;
        }
        
        try {
            municipio = new TbMunicipio(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return municipio;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbMunicipio) {
                r = ((TbMunicipio) value).getIdMunicipio().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
