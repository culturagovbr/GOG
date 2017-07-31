package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.SubClassificacaoDAO;
import br.com.xti.ouvidoria.model.TbSubClassificacao;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorSubClassificacao extends ConversorBase {
	
	@EJB
	private SubClassificacaoDAO dao;
	
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbSubClassificacao subClassificacao = null;
        if(value == null || value.isEmpty()) {
            return subClassificacao;
        }
        
        try {
            subClassificacao = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) { }
        return subClassificacao;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbSubClassificacao) {
                r = ((TbSubClassificacao) value).getIdSubClassificacao().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
