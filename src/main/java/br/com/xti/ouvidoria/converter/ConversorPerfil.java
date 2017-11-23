package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.PerfilDAO;
import br.com.xti.ouvidoria.model.TbPerfil;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorPerfil extends ConversorBase {
    
	@EJB
	private PerfilDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbPerfil perfil = null;
        if(value == null || value.isEmpty()) {
            return perfil;
        }
        
        try {
            perfil = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return perfil;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbPerfil) {
                r = ((TbPerfil) value).getIdPerfil().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
