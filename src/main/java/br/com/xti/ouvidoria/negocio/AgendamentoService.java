package br.com.xti.ouvidoria.negocio;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import br.com.xti.ouvidoria.dao.ManifestacaoDTODAO;
import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.dao.PreferenciaSistemaDAO;
import br.com.xti.ouvidoria.helper.PalavrasChavesHelper;
import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbPreferenciaSistema;
import br.com.xti.ouvidoria.util.EmailService;

/**
 * Classe responsável por executar atividades agendadas
 * @author Clelson
 */
@Stateless
public class AgendamentoService {

    @EJB
    private ManifestacaoDTODAO manifestacaoDTODAO;
    @EJB
    private ParametroDAO parametroDAO;
	@EJB
	private EmailService emailService;
	@EJB
	private PreferenciaSistemaDAO preferenciaSistemaDAO;



    /**
     * Tarefa agendada: Alerta de manifestações Em Monitoramento
     * Envia e-mail com a lista de manifestações que estão EM MONITORAMENTO e com a data de monitoramento vencida
     * 
     */
    @Schedule(dayOfWeek="*", hour="6")
    public void alertaMonitoramento(){
    	try {
    		Date dataAtual = Calendar.getInstance().getTime();
    		List<TbManifestacao> manifestacoesMonitoradas = manifestacaoDTODAO.recuperaManifestacoesEmMonitoramento(dataAtual);
    		
    		if (manifestacoesMonitoradas != null && !manifestacoesMonitoradas.isEmpty()){
    			// Gera e-mail de alerta de manifestações em monitoramento
    			StringBuffer urlsManifestacoes = new StringBuffer();
    			StringBuffer listaNumerosManifestacoes = new StringBuffer();

    			String urlBase = parametroDAO.getUrlBase() ;
        		String emailMonitoramento = parametroDAO.getEmailMonitoramento();
    			TbPreferenciaSistema preferenciasSistema = preferenciaSistemaDAO.findAll().get(0);
    			String userName = 	preferenciasSistema.getNomeOuvidoria();

        		for (TbManifestacao tbManifestacao : manifestacoesMonitoradas) {
    				String urlManifestacao = "<li><a href='"
    						+ urlBase
    						+ "/pages/manifestacao/administrar.xhtml?num="
    						+ tbManifestacao.getNrManifestacao()
    						+ "'>Nr. Manifestação "
    						+ tbManifestacao.getNrManifestacao()
    						+ "</a></li>";
    				urlsManifestacoes.append(urlManifestacao + " <br/>");
    				listaNumerosManifestacoes.append("\tNr. Manifestação " + tbManifestacao.getNrManifestacao() + "\n");
    			}
    			
    			EmailService.Email email = emailService.newEmail();
    			
    			email.addDestinatario(userName, emailMonitoramento);
    			email.setAssunto("Monitoramento das Manifestações");
    			
    			StringBuilder emailTextoHtml = new StringBuilder()
    			.append("<div style='font-family: Arial, Verdana; font-size: 14px;'>")
    			.append("<center><span style='color: #006600; font-weight: bold;'>")
    			.append("Ouvidoria Ministério da Cultura")
    			.append("</span></center>")
    			.append("<br/>")
    			.append("<p>Prezados,</p>")
    			.append("<p>A(s) manifestação(ões) listadas a seguir estão com a data de monitoramento vencida: </p>")
    			.append(urlsManifestacoes.toString())
    			.append("<p>Acesse o sistema para monitorar a(s) manifestação(ões).</p>")
    			.append("<br/>")
    			.append("<center><span style='color: #006600; font-weight: bold; font-size: 11px;'>")
    			.append("Mensagem enviada automaticamente pelo sistema de Ouvidoria do Ministério da Cultura.")
    			.append("</span></center>")
    			.append("</div>");
    			
    			// configura a mensagem para o formato texto simples
    			StringBuilder emailTexto = new StringBuilder()
    			.append("Prezados,\n\n")
    			.append("A(s) manifestação(ões) listadas a seguir estão com a data de monitoramento vencida: \n")
    			.append(listaNumerosManifestacoes.toString())
    			.append("Acesse o sistema para monitorar a(s) manifestação(ões).");
    			
    			email.setTextoHtml(emailTextoHtml.toString());
    			email.setTextoSemFormatacao(emailTexto.toString());
    			
    			emailService.envia(email);
    		}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }
    


}
