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

@ManagedBean(name = "emailMultiploValidator")
@RequestScoped
public class EmailMultiploValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\."
            + "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*"
            + "(\\.[A-Za-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    public EmailMultiploValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        String emails = value.toString().replaceAll(",", ";");
        if (emails.endsWith(";")) {
            emails = emails.substring(0, emails.length() - 1);
        }
        if (value.toString().contains(";")) {
            String[] ms = emails.split(";");
            for (String string : ms) {
                if (!validaEmail(string)) {
                    FacesMessage msg =
                            new FacesMessage("Formato de e-mail inválido",
                            "Formato de e-mail inválido: " + string);
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }

        } else {
            if (!validaEmail(emails)) {
                FacesMessage msg =
                        new FacesMessage("Formato de e-mail inválido",
                        "Formato de e-mail inválido: " + emails);
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }

    }

    private boolean validaEmail(String email) {
        if (!email.trim().equals("")) {
            matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return true;
    }
}