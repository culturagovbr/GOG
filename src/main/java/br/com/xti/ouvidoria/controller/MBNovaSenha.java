package br.com.xti.ouvidoria.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.xti.ouvidoria.dao.EmailAutomatizadoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.helper.PalavrasChavesHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;
import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.EmailService;
import br.com.xti.ouvidoria.util.JSFUtils;
import br.com.xti.ouvidoria.util.PasswordUtils;

/**
 * @author Samuel Correia Guimarães
 */
@ManagedBean(name = "mBNovaSenha")
@ViewScoped
public class MBNovaSenha implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityService securityService;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@EJB
	private ManifestacaoDAO manifestacaoDAO;
	
	@EJB
	private EmailAutomatizadoDAO emailAutomatizadoDAO;
	
	@EJB
	private EmailService emailService;
	
	private static final String LOGGED_INDEX_PAGE = "/pages/manifestacao/listarmanifestacoes.xhtml";
    
    private String login;
    private String email;
    private String novaSenha;
    private String novaSenhaConfirmar;
    private Integer numManifestacao;
    
    @PostConstruct
    public void init() {
    	try {
    		TbUsuario user = securityService.getUser();
    		if (user != null && !StatusUsuarioEnum.NOVA_SENHA.getId().equals(user.getStStatus())) {
    			JSFUtils.redirect("/");
    		}
    	} catch (Exception e) {
    		// faz nada
    	}
    }
    
    public void sendNewManifestationPassword() {
    	TbManifestacao manifestation = null;
    	try {
    		manifestation = manifestacaoDAO.getManifestacaoPorNumero(numManifestacao);
    		if(ValidacaoHelper.isNotEmpty(manifestation)) {
    			String senha = PasswordUtils.generatePassword();
    			manifestation.setDsSenha(senha);
    			manifestacaoDAO.edit(manifestation);
    			
    			TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.NOVA_SENHA_MANIFESTACAO);
    			String emailAssunto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestation, false);
    			String emailTexto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestation, false);
    			
    			EmailService.Email newPasswordEmail = emailService.newEmail();
    			newPasswordEmail.addDestinatario(manifestation.getNmPessoa(), manifestation.getEeEmailUsuario());
    			newPasswordEmail.setAssunto(emailAssunto);
    			newPasswordEmail.setTextoHtml(emailTexto);
    			newPasswordEmail.setTextoSemFormatacao(emailTexto);
    			
    			emailService.envia(newPasswordEmail);
    			MensagemFaceUtil.info("Sua nova senha foi encaminhada para o e-mail cadastrado na manifestação", null, "numManifestacao");
    		} else {
    			MensagemFaceUtil.alerta(String.format("Nenhuma manifestação foi encontrado com o número: %s", numManifestacao), null, "numManifestacao");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		MensagemFaceUtil.alerta("Ocorreu um erro ao recuperar sua senha. Contate o administrador do sistema.", null, "numManifestacao");
    	}
    }
    
    public void sendNewAccountPassword() {
        TbUsuario user;
        try {
            user = usuarioDAO.findByEmail(email);
            if (user != null) {
                String senha = PasswordUtils.generatePassword();
                String senhaMd5 = PasswordUtils.getMD5(senha);
                user.setNmSenha(senhaMd5.toUpperCase());
                usuarioDAO.edit(user);
                
                user.setNmSenha(senha);
                TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.RECUPERACAO_SENHA);
                String emailTexto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), user);
                
                EmailService.Email newPasswordEmail = emailService.newEmail();
                newPasswordEmail.addDestinatario(user.getNmUsuario(), user.getEeEmail());
                newPasswordEmail.setAssunto(emailAutomatizado.getNmTituloEmail());
                newPasswordEmail.setTextoHtml(emailTexto);
                newPasswordEmail.setTextoSemFormatacao(emailTexto);
                
                emailService.envia(newPasswordEmail);
                MensagemFaceUtil.info(String.format("Sua nova senha foi encaminhada para o e-mail: %s", email), null, "email");
            } else {
                MensagemFaceUtil.alerta(String.format("Nenhum usuário foi encontrado com e-mail: %s", email), null, "email");
            }
        } catch (EJBException e) {
        	MensagemFaceUtil.alerta("Foi encontrado mais de um usuário com o mesmo e-mail. Contate o administrador do sistema.", null, "email");
        } catch (Exception e) {
        	e.printStackTrace();
            MensagemFaceUtil.alerta("Ocorreu um erro ao recuperar sua senha. Contate o administrador do sistema.", null, "email");
        }
    }
    
    public void saveNewPassword() {
        if (novaSenha.equalsIgnoreCase(novaSenhaConfirmar)) {
            try {
                TbUsuario usuario = securityService.getUser();
                String senhaMd5 = PasswordUtils.getMD5(novaSenha).toUpperCase();
                if (senhaMd5.equalsIgnoreCase(usuario.getNmSenha())) {
                    MensagemFaceUtil.erro("A senha não pode ser a mesma da anterior", null);
                    return;
                }
                
                usuario.setNmSenha(senhaMd5);
                usuario.setStStatus(StatusUsuarioEnum.ATIVO.getId());
                usuarioDAO.edit(usuario);
                
                JSFUtils.redirect(LOGGED_INDEX_PAGE);
                MensagemFaceUtil.info("Senha alterada com sucesso", null);
            } catch (Exception e) {
                MensagemFaceUtil.erro("Erro ao alterar sua senha", null);
            }
        } else {
            MensagemFaceUtil.erro("Senhas digitadas são diferentes", null);
        }
    }
    
    // GETTERs e SETTERs
    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public void setNovaSenhaConfirmar(String novaSenhaConfirmar) {
        this.novaSenhaConfirmar = novaSenhaConfirmar;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public String getNovaSenhaConfirmar() {
        return novaSenhaConfirmar;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

	public Integer getNumManifestacao() {
		return numManifestacao;
	}

	public void setNumManifestacao(Integer numManifestacao) {
		this.numManifestacao = numManifestacao;
	}
    
}
