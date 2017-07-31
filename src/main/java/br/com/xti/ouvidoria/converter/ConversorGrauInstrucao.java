package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.GrauInstrucaoDAO;
import br.com.xti.ouvidoria.model.TbGrauInstrucao;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorGrauInstrucao extends ConversorBase {
	
	@EJB
	private GrauInstrucaoDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbGrauInstrucao grauInstrucao = null;
        if(value == null || value.isEmpty()) {
            return grauInstrucao;
        }
        
        try {
            grauInstrucao = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return grauInstrucao;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbGrauInstrucao) {
                r = ((TbGrauInstrucao) value).getIdGrauInstrucao().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
    
}
