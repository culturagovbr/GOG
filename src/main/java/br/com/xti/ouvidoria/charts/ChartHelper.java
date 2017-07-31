package br.com.xti.ouvidoria.charts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.xti.ouvidoria.charts.pojo.DiasRespostaAOuvidoria;
import br.com.xti.ouvidoria.charts.pojo.MensagemEnviadaUnidades;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebida;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaClassificacao;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaRegiaoEconomica;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaTipo;
import br.com.xti.ouvidoria.charts.pojo.MensagemSolucionada;
import br.com.xti.ouvidoria.charts.pojo.MensagemSolucionadaUnidadeSolucionadora;
import br.com.xti.ouvidoria.charts.pojo.RespostaQuestionario;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.enums.RespostasQuestionariosEnum;

public class ChartHelper {
	
	private static final String TEMPLATE_TOTAL_MENSAGENS_RECEBIDAS_INTERNA_VINCULADA;
	private static final String TEMPLATE_QUESTIONARIO;
	private static final String TEMPLATE_DATA_CHART;
	
	public static MensagemRecebida criarMensagemRecebida(List<TbManifestacao> manifestacoes, int mes, int ano) {
		String periodo = DataHelper.getNomePeriodo(mes, ano);
		
		if(ValidacaoHelper.isEmpty(manifestacoes)) {
			return new MensagemRecebida(periodo, DataHelper.criarData(mes, ano), null, null, 0);
		}
		
		int quantidadeTotal = 0;
		List<TbManifestacao> manifestacoesInternas = new ArrayList<TbManifestacao>();
		List<TbManifestacao> manifestacoesVinculadas = new ArrayList<TbManifestacao>();
		for (TbManifestacao m : manifestacoes) {
			boolean hasInterna = Boolean.FALSE;
			boolean hasVinculada = Boolean.FALSE;
			Collection<TbEncaminhamento> encaminhamentos = m.getTbEncaminhamentoCollection();
			for (TbEncaminhamento e : encaminhamentos) {
				if(hasInterna && hasVinculada) {
					break;
				}
				
				if(e.getIdUnidadeRecebeu().isVinculada()) {
					hasVinculada = Boolean.TRUE;
				} else {
					hasInterna = Boolean.TRUE;
				}
			}
			
			quantidadeTotal++;
			if(hasInterna) {
				manifestacoesInternas.add(m);
			}
			
			if(hasVinculada) {
				manifestacoesVinculadas.add(m);
			}
		}
		return new MensagemRecebida(periodo, DataHelper.criarData(mes, ano), manifestacoesInternas, manifestacoesVinculadas, quantidadeTotal);
	}
	
	public static void getTotalMensagensRecebidasGeral(final Collection<MensagemRecebida> itens, StringBuilder labels, StringBuilder dados) {
		for (MensagemRecebida mensagemRecebida : itens) {
			String periodo = mensagemRecebida.getPeriodo();
			labels.append("'"+periodo+"',");
			dados.append(String.format(TEMPLATE_DATA_CHART, periodo, 
					mensagemRecebida.getQuantidadeTotal(), mensagemRecebida.getIdManifestacoes()));
		}
	}
	
	public static void getTotalMensagensRecebidasInternaVinculada(final Collection<MensagemRecebida> itens, StringBuilder labels, StringBuilder dados) {
		StringBuilder dadosInterna = new StringBuilder();
		StringBuilder dadosVinculada = new StringBuilder();
		for (MensagemRecebida mensagemRecebida : itens) {
			String periodo = mensagemRecebida.getPeriodo();
			labels.append("'"+periodo+"',");
			
			dadosInterna.append(String.format(TEMPLATE_DATA_CHART, periodo,
					mensagemRecebida.getQuantidadeInterna(),
					mensagemRecebida.getIdManifestacoesInternas()));
			
			dadosVinculada.append(String.format(TEMPLATE_DATA_CHART, periodo,
					mensagemRecebida.getQuantidadeVinculada(),
					mensagemRecebida.getIdManifestacoesVinculadas()));
		}
		dados.append(String.format(TEMPLATE_TOTAL_MENSAGENS_RECEBIDAS_INTERNA_VINCULADA, dadosInterna, dadosVinculada));
	}
	
