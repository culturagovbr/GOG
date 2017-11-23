package br.com.xti.ouvidoria.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorFuncaoUsuario extends ConversorBase {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        FuncaoUsuarioEnum func = null;
        if(value == null || value.isEmpty()) {
            return func;
        }
        
        func = EnumHelper.getFuncaoUsuarioEnum(value);
        return func;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof FuncaoUsuarioEnum) {
                r = ((FuncaoUsuarioEnum) value).getId().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
