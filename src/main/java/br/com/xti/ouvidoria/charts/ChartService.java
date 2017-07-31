package br.com.xti.ouvidoria.charts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import br.com.xti.ouvidoria.charts.dao.ChartDao;
import br.com.xti.ouvidoria.charts.enums.ChartUnidadeEnum;
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
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.RegiaoHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbRespostaQuestionario;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;
import br.com.xti.ouvidoria.model.TbUF;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.enums.RegiaoEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;

@Named("chartService")
@ConversationScoped
public class ChartService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ChartDao dao;
	
	public Collection<MensagemRecebida> getMensagensRecebidasEntreDatas(Date dataDe, Date dataAte) {
		Set<MensagemRecebida> set = new TreeSet<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			List<TbManifestacao> manifestacoesMes = new ArrayList<>();
			int mes = DataHelper.getMes(manifestacoes.get(0).getDtCadastro());
			int ano = DataHelper.getAno(manifestacoes.get(0).getDtCadastro());
			for (TbManifestacao m : manifestacoes) {
				int mesCorrente = DataHelper.getMes(m.getDtCadastro());
				int anoCorrente = DataHelper.getAno(m.getDtCadastro());
				if(mesCorrente != mes) {
					set.add(ChartHelper.criarMensagemRecebida(manifestacoesMes, mes, ano));
					if(anoCorrente != ano) ano = anoCorrente;
					mes = mesCorrente;
					manifestacoesMes = new ArrayList<>();
				}
				manifestacoesMes.add(m);
			}
			set.add(ChartHelper.criarMensagemRecebida(manifestacoesMes, mes, ano));
			
			// Completa o set com as informações com possíveis meses que não tem dados
			int anoAte = DataHelper.getAno(dataAte);
			int mesAte = DataHelper.getMes(dataAte);
			int anoDe = DataHelper.getAno(dataDe);
			int mesDe = DataHelper.getMes(dataDe);
			formes: for (int year = anoDe; year <= anoAte; year++) {
				// Define mes inicial para o loop
				int month;
				if(year == anoDe) month = mesDe;
				else month = 0; // Janeiro
				// Define o mes final para o loop
				int monthMax;
				if(year == anoAte) monthMax = mesAte;
				else monthMax = 11; // Dezembro
					
				for (; month <= monthMax ; month++) {
					set.add(ChartHelper.criarMensagemRecebida(null, month, year));
					
					if(year == anoAte && month == mesAte)
						break formes;
				}
			}
		}
		
		return set;
	}
	
	public Collection<MensagemEnviadaUnidades> getMensagensEnviadasUnidadesEntreDatas(Date dataDe, Date dataAte, ChartUnidadeEnum unidadesParaMostrar) {
		Set<MensagemEnviadaUnidades> set = new LinkedHashSet<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			int totalInterna = 0;
			int totalVinculada = 0;
			Map<TbUnidade, MensagemEnviadaUnidades> map = new HashMap<>();
			for (TbManifestacao m : manifestacoes) {
				for (TbEncaminhamento e : m.getTbEncaminhamentoCollection()) {
					TbUnidade unidade = e.getIdUnidadeRecebeu();
					
					if(unidade.isVinculada()) {
						totalVinculada++;
					} else {
						totalInterna++;
					}
					
					if(map.containsKey(unidade)) {
						map.get(unidade).addQuantidade();
						map.get(unidade).addIdManifestacao(m.getIdManifestacao());
					} else {
						map.put(unidade, new MensagemEnviadaUnidades(unidade, m.getIdManifestacao(), 1, 0));
					}
				}
			}
			
			// Define o Valor Total
			int total = 0;
			switch (unidadesParaMostrar) {
				case UNIDADES_INTERNAS: total = totalInterna; break;
				case UNIDADES_VINCULADAS: total = totalVinculada; break;
				default: total = totalVinculada + totalInterna; break;
			}
			
			for (Map.Entry<TbUnidade, MensagemEnviadaUnidades> entry : map.entrySet()) {
				switch (unidadesParaMostrar) {
					case UNIDADES_INTERNAS:
						if (entry.getKey().isVinculada()) {
							continue;
						}
						break;
					case UNIDADES_VINCULADAS:
						if(!entry.getKey().isVinculada()) {
							continue;
						}
						break;
					default:
						break;
				}
				ChartHelper.ajustarMensagemEnviadaUnidades(entry.getValue(), total);
				set.add(entry.getValue());
			}
		}
		
		return set;
	}
	
	public Collection<MensagemRecebidaTipo> getTipoMensagensRecebidasEntreDatas(Date dataDe, Date dataAte, ChartUnidadeEnum unidadesParaMostrar) {
		Set<MensagemRecebidaTipo> set = new LinkedHashSet<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			int total = 0;
			Map<TbTipoManifestacao, MensagemRecebidaTipo> map = new HashMap<>();
			for (TbManifestacao m : manifestacoes) {
				TbTipoManifestacao tipo = m.getIdTipoManifestacao();
				for (TbEncaminhamento e : m.getTbEncaminhamentoCollection()) {
					TbUnidade unidade = e.getIdUnidadeRecebeu();
					switch (unidadesParaMostrar) {
						case UNIDADES_INTERNAS:
							if(unidade.isVinculada()) {
								continue;
							}
							break;
						case UNIDADES_VINCULADAS:
							if(!unidade.isVinculada()) {
								continue;
							}
							break;
						default:
							break;
					}
					
					total++;
					if(map.containsKey(tipo)) {
						map.get(tipo).addIdManifestacao(m.getIdManifestacao());
					} else {
						map.put(tipo, new MensagemRecebidaTipo(tipo, m.getIdManifestacao(), 0));
					}
				}
				
			}
			
			for (Map.Entry<TbTipoManifestacao, MensagemRecebidaTipo> entry : map.entrySet()) {
				ChartHelper.ajustarTipoMensagemRecebida(entry.getValue(), total);
				set.add(entry.getValue());
			}
		}
		
		return set;
	}

	public Collection<MensagemRecebidaRegiaoEconomica> getMensagensRecebidasRegiaoEconomicaEntreDatas(Date dataDe, Date dataAte) {
		Set<MensagemRecebidaRegiaoEconomica> set = new LinkedHashSet<>();
		Map<RegiaoEnum, MensagemRecebidaRegiaoEconomica> map = new HashMap<>();
		map.put(RegiaoEnum.NORTE, MensagemRecebidaRegiaoEconomica.regiaoNorte());
		map.put(RegiaoEnum.NORDESTE, MensagemRecebidaRegiaoEconomica.regiaoNordeste());
		map.put(RegiaoEnum.CENTRO_OESTE, MensagemRecebidaRegiaoEconomica.regiaoCentroOeste());
		map.put(RegiaoEnum.SUDESTE, MensagemRecebidaRegiaoEconomica.regiaoSudeste());
		map.put(RegiaoEnum.SUL, MensagemRecebidaRegiaoEconomica.regiaoSul());
		map.put(RegiaoEnum.NAO_ESPECIFICADO, MensagemRecebidaRegiaoEconomica.regiaoNaoEspecificada());
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			int total = 0;
			for (TbManifestacao m : manifestacoes) {
				total++;
				TbUF uf = m.getIdUF();
				
				if(uf == null || uf.getIdUF() == 0)
					map.get(RegiaoEnum.NAO_ESPECIFICADO).addIdManifestacao(m.getIdManifestacao());
				else {
					switch (RegiaoHelper.getRegiaoPorIdUf(uf.getIdUF())) {
						case NORTE: map.get(RegiaoEnum.NORTE).addIdManifestacao(m.getIdManifestacao()); break;
						case NORDESTE: map.get(RegiaoEnum.NORDESTE).addIdManifestacao(m.getIdManifestacao()); break;
						case CENTRO_OESTE: map.get(RegiaoEnum.CENTRO_OESTE).addIdManifestacao(m.getIdManifestacao()); break;
						case SUDESTE: map.get(RegiaoEnum.SUDESTE).addIdManifestacao(m.getIdManifestacao()); break;
						case SUL: map.get(RegiaoEnum.SUL).addIdManifestacao(m.getIdManifestacao()); break;
						default: break;
					}
				}
			}
			
			for (Map.Entry<RegiaoEnum, MensagemRecebidaRegiaoEconomica> entry : map.entrySet()) {
				ChartHelper.ajustarMensagemRecebidaRegiaoEconomica(entry.getValue(), total);
				set.add(entry.getValue());
			}
			
		}
		
		return set;
	}
	
	public Collection<MensagemSolucionada> getMensagensSolucionadasEntreDatas(Date dataDe, Date dataAte) {
		Set<MensagemSolucionada> set = new TreeSet<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesSolucionadasEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			List<TbManifestacao> manifestacoesMes = new ArrayList<>();
			int mes = DataHelper.getMes(manifestacoes.get(0).getDtFechamento());
			int ano = DataHelper.getAno(manifestacoes.get(0).getDtFechamento());
			for (TbManifestacao m : manifestacoes) {
				int mesCorrente = DataHelper.getMes(m.getDtFechamento());
				if(mesCorrente != mes) {
					set.add(ChartHelper.criarMensagemSolucionada(manifestacoesMes, mes, ano));
					mes = mesCorrente;
					manifestacoesMes = new ArrayList<>();
				}
				manifestacoesMes.add(m);
			}
			set.add(ChartHelper.criarMensagemSolucionada(manifestacoesMes, mes, ano));
			
			// Completa o set com as informações com possíveis meses que não tem dados
			int anoAte = DataHelper.getAno(dataAte);
			int mesAte = DataHelper.getMes(dataAte);
			int anoDe = DataHelper.getAno(dataDe);
			int mesDe = DataHelper.getMes(dataDe);
			formes: for (int year = anoDe; year <= anoAte; year++) {
				// Define mes inicial para o loop
				int month;
				if(year == anoDe) month = mesDe;
				else month = 0; // Janeiro
				// Define o mes final para o loop
				int monthMax;
				if(year == anoAte) monthMax = mesAte;
				else monthMax = 11; // Dezembro
					
				for (; month <= monthMax ; month++) {
					set.add(ChartHelper.criarMensagemSolucionada(null, month, year));
					
					if(year == anoAte && month == mesAte)
						break formes;
				}
			}
		}
		
		return set;
	}
	
	public Collection<RespostaQuestionario> getRespostaQuestionario(int idQuestionario) {
		Map<Integer, RespostaQuestionario> map = new HashMap<>();
		
		List<TbRespostaQuestionario> respostas = dao.getRespsotasQuestionario(idQuestionario);
		if(!respostas.isEmpty()) {
			for (TbRespostaQuestionario resp : respostas) {
				RespostaQuestionario resposta;
				if(map.containsKey(resp.getPergunta().getIdPergunta()))
					resposta = map.get(resp.getPergunta().getIdPergunta());
				else {
					resposta = new RespostaQuestionario();
					resposta.setIdPergunta(resp.getPergunta().getIdPergunta());
					resposta.setNomePergunta(resp.getPergunta().getDsPergunta());
					resposta.setPosicao(resp.getPergunta().getPosicaoPergunta());
					
					map.put(resposta.getIdPergunta(), resposta);
				}
				
				if(ValidacaoHelper.isNotEmpty(resp.getDsResposta())) {
					switch(EnumHelper.getRespostaQuestionarioEnum(String.valueOf(resp.getDsResposta()))) {
						case CONCORDO_TOTALMENTE:
							resposta.addConcordoTotalmente();
							resposta.addConcordoTotalmenteNrManifestacao(resp.getManifestacao().getIdManifestacao());
							break;
						case CONCORDO:
							resposta.addConcordo();
							resposta.addConcordoNrManifestacao(resp.getManifestacao().getIdManifestacao());
							break;
						case DISCORDO:
							resposta.addDiscordo();
							resposta.addDiscordoNrManifestacao(resp.getManifestacao().getIdManifestacao());
							break;
						case DISCORDO_TOTALMENTE:
							resposta.addDiscordoTotalmente();
							resposta.addDiscordoTotalmenteNrManifestacao(resp.getManifestacao().getIdManifestacao());
							break;
						default: break;
					}
				}
			}
		}
		
		List<RespostaQuestionario> itens = new ArrayList<RespostaQuestionario>(map.values());
		Collections.sort(itens);
		return itens;
	}
	
	public Collection<MensagemRecebidaTipo> getTipoMensagensRecebidasEntreDatasPorClassificacao(Date dataDe, Date dataAte, int idClassificacao) {
		Set<MensagemRecebidaTipo> set = new LinkedHashSet<>();
				
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatasPorClassificacao(dataDe, dataAte, idClassificacao);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			Map<TbTipoManifestacao, MensagemRecebidaTipo> map = new HashMap<>();
			for (TbManifestacao m : manifestacoes) {
				TbTipoManifestacao tipo = m.getIdTipoManifestacao();
				if(map.containsKey(tipo)) {
					map.get(tipo).addIdManifestacao(m.getIdManifestacao());
				} else {
					map.put(tipo, new MensagemRecebidaTipo(tipo, m.getIdManifestacao(), 0));
				}
			}
			
			for (Map.Entry<TbTipoManifestacao, MensagemRecebidaTipo> entry : map.entrySet()) {
				ChartHelper.ajustarTipoMensagemRecebida(entry.getValue(), manifestacoes.size());
				set.add(entry.getValue());
			}
		}
		
		return set;
	}
	
	public Collection<MensagemRecebidaClassificacao> getClassificacaoMensagensRecebidasEntreDatas(Date dataDe, Date dataAte) {
		List<MensagemRecebidaClassificacao> dados = new ArrayList<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			int total = 0;
			Map<TbClassificacao, MensagemRecebidaClassificacao> map = new HashMap<>();
			for (TbManifestacao m : manifestacoes) {
				for (TbClassificacao classificacao : m.getTbClassificacaoCollection()) {
					if(map.containsKey(classificacao)) {
						map.get(classificacao).addIdManifestacao(m.getIdManifestacao());
					} else {
						map.put(classificacao, new MensagemRecebidaClassificacao(classificacao, m.getIdManifestacao(), 0));
					}
					
					total++;
				}
			}
			
			for (Map.Entry<TbClassificacao, MensagemRecebidaClassificacao> entry : map.entrySet()) {
				ChartHelper.ajustarClassificacaoMensagemRecebida(entry.getValue(), total);
				dados.add(entry.getValue());
			}
		}
		
		Collections.sort(dados);
		return dados;
	}
	
	public Collection<MensagemSolucionadaUnidadeSolucionadora> getMensagensSolucionadasPorUnidadeSolucionadoraEntreDatas(Date dataDe, Date dataAte) {
		Set<MensagemSolucionadaUnidadeSolucionadora> set = new LinkedHashSet<>();
		
		List<TbManifestacao> manifestacoes = dao.getManifestacoesSolucionadasEntreDatas(dataDe, dataAte);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			int total = 0;
			Map<TbUnidade, MensagemSolucionadaUnidadeSolucionadora> map = new HashMap<>();
			for (TbManifestacao m : manifestacoes) {
				for (TbUnidade unidade : m.getTbUnidadeAreaSolucionadoraCollection()) {
					if(map.containsKey(unidade)) {
						map.get(unidade).addIdManifestacao(m.getIdManifestacao());
					} else {
						map.put(unidade, new MensagemSolucionadaUnidadeSolucionadora(unidade, m.getIdManifestacao(), 0));
					}
					
					total++;
				}
			}
			
			for (Map.Entry<TbUnidade, MensagemSolucionadaUnidadeSolucionadora> entry : map.entrySet()) {
				ChartHelper.ajustarUnidadeSolucionadoraMensagemSolucionada(entry.getValue(), total);
				set.add(entry.getValue());
			}
		}
		
		return set;
	}

	public Collection<DiasRespostaAOuvidoria> getDiasRespostaAOuvidoria(Date dataDe, Date dataAte, List<Integer> idUnidades) {
		Set<DiasRespostaAOuvidoria> set = new TreeSet<>();
		int idOuvidoria = UnidadeEnum.OUVIDORIA.getId();
		List<TbManifestacao> manifestacoes = dao.getManifestacoesEntreDatas(dataDe, dataAte, idOuvidoria, idUnidades);
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			Map<TbUnidade, DiasRespostaAOuvidoria> map = new HashMap<>();
			for (TbManifestacao manifestacao : manifestacoes) {
				for (TbEncaminhamento encaminhamento : manifestacao.getTbEncaminhamentoCollection()) {
					if(StatusEncaminhamentoEnum.RETORNADA.getId().equals(encaminhamento.getStEncaminhamento())) {
						TbUnidade unidade = encaminhamento.getIdUnidadeRecebeu();
						if (ValidacaoHelper.isEmpty(idUnidades) || idUnidades.contains(unidade.getIdUnidade().toString())) {
							int dias = DataHelper.getDiferencaEntreDatasEmDias(
									encaminhamento.getDtCriacaoEncaminhamento(),
									encaminhamento.getDtRespostaTramite());
							
							if(map.containsKey(unidade)) {
								map.get(unidade).addIdManifestacao(manifestacao.getIdManifestacao());
								map.get(unidade).addDias(dias);
							} else {
								map.put(unidade, new DiasRespostaAOuvidoria(unidade, manifestacao.getIdManifestacao(), dias));
							}
						}
					}
				}
			}
			
			Iterator<DiasRespostaAOuvidoria> iterator = map.values().iterator();
			for( ; iterator.hasNext() ; ) {
				DiasRespostaAOuvidoria bean = iterator.next();
				bean.calcularPorcetagem();
				if(ValidacaoHelper.isEmpty(manifestacoes) && ValidacaoHelper.isEmpty(bean.getQuantidade())) {
					iterator.remove();
				}
			}
			
			set.addAll(map.values());
		}
		
		return set;
	}
	
}