	public static void ajustarMensagemEnviadaUnidades(MensagemEnviadaUnidades meu, int total) {
		int quantidade = meu.getQuantidade();
		meu.setPorcentagem( ((double) quantidade * 100) / (double) total );
	}

	public static void getTotalMensagensEnviadasUnidades(final Collection<MensagemEnviadaUnidades> itens, StringBuilder dados) {
		for (MensagemEnviadaUnidades meu : itens) {
			String nomeUnidade = meu.getUnidade().getSgUnidade();
			String name = nomeUnidade + " (" + meu.getQuantidade() + ")";
			dados.append(String.format(TEMPLATE_DATA_CHART, name, 
					meu.getPorcentagemString(), meu.getIdManifestacoes()));
		}
	}
	
	public static void ajustarTipoMensagemRecebida(MensagemRecebidaTipo mrt, int total) {
		int quantidade = mrt.getQuantidade();
		mrt.setPorcentagem( ((double)quantidade * 100) / (double) total );
	}
	
	public static void ajustarClassificacaoMensagemRecebida(MensagemRecebidaClassificacao mrc, int total) {
		int quantidade = mrc.getQuantidade();
		mrc.setPorcentagem( ((double)quantidade * 100) / (double) total );
	}
	
	public static void ajustarUnidadeSolucionadoraMensagemSolucionada(MensagemSolucionadaUnidadeSolucionadora msus, int total) {
		int quantidade = msus.getQuantidade();
		msus.setPorcentagem( ((double)quantidade * 100) / (double) total );
	}
	
	public static void getTotalMensagensRecebidasClassificacao(final Collection<MensagemRecebidaTipo> itens, StringBuilder dados) {
		for (MensagemRecebidaTipo mrt : itens) {
			String nomeTipoManifestacao = mrt.getTipoManifestacao().getNmTipoManifestacao();
			String name = nomeTipoManifestacao + " (" + mrt.getQuantidade() + ")";
			dados.append(String.format(TEMPLATE_DATA_CHART, name, 
					mrt.getPorcentagemString(), mrt.getIdManifestacoes()));
		}
	}
	
	/**
	 * @param itens
	 * @param dados
	 * @return Quantidade de vezes que as cinco classificações mais usadas foram usadas.
	 */
	public static int getMensagensRecebidasClassificacaoCincoMais(final Collection<MensagemRecebidaClassificacao> itens, StringBuilder dados) {
		List<MensagemRecebidaClassificacao> cincoMais = new ArrayList<MensagemRecebidaClassificacao>();
		// Pegando as 5 mais usadas
		int contador = 1;
		int total = 0;
		for (MensagemRecebidaClassificacao mrc : itens) {
			if(contador > 5) break;
			cincoMais.add(mrc);
			contador++;
			total += mrc.getQuantidade();
		}
		
		// Setando porcentagem de uso das classificações
		for (MensagemRecebidaClassificacao mrc : cincoMais) {
			ajustarClassificacaoMensagemRecebida(mrc, total);
			String nomeDescricao = mrc.getClassificacao().getDsClassificacao();
			String name = nomeDescricao + " (" + mrc.getQuantidade() + ")"; 
			dados.append(String.format(TEMPLATE_DATA_CHART, name,
					mrc.getPorcentagemString(), mrc.getIdManifestacoes()));
		}
		
		return total;
	}
	
	public static void getTotalMensagensSolucionadasUnidadeSolucionadora(final Collection<MensagemSolucionadaUnidadeSolucionadora> itens, StringBuilder dados) {
		for (MensagemSolucionadaUnidadeSolucionadora msus : itens) {
			String nomeUnidade = msus.getUnidade().getNmUnidade();
			String name = nomeUnidade + " (" + msus.getQuantidade() + ")";
			dados.append(String.format(TEMPLATE_DATA_CHART, name, 
					msus.getPorcentagemString(), msus.getIdManifestacoes()));
		}
	}
	
	public static void ajustarMensagemRecebidaRegiaoEconomica(MensagemRecebidaRegiaoEconomica mrre, int total) {
		int quantidade = mrre.getQuantidade();
		mrre.setPorcentagem( ((double)quantidade * 100) / (double) total );
	}
	
