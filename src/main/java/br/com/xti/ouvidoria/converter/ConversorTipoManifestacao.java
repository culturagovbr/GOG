package br.com.xti.ouvidoria.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.xti.ouvidoria.dao.TipoManifestacaoDAO;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;

/**
 *
 * @author samuel.guimaraes
 */
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ConversorTipoManifestacao extends ConversorBase {
    
    @EJB
    private TipoManifestacaoDAO dao;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TbTipoManifestacao tipoManifestacao = null;
        if(value == null || value.isEmpty()) {
            return tipoManifestacao;
        }
        
        try {
            tipoManifestacao = dao.find(Integer.parseInt(value));
        } catch (NumberFormatException e) {}
        return tipoManifestacao;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String r = null;
        try {
            if (value instanceof TbTipoManifestacao) {
                r = ((TbTipoManifestacao) value).getIdTipoManifestacao().toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return r;
    }
}
