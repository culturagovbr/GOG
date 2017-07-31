/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.validator;

 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;
 
@ManagedBean(name = "emailValidator")
@RequestScoped
public class EmailValidator implements Validator{
 
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
	private Pattern pattern;
	private Matcher matcher;
 
	public EmailValidator(){
		  pattern = Pattern.compile(EMAIL_PATTERN);
	}
 
	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(!ValidacaoHelper.isNotEmpty((String) value))
			return;
		
		matcher = pattern.matcher(value.toString());
		if(!matcher.matches()){
			FacesMessage msg = 
				new FacesMessage("Formato de e-mail inválido", 
						"Formato de e-mail inválido: " + value);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
 
	}
}