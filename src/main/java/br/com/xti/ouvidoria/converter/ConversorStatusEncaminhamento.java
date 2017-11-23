package br.com.xti.ouvidoria.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorStatusEncaminhamento extends ConversorBase {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        StatusEncaminhamentoEnum status = null;
        if(value == null || value.isEmpty()) {
            return status;
        }
        
        status = EnumHelper.getStatusEncaminhamentoEnum(value);
        return status;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof StatusEncaminhamentoEnum) {
                r = ((StatusEncaminhamentoEnum) value).getId().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
