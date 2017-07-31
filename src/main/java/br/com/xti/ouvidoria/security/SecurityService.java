package br.com.xti.ouvidoria.security;


import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.CONFIGURAR_PREFERENCIAS_DE_SISTEMA;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.GERAR_GRAFICOS;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_AREA_DE_ENTRADA;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_AVISO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_CLASSIFICACOES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_EMAILS_DE_NOTIFICACAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_ENCAMINHAMENTO_PADRONIZADO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_FAIXA_ETARIA;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_GRAU_INSTRUCAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_MEIO_DE_ENTRADA;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_MEIO_DE_RESPOSTA;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_PRIORIDADE;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_QUESTIONARIO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_RESPOSTAS_MANIFESTACOES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_SUBCLASSIFICACOES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_TIPO_MANIFESTACAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_UNIDADE;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.REALIZAR_EXTRACAO_DE_DADOS;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.VISUALIZAR_ESTATISTICAS_DE_MANIFESTACAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.VISUALIZAR_LOG_DE_OPERACOES;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum;
import br.com.xti.ouvidoria.util.JSFUtils;


/**
 * @author Samuel Correia Guimarães
 * @since v1.2.0 - 14/11/2014
 */
@Named("security")
@SessionScoped
public class SecurityService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private TbUsuario user;
	private FuncaoUsuarioEnum profile;
	private Set<FuncionalidadeEnum> roles = new HashSet<>();
	
	/**
	 * Verifica se o usuário logado tem a role informada
	 * 
	 * @param role nome da role a ser verificada
	 * @return <code>true</code> se o usuário logado possuir a role e <code>false</code> caso contrário
	 */
	public boolean hasRole(String role) {
		return roles.contains(FuncionalidadeEnum.valueOf(role.toUpperCase()));
	}
	
	/**
	 * Verifica se o usuário logado possui as roles informadas por parâmetro.
	 * Se o parâmetro <b>mustHaveAllRoles</b> for <code>true</code> o usuário
	 * deve possuir todas as roles informadas pelo parâmetro <b>roles</b>, mas
	 * no caso do parâmetro ser <code>false</code> o usuário precisa possuir
	 * apenas uma das roles informadas.
	 * 
	 * @param rolesToValidate nomes das roles a serem verificadas
	 * @param mustHaveAllRoles usuário deve ou não ter todas as roles
	 * @return
	 */
	private boolean isUserInRoles(List<FuncionalidadeEnum> rolesToValidate, boolean mustHaveAllRoles) {
		boolean isUserInRoles = Boolean.FALSE;
		
		if(ValidacaoHelper.isNotEmpty(rolesToValidate)) {
			for (FuncionalidadeEnum role : rolesToValidate) {
				isUserInRoles = roles.contains(role);
				if(mustHaveAllRoles && !isUserInRoles) {
					break;
				} else if(!mustHaveAllRoles && isUserInRoles) {
					break;
				}
			}
		}
		
		return isUserInRoles;
	}
	
	/**
	 * @return Nome do usuário Logado
	 */
	public String getUserName() {
		return user == null ? "" :  user.getNmUsuario();
	}
	
	/**
	 * Retorna o perfil do usuário logado, pode ser um dos seguintes:
	 * <ul>
	 *   <li>OUVIDOR</li>
	 *   <li>INTERLOCUTOR</li>
	 *   <li>OPERADOR</li>
	 *   <li>MANIFESTANTE</li>
	 *   <li>ADMINISTRADOR</li>
	 * </ul>
	 * 
	 * @return Perfil do usuário ou <code>NULL</code> caso não esteja vinculado a nenhum;
	 */
	public FuncaoUsuarioEnum getUserProfile(){
		if(profile == null && loggedIn()) {
			profile = EnumHelper.getFuncaoUsuarioEnum(user.getTpFuncao());
		}
		return profile;
    }
	
	/**
	 * @return <code>true</code> se o usuário estiver 
	 * logado e <code>false</code> caso contrário
	 */
	public boolean loggedIn() {
		return user != null;
	}
	
	/**
	 * Efetua o <code>logout</code> do usuário
	 */
	public void logout() {
		try {
			JSFUtils.redirect("/logout");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return se deve ou não mostrar o menu de administração
	 */
	public boolean enableAdministrationMenu() {
		List<FuncionalidadeEnum> functionalities = new ArrayList<FuncionalidadeEnum>();
		functionalities.add(MANTER_AREA_DE_ENTRADA);
		functionalities.add(MANTER_MEIO_DE_ENTRADA);
		functionalities.add(MANTER_MEIO_DE_RESPOSTA);
		functionalities.add(MANTER_PRIORIDADE);
		functionalities.add(MANTER_CLASSIFICACOES);
		functionalities.add(MANTER_SUBCLASSIFICACOES);
		functionalities.add(MANTER_TIPO_MANIFESTACAO);
		functionalities.add(MANTER_FAIXA_ETARIA);
		functionalities.add(MANTER_UNIDADE);
		functionalities.add(MANTER_GRAU_INSTRUCAO);
		functionalities.add(MANTER_QUESTIONARIO);
		return isUserInRoles(functionalities, false);
	}
	
	/**
	 * @return se deve ou não mostrar o menu de sistema
	 */
	public boolean enableSystemMenu() {
		List<FuncionalidadeEnum> functionalities = new ArrayList<FuncionalidadeEnum>();
		functionalities.add(CONFIGURAR_PREFERENCIAS_DE_SISTEMA);
		functionalities.add(VISUALIZAR_LOG_DE_OPERACOES);
		return isUserInRoles(functionalities, false);
	}
	
	/**
	 * @return se deve ou não mostrar o menu de relatório
	 */
	public boolean enableReportMenu() {
		List<FuncionalidadeEnum> functionalities = new ArrayList<FuncionalidadeEnum>();
		functionalities.add(REALIZAR_EXTRACAO_DE_DADOS);
		functionalities.add(VISUALIZAR_ESTATISTICAS_DE_MANIFESTACAO);
		return isUserInRoles(functionalities, false);
	}
	
	/**
	 * @return se deve ou não mostrar o menu de gráfico
	 */
	public boolean enableChartMenu() {
		List<FuncionalidadeEnum> functionalities = new ArrayList<FuncionalidadeEnum>();
		functionalities.add(GERAR_GRAFICOS);
		return isUserInRoles(functionalities, false);
	}
	
	/**
	 * @return se deve ou não mostrar o menu de comunicação
	 */
	public boolean enableCommunicationMenu() {
		List<FuncionalidadeEnum> functionalities = new ArrayList<FuncionalidadeEnum>();
		functionalities.add(MANTER_EMAILS_DE_NOTIFICACAO);
		functionalities.add(MANTER_ENCAMINHAMENTO_PADRONIZADO);
		functionalities.add(MANTER_RESPOSTAS_MANIFESTACOES);
		functionalities.add(MANTER_AVISO);
		return isUserInRoles(functionalities, false);
	}
	
	/**
	 * @return <code>true</code> se o usário logado 
	 * for Ouvidor e <code>false</code> caso contrário
	 */
	public boolean isOuvidor() {
		return loggedIn() && profile == FuncaoUsuarioEnum.OUVIDOR;
	}
	
	/**
	 * @return <code>true</code> se o usário logado 
	 * for Interlocutor e <code>false</code> caso contrário
	 */
	public boolean isInterlocutor() {
        return loggedIn() && profile == FuncaoUsuarioEnum.INTERLOCUTOR;
    }

	/**
	 * @return <code>true</code> se o usário logado 
	 * for Operador e <code>false</code> caso contrário
	 */
	public boolean isOperador() {
		return loggedIn() && profile == FuncaoUsuarioEnum.OPERADOR;
	}
	
	/**
	 * @return <code>true</code> se o usário logado 
	 * for Manifestante e <code>false</code> caso contrário
	 */
    public boolean isManifestante() {
        return !loggedIn() || profile == FuncaoUsuarioEnum.MANIFESTANTE;
    }
    
    /**
     * @return <code>true</code> se o usário logado 
     * for Manifestante e <code>false</code> caso contrário
     */
    public boolean isManifestanteLogged() {
    	return loggedIn() && profile == FuncaoUsuarioEnum.MANIFESTANTE;
    }
    
    /**
	 * @return <code>true</code> se o usário logado 
	 * for Administrador e <code>false</code> caso contrário
	 */
    public boolean isAdministrador() {
    	return loggedIn() && profile == FuncaoUsuarioEnum.ADMINISTRADOR;
    }
    
	/**
	 * @return Usuário logado
	 */
	public TbUsuario getUser() {
		return user;
	}

	/**
	 * Seta o usuário logado e a função do mesmo
	 * 
	 * @param user usuário logado
	 */
	protected void setUser(TbUsuario user) {
		this.user = user;
		this.profile = EnumHelper.getFuncaoUsuarioEnum(user.getTpFuncao());
	}

	/**
	 * @return lista de funcionalidades que o usuário logado tem permissão
	 */
	public Set<FuncionalidadeEnum> getRoles() {
		return roles;
	}

	/**
	 * Seta a lista de funcionalidades que o usuário logado tem permissão
	 * 
	 * @param roles lista de funcionalidades
	 */
	protected void setRoles(Set<FuncionalidadeEnum> roles) {
		this.roles = roles;
	}
	
}
