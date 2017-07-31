/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.validator;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.model.TbUsuario;

/**
 *
 * @author renato
 */
@ManagedBean(name = "usuarioCadastradoValidator")
@RequestScoped
public class UsuarioCadastradoValidator implements javax.faces.validator.Validator {

    @EJB
    private UsuarioDAO usuarioDAO;

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        try {
            String login = object.toString();
            TbUsuario user = usuarioDAO.findByLogin(login);
            if (user != null) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário " + object +" já existente",""));
            }
        } catch (ValidatorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar usuário: " + object,"Erro ao buscar usuário: " + object));
        }
    }
}
