package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.TipoUsuarioEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.PasswordUtils;

/**
 * @author Samuel Correia Guimarães
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBMinhasInformacoes")
@ViewScoped
public class MBMinhasInformacoes implements Serializable {
	
	@Inject
	private SecurityService securityService;
	
	@EJB
	private UsuarioDAO usuarioDAO;

    private TbUsuario usuario;
    private String senhaAtual;
    private String novaSenha;
    private String novaSenhaConfirmar;
    private String email;

    public void setNovaSenhaConfirmar(String novaSenhaConfirmar) {
        this.novaSenhaConfirmar = novaSenhaConfirmar;
    }

    public String getNovaSenhaConfirmar() {
        return novaSenhaConfirmar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @PostConstruct
    public void init() {
        usuario = securityService.getUser();
        email = usuario.getEeEmail();
    }

    public void alterarMinhasInformacoes(ActionEvent actionEvent) {
        // Define se será atualizado a senha
        if (senhaAtual != null && !senhaAtual.isEmpty()) {
            
            if (usuario.getNmSenha().equals(PasswordUtils.getMD5(senhaAtual).toUpperCase())) {
                if (novaSenha != null && !novaSenha.isEmpty()) {
                    usuario.setNmSenha(PasswordUtils.getMD5(novaSenha).toUpperCase());
                } else {
                    MensagemFaceUtil.erro("Erro", "A senha não pode ser vazia;");
                    return;
                }
                if(!novaSenha.equals(novaSenhaConfirmar)){
                    MensagemFaceUtil.erro("Erro", "As novas senhas não conferem.");
                    return;
                }
            } else {
                MensagemFaceUtil.erro("Erro", "Senha atual incorreta.");
                return;
            }
        }

        if (!email.equals(getUsuario().getEeEmail())) {
            try {
                TbUsuario user = usuarioDAO.findByEmail(email);
                if (user != null) {
                    MensagemFaceUtil.erro("Erro", " E-mail " + email + " já cadastrado");
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(MBMinhasInformacoes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        usuario.setEeEmail(email);
        try {
            // Atualiza as informações do usuário
            usuarioDAO.edit(usuario);
        } catch (InfrastructureException ex) {
            ex.printStackTrace();

            MensagemFaceUtil.info("Erro", "Erro ao atualizar as informações");
        }
        MensagemFaceUtil.info("Sucesso", "Dados atualizados com sucesso!");
    }

    public TbUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TbUsuario usuario) {
        this.usuario = usuario;
        email = usuario.getEeEmail();
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public boolean habilitaEdicao() {
        return usuario.getTpUsuario() != null && usuario.getTpUsuario().equals(TipoUsuarioEnum.INTERNO.getId());
    }
}
