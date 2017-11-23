package br.com.xti.ouvidoria.util;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import br.com.xti.ouvidoria.dao.PreferenciaSistemaDAO;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbPreferenciaSistema;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;

/**
 * @author Samuel Correia Guimarães
 */
@Stateless
public class EmailService {

    @EJB
    private PreferenciaSistemaDAO preferenciaSistemaDAO;
    
    private TbPreferenciaSistema preferencia;
    
    @PostConstruct
    public void init() {
        preferencia = preferenciaSistemaDAO.find(1);
    }

    public Email newEmail() {
        return new Email();
    }

    /**
	 * Envia o email para os destinatários em uma nova thread pra que não segure
	 * o processamento principal
	 * 
	 * @param email
	 *            Dados do email a ser enviado
	 * @throws EmailException
	 */
    public void envia(Email email) throws EmailException {
        final HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setHtmlMsg(email.getTextoHtml());
        htmlEmail.setTextMsg(email.getTextoSemFormatacao());
        
        // Anexos
        for(AnexoEmail anexo : email.getAnexos()) {
        	EmailAttachment attachment = new EmailAttachment();
        	attachment.setDisposition(EmailAttachment.ATTACHMENT);
        	attachment.setDescription(anexo.getDescricao());
        	attachment.setName(anexo.getNome());
        	attachment.setPath(anexo.getCaminho());
        	htmlEmail.attach(attachment);
        }
        for (DestinatarioEmail destinatario : email.getDestinatarios()) {
            htmlEmail.addTo(destinatario.getEmail(), destinatario.getNome()); //destinatário  
        }
        for (DestinatarioEmail destinatario : email.getDestinatariosCc()) {
            htmlEmail.addCc(destinatario.getEmail(), destinatario.getNome()); //destinatário copia
        }
        for (DestinatarioEmail destinatario : email.getDestinatariosCco()) {
            htmlEmail.addBcc(destinatario.getEmail(), destinatario.getNome()); //destinatário copia oculta
        }
        
        htmlEmail.setCharset("utf-8");
        htmlEmail.setHostName(preferencia.getHostEmail()); // o servidor SMTP para envio do e-mail  
        htmlEmail.setFrom(preferencia.getEmailOuvidoria(), preferencia.getNomeOuvidoria()); // remetente  
        htmlEmail.setSubject(email.getAssunto()); // assunto do e-mail  
        htmlEmail.setAuthentication(preferencia.getUsuarioEmail(), preferencia.getSenhaEmail());
        htmlEmail.setSmtpPort(preferencia.getPortaEmail());
        htmlEmail.setSslSmtpPort(String.valueOf(preferencia.getPortaEmail()));
        htmlEmail.setSSLOnConnect((preferencia.getSslEmail() == null ? Boolean.FALSE : preferencia.getSslEmail().equals(BooleanEnum.SIM.getId())));
        
        // Envia email em uma nova Thread
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					htmlEmail.send();
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
    }
    
    /**
	 * Envia email de notificação ao manfestante informando sobre uma nova
	 * comunicação externa na manifestação
	 * 
	 * @param nomeRemetente
	 *            Nome do remetente
	 * @param emailRemetente
	 *            Email do remetente
	 * @param numeroManifestacao
	 *            Número da manifestação
	 * @throws EmailException
	 */
    public void enviaEmailNotificacaoNovaMensagem(TbManifestacao manifestacao) throws EmailException {
		String nomeRemetente = manifestacao.getNmPessoa();
		Integer numeroManifestacao = manifestacao.getNrManifestacao();
		
    	if(ValidacaoHelper.isNotEmpty(nomeRemetente)) {
    		nomeRemetente = " Sr(a). " + nomeRemetente;
    	} else {
    		nomeRemetente = "";
    	}
    	
        // configura a mensagem para o formato HTML  
        StringBuilder emailTextoHtml = new StringBuilder()
        .append("<div style='font-family: Arial, Verdana; font-size: 14px;'>")
        .append("<center><span style='color: #006600; font-weight: bold;'>")
        .append("Ouvidoria Ministério da Cultura")
        .append("</span></center>")
        .append("<br/>")
        .append("<p>Prezado%s,</p>")
        .append("<p>A manifestação de número %s tem um novo comentário. Acesse o sistema para verificar a mensagem.</p>")
        .append("<br/>")
        .append("<center><span style='color: #006600; font-weight: bold; font-size: 11px;'>")
        .append("Mensagem enviada automaticamente pelo sistema de Ouvidoria do Ministério da Cultura.")
        .append("</span></center>")
        .append("</div>");

        // configura a mensagem para o formato texto simples
        StringBuilder emailTexto = new StringBuilder()
        .append("Prezado%s,\n\n")
        .append("A manifestação de número %s tem um novo comentário. Acesse o sistema para verificar a mensagem.");

        // envia e-mail para todos os emails cadastrados
        for (String emailRemetente : ManifestacaoUtils.converterEmListaDeEmails(manifestacao, true)) {
        	Email email = new Email();
        	email.addDestinatario(nomeRemetente, emailRemetente);
        	email.setAssunto("[Ouvidoria Videira] Nova mensagem na Manifestação: " + numeroManifestacao);
        	email.setTextoHtml(String.format(emailTextoHtml.toString(), nomeRemetente, numeroManifestacao));
        	email.setTextoSemFormatacao(String.format(emailTexto.toString(), nomeRemetente, numeroManifestacao));
        	envia(email);
        }
    }
    
    
    
