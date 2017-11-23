/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.validator;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;

@ManagedBean(name = "numeroProcessoValidator")
@RequestScoped
public class NumeroProcessoValidator implements Validator {
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    	if(ValidacaoHelper.isNotEmpty(value)){
            String num = value.toString();
            if(num.length() != 20){
            	FacesMessage msg = new FacesMessage("Número Processo Inválido","O número do processo deve conter 20.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
    }
}