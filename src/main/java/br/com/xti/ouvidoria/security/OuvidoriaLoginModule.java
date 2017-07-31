package br.com.xti.ouvidoria.security;

import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.ATUALIZAR_MINHAS_INFORMACOES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.GERENCIAR_MANIFESTACAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_EMAILS_DE_NOTIFICACAO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_ENCAMINHAMENTO_PADRONIZADO;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_FILTROS_PERSONALIZADOS;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.MANTER_RESPOSTAS_MANIFESTACOES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.REALIZAR_PESQUISA_NOS_TRAMITES;
import static br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum.REGISTRAR_MANIFESTACAO;

import java.security.acl.Group;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.PerfilDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.helper.CdiHelper;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbPerfil;
import br.com.xti.ouvidoria.model.TbPerfilxFuncionalidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum;
import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;

/**
 * Modulo de login para autenticação dos usuários do sistema Ouvidoria
 * 
 * @author Samuel Correia Guimarães
 * @since 12/11/2014 v1.2.0
 */
public class OuvidoriaLoginModule extends UsernamePasswordLoginModule {
	
	@Inject
	private SecurityService securityService;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	@Inject
	private ManifestacaoDAO manifestacaoDAO;
	
	@Inject
	private PerfilDAO perfilDAO;
	
	@Inject
	private HttpServletRequest request;
	
	private TbUsuario user;
	private FuncaoUsuarioEnum profile;
	
	@SuppressWarnings("rawtypes")
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		super.initialize(subject, callbackHandler, sharedState, options);
		
		// inicializa classes com injeção de dependência
		try {
			CdiHelper.programmaticInjection(OuvidoriaLoginModule.class, this);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String getUsersPassword() throws LoginException {
		return "";
	}
	
	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {
		return doLogin(getUsername(), inputPassword);
	}
	
	private boolean doLogin(String login, String password) {
		boolean logged = Boolean.FALSE;
		try {
			// Tenta logar um usuário (login e senha)
            user = usuarioDAO.login(login, password);
            if (user != null) {
                if (user.isAtivoOuNovaSenha()) {
                    profile = EnumHelper.getFuncaoUsuarioEnum(user.getTpFuncao());
                    if (profile == null) {
            			request.setAttribute("errorMessage", "Usuário sem perfil associado");
                    } else {
                    	securityService.setUser(user);
                    	logged = Boolean.TRUE;
                    	
                    	StatusUsuarioEnum userStatus = EnumHelper.getStatusUsuarioEnum(user.getStStatus());
                    	if(StatusUsuarioEnum.NOVA_SENHA == userStatus) {
                    		request.setAttribute("loginType", LoginTypeEnum.NEW_PASSWORD);
                    	} else {
                    		request.setAttribute("loginType", LoginTypeEnum.USER);
                    	}
                    }
                } else {
                	request.setAttribute("errorMessage", "Usuário inativo");
                }
            } else {
            	// Tenta logar pela manifestação (número e senha)
        		int manifestarionNumber = Integer.parseInt(login);
	            TbManifestacao manifestation = manifestacaoDAO.getManifestacaoPorNumeroSenha(manifestarionNumber, password);
	            if (manifestation != null) {
	            	logged = Boolean.TRUE;
	            	profile = FuncaoUsuarioEnum.MANIFESTANTE;
	            	request.setAttribute("loginType", LoginTypeEnum.MANIFESTATION);
	            	request.setAttribute("manifestation", manifestation);
	            } else {
	            	request.setAttribute("errorMessage", "Login ou senha inválidos");
	            }
            }
        } catch (Exception e) {
        	System.err.println("Erro ao tentar efetuar login do usuário: " + login);
        	request.setAttribute("errorMessage", "Login ou senha inválidos");
        	e.printStackTrace();
        }
		
		return logged;
    }
	
	
	@Override
	protected Group[] getRoleSets() throws LoginException {
		SimpleGroup group = new SimpleGroup("Roles");
		Set<FuncionalidadeEnum> roles = new HashSet<>();
		
		try {
			if(profile == null) {
				roles.add(REGISTRAR_MANIFESTACAO);
			} else if (profile.equals(FuncaoUsuarioEnum.ADMINISTRADOR)) {
				roles.addAll(Arrays.asList(FuncionalidadeEnum.values()));
			} else if (profile.equals(FuncaoUsuarioEnum.OPERADOR)
					|| profile.equals(FuncaoUsuarioEnum.INTERLOCUTOR)
					|| profile.equals(FuncaoUsuarioEnum.OUVIDOR)) {
				roles.addAll(Arrays.asList(
						GERENCIAR_MANIFESTACAO,
						ATUALIZAR_MINHAS_INFORMACOES,
						REALIZAR_PESQUISA_NOS_TRAMITES));
	
				if (profile.equals(FuncaoUsuarioEnum.OUVIDOR)) {
					roles.addAll(Arrays.asList(
							REGISTRAR_MANIFESTACAO,
							MANTER_FILTROS_PERSONALIZADOS,
							MANTER_EMAILS_DE_NOTIFICACAO,
							MANTER_ENCAMINHAMENTO_PADRONIZADO,
							MANTER_RESPOSTAS_MANIFESTACOES));
				}
	
				// Adicionando as permissões vinculadas aos perfis do usuário
				List<TbPerfil> activeProfiles = perfilDAO.getPerfisAtivos(user);
				for (TbPerfil profile : activeProfiles) {
					for (TbPerfilxFuncionalidade func : profile.getTbPerfilxFuncionalidadeCollection()) {
						int functionalityId = func.getIdFuncionalidade().getIdFuncionalidade(); 
						FuncionalidadeEnum role = EnumHelper.getFuncionalidadeEnum(functionalityId);
						roles.add(role);
					}
				}
			} else if (profile.equals(FuncaoUsuarioEnum.MANIFESTANTE)) {
				roles.addAll(Arrays.asList(GERENCIAR_MANIFESTACAO,
						ATUALIZAR_MINHAS_INFORMACOES, REGISTRAR_MANIFESTACAO));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Adiciona as permissões
		for (FuncionalidadeEnum role : roles) {
			group.addMember(new SimplePrincipal(role.toString().toLowerCase()));
		}
		securityService.setRoles(roles);
		
		return new Group[] { group };
	}
	
}
