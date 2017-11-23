package br.com.xti.ouvidoria.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.enums.RacaEnum;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorRaca extends ConversorBase {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        RacaEnum status = null;
        if(value == null || value.isEmpty()) {
            return status;
        }
        
        status = EnumHelper.getTipoRacaEnum(value);
        return status;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof RacaEnum) {
                r = ((RacaEnum) value).getId().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