	public class Email {
		private String assunto;
		private String textoHtml;
		private String textoSemFormatacao;
		private ArrayList<DestinatarioEmail> destinatarios = new ArrayList<>();
		private ArrayList<DestinatarioEmail> destinatariosCc = new ArrayList<>();
		private ArrayList<DestinatarioEmail> destinatariosCco = new ArrayList<>();
		private ArrayList<AnexoEmail> anexos = new ArrayList<>();

		public void addAnexo(String caminhoAnexo, String nome, String descricao) {
			anexos.add(new AnexoEmail(nome, caminhoAnexo, descricao));
		}

		public void addDestinatario(String nome, String email) {
			destinatarios.add(new DestinatarioEmail(nome, email));
		}

		public void addDestinatarioCc(String nome, String email) {
			destinatariosCc.add(new DestinatarioEmail(nome, email));
		}

		public void addDestinatarioCco(String nome, String email) {
			destinatariosCco.add(new DestinatarioEmail(nome, email));
		}

		public ArrayList<AnexoEmail> getAnexos() {
			return anexos;
		}

		public void setAnexos(ArrayList<AnexoEmail> anexos) {
			this.anexos = anexos;
		}

		public ArrayList<DestinatarioEmail> getDestinatarios() {
			return destinatarios;
		}

		public void setDestinatarios(ArrayList<DestinatarioEmail> destinatarios) {
			this.destinatarios = destinatarios;
		}

		public void clearDestinatarios() {
			this.destinatarios = new ArrayList<DestinatarioEmail>();
		}

		public ArrayList<DestinatarioEmail> getDestinatariosCc() {
			return destinatariosCc;
		}

		public void setDestinatariosCc(
				ArrayList<DestinatarioEmail> destinatariosCc) {
			this.destinatariosCc = destinatariosCc;
		}

		public ArrayList<DestinatarioEmail> getDestinatariosCco() {
			return destinatariosCco;
		}

		public void setDestinatariosCco(
				ArrayList<DestinatarioEmail> destinatariosCco) {
			this.destinatariosCco = destinatariosCco;
		}

		public String getAssunto() {
			return assunto;
		}

		public void setAssunto(String assunto) {
			this.assunto = assunto;
		}

		public String getTextoHtml() {
			return textoHtml;
		}

		public void setTextoHtml(String textoHtml) {
			this.textoHtml = textoHtml;
		}

		public String getTextoSemFormatacao() {
			return textoSemFormatacao;
		}

		public void setTextoSemFormatacao(String textoSemFormatacao) {
			this.textoSemFormatacao = textoSemFormatacao;
		}

	}
    
	public class AnexoEmail {
		private String nome;
		private String caminho;
		private String descricao;

		public AnexoEmail(String nome, String caminho, String descricao) {
			this.nome = nome;
			this.caminho = caminho;
			this.descricao = descricao;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getCaminho() {
			return caminho;
		}

		public void setCaminho(String caminho) {
			this.caminho = caminho;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
	}

	public class DestinatarioEmail {
		private String nome;
		private String email;

		public DestinatarioEmail(String nome, String email) {
			this.nome = nome;
			this.email = email;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@Override
		public String toString() {
			return nome + " > " + email;
		}
		
	}
	
}