	public static void getTotalMensagensRecebidasRegiaoEconomica(final Collection<MensagemRecebidaRegiaoEconomica> itens, StringBuilder dados) {
		for (MensagemRecebidaRegiaoEconomica mrre : itens) {
			String nomeRegiao = mrre.getRegiao().getNome();
			String name = nomeRegiao + " (" + mrre.getQuantidade() + ")";
			dados.append(String.format(TEMPLATE_DATA_CHART, name, 
					mrre.getPorcentagemString(), mrre.getIdManifestacoes()));
		}
	}
	
	public static MensagemSolucionada criarMensagemSolucionada(List<TbManifestacao> manifestacoes, int mes, int ano) {
		String periodo = DataHelper.getNomePeriodo(mes, ano);

		if(ValidacaoHelper.isEmpty(manifestacoes)) {
			return new MensagemSolucionada(periodo, DataHelper.criarData(mes, ano), null);
		}
		
		return new MensagemSolucionada(periodo, DataHelper.criarData(mes, ano), manifestacoes);
	}
	
	public static void getTotalMensagensSolucionadasGeral(final Collection<MensagemSolucionada> itens, StringBuilder labels, StringBuilder dados) {
		for (MensagemSolucionada mensagemSolucionada : itens) {
			String periodo = mensagemSolucionada.getPeriodo();
			labels.append("'"+periodo+"',");
			dados.append(String.format(TEMPLATE_DATA_CHART, periodo, 
					mensagemSolucionada.getQuantidade(), mensagemSolucionada.getIdManifestacoes()));
		}
	}
	
	public static void getRespostaQuestionarios(final Collection<RespostaQuestionario> itens, StringBuilder labels, StringBuilder dados) {
		String concordoTotalmente = "";
		String concordo = "";
		String discordo = "";
		String discordoTotalmente = "";
		
		for (RespostaQuestionario resposta : itens) {
			labels.append("'"+resposta.getNomePergunta()+"',");
			concordoTotalmente += "{y:" + resposta.getConcordoTotalmente() + ",drilldown:{name: '"+resposta.getConcordoTotalmenteNrManifestacoes()+"'}},";
			concordo += "{y:" + resposta.getConcordo() + ",drilldown:{name: '"+resposta.getConcordoNrManifestacoes()+"'}},";
			discordo += "{y:" + resposta.getDiscordo() + ",drilldown:{name: '"+resposta.getDiscordoNrManifestacoes()+"'}},";
			discordoTotalmente += "{y:" + resposta.getDiscordoTotalmente() + ",drilldown:{name: '"+resposta.getDiscordoTotalmenteNrManifestacoes()+"'}},";
		}
		dados.append(String.format(TEMPLATE_QUESTIONARIO, concordoTotalmente, concordo, discordo, discordoTotalmente));
	}
	
	public static void getMediaDiasRespostaAOuvidoria(final Collection<DiasRespostaAOuvidoria> itens, StringBuilder labels, StringBuilder dados) {
		for (DiasRespostaAOuvidoria bean : itens) {
			String siglaUnidade = bean.getUnidade().getSgUnidade();
			String label = String.format("%s (%s)", siglaUnidade, bean.getQuantidadeManifestacoes());
			labels.append("'" + label + "',");
			dados.append(String.format(TEMPLATE_DATA_CHART, siglaUnidade, 
					bean.getPorcentagemString(), bean.getIdManifestacoes()));
		}
	}
	
	
	static {
		TEMPLATE_TOTAL_MENSAGENS_RECEBIDAS_INTERNA_VINCULADA = "{name: 'Unidades do MinC', data: [%s]}, {name: 'Unidades Vinculadas', data: [%s]}";
		TEMPLATE_DATA_CHART = "{name:'%s', y:%s, drilldown:{name: '%s'}},";
		
		{
			StringBuilder sb = new StringBuilder()
			.append("{name: '").append(RespostasQuestionariosEnum.CONCORDO_TOTALMENTE.getDescricao()).append("', data: [%s]},")
			.append("{name: '").append(RespostasQuestionariosEnum.CONCORDO.getDescricao()).append("', data: [%s]},")
			.append("{name: '").append(RespostasQuestionariosEnum.DISCORDO.getDescricao()).append("', data: [%s]},")
			.append("{name: '").append(RespostasQuestionariosEnum.DISCORDO_TOTALMENTE.getDescricao()).append("', data: [%s]}");
			TEMPLATE_QUESTIONARIO = sb.toString();
		}
	}
	
}
