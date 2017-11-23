package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.MeioRespostaDAO;
import br.com.xti.ouvidoria.model.TbMeioResposta;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorMeioResposta extends ConversorBase {
	
	@EJB
	private MeioRespostaDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbMeioResposta meioResposta = null;
        if(value == null || value.isEmpty()) {
            return meioResposta;
        }
        
        try {
            meioResposta = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return meioResposta;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbMeioResposta) {
                r = ((TbMeioResposta) value).getIdMeioResposta().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
