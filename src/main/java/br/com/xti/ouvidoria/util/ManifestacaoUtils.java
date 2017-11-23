package br.com.xti.ouvidoria.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import br.com.xti.ouvidoria.extraction.ExtracaoHelper;
import br.com.xti.ouvidoria.extraction.ManifestacaoVO;
import br.com.xti.ouvidoria.helper.CdiHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.security.SecurityService;

public class ManifestacaoUtils {
	
	private static SecurityService securityService;
	
	static {
		securityService = CdiHelper.getFacadeWithJNDI(SecurityService.class);
	}
	
	public static Date getDataLimiteAtendimentoUnidade(TbEncaminhamento encaminhamento) {
    	Date dataFinalAtendimento = null;
    	if(encaminhamento != null) {
	    	TbManifestacao manifestation = encaminhamento.getIdManifestacao();
	    	int diasParaResponder = manifestation.getIdTipoManifestacao().getPrazoAreaSolucionadora();
    		Calendar c = Calendar.getInstance();
    		c.setTime(encaminhamento.getDtEnvioTramite());
    		c.add(Calendar.DAY_OF_MONTH, diasParaResponder);
    		dataFinalAtendimento = c.getTime();
    	}
    	return dataFinalAtendimento;
    }
	
	public static Date getDataLimiteAtendimentoUnidadeUsuarioLogado(TbManifestacao manifestacao) {
		TbEncaminhamento encaminhamento = getEncaminhamentoDoUsuarioLogado(manifestacao);
		return getDataLimiteAtendimentoUnidade(encaminhamento);
	}
	
	public static int getDiasAtrasoEncaminahmento(TbEncaminhamento encaminhamento) {
		int diasEmAtraso = 0;
		
		if(StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(encaminhamento.getStEncaminhamento())) {
			Date dataLimiteParaResponder = getDataLimiteAtendimentoUnidade(encaminhamento);
			DateTimeZone BRAZIL = DateTimeZone.forID("America/Sao_Paulo");
			DateTime start = new LocalDate(dataLimiteParaResponder.getTime(), BRAZIL).toDateTimeAtStartOfDay();
			DateTime end = new LocalDate(new Date().getTime(), BRAZIL).toDateTimeAtStartOfDay();
			Days days = Days.daysBetween(start, end);
			
			if(days.isGreaterThan(null)) {
				diasEmAtraso = days.getDays();
			}
		}
		
		return diasEmAtraso;
	}
	
	public static boolean isEncaminhamentoAtrasado(TbEncaminhamento encaminhamento) {
		boolean atrasado = false;
		if(getDiasAtrasoEncaminahmento(encaminhamento) > 0) {
			atrasado = true;
		}
		return atrasado;
	}
	
	/**
	 * @param manifestacao manifestação
	 * @return Encaminhamento realizado a unidade do usuário logado
	 */
	public static TbEncaminhamento getEncaminhamentoDoUsuarioLogado(TbManifestacao manifestacao) {
		TbUnidade unidadeUsuario = securityService.getUser().getIdUnidade();
		Collection<TbEncaminhamento> encaminhamentos = manifestacao.getTbEncaminhamentoCollection();
		if (encaminhamentos != null) {
			for (TbEncaminhamento encaminhamento : encaminhamentos) {
				if (encaminhamento.getIdUnidadeRecebeu().equals(unidadeUsuario)) {
					return encaminhamento;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param manifestacao Manifestação
	 * @param incluirEmailPrincipal se deve incluir o email principal na lista
	 * @return lista de emails (do manifestante) cadastrados na manifestação
	 */
	public static List<String> converterEmListaDeEmails(TbManifestacao manifestacao, boolean incluirEmailPrincipal) {
		List<String> list = new ArrayList<String>();
		String secondaryEmailsText = manifestacao.getEeEmailSecundario();
		if(ValidacaoHelper.isNotEmpty(secondaryEmailsText)) {
			String[] arrSecondaryEmails = secondaryEmailsText.split("[,;]+");
			list.addAll(Arrays.asList(arrSecondaryEmails));
		}
		
		if (incluirEmailPrincipal) {
			list.add(0, manifestacao.getEeEmailUsuario());
		}
		
		return list;
	}
	
	/**
	 * Gera um relatório com o nome e manifestações informados e efetua o download
	 * 
	 * @param reportName Nome do relatório a ser gerado
	 * @param manifestacoes Lista de manifestações a serem exportadas para o relatório
	 * @throws Exception Caso aconteça qualquer tipo de exceção ao tentar gerar e baixar o relatório
	 */
	public static void downloadReport(String reportName, List<TbManifestacao> manifestacoes) throws Exception {
		FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s.xls\"", reportName));
        
        // Converte a lista em um arquivo .xls
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream templateFile = classLoader.getResourceAsStream("/TemplateExtracaoDados.xls");
        Map<String, List<ManifestacaoVO>> beans = new HashMap<String, List<ManifestacaoVO>>();
        beans.put("data", ExtracaoHelper.convert(manifestacoes));
        
        XLSTransformer transformer = new XLSTransformer();
        Workbook workbook = transformer.transformXLS(templateFile, beans);
        workbook.write(response.getOutputStream());
        facesContext.responseComplete();
    }

}
