package br.com.xti.ouvidoria.charts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.xti.ouvidoria.charts.dao.ChartDao;
import br.com.xti.ouvidoria.charts.enums.ChartUnidadeEnum;
import br.com.xti.ouvidoria.charts.pojo.DiasRespostaAOuvidoria;
import br.com.xti.ouvidoria.charts.pojo.IResultadoGrafico;
import br.com.xti.ouvidoria.charts.pojo.MensagemEnviadaUnidades;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebida;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaClassificacao;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaRegiaoEconomica;
import br.com.xti.ouvidoria.charts.pojo.MensagemRecebidaTipo;
import br.com.xti.ouvidoria.charts.pojo.MensagemSolucionada;
import br.com.xti.ouvidoria.charts.pojo.MensagemSolucionadaUnidadeSolucionadora;
import br.com.xti.ouvidoria.charts.pojo.RespostaQuestionario;
import br.com.xti.ouvidoria.controller.MBListarManifestacoes;
import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.QuestionarioDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbQuestionario;
import br.com.xti.ouvidoria.util.JSFUtils;


@ViewScoped
@ManagedBean(name="mBCharts")
public class MBCharts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ChartDao dao;
	
	@EJB
	private QuestionarioDAO questionarioDAO;
	
	@EJB
	private ClassificacaoDAO classificacaoDAO;
	
	@EJB
	private ManifestacaoDAO manifestacaoDAO;
	
	@Inject
	private ChartService service;
	
	
	private Date dataDe;
	private Date dataAte;
	private int codigo;
	private int total;
	private StringBuilder dados = new StringBuilder();
	private StringBuilder labels = new StringBuilder();
	private StringBuilder titulo = new StringBuilder();
	private StringBuilder subtitulo = new StringBuilder();
	private List<TbManifestacao> listaManifestacoes = new ArrayList<>();
	private List<TbManifestacao> listaManifestacoesFiltro = new ArrayList<>();
	private List<Integer> idUnidades = new ArrayList<>();
	private static Map<String,Object> meses = new LinkedHashMap<>();
	
	
	private void inicializar(Collection<? extends IResultadoGrafico> itens) {
		labels.setLength(0);
		dados.setLength(0);
		titulo.setLength(0);
		subtitulo = DataHelper.getNomeSubtitulo(dataDe, dataAte);
		listaManifestacoes = new ArrayList<>();
		listaManifestacoesFiltro = new ArrayList<>();
		
		// quantidade
		total = 0;
		for (IResultadoGrafico i : itens) {
			total += i.getQuantidade();
		}
	}

	public void getTotalMensagensRecebidasGeral() {
		Collection<MensagemRecebida> itens = service.getMensagensRecebidasEntreDatas(dataDe, dataAte);
		inicializar(itens);
		ChartHelper.getTotalMensagensRecebidasGeral(itens, labels, dados);
	}

	public void getTotalMensagensRecebidasInternaVinculada() {
		Collection<MensagemRecebida> itens = service.getMensagensRecebidasEntreDatas(dataDe, dataAte);
		inicializar(itens);
		ChartHelper.getTotalMensagensRecebidasInternaVinculada(itens, labels, dados);
	}

	public void getTotalMensagensEnviadasUnidadesPrefeitura() {
		Collection<MensagemEnviadaUnidades> itens = service.getMensagensEnviadasUnidadesEntreDatas(dataDe, dataAte, ChartUnidadeEnum.UNIDADES_INTERNAS);
		inicializar(itens);
		ChartHelper.getTotalMensagensEnviadasUnidades(itens, dados);
	}

	public void getTotalMensagensEnviadasUnidadesVinculadas() {
		Collection<MensagemEnviadaUnidades> itens = service.getMensagensEnviadasUnidadesEntreDatas(dataDe, dataAte, ChartUnidadeEnum.UNIDADES_VINCULADAS);
		inicializar(itens);
		ChartHelper.getTotalMensagensEnviadasUnidades(itens, dados);
	}
	
	public void getTipoMensagensRecebidasUnidadesPrefeitura() {
		Collection<MensagemRecebidaTipo> itens = service.getTipoMensagensRecebidasEntreDatas(dataDe, dataAte, ChartUnidadeEnum.UNIDADES_INTERNAS);
		inicializar(itens);
		ChartHelper.getTotalMensagensRecebidasClassificacao(itens, dados);
	}
	
	public void getTipoMensagensRecebidasUnidadesVinculadas() {
		Collection<MensagemRecebidaTipo> itens = service.getTipoMensagensRecebidasEntreDatas(dataDe, dataAte, ChartUnidadeEnum.UNIDADES_VINCULADAS);
		inicializar(itens);
		ChartHelper.getTotalMensagensRecebidasClassificacao(itens, dados);
	}
	
	public void getTotalMensagensRecebidasRegiaoEconomica() {
		Collection<MensagemRecebidaRegiaoEconomica> itens = service.getMensagensRecebidasRegiaoEconomicaEntreDatas(dataDe, dataAte);
		inicializar(itens);
		ChartHelper.getTotalMensagensRecebidasRegiaoEconomica(itens, dados);
	}

	public void getTotalMensagensSolucionadasGeral() {
		Collection<MensagemSolucionada> itens = service.getMensagensSolucionadasEntreDatas(dataDe, dataAte);
		inicializar(itens);
		ChartHelper.getTotalMensagensSolucionadasGeral(itens, labels, dados);
	}
	
	public void getRespostasQuestionarios() {
		Collection<RespostaQuestionario> itens = service.getRespostaQuestionario(codigo);
		inicializar(itens);
		TbQuestionario questionario = questionarioDAO.find(codigo);
		setSubtitulo(questionario.getNmQuestionario());
		setTotal(questionario.getManifestacoes().size());
		ChartHelper.getRespostaQuestionarios(itens, labels, dados);
		listaManifestacoes.clear();
	}
	
	public void getTotalMensagensClassificacaoTipos() {
		Collection<MensagemRecebidaTipo> itens = service.getTipoMensagensRecebidasEntreDatasPorClassificacao(dataDe, dataAte, codigo);
		inicializar(itens);
		subtitulo.deleteCharAt(subtitulo.length() - 1)
		.append("<br />")
		.append(classificacaoDAO.find(codigo).getDsClassificacao())
		.append("'");
		ChartHelper.getTotalMensagensRecebidasClassificacao(itens, dados);
	}
	
	public void getClassificacoesCincoMais() {
		Collection<MensagemRecebidaClassificacao> itens = service.getClassificacaoMensagensRecebidasEntreDatas(dataDe, dataAte);
		inicializar(itens);
		total = ChartHelper.getMensagensRecebidasClassificacaoCincoMais(itens, dados);
	}
	
	public void getTotalMensagensSolucionadasUnidadeSolucionadora() {
		Collection<MensagemSolucionadaUnidadeSolucionadora> itens = service.getMensagensSolucionadasPorUnidadeSolucionadoraEntreDatas(dataDe, dataAte);
		inicializar(itens);
		ChartHelper.getTotalMensagensSolucionadasUnidadeSolucionadora(itens, dados);
	}
	
	public void getMediaDiasRespostaAOuvidoria() {
		Collection<DiasRespostaAOuvidoria> itens = service.getDiasRespostaAOuvidoria(dataDe, dataAte, idUnidades);
		inicializar(itens);
		int totalCount = 0;
		if(ValidacaoHelper.isNotEmpty(itens)) {
			for (DiasRespostaAOuvidoria bean : itens) {
				totalCount += bean.getTotalCount();
			}
			total /= totalCount;
		}
		ChartHelper.getMediaDiasRespostaAOuvidoria(itens, labels, dados);
	}
	
	
	public void setIdManifestacoes() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		String ids = map.get("idManifestacoes");
		
		if(ValidacaoHelper.isNotEmpty(ids)) {
			FiltroPersonalizado filtro = new FiltroPersonalizado();
			for (String id : ids.split("[,]+")) {
				filtro.addManIdLista(new Integer(id));
			}
			setListaManifestacoes(manifestacaoDAO.getManifestacoes(filtro));
			setListaManifestacoesFiltro(getListaManifestacoes());
		}
	}
	
	public void limparFiltros() {
		dataAte = null;
		dataDe = null;
		idUnidades = new ArrayList<>();
		listaManifestacoes = new ArrayList<>();
		listaManifestacoesFiltro = new ArrayList<>();
	}
	
	public void goToManifestationPage(TbManifestacao manifestation) {
		try {
			JSFUtils.redirect(String.format(MBListarManifestacoes.URL_MANAGE_MANIFESTATION, manifestation.getNrManifestacao()));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	// GETTERs e SETTERs
	public Map<String, Object> getValoresMes() {
		return meses;
	}
	
	public Map<String, Object> getValoresAno() {
		Map<String,Object> anos = new LinkedHashMap<>();
		for (int i = 2010; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
			anos.put(String.valueOf(i), i);
		}
		return anos;
	}
	
	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public String getDados() {
		String data = dados.toString();
		return data;
	}

	public void setDados(String dados) {
		this.dados = new StringBuilder(dados);
	}

	public String getLabels() {
		return labels.toString();
	}

	public void setLabels(String labels) {
		this.labels = new StringBuilder(labels);
	}

	public String getSubtitulo() {
		return subtitulo.toString();
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = new StringBuilder("'" + subtitulo + "'");
	}

	public String getTitulo() {
		return titulo.toString();
	}

	public void setTitulo(String titulo) {
		this.titulo = new StringBuilder(titulo);
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<TbManifestacao> getListaManifestacoes() {
		return listaManifestacoes;
	}

	public void setListaManifestacoes(List<TbManifestacao> listaManifestacoes) {
		this.listaManifestacoes = listaManifestacoes;
	}
	
	public List<TbManifestacao> getListaManifestacoesFiltro() {
		return listaManifestacoesFiltro;
	}

	public void setListaManifestacoesFiltro(List<TbManifestacao> listaManifestacoesFiltro) {
		this.listaManifestacoesFiltro = listaManifestacoesFiltro;
	}
	
	public List<Integer> getIdUnidades() {
		return idUnidades;
	}
	
	public void setIdUnidades(List<Integer> idUnidades) {
		this.idUnidades = idUnidades;
	}
	
	
	
	static {
		meses.put("Janeiro", 1);
		meses.put("Fevereiro", 2);
		meses.put("Março", 3);
		meses.put("Abril", 4);
		meses.put("Maio", 5);
		meses.put("Junho", 6);
		meses.put("Julho", 7);
		meses.put("Agosto", 8);
		meses.put("Setembro", 9);
		meses.put("Outubro", 10);
		meses.put("Novembro", 11);
		meses.put("Dezembro", 12);
	}
}
