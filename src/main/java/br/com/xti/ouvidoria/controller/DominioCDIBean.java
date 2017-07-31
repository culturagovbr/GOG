package br.com.xti.ouvidoria.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;

/**
 * Classe responsável por centralizar as informações de Domínio
 *
 * @author Samuel Correia Guimarães
 */
@Named
@ApplicationScoped
public class DominioCDIBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
    private ParametroDAO parametroDAO;

	public String getUrlBase() {
		String url = "http://ouvidoria.cultura.gov.br";
		try {
			url = parametroDAO.getUrlBase();
		} catch (InfrastructureException e) {
			e.printStackTrace();
		}
		return url;
	}

}
