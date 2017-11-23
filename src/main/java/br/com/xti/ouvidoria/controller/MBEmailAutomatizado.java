package br.com.xti.ouvidoria.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.xti.ouvidoria.dao.EmailAutomatizadoDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;

/**
 * @author Samuel Correia Guimarães
 */
@ManagedBean(name = "mBEmailAutomatizado")
@ViewScoped
public class MBEmailAutomatizado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
    private EmailAutomatizadoDAO dao;
	
    private TbEmailAutomatizado emailNovaManifestacao;
    private TbEmailAutomatizado emailPrimeiroTramite;
    private TbEmailAutomatizado emailRespostaFinal;
    private TbEmailAutomatizado emailRecuperacaoSenha;
    private TbEmailAutomatizado emailResponderQuestionario;
    private TbEmailAutomatizado emailInterlocutorOuvidoria;
    private TbEmailAutomatizado emailOperador;
    private TbEmailAutomatizado emailManifestante;
    private TbEmailAutomatizado emailOuvidoria;
    private TbEmailAutomatizado emailInterlocutorOperador;
    private TbEmailAutomatizado emailAtrasoInterlocutor;
    private TbEmailAutomatizado emailAtrasoOperador;
    private TbEmailAutomatizado emailNovaSenhaManifestacao;
    
    @PostConstruct
    public void init() {
    	try {
    		emailNovaManifestacao = initEmail(EmailAutomatizadoEnum.NOVO);
    		emailPrimeiroTramite = initEmail(EmailAutomatizadoEnum.PRIMEIRO_TRAMITE);
    		emailRespostaFinal = initEmail(EmailAutomatizadoEnum.RESPOSTA_FINAL);
    		emailRecuperacaoSenha = initEmail(EmailAutomatizadoEnum.RECUPERACAO_SENHA);
    		emailResponderQuestionario = initEmail(EmailAutomatizadoEnum.RESPONDER_QUESTIONARIO);
    		emailInterlocutorOuvidoria = initEmail(EmailAutomatizadoEnum.INTERLOCUTOR_OUVIDORIA);
    		emailOperador = initEmail(EmailAutomatizadoEnum.OPERADOR);
    		emailManifestante = initEmail(EmailAutomatizadoEnum.MANIFESTANTE);
    		emailOuvidoria = initEmail(EmailAutomatizadoEnum.OUVIDORIA);
    		emailInterlocutorOperador = initEmail(EmailAutomatizadoEnum.INTERLOCUTOR_OPERADOR);
    		emailAtrasoInterlocutor= initEmail(EmailAutomatizadoEnum.ATRASO_INTERLOCUTOR);
    		emailAtrasoOperador = initEmail(EmailAutomatizadoEnum.ATRASO_OPERADOR);
    		emailNovaSenhaManifestacao = initEmail(EmailAutomatizadoEnum.NOVA_SENHA_MANIFESTACAO);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * Inicializa (busca no banco de dados) o email do tipo informado por
	 * parâmetro. Se o email existir no banco de dados são carregados suas
	 * informações, caso não haja um email com o tipo informado é criado um novo
	 * e cadastrado no banco.
	 * 
	 * @param emailType
	 *            tipo de email a ser inicializado
	 * @return instância do tipo de email informado
	 * @throws InfrastructureException
	 *             se ocorrer erro no cadastro do e-mail
	 */
	private TbEmailAutomatizado initEmail(EmailAutomatizadoEnum emailType) throws InfrastructureException {
		TbEmailAutomatizado email = dao.findByTipo(emailType);
		if (email == null) {
			email = newInstance();
			email.setTpEmail(emailType.getId());
			dao.create(email);
		}
		return email;
	}

	/**
	 * Atualiza o assunto e descrição do padrão de email do tipo informado
	 * 
	 * @param email
	 *            Email a ser atualizado
	 */
    public void alterar(TbEmailAutomatizado email){
        try {
            dao.edit(email);
            MensagemFaceUtil.info("Alteração realizada com sucesso: " +email.getNmTituloEmail(), null);
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Ocorreu um erro ao atualizar email. Entre em contato com o administrador do sistema.", null);
        }
    }
    
    /**
     * @return Nova instância de Email Automatizado com valores padrão
     */
    private TbEmailAutomatizado newInstance() {
        TbEmailAutomatizado t = new TbEmailAutomatizado();
        t.setDsEmail("Descrição do E-mail");
        t.setNmTituloEmail("Título do E-mail");
        t.setTpEmail(EmailAutomatizadoEnum.NOVO.getId().toString());
        return t;
    }
    
    
    // GETTERs e SETTERs
    public TbEmailAutomatizado getEmailNovaManifestacao() {
        return emailNovaManifestacao;
    }

    public void setEmailNovaManifestacao(TbEmailAutomatizado emailNovaManifestacao) {
        this.emailNovaManifestacao = emailNovaManifestacao;
    }

    public TbEmailAutomatizado getEmailPrimeiroTramite() {
        return emailPrimeiroTramite;
    }

    public void setEmailPrimeiroTramite(TbEmailAutomatizado emailPrimeiroTramite) {
        this.emailPrimeiroTramite = emailPrimeiroTramite;
    }

    public TbEmailAutomatizado getEmailRespostaFinal() {
        return emailRespostaFinal;
    }
    
    public TbEmailAutomatizado getEmailRecuperacaoSenha() {
        return emailRecuperacaoSenha;
    }

    public void setEmailRecuperacaoSenha(TbEmailAutomatizado emailRecuperacaoSenha) {
        this.emailRecuperacaoSenha = emailRecuperacaoSenha;
    }
    
    public TbEmailAutomatizado getEmailResponderQuestionario() {
		return emailResponderQuestionario;
	}

	public void setEmailResponderQuestionario(TbEmailAutomatizado emailResponderQuestionario) {
		this.emailResponderQuestionario = emailResponderQuestionario;
	}

	public void setEmailRespostaFinal(TbEmailAutomatizado emailRespostaFinal) {
        this.emailRespostaFinal = emailRespostaFinal;
    }
	
	public TbEmailAutomatizado getEmailInterlocutorOuvidoria() {
		return emailInterlocutorOuvidoria;
	}

	public void setEmailInterlocutorOuvidoria(
			TbEmailAutomatizado emailInterlocutorOuvidoria) {
		this.emailInterlocutorOuvidoria = emailInterlocutorOuvidoria;
	}

	public TbEmailAutomatizado getEmailInterlocutorOperador() {
		return emailInterlocutorOperador;
	}

	public void setEmailInterlocutorOperador(
			TbEmailAutomatizado emailInterlocutorOperador) {
		this.emailInterlocutorOperador = emailInterlocutorOperador;
	}

	public TbEmailAutomatizado getEmailOperador() {
		return emailOperador;
	}

	public void setEmailOperador(TbEmailAutomatizado emailOperador) {
		this.emailOperador = emailOperador;
	}

	public TbEmailAutomatizado getEmailManifestante() {
		return emailManifestante;
	}

	public void setEmailManifestante(TbEmailAutomatizado emailManifestante) {
		this.emailManifestante = emailManifestante;
	}

	public TbEmailAutomatizado getEmailOuvidoria() {
		return emailOuvidoria;
	}

	public void setEmailOuvidoria(TbEmailAutomatizado emailOuvidoria) {
		this.emailOuvidoria = emailOuvidoria;
	}

	public TbEmailAutomatizado getEmailAtrasoInterlocutor() {
		return emailAtrasoInterlocutor;
	}

	public void setEmailAtrasoInterlocutor(
			TbEmailAutomatizado emailAtrasoInterlocutor) {
		this.emailAtrasoInterlocutor = emailAtrasoInterlocutor;
	}

	public TbEmailAutomatizado getEmailAtrasoOperador() {
		return emailAtrasoOperador;
	}

	public void setEmailAtrasoOperador(TbEmailAutomatizado emailAtrasoOperador) {
		this.emailAtrasoOperador = emailAtrasoOperador;
	}

	public TbEmailAutomatizado getEmailNovaSenhaManifestacao() {
		return emailNovaSenhaManifestacao;
	}

	public void setEmailNovaSenhaManifestacao(
			TbEmailAutomatizado emailNovaSenhaManifestacao) {
		this.emailNovaSenhaManifestacao = emailNovaSenhaManifestacao;
	}

}