package br.com.xti.ouvidoria.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.UploadedFile;

import br.com.xti.ouvidoria.comparator.UnidadeSiglaComparator;
import br.com.xti.ouvidoria.controller.generic.AbstractManifestationController;
import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.EmailAutomatizadoDAO;
import br.com.xti.ouvidoria.dao.EncaminhamentoPadronizadoDAO;
import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.dao.PreferenciaSistemaDAO;
import br.com.xti.ouvidoria.dao.QuestionarioDAO;
import br.com.xti.ouvidoria.dao.RespostaManifestacaoDAO;
import br.com.xti.ouvidoria.dao.SubClassificacaoDAO;
import br.com.xti.ouvidoria.dao.TipoManifestacaoDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.FileHelper;
import br.com.xti.ouvidoria.helper.PalavrasChavesHelper;
import br.com.xti.ouvidoria.helper.ReflectionHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.manifestacao.view.ManifestacaoTabView;
import br.com.xti.ouvidoria.model.TbAnexo;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbComunicacaoExternaxAnexo;
import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbEncaminhamentoPadronizado;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbPreferenciaSistema;
import br.com.xti.ouvidoria.model.TbPrioridade;
import br.com.xti.ouvidoria.model.TbQuestionario;
import br.com.xti.ouvidoria.model.TbRespostaManifestacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;
import br.com.xti.ouvidoria.model.TbTramite;
import br.com.xti.ouvidoria.model.TbTramitexAnexo;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;
import br.com.xti.ouvidoria.util.EmailService;
import br.com.xti.ouvidoria.util.JSFUtils;
import br.com.xti.ouvidoria.util.ManifestacaoUtils;
import br.com.xti.ouvidoria.util.PasswordUtils;

/**
 * @author Marcos Rodrigo Ribeiro
 * @author Renato Simões Moreira
 * @author Samuel Correia Guimarães
 */
@ManagedBean(name="mBManifestacao")
@ViewScoped
public class MBManifestacao extends AbstractManifestationController implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	private TipoManifestacaoDAO tipomanifestacaoDAO;
	
	@EJB
	private ClassificacaoDAO classificacaoDAO;
	
	@EJB
	private SubClassificacaoDAO subClassificacaoDAO;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private EncaminhamentoPadronizadoDAO encaminhamentoPadronizadoDAO;
	
	@EJB
	private EmailService emailService;
	
	@EJB
	private PreferenciaSistemaDAO preferenciaSistemaDAO;
	
	@EJB
	private EmailAutomatizadoDAO emailAutomatizadoDAO;
	
	@EJB
	private QuestionarioDAO questionarioDAO;
	
	@EJB
	private RespostaManifestacaoDAO respostaManifestacaoDAO;
	
	
	private static final String MENSAGEM_ATRASO_UNIDADE = 
			"Esta manifestação se encontra em atraso por parte %s %s. Caso queira notificar os envolvidos favor clicar no botão ao lado";
	
	
    private Integer idAreaEntrada;
    private Integer idMeioEntrada;
    private Integer idFaixaEtaria;
    private Integer idTipoManifestacao;
    private Integer idMunicipio;
    private Integer idPais;
    private Integer idResposta;
    private Integer idUsuario;
    private Integer idUf;
    private Integer idEncaminhamentoPadronizado;
    private Integer idEncaminhamentoPadronizadoTramite;
    private Integer idRespostaManifestacao;
    private Integer nrManifestacaoSucesso;
    private Integer idManifestacao;
    private Integer numeroManifestacao;
    private String dsEncaminhamento;
    private String dsMensagemAoManifestante;
    private String dsMensagemAreaTecnica;
    private Integer idUnidadeEncaminhamento;
    private Integer idUsuarioEncaminhamento;
    private String codStatusManifetacao;
    private boolean mensagemAoManifestante;
    private boolean inicializando;
    
    private List<TbAnexo> arquivosTramite;
    private List<TbAnexo> arquivosEncaminhamento;
    private List<TbAnexo> arquivosComunicacaoExterna;
    private List<TbTipoManifestacao> tiposManifestacao;
    private List<TbRespostaManifestacao> respostasManifestacao;
    private List<TbEncaminhamentoPadronizado> encaminhamentosPadronizados;
    private List<TbPrioridade> prioridades;
    private List<String> idClassificacao;
    private List<String> idSubClassificacao;
    private List<String> idUnidadeAreaSolucionadora;
    private List<String> idOrgaoDestino;
    private List<Integer> tramitesEscolhidos;
    
    private boolean monitorar;
    private Date 	dataMonitoramento;
    private int 	quantidadeDias;
    
    // VALIDADOS
    /** Prioridade selecionada para a manifestação */
    private Integer idPrioridade;
    private TbQuestionario questionario;
    
    /** Unidades que podem ser área solucionadora (CACHE) */
    private List<TbUnidade> unidadesCompleta;
    /** Unidades que ainda não receberam encaminhamento */
    private List<TbUnidade> listaUnidades;
    /** Unidades que ainda não receberam encaminhamento (CACHE) */
    private List<TbUnidade> listaUnidadesCompleta;
    /** Encaminhamentos que estão atrasados por parte das unidades */
    private List<TbEncaminhamento> encaminhamentosAtrasados;
    
    // Classificação e Subclassificação
    /** Classificações associadas a manifestação*/
    private List<TbClassificacao> classificacoes;
    /** Classificações associadas a manifestação (CACHE) */
    private List<TbClassificacao> classificacoesCompleta;
    /** Subclassificações associadas a manifestação*/
    private List<TbSubClassificacao> subClassificacoes;
    /** Subclassificações associadas a manifestação (CACHE) */
    private List<TbSubClassificacao> subClassificacoesCompleta;
    
    
    // Atributos e Métodos para filtro dos componentes SelectManyMenu de 
    // "Orão de Destino", "Classificação", "Subclassificação" e "Área Solucionadora"
    private String txtFiltroOrgaoDestino;
    private String txtFiltroClassificacao;
    private String txtFiltroSubclassificacao;
    private String txtFiltroAreaSolucionadora;
    
    
    
    @Override
    public void loadManifestation() {
    	if(this.manifestacao == null) {
    		super.loadManifestation();
    		//atualiza variavel de controle caso a manifestação já possua uma mensagem do OUVIDOR ao MANIFESTANTE
    		mensagemAoManifestante = false;
    		if(this.manifestacao != null) {
    			Collection<TbComunicacaoExterna> communicationList = manifestacao.getTbComunicacaoExternaCollection();
    			if (ValidacaoHelper.isNotEmpty(communicationList)) {
    				for (TbComunicacaoExterna externalCommunication : communicationList) {
    					TbUsuario userCommunication = externalCommunication.getIdUsuario();
    					if (userCommunication != null) {
    						String userProfileId = userCommunication.getTpFuncao();
    						if(userProfileId != null) {
    							FuncaoUsuarioEnum userProfile = EnumHelper.getFuncaoUsuarioEnum(userProfileId);
    							if(userProfile == FuncaoUsuarioEnum.OUVIDOR || userProfile == FuncaoUsuarioEnum.ADMINISTRADOR) {
    								mensagemAoManifestante = true;
    								break;
    							}
    						}
    					}
    				}
				}
    			init();
    		}
    	}
    }
    
    private void init() {
		// Mantem os dados do gráfico no Escopo Flash, caso tenha vindo da página de gráfico de questionário
    	inicializando = Boolean.TRUE;
    	initFields();
        tiposManifestacao = tipomanifestacaoDAO.findAll();
        
        encaminhamentosPadronizados = encaminhamentoPadronizadoDAO.findAll();
        Collections.sort(encaminhamentosPadronizados);
        
        prioridades = prioridadeDAO.findAll();
        Collections.sort(prioridades);
        
        preparaListaAreasSolucionadoras();
        mountTabs();
        
        // Carrega as opções iniciais das "combos"
        classificacoes = null;
        classificacoes = new ArrayList<TbClassificacao>();
        if(ValidacaoHelper.isNotEmpty(manifestacao.getTbClassificacaoCollection())) {
        	classificacoes.addAll(manifestacao.getTbClassificacaoCollection());
        }
        classificacoesCompleta = new ArrayList<TbClassificacao>(classificacoes);
        
        subClassificacoes = null;
        subClassificacoes = new ArrayList<TbSubClassificacao>();
        if(ValidacaoHelper.isNotEmpty(manifestacao.getTbSubClassificacaoCollection())) {
        	subClassificacoes.addAll(manifestacao.getTbSubClassificacaoCollection());
        }
        subClassificacoesCompleta = new ArrayList<TbSubClassificacao>(subClassificacoes);
        
        // Seta os valores para mostrar na tela
        if(ValidacaoHelper.isNotEmpty(classificacoes)) {
        	for (TbClassificacao c : classificacoes) {
        		idClassificacao.add(String.valueOf(c.getIdClassificacao()));
        	}
        }
        
        ajustaSubClassificacoes();
        if(ValidacaoHelper.isNotEmpty(subClassificacoes)) {
        	for (TbSubClassificacao sc : subClassificacoes) {
        		idSubClassificacao.add(String.valueOf(sc.getIdSubClassificacao()));
        	}
        }
        
        if(manifestacao.getIdPrioridade() != null) {
        	idPrioridade = manifestacao.getIdPrioridade().getIdPrioridade();
        }
        ajustaListaStatusManifestacao();
        
        idUnidadeAreaSolucionadora = null;
        idUnidadeAreaSolucionadora = new ArrayList<String>();
        List<TbUnidade> listAreaSolucionadora = new ArrayList<>(manifestacao.getTbUnidadeAreaSolucionadoraCollection());
        if(ValidacaoHelper.isNotEmpty(listAreaSolucionadora)) {
        	for (TbUnidade u : listAreaSolucionadora) {
        		idUnidadeAreaSolucionadora.add(String.valueOf(u.getIdUnidade()));
        	}
        }
        
        // Verifica se existe encaminhamentos atrasados
        encaminhamentosAtrasados = new  ArrayList<TbEncaminhamento>();
        Collection<TbEncaminhamento> encaminhamentos = manifestacao.getTbEncaminhamentoCollection();
        if(ValidacaoHelper.isNotEmpty(encaminhamentos)) {
        	for (TbEncaminhamento encaminhamento : encaminhamentos) {
				if(ManifestacaoUtils.isEncaminhamentoAtrasado(encaminhamento)) {
					encaminhamentosAtrasados.add(encaminhamento);
				}
			}
        }
        
        inicializando = Boolean.FALSE;
    }
    
    private void initFields() {
        idAreaEntrada = 0;
        idMeioEntrada = 0;
        idFaixaEtaria = 0;
        idTipoManifestacao = 0;
        idMunicipio = 0;
        idPais = 0;
        idResposta = 0;
        idPrioridade = 0;
        idUsuario = 0;
        idUf = 0;
        idEncaminhamentoPadronizado = 0;
        idEncaminhamentoPadronizadoTramite = 0;
        idRespostaManifestacao = 0;
        idUnidadeTramite = 0;
        idUsuarioTramite = 0;
        nrManifestacaoSucesso = 0;
        idUnidadeEncaminhamento = 0;
        idUsuarioEncaminhamento = 0;
        txtFiltroAreaSolucionadora = "";
        txtFiltroClassificacao= "";
        txtFiltroOrgaoDestino = "";
        txtFiltroSubclassificacao = "";
        dsEncaminhamento =  "";
        dsMensagemAoManifestante =  "";
        dsMensagemAreaTecnica =  "";
        codStatusManifetacao = "";
        idUnidadeAreaSolucionadora = new ArrayList<String>();
        idClassificacao = new ArrayList<String>();
        idSubClassificacao = new ArrayList<String>();
        idOrgaoDestino = new ArrayList<String>();
        tramitesEscolhidos = new ArrayList<>();
        mensagemAoManifestante = false;
        unidades = new ArrayList<>();
        arquivosTramite = new ArrayList<>();
        arquivosEncaminhamento = new ArrayList<>();
        arquivosComunicacaoExterna = new ArrayList<>();
        questionario = new TbQuestionario();
    }
    
    public boolean showTakeUpManifestation() {
		return (isManifestacaoNova() || hasUsuarioAnalisador())
				&& (!isManifestacaoEmAnalise())
				&& (securityService.isOuvidor() || securityService.isAdministrador());
    }
    
    public boolean showSecretData() {
    	boolean showSecretData = false;
    	if(securityService.isManifestante() || securityService.hasRole(FuncionalidadeEnum.VISUALIZAR_DADOS_SIGILOSOS.toString())) {
			showSecretData = true;
		} else {
			String secrecy = manifestacao.getSiSigilo();
			if (BooleanEnum.SIM.getId().equals(secrecy)) {
				showSecretData = securityService.isOuvidor() || securityService.isAdministrador();
			}
		}
    	return showSecretData;
    }
    
    
    public void filtrarOrgaoDestino() {
    	listaUnidades.clear();
    	if(txtFiltroOrgaoDestino.isEmpty()) {
    		listaUnidades.addAll(listaUnidadesCompleta);
    	} else {
    		Predicate predicate = new Predicate() {
				@Override
				public boolean evaluate(Object obj) {
					TbUnidade department = (TbUnidade) obj;
					String nameToCompare = department.getNomeFormatado().toLowerCase();
					return nameToCompare.contains(txtFiltroOrgaoDestino.toLowerCase());
				}
			};
    		CollectionUtils.select(listaUnidadesCompleta, predicate, listaUnidades);
    	}
    }
    
    public void filtrarClassificacao() {
    	classificacoes.clear();
    	if(txtFiltroClassificacao.isEmpty()) {
    		classificacoes.addAll(classificacoesCompleta);
    	} else {
    		Predicate predicate = new Predicate() {
    			@Override
    			public boolean evaluate(Object obj) {
    				TbClassificacao c = (TbClassificacao) obj;
    				String nomeParaComparar = c.getDsClassificacao().toLowerCase();
    				return nomeParaComparar.contains(txtFiltroClassificacao.toLowerCase());
    			}
    		};
    		CollectionUtils.select(classificacoesCompleta, predicate, classificacoes);
    	}
    }
    
    public void filtrarSubclassificacao() {
    	subClassificacoes.clear();
    	if(txtFiltroSubclassificacao.isEmpty()) {
    		subClassificacoes.addAll(subClassificacoesCompleta);
    	} else {
    		Predicate predicate = new Predicate() {
    			@Override
    			public boolean evaluate(Object obj) {
    				TbSubClassificacao sub = (TbSubClassificacao) obj;
    				String nomeParaComparar = sub.getDsSubClassificacao().toLowerCase();
    				return nomeParaComparar.contains(txtFiltroSubclassificacao.toLowerCase());
    			}
    		};
    		CollectionUtils.select(subClassificacoesCompleta, predicate, subClassificacoes);
    	}
    }
    
    public void filtrarAreaSolucionadora() {
    	unidades.clear();
    	if(txtFiltroAreaSolucionadora.isEmpty()) {
    		unidades.addAll(unidadesCompleta);
    	} else {
    		Predicate predicate = new Predicate() {
    			@Override
    			public boolean evaluate(Object obj) {
    				TbUnidade u = (TbUnidade) obj;
    				String nomeParaComparar = u.getSgUnidade().toLowerCase() + " - " + u.getNmUnidade().toLowerCase();
    				return nomeParaComparar.contains(txtFiltroAreaSolucionadora.toLowerCase());
    			}
    		};
    		CollectionUtils.select(unidadesCompleta, predicate, unidades);
    	}
    }
    
    public String voltar() {
    	return "listarmanifestacoes.xhtml?faces-redirect=true";
    }
    
    public boolean isManifestacaoNova() {
    	StatusManifestacaoEnum status = EnumHelper.getStatusManifestacaoEnum(manifestacao.getStStatusManifestacao());
    	return status == StatusManifestacaoEnum.NOVA;
    }
    
    public boolean isManifestacaoEmAnalise() {
    	StatusManifestacaoEnum status = EnumHelper.getStatusManifestacaoEnum(manifestacao.getStStatusManifestacao());
    	return status == StatusManifestacaoEnum.EM_ANALISE;
    }
    
    public boolean isManifestacaoNovaOuEmAnalise() {
    	return isManifestacaoNova() || isManifestacaoEmAnalise();
    }
    
    public boolean mostrarBotaoBloquearManifestacao() {
    	if(isManifestacaoBloqueada())
    		return manifestacao.getIdUsuarioBloqueou().equals(securityService.getUser());
    	
    	return true;
    }
    
    public boolean isManifestacaoNaCaixaRetornadas() {
    	if(securityService.isAdministrador() || securityService.isOuvidor()) {
	    	if(StatusManifestacaoEnum.EM_ANDAMENTO.getId().equalsIgnoreCase(manifestacao.getStStatusManifestacao())) {
	    		for (TbEncaminhamento e : manifestacao.getTbEncaminhamentoCollection()) {
					if(StatusEncaminhamentoEnum.RETORNADA.getId().equalsIgnoreCase(e.getStEncaminhamento())) {
						return true;
					}
	    		}
	    	}
    	}
    	return false;
    }
    
    public boolean hasUsuarioAnalisador() {
    	return manifestacao.getIdUsuarioAnalisador() != null;
    }
    
    public boolean isManifestacaoBloqueada() {
    	return manifestacao.getIdUsuarioBloqueou() != null;
    }
    
    public boolean mostrarBotaoSolucionarManifestacao() {
    	if(securityService.isOuvidor() || securityService.isAdministrador()) {
    		boolean podeEditar = !desabilitaEdicaoManifestacao();
    		boolean temAreaSolucionadora = !idUnidadeAreaSolucionadora.isEmpty();
    		return podeEditar && temAreaSolucionadora;
    	}
    	
    	return false;
    }
    
    public boolean mostrarBotaoReativarManifestacao() {
    	if(securityService.isOuvidor() || securityService.isAdministrador()) {
    		return StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao());
    	}
    	
    	return false;
    }
    
    public boolean mostrarBotaoEncaminharManifestacao() {
    	boolean mostrarBotaoEncaminharManifestacao = false;
    	if(!securityService.isManifestante()) {
	    	if(securityService.isOuvidor() || securityService.isAdministrador()) {
	    		mostrarBotaoEncaminharManifestacao = true;
	    	} else {
		    	TbUnidade department = securityService.getUser().getIdUnidade();
		    	if(securityService.isInterlocutor() && department.isEncaminharOutrasAreas()) {
		    		mostrarBotaoEncaminharManifestacao = true;
		    	}
	    	}
    	}
    	return mostrarBotaoEncaminharManifestacao;
    }
    
    public boolean mostrarBotaoResponderAOuvidoria() {
    	boolean mostrar = false;
    	if(!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao()) && securityService.isInterlocutor()) {
    		TbEncaminhamento e = ManifestacaoUtils.getEncaminhamentoDoUsuarioLogado(manifestacao);
    		List<TbTramite> listTramites = new ArrayList<TbTramite>(e.getTbTramiteCollection());
    		TbTramite ultimoTramite = listTramites.get(listTramites.size() - 1);
    		
    		if(e.getIdUnidadeEnviou().getIdUnidade() == UnidadeEnum.OUVIDORIA.getId()) {
    			if(!(UnidadeEnum.OUVIDORIA.getId().equals(ultimoTramite.getIdUnidadeEnvio().getIdUnidade()) || ultimoTramite.getIdUnidadeEnvio() == null)) {
    				mostrar = true;
    			}
    		}
    	}
    	
		return mostrar;
    }
    
    public String mostrarDataFechamento() {
    	if(StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {
    		try {
    			return DataHelper.converterData(manifestacao.getDtFechamento());
			} catch (Exception e) {
				return "(fechada)";
			}
		} else
			return "(ainda aberto)";
    }
    
    public void bloquearManifestacao() {
    	try {
			manifestacao.setIdUsuarioBloqueou(securityService.getUser());
			dao.edit(manifestacao);
		} catch (InfrastructureException e) {
			e.printStackTrace();
		}
    }
    
    public void desbloquearManifestacao() {
    	try {
			manifestacao.setIdUsuarioBloqueou(null);
    		dao.edit(manifestacao);
    	} catch (InfrastructureException e) {
    		e.printStackTrace();
    	}
    }
    
    
    // GETTEs e SETTERs
    public TbQuestionario getQuestionario() {
		return questionario;
	}

	public void setQuestionario(TbQuestionario questionario) {
		this.questionario = questionario;
	}

	public QuestionarioDAO getQuestionarioDAO() {
		return questionarioDAO;
	}

	public void setQuestionarioDAO(QuestionarioDAO questionarioDAO) {
		this.questionarioDAO = questionarioDAO;
	}

    public Integer getIdAreaEntrada() {
        return idAreaEntrada;
    }

    public void setIdAreaEntrada(Integer idAreaEntrada) {
        this.idAreaEntrada = idAreaEntrada;
    }

    public Integer getIdMeioEntrada() {
        return idMeioEntrada;
    }

    public void setIdMeioEntrada(Integer idMeioEntrada) {
        this.idMeioEntrada = idMeioEntrada;
    }

    public Integer getIdFaixaEtaria() {
        return idFaixaEtaria;
    }

    public void setIdFaixaEtaria(Integer idFaixaEtaria) {
        this.idFaixaEtaria = idFaixaEtaria;
    }

    public Integer getIdTipoManifestacao() {
        return idTipoManifestacao;
    }

    public void setIdTipoManifestacao(Integer idTipoManifestacao) {
        this.idTipoManifestacao = idTipoManifestacao;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idUf = null;
        this.idMunicipio = null;
        this.idPais = idPais;
    }
    
    
    public List<String> getIdSubClassificacao() {
        if(ValidacaoHelper.isNotEmpty(manifestacao.getTbSubClassificacaoCollection())) {
        	for (TbSubClassificacao subClassificacao : manifestacao.getTbSubClassificacaoCollection()) {
				try {
					idSubClassificacao.add(String.valueOf(ReflectionHelper.getValorID(subClassificacao)));
				} catch (Exception e) { }
			}
        }
        return idSubClassificacao;
    }
    
    
    public void setIdSubClassificacao(List<String> idSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdPrioridade() {
        if (manifestacao.getIdPrioridade() != null && idPrioridade == null) {
            idPrioridade = manifestacao.getIdPrioridade().getIdPrioridade();
        }
        return idPrioridade;
    }

    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }

    public Integer getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(Integer idResposta) {
        this.idResposta = idResposta;
    }
    
    public Integer getIdUf() {
        return idUf;
    }

    public void setIdUf(Integer idUf) {
        this.idUf = idUf;
    }
    
    public boolean desativarBotaoResponderQuestionario() {
		if (StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {
			if (manifestacao.getIdQuestionario() == null || ValidacaoHelper.isEmpty(manifestacao.getIdQuestionario().getIdQuestionario())) {
				if (questionarioDAO.getQuestionarioAtivo() != null) {
					return false;
				}
			}
		}
		return true;
	}
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {
    	UploadedFile uploadedFile = event.getFile();
    	if (uploadedFile != null) {
    		try {
    			String uploadedFilename = uploadedFile.getFileName();
    			String normalizedFileName = FileHelper.normalizeName(uploadedFilename);
    			String pathName = FileHelper.getUploadedFilePathname(uploadedFilename, manifestacao.getNrManifestacao());
    			
    			File file = new File(pathName);
    			TbAnexo anexo = new TbAnexo();
    			anexo.setNmAnexo(normalizedFileName);
    			anexo.setDsCaminhoAnexo(file.getName());
    			try {
    				FileUtils.writeByteArrayToFile(file, uploadedFile.getContents());
    				switch (event.getComponent().getId()) {
	    				case "fileComunicacaoExterna":
	    				case "fileComunicacaoExternaManifestante":
	    					arquivosComunicacaoExterna.add(anexo);
	    					break;
	    				case "fileTramite":
	    					arquivosTramite.add(anexo);
	    					break;
	    				case "fileEncaminhamento":
	    					arquivosEncaminhamento.add(anexo);
	    					break;
    				}
    				MensagemFaceUtil.info("Arquivo enviado", String.format("Arquivo enviado com sucesso: %s", file.getName()));
    			} catch (IOException ex) {
    				ex.printStackTrace();
    				MensagemFaceUtil.erro("Erro", String.format("Erro ao enviar o arquivo %s", file.getName()));
    			}
    		} catch (InfrastructureException ex) {
    			ex.printStackTrace();
    			MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao efetuar a operação: " + ex.getMessage());
    		}
    	}
    }
    
    public void removerArquivo(TbAnexo anexo, Collection<TbAnexo> listaArquivos) {
    	if (ValidacaoHelper.isNotEmpty(listaArquivos)) {
    		String arquivo = anexo.getDsCaminhoAnexo();
    		File file = null;
    		try {
    			file = new File(parametroDAO.getDiretorioAnexo() + arquivo);
    			if (file.exists()) {
    				if(file.delete()) {
    					MensagemFaceUtil.info("Arquivo excluido", String.format("Arquivo excluido com sucesso: %s", file.getName()));
    					listaArquivos.remove(anexo);
    				} else {
    					throw new Exception();
    				}
    			}
    		} catch (Exception ex) {
    			String fileName = "<UNKNOWN>";
    			if(file != null) {
    				fileName = file.getName();
    			}
    			MensagemFaceUtil.info("Erro", String.format("Não foi possível excluir o arquivo: ", fileName));
    			ex.printStackTrace();
    		}
    	}
    }
    
    public TbEncaminhamentoPadronizado getEncaminhamentoPadronizado() {
        return encaminhamentoPadronizadoDAO.find(idEncaminhamentoPadronizado == null ? 0 : idEncaminhamentoPadronizado);
    }

    public TbEncaminhamentoPadronizado getEncaminhamentoPadronizadoTramite() {
    	return encaminhamentoPadronizadoDAO.find(idEncaminhamentoPadronizadoTramite == null ? 0 : idEncaminhamentoPadronizadoTramite);
    }

    public Integer getNrManifestacaoSucesso() {
        return nrManifestacaoSucesso;
    }
    
    public List<String> getIdUnidadeAreaSolucionadora() {
    	if(ValidacaoHelper.isNotEmpty(manifestacao.getTbUnidadeAreaSolucionadoraCollection())) {
        	for (TbUnidade u : manifestacao.getTbUnidadeAreaSolucionadoraCollection()) {
				try {
					idUnidadeAreaSolucionadora.add(String.valueOf(ReflectionHelper.getValorID(u)));
				} catch (Exception e) { }
			}
        }
        return idUnidadeAreaSolucionadora;
    }
    
    public void setIdUnidadeAreaSolucionadora(List<String> idUnidadeAreaSolucionadora) {
        this.idUnidadeAreaSolucionadora = idUnidadeAreaSolucionadora;
    }

    public void alteraTipoManifestacao() {
        try {
            TbTipoManifestacao tipomanifestacao = tipomanifestacaoDAO.find(manifestacao.getIdTipoManifestacao().getIdTipoManifestacao());
            String tipoAnterior = manifestacao.getIdTipoManifestacao().getNmTipoManifestacao();

            manifestacao.setIdTipoManifestacao(tipomanifestacao);
            dao.edit(manifestacao);
            MensagemFaceUtil.info("Alteração de Tipo de Manifestação", String.format("Tipo de manifestação alterado de %s para %s.", tipoAnterior, tipomanifestacao.getNmTipoManifestacao()));
        } catch (InfrastructureException ex) {
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao tentar alterar tipo da manifestação: " + ex.getMessage());
        }
    }
    
    public Integer getIdEncaminhamentoPadronizado() {
        return idEncaminhamentoPadronizado;
    }

    public void setIdEncaminhamentoPadronizado(Integer idEncaminhamentoPadronizado) {
        this.idEncaminhamentoPadronizado = idEncaminhamentoPadronizado;
        setDsEncaminhamento(getEncaminhamentoPadronizado().getDsConteudo());
    }

    public Integer getIdEncaminhamentoPadronizadoTramite() {
        return idEncaminhamentoPadronizadoTramite;
    }

    public void setIdEncaminhamentoPadronizadoTramite(Integer idEncaminhamentoPadronizadoTramite) {
        this.idEncaminhamentoPadronizadoTramite = idEncaminhamentoPadronizadoTramite;
        setDsMensagemAreaTecnica(getEncaminhamentoPadronizadoTramite().getDsConteudo());
    }
    
    private String mensagemEncaminhamento;

    public void ajustaMensagemEncaminhamento() {
        mensagemEncaminhamento = PalavrasChavesHelper.converterPalavrasChaves(dsEncaminhamento, manifestacao, false);
    }

    public String getDsEncaminhamento() {
        return mensagemEncaminhamento;
    }
    
    public void setDsEncaminhamento(String dsEncaminhamento) {
        this.dsEncaminhamento = dsEncaminhamento;
    }
    
    public List<String> getIdClassificacao() {
//    	if(ValidacaoHelper.isNotEmpty(manifestacao.getTbClassificacaoCollection())) {
//        	for (TbClassificacao classificacao : manifestacao.getTbClassificacaoCollection()) {
//				try {
//					idClassificacao.add(String.valueOf(ReflectionHelper.getValorID(classificacao)));
//				} catch (Exception e) { }
//			}
//        }
        return idClassificacao;
    }
    
    public void setIdClassificacao(List<String> idClassificacao) {
        this.idClassificacao = idClassificacao;
    }
    
    public void mostraTodasClassificacoes() {
    	classificacoes = classificacaoDAO.findAll();
    	Collections.sort(classificacoes);
    	classificacoesCompleta = new ArrayList<TbClassificacao>(classificacoes);
    }
    
    public void mostraTodasSubclassificacoes() {
    	subClassificacoes = subClassificacaoDAO.findAll();
    	Collections.sort(subClassificacoes);
    	subClassificacoesCompleta = new ArrayList<TbSubClassificacao>(subClassificacoes);
    }
    
    public void ajustaClassificacao() {
    	// Se selecionar a Ouvidoria em orgão de destino aparece todas as classificações
    	if(idOrgaoDestino.contains(String.valueOf(UnidadeEnum.OUVIDORIA.getId()))) {
    		mostraTodasClassificacoes();
    		return;
    	}
    	
    	if(ValidacaoHelper.isNotEmpty(idOrgaoDestino)) {
    		if(idOrgaoDestino.size() == 1) {
    			int idUnidade = Integer.parseInt(idOrgaoDestino.get(0));
    			TbUnidade unidade = unidadeDAO.find(idUnidade);
                
                if (unidade != null) {
                    try {
                        classificacoes = classificacaoDAO.findByUnidade(unidade);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                	classificacoes = new ArrayList<>();
                }
    		} else {
    			classificacoes = classificacaoDAO.findAll();
    		}
    	} else {
    		classificacoes = new ArrayList<>();
    	}
    	Collections.sort(classificacoes);
    	classificacoesCompleta = new ArrayList<TbClassificacao>(classificacoes);
    }
    
    public void ajustaSubClassificacoes() {
    	try {
    		if(inicializando || ValidacaoHelper.isEmpty(idClassificacao)) {
    			return;
    		}
    		
    		List<TbClassificacao> listClassificacao = new ArrayList<>();
    		for (String id : idClassificacao) {
    			listClassificacao.add(classificacaoDAO.find(Integer.parseInt(id)));
    		}
    		
            manifestacao.setTbClassificacaoCollection(listClassificacao);

            subClassificacoes = null;
            subClassificacoes = new ArrayList<>();
            if (ValidacaoHelper.isNotEmpty(listClassificacao)) {
            	if(listClassificacao.size() == 1) {
            		TbClassificacao c = listClassificacao.get(0);
        			subClassificacoes.addAll(c.getTbSubClassificacaoCollection());
            	} else {
            		subClassificacoes.addAll(subClassificacaoDAO.findAll());
            	}
            }
        } catch (Exception ex) {
            MensagemFaceUtil.info("Gravação de Classificação", "Ocorreu um erro ao gravar Classificação: " + ex.getMessage());
        }
    	Collections.sort(subClassificacoes);
    	subClassificacoesCompleta = new ArrayList<TbSubClassificacao>(subClassificacoes);
    }
    
    public void gravaSubClassificacao() {
        try {
        	List<TbSubClassificacao> listSubClassificacao = new ArrayList<>();
    		for (String id : idSubClassificacao)
				listSubClassificacao.add(subClassificacaoDAO.find(Integer.parseInt(id)));
    		
            manifestacao.setTbSubClassificacaoCollection(listSubClassificacao);
        } catch (Exception ex) {
            MensagemFaceUtil.erro("Gravação de Subclassificação", "Ocorreu um erro ao gravar subclassificação: " + ex.getMessage());
        }
    }
    
    public Collection<TbSubClassificacao> getSubClassificacoes() {
        return subClassificacoes;
    }
    
    public Collection<TbRespostaManifestacao> getRespostasManifestacao() {
    	if(!ValidacaoHelper.isNotEmpty(respostasManifestacao)) {
    		respostasManifestacao = respostaManifestacaoDAO.findAll();
    		Collections.sort(respostasManifestacao);
    	}
    	return respostasManifestacao;
    }
    
    public Collection<TbClassificacao> getClassificacoes() {
        return classificacoes;
    }
    
    public Boolean habilitaSubClassificacao() {
        return !(idClassificacao != null);
    }
    
    public List<TbPrioridade> getPrioridades() {
        return prioridades;
    }
    
    public void gravaPrioridade() {
        try {
            TbPrioridade prioridade = prioridadeDAO.find(getIdPrioridade());
            manifestacao.setIdPrioridade(prioridade);
            dao.edit(manifestacao);

            MensagemFaceUtil.info("Alteração de Prioridade", "Prioridade gravada com sucesso.");
        } catch (InfrastructureException ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Alteração de Prioridade", "Ocorreu um erro ao gravar prioridade: " + ex.getMessage());
        }
    }

	public void setaOrgaoDestino(){
		this.txtFiltroOrgaoDestino = manifestacao.getNrPronac();
	}

    public void gravaStatusManifestacao() {
        try {
			//STATUS EM ANALISE
			if (StatusManifestacaoEnum.EM_ANALISE.getId().equals(manifestacao.getStStatusManifestacao())) {
                manifestacao.setIdUsuarioAnalisador(securityService.getUser());
            }
            //STATUS SOLUCIONADA
            if (StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {
                //recupera status anterior da manifestacao
                TbManifestacao man = dao.find(manifestacao.getIdManifestacao());
                String statusAnterior = man.getStStatusManifestacao();
                //se o status anterior for SOLUCIONADA não faz nada
                if(statusAnterior.equals(StatusManifestacaoEnum.SOLUCIONADA.getId())) {
                	return;
                }
                //retorna manifestacao ao status anterior ate o usuario confirmar a acao de marcar como solucionada
                manifestacao.setStStatusManifestacao(statusAnterior);

                if (mensagemAoManifestante) {
                    JSFUtils.executeJavaScript("dlgEncerraSemMensagemFinal.show()");
                } else {
                    JSFUtils.executeJavaScript("dlgEncerraComMensagemFinal.show()");
                }
                
                mensagemManifestante = carregarMensagensSelecionadas(true);
            } else {
                dao.edit(manifestacao);
                ajustaListaStatusManifestacao();
            }
			setaOrgaoDestino();
			filtrarOrgaoDestino();

        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Alteração de Status", "Ocorreu um erro ao gravar o status da manifestação: " + ex.getMessage());
        }
    }
    
    public String carregarMensagensSelecionadas(boolean isComunicacaoExterna) {
    	StringBuilder sb = new StringBuilder();
    	for (Integer id : tramitesEscolhidos) {
    		TbTramite t = tramiteDAO.find(id);
    		// Texto do trâmite
			sb.append(t.getDsDescricao()).append("<br/><br/>");
			// Anexos do trâmite
			{
				Collection<TbTramitexAnexo> anexos = t.getTbTramitexAnexoCollection();
				for (TbTramitexAnexo anexoTramite : anexos) {
					try {
						File file = new File(parametroDAO.getDiretorioAnexo() + String.format("%d_%d_%s", manifestacao.getNrManifestacao(), new Date().getTime(), anexoTramite.getIdAnexo().getNmAnexo()));
		                TbAnexo anexo = new TbAnexo();
		                anexo.setNmAnexo(anexoTramite.getIdAnexo().getNmAnexo());
		                anexo.setDsCaminhoAnexo(file.getName());
	                    FileUtils.copyFile(new File(parametroDAO.getDiretorioAnexo() + anexoTramite.getIdAnexo().getDsCaminhoAnexo()), file); // faz uma cópia do arquivo
	                    
	                    if(isComunicacaoExterna) {
	                    	arquivosComunicacaoExterna.add(anexo);
	                    } else {
	                    	arquivosTramite.add(anexo);
	                    }
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
				}
			}
		}
    	
    	return sb.toString();
    }
    
    public void gravaSolucionarManifestacao() {
    	// Verifica se o Texto enviado é o padrão
    	TbRespostaManifestacao respostaManifestacao = getRespostaManifestacao();
    	if(respostaManifestacao != null) {
    		String textoPadraoComPalavrasChave = respostaManifestacao.getDsResposta();
    		String textoPadraoSemPalavrasChave = PalavrasChavesHelper.converterPalavrasChaves(textoPadraoComPalavrasChave, manifestacao, false);
    		if(dsMensagemAoManifestante.equals(textoPadraoSemPalavrasChave)) {
    			JSFUtils.executeJavaScript("dlgRespostaSemAlteracao.show()");
    		} else {
    			gravaManifestacao();
    		}
        } else {
        	gravaManifestacao();
		}
    }
    
    public void controlaMonitoramento() {
    	if (monitorar){
    		quantidadeDias = 5;
    		setDataMonitoramento(Calendar.getInstance().getTime());
    	}
    	else{
    		quantidadeDias = 0;
    		setDataMonitoramento(null);
    	}
    }

    public void iniciarModalMonitoramento() {
   		quantidadeDias = 5;
   		setDataMonitoramento(Calendar.getInstance().getTime());
   		setMonitorar(true);
    }

    public void defineQuantidadeDias(SlideEndEvent event) {
    	setQuantidadeDias(event.getValue());
    	Calendar dataInformada = Calendar.getInstance();
    	dataInformada.add(Calendar.DAY_OF_MONTH, quantidadeDias);
    	setDataMonitoramento(dataInformada.getTime());
    }
    
    
    public void gravaMonitorarManifestacao() {
        try {
            //Configura o status da manifestação 
            if (monitorar){ // Configura os dados de monitoramento da manifestação
            	if (dataMonitoramento == null){
            		MensagemFaceUtil.erro("Para monitorar uma manifestação deve ser informada uma data para o monitoramento", "Monitoramento");
            		return;
            	}

            	//atualiza data de modificação e status da manifestação
            	manifestacao.setDtUltimaAtualizacao(new Date());
            	manifestacao.setDtFechamento(new Date());
                manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.EM_MONITORAMENTO.getId());

                // Configura a hora da data de monitoramento para o início do dia
                Calendar novaDataMonitoramento = Calendar.getInstance();
                novaDataMonitoramento.setTime(dataMonitoramento);
                novaDataMonitoramento.set(Calendar.HOUR_OF_DAY, 0);
                novaDataMonitoramento.set(Calendar.MINUTE, 0);
                manifestacao.setDataMonitoramento(novaDataMonitoramento.getTime());
                manifestacao.setIdUsuarioAnalisador(securityService.getUser());
                
                // Seta todos os tramites como retornados
                for (TbEncaminhamento enc : manifestacao.getTbEncaminhamentoCollection()) {
                	for (TbTramite t : enc.getTbTramiteCollection()) {
                		t.setStRetornada(BooleanEnum.SIM.getId());
                		tramiteDAO.edit(t);
                	}
                }
                dao.edit(manifestacao);
                
                
                // zerando variaveis
                mensagemManifestante = null;
                dsMensagemAoManifestante = null;
                
                showRedirectPageModal();
            }
        } catch (InfrastructureException ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Alteração de Status", "Ocorreu um erro ao gravar o status da manifestação: " + ex.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	MensagemFaceUtil.erro("Ocorreu um erro ao tentar redirecionar o usuário", "Ocorreu um erro ao tentar redirecionar o usuário");
		}
        
    }
    
    
    public void gravaManifestacao() {
    	if(ValidacaoHelper.isNotEmpty(idUnidadeAreaSolucionadora)) {
	    	Set<TbUnidade> listAreaSolucionadora = new HashSet<>();
			for (String id : idUnidadeAreaSolucionadora) {
				listAreaSolucionadora.add(unidadeDAO.find(Integer.parseInt(id)));
			}
		
	        manifestacao.setTbUnidadeAreaSolucionadoraCollection(listAreaSolucionadora);
        } else {
            MensagemFaceUtil.erro("Para solucionar uma manifestação deve ser selecionada uma unidade solucionadora", "Unidade Solucionadora");
            return;
        }
    	
        try {
            //atualiza data de modificação e status da manifestação
            manifestacao.setDtUltimaAtualizacao(new Date());
            manifestacao.setDtFechamento(new Date());
            //Configura o status da manifestação 
            if (monitorar){ // Configura os dados de monitoramento da manifestação
            	if (dataMonitoramento == null){
            		MensagemFaceUtil.erro("Para monitorar uma manifestação deve ser informada uma data para o monitoramento", "Monitoramento");
            		return;
            	}
                manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.EM_MONITORAMENTO.getId());

                // Configura a hora da data de monitoramento para o início do dia
                Calendar novaDataMonitoramento = Calendar.getInstance();
                novaDataMonitoramento.setTime(dataMonitoramento);
                novaDataMonitoramento.set(Calendar.HOUR_OF_DAY, 0);
                novaDataMonitoramento.set(Calendar.MINUTE, 0);
                manifestacao.setDataMonitoramento(novaDataMonitoramento.getTime());
            }else{
                manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.SOLUCIONADA.getId());
                manifestacao.setDataMonitoramento(null);
            }
            manifestacao.setIdUsuarioAnalisador(securityService.getUser());
            
            // Seta todos os tramites como retornados
            for (TbEncaminhamento enc : manifestacao.getTbEncaminhamentoCollection()) {
            	for (TbTramite t : enc.getTbTramiteCollection()) {
            		t.setStRetornada(BooleanEnum.SIM.getId());
            		tramiteDAO.edit(t);
            	}
			}
        	dao.edit(manifestacao);

            /*------- inicio - gravando comunicacao externa ------------*/
            TbComunicacaoExterna comunicacaoExterna = new TbComunicacaoExterna();
            comunicacaoExterna.setDtComunicacao(new Date());
            comunicacaoExterna.setIdManifestacao(manifestacao);
            comunicacaoExterna.setStRespostaFinal("1");
            comunicacaoExterna.setStComunicacaoPublica(BooleanEnum.NAO.getId());

            TbUsuario usuarioOuvidor = usuarioDAO.find(securityService.getUser().getIdUsuario());
            comunicacaoExterna.setIdUsuario(usuarioOuvidor);
            
            //grava texto final de envio ao manifestante caso o ouvidor o tenha enviado
            if(ValidacaoHelper.isNotEmpty(dsMensagemAoManifestante.trim())) {
                comunicacaoExterna.setDsComunicacao(dsMensagemAoManifestante);
            } else {
                comunicacaoExterna.setDsComunicacao("<b>Manifestação "
                		+ (isMonitorar() ? "Em Monitoramento" : "Solucionada")
                		+ " sem mensagem adicional do Ouvidor ao Manifestante. \nConsiderar a última mensagem como resposta final. \n<span style='font-size:10px'>(Esta mensagem foi gerada automaticamente pelo sistema)</span></b>");
            }
            comunicacaoExternaDAO.create(comunicacaoExterna);
            
            //----- gravando anexos ---------//
            String diretorioAnexo = parametroDAO.getDiretorioAnexo();
            ArrayList<TbComunicacaoExternaxAnexo> anexos = new ArrayList<>();
            for (TbAnexo anexo : arquivosComunicacaoExterna) {
                anexoDAO.create(anexo);
                
                TbComunicacaoExternaxAnexo tbComunicacaoExternaxAnexo = new TbComunicacaoExternaxAnexo();
                tbComunicacaoExternaxAnexo.setIdAnexo(anexo);
                tbComunicacaoExternaxAnexo.setIdComunicacaoExterna(comunicacaoExterna);
                anexos.add(tbComunicacaoExternaxAnexo);
            }
            comunicacaoExterna.setTbComunicacaoExternaxAnexoCollection(anexos);
            comunicacaoExternaDAO.edit(comunicacaoExterna);
            /*------- fim - gravando comunicacao externa ------------*/

            //ENVIA E-MAIL AO MANIFESTANTE
            if ((manifestacao.getEeEmailUsuario() != null || manifestacao.getEeEmailSecundario() != null) && "1".equals(manifestacao.getStResposta())) {

                try {
                    EmailService.Email emailManifestante = emailService.newEmail();

                    //adiciona email primario como destinatario
                    String nomeManifestante = ValidacaoHelper.isNotEmpty(manifestacao.getNmPessoa()) ? manifestacao.getNmPessoa() : "Manifestante";
                    emailManifestante.addDestinatario(nomeManifestante, manifestacao.getEeEmailUsuario());
                    
                    //----- adicionando anexos ao email ---------//
                    for (TbAnexo anexo : arquivosComunicacaoExterna) {
                    	emailManifestante.addAnexo(diretorioAnexo + anexo.getDsCaminhoAnexo(), anexo.getNmAnexo(), anexo.getNmAnexo());
                    }
                    
                    //adiciona emails secundarios com o copia
                    if (manifestacao.getEeEmailSecundario() != null && !manifestacao.getEeEmailSecundario().isEmpty()) {
                        for (String emailSecundario : manifestacao.getEeEmailSecundario().split("[,;]+")) {
                        	emailManifestante.addDestinatarioCc(nomeManifestante, emailSecundario);
                        }
                    }
                    
                    //recupera texto padrao para o email
                    TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.RESPOSTA_FINAL);

                    //adiciona texto e assunto de email padrao
                    StringBuilder emailTextoHtml = new StringBuilder();
                    StringBuilder emailTexto = new StringBuilder();

                    emailManifestante.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, dsMensagemAoManifestante, false));
                    emailTextoHtml.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, dsMensagemAoManifestante, false));
                    emailTexto.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, dsMensagemAoManifestante, false));

                    //emailManifestante.setAssunto("[Ouvidoria Prefeitura] Encaminhamento de Manifestação para análise: " + manifestacao.getNrManifestacao());
                    emailManifestante.setTextoHtml(emailTextoHtml.toString());
                    emailManifestante.setTextoSemFormatacao(emailTexto.toString());
                    
                    emailService.envia(emailManifestante);
                } catch (EmailException e) {

                    e.printStackTrace();

                    MensagemFaceUtil.alerta("O Status da Manifestação foi gravado com sucesso, porém não foi possível enviar o e-mail ao Manifestante", null);
                    JSFUtils.updateComponent("frmMessageEncaminhamento");
                    JSFUtils.updateComponent("frmTramiteManifestacao");
                }
            }
            
            // Verifica se existe algum Questionário ativado para enviar e-mail
            try {
	            TbQuestionario questionario = questionarioDAO.getQuestionarioAtivo();
	            if(questionario != null) {
	            	 EmailService.Email emailQuestionario = emailService.newEmail();
	            	 
	            	 String nomeManifestante = (ValidacaoHelper.isNotEmpty(manifestacao.getNmPessoa())) ? manifestacao.getNmPessoa() : "Manifestante";
	            	 emailQuestionario.addDestinatario(nomeManifestante, manifestacao.getEeEmailUsuario());
	
	                 TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.RESPONDER_QUESTIONARIO);
	
	                 //adiciona texto e assunto de email padrao
	                 StringBuilder emailTextoHtml = new StringBuilder();
	                 StringBuilder emailTexto = new StringBuilder();
	
	                 emailQuestionario.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, dsMensagemAoManifestante, false));
	                 emailTextoHtml.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, dsMensagemAoManifestante, false));
	                 emailTexto.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, dsMensagemAoManifestante, false));
	
	                 emailQuestionario.setTextoHtml(emailTextoHtml.toString());
	                 emailQuestionario.setTextoSemFormatacao(emailTexto.toString());
	                 
	                 emailService.envia(emailQuestionario);
	            }
            } catch (Exception e) {
				// Do nothing
            	e.printStackTrace();
			}
            
            // zerando variaveis
            mensagemManifestante = null;
            dsMensagemAoManifestante = null;
            
            showRedirectPageModal();
        } catch (InfrastructureException ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Alteração de Status", "Ocorreu um erro ao gravar o status da manifestação: " + ex.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	MensagemFaceUtil.erro("Ocorreu um erro ao tentar redirecionar o usuário", "Ocorreu um erro ao tentar redirecionar o usuário");
		}
        
    }
    
    public String getEmailAoManifestante() {
    	return dsMensagemAoManifestante;
    }
    
    public String getTextoEncaminhamento() {
    	return dsEncaminhamento;
    }
    
    public String getTextoTramite() {
    	return dsMensagemAreaTecnica;
    }
    
    public boolean habilitarBotoesResponder() {
//    	if(manifestacao.getIdQuestionario() != null)
//    	TbQuestionario questionario = questionarioDAO.getQuestionarioAtivo();
    	return true;
    }
    
    
    private HtmlSelectOneMenu comboStatusManifestacao;
    
    public HtmlSelectOneMenu getComboStatusManifestacao() {
        return comboStatusManifestacao;
    }
    
    public void setComboStatusManifestacao(HtmlSelectOneMenu comboStatusManifestacao) {
        this.comboStatusManifestacao = comboStatusManifestacao;
    }

    public void salvaEncaminhamento() {
    	Integer qtdEncaminhamento = 0;
    	
    	if (manifestacao.getTbEncaminhamentoCollection() != null) {
    		qtdEncaminhamento = manifestacao.getTbEncaminhamentoCollection().size();
    	}
    	
    	TbUsuario usuario = securityService.getUser();
    	for(String idUnidadeStr : idOrgaoDestino) {
	        try {
	        	int idUnidade = Integer.parseInt(idUnidadeStr);
	        	
	            //atualiza data de modificacao da manifestacao
	            manifestacao.setDtUltimaAtualizacao(new Date());
	            //atualiza status da manifestacao caso ela ainda esteja como nova
	            if (manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.EM_ANALISE.getId()) || manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA.getId())) {
	                manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
	                dao.edit(manifestacao);
	            }
	            
	            /*-----------------------------------------------------*/
	            /*------- inicio - gravando encaminhamento ------------*/
	            /*-----------------------------------------------------*/
	            TbEncaminhamento encaminhamento = new TbEncaminhamento();
	            encaminhamento.setDsDescricao(dsEncaminhamento);
	            encaminhamento.setDtCriacaoEncaminhamento(new Date());
	            encaminhamento.setDtEnvioTramite(new Date());
	            encaminhamento.setIdManifestacao(manifestacao);
	            encaminhamento.setIdUsuarioEnviou(usuario);
	            encaminhamento.setIdUnidadeEnviou(usuario.getIdUnidade());
	            encaminhamento.setStEncaminhamento(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
	            
	            TbUnidade unidade = unidadeDAO.find(idUnidade);
	            encaminhamento.setIdUnidadeRecebeu(unidade);
	            
	            encaminhamentoDAO.create(encaminhamento);
	            qtdEncaminhamento++;
	            /*------- fim - gravando encaminhamento ------------*/
	            
	            
	            /*-----------------------------------------------------*/
	            /*------- inicio - gravando tramite -------------------*/
	            /*-----------------------------------------------------*/
	            TbTramite tramite = new TbTramite();
	            tramite.setDsDescricao(dsEncaminhamento);
	            tramite.setDtTramite(new Date());
	            tramite.setIdEncaminhamento(encaminhamento);
				tramite.setIdUnidadeEnvio(unidade);
	            tramite.setIdUsuarioEmissor(usuario);
	            tramite.setStRetornada(BooleanEnum.NAO.getId());
	            tramite.setStNotificacao(BooleanEnum.NAO.getId());
	            tramite.setStTramitePublico(BooleanEnum.NAO.getId());
	
	            TbUsuario usuarioReceptor = null;
	            if (ValidacaoHelper.isNotEmpty(idUsuarioEncaminhamento)) {
	                usuarioReceptor = usuarioDAO.find(idUsuarioEncaminhamento);
	                tramite.setIdUsuarioReceptor(usuarioReceptor);
	            }
				
	            /*-----------------------------------------------------*/
	            /*------------------- ALTERAÇÕES ----------------------*/
	            /*-----------------------------------------------------*/

				if (ValidacaoHelper.isEmpty(usuarioReceptor) && ValidacaoHelper.isNotOne(unidade)){					
					if(ValidacaoHelper.isEquals(idUnidade,3043)){
						usuarioReceptor = usuarioDAO.find(5);
					}
					tramite.setIdUsuarioReceptor(usuarioReceptor);
				}
	            
	            //----- gravando anexos ---------//
				ArrayList<TbTramitexAnexo> anexosTramite = new ArrayList<>();
				for (TbAnexo anexo : arquivosEncaminhamento) {
					anexoDAO.create(anexo);
					TbTramitexAnexo tbTramitexAnexo = new TbTramitexAnexo();
					tbTramitexAnexo.setIdAnexo(anexo);
					tbTramitexAnexo.setIdTramite(tramite);
					anexosTramite.add(tbTramitexAnexo);
				}
				tramite.setTbTramitexAnexoCollection(anexosTramite);
				tramiteDAO.edit(tramite);
	            /*------- fim - gravando tramite ------------*/
				
	            try {
	            	enviarEmailsEncaminhamento(unidade, usuarioReceptor);
	                //----- enviando e-mail ao MANIFESTANTE ---------//
	                if (qtdEncaminhamento == 1) {
	                    EmailService.Email emailManifestante = emailService.newEmail();
	                    if ((manifestacao.getEeEmailUsuario() != null) && "1".equals(manifestacao.getStResposta())) {
	
	                        String nomeManifestante = (manifestacao.getNmPessoa() != null) ? manifestacao.getNmPessoa() : "Manifestante";
	                        emailManifestante.addDestinatario(nomeManifestante, manifestacao.getEeEmailUsuario());
	
	                        TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.PRIMEIRO_TRAMITE);
	
	                        //adiciona texto e assunto de email padrao
	                        StringBuilder emailTextoHtml = new StringBuilder();
	                        StringBuilder emailTexto = new StringBuilder();
	
	                        emailManifestante.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, false));
	                        emailTextoHtml.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, false));
	                        emailTexto.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, false));
	
	                        emailManifestante.setTextoHtml(emailTextoHtml.toString());
	                        emailManifestante.setTextoSemFormatacao(emailTexto.toString());
	                        emailService.envia(emailManifestante);
	                    }
	                }
	                MensagemFaceUtil.info("Manifestação encaminhada com sucesso.", "Manifestação encaminhada com sucesso.");
	            } catch (Exception e) {
	                e.printStackTrace();
	                MensagemFaceUtil.alerta("Manifestação encaminhada mas não possível notiticar por e-mail.", "O seu encaminhamento foi realizado com sucesso porém, não foi possível notificar os envolvidos por e-mail.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            MensagemFaceUtil.erro("Erro ao encaminhar Manifestação.", e.getMessage());
	        }
    	}
    	
    	ajustaListaStatusManifestacao();
    	preparaListaAreasSolucionadoras();
        mountTabs();
        //zerando variaveis
        idUnidadeEncaminhamento = null;
        idUsuarioEncaminhamento = null;
        mensagemEncaminhamento = null;
        dsEncaminhamento = null;
        idEncaminhamentoPadronizado = null;
        arquivosEncaminhamento = new ArrayList<>();
        
        showRedirectPageModal();
    }
    
    private void enviarEmailTramiteOperador(TbUsuario usuarioReceptor) {
        try {
			if (usuarioReceptor != null
					&& StatusUsuarioEnum.ATIVO.getId() == usuarioReceptor.getStStatus()
					&& FuncaoUsuarioEnum.OPERADOR.getId().equals(usuarioReceptor.getTpFuncao())) {
				
                EmailService.Email email = emailService.newEmail();
                email.addDestinatario(usuarioReceptor.getNmUsuario(), usuarioReceptor.getEeEmail());
                
                //recupera texto padrao para o email
                TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.OPERADOR);

                //adiciona texto e assunto de email padrao
                String emailTexto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, true);

                email.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, true));
                email.setTextoHtml(emailTexto);
                email.setTextoSemFormatacao(emailTexto);
                
                emailService.envia(email);
	    	}
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    private void enviarEmailsEncaminhamento(TbUnidade unidade, TbUsuario usuarioReceptor) {
    	EmailService.Email email = emailService.newEmail();
    	
    	TbEmailAutomatizado emailAutomatizado = null;
    	// Esse e-mail será enviado a ouvidoria sempre que uma manifestação
    	// retornar a ela, seja por um interlocutor ou operador.
    	if(UnidadeEnum.OUVIDORIA.getId() == unidade.getIdUnidade()) {
    		emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.OUVIDORIA);
    	} else {
    		// Esse e-mail será enviado do operador para o interlocutor
    		// sempre que houver trâmite do operador para o interlocutor.
    		if(FuncaoUsuarioEnum.OPERADOR == securityService.getUserProfile()) {
    			emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.INTERLOCUTOR_OPERADOR);
    		} else {
				// Esse e-mail será enviado ao interlocutor sempre que houver um
				// novo trâmite para a sua área.
				emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.INTERLOCUTOR_OUVIDORIA);
    		}
    	}
    	
    	if(emailAutomatizado == null) {
    		return;
    	}

        String emailTexto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, true);
        email.addDestinatario(unidade.getNmUnidade(), unidade.getEeEmail());
        email.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, true));
        email.setTextoHtml(emailTexto);
        email.setTextoSemFormatacao(emailTexto);
    
        // Envia para o Email da Unidade
        try {
	        emailService.envia(email);
        } catch (Exception e) {
    		e.printStackTrace();
    	}
        
    	// Envia E-mail para os interlocutores
        if(UnidadeEnum.OUVIDORIA.getId() != unidade.getIdUnidade()) {
	        Collection<TbUsuario> usuariosUnidade = unidade.getTbUsuarioCollection();
	        for (TbUsuario usuario : usuariosUnidade) {
				if (FuncaoUsuarioEnum.INTERLOCUTOR.getId().equals(usuario.getTpFuncao())
						&& StatusUsuarioEnum.ATIVO.getId() == usuario.getStStatus()) {
	            	try {
	            		email.clearDestinatarios();
	            		email.addDestinatario(usuario.getNmUsuario(), usuario.getEeEmail());
	        	        emailService.envia(email);
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	}
	            }
	        }
        }
        
        // Envia E-Mail para o operador
        if(usuarioReceptor != null) {
        	enviarEmailTramiteOperador(usuarioReceptor);
        }
    }

    public void salvaComunicacaoExterna() {
        try {
            //atualiza data de modificação da manifestação
            manifestacao.setDtUltimaAtualizacao(new Date());
            // Verifica se irá alterar o status da manifestação
            if(securityService.isManifestante() && StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId().equals(manifestacao.getStStatusManifestacao())) {
            	manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA.getId());
            } else if(ValidacaoHelper.isEmpty(manifestacao.getTbEncaminhamentoCollection())
        		|| StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA.getId().equals(manifestacao.getStStatusManifestacao())
        		|| StatusManifestacaoEnum.EM_ANDAMENTO.getId().equals(manifestacao.getStStatusManifestacao())) {
            	if(StatusManifestacaoEnum.EM_ANDAMENTO.getId().equals(manifestacao.getStStatusManifestacao())) {
            		for (TbEncaminhamento e : manifestacao.getTbEncaminhamentoCollection()) {
						if(StatusEncaminhamentoEnum.RETORNADA.getId().equals(e.getStEncaminhamento())) {
							manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId());
							break;
						}
					}
            	} else {
            		if(!StatusManifestacaoEnum.NOVA.getId().equals(manifestacao.getStStatusManifestacao())) {
            			manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId());
            		}
            	}
            }
            dao.edit(manifestacao);

            /*------- inicio - gravando comunicacao externa ------------*/
            TbComunicacaoExterna comunicacaoExterna = new TbComunicacaoExterna();
            comunicacaoExterna.setDsComunicacao(dsMensagemAoManifestante);
            comunicacaoExterna.setDtComunicacao(new Date());
            comunicacaoExterna.setIdManifestacao(manifestacao);
            comunicacaoExterna.setStComunicacaoPublica(BooleanEnum.NAO.getId());
            
            TbUsuario usuario = null;
            if(securityService.isManifestante()) {
            	usuario = manifestacao.getIdUsuarioManifestante();
            } else {
            	usuario = usuarioDAO.find(securityService.getUser().getIdUsuario());
            }
            comunicacaoExterna.setIdUsuario(usuario);
            
            mensagemAoManifestante = true;

            //----- gravando anexos ---------//
            ArrayList<TbComunicacaoExternaxAnexo> anexos = new ArrayList<>();
            for (TbAnexo anexo : arquivosComunicacaoExterna) {
                anexoDAO.create(anexo);

                TbComunicacaoExternaxAnexo tbComunicacaoExternaxAnexo = new TbComunicacaoExternaxAnexo();
                tbComunicacaoExternaxAnexo.setIdAnexo(anexo);
                tbComunicacaoExternaxAnexo.setIdComunicacaoExterna(comunicacaoExterna);
                anexos.add(tbComunicacaoExternaxAnexo);
            }
            comunicacaoExterna.setTbComunicacaoExternaxAnexoCollection(anexos);
            comunicacaoExternaDAO.edit(comunicacaoExterna);
            /*------- fim - gravando comunicacao externa ------------*/
            
            /*------- enviar email para o manifestante ------------*/
            if(securityService.isManifestante()) {
            	TbUnidade ouvidoria = unidadeDAO.find(UnidadeEnum.OUVIDORIA.getId());
                EmailService.Email email = emailService.newEmail();
                email.addDestinatario(ouvidoria.getNmUnidade(), ouvidoria.getEeEmail());
                
                //recupera texto padrao para o email
                TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.MANIFESTANTE);

                //adiciona texto e assunto de email padrao
                String emailTexto = PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, true);
                email.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, true));
                email.setTextoHtml(emailTexto);
                email.setTextoSemFormatacao(emailTexto);
                
                emailService.envia(email);
            } else {
        		emailService.enviaEmailNotificacaoNovaMensagem(manifestacao);
            }
            
            //zerando variaveis
            mensagemManifestante = null;
            dsMensagemAoManifestante = null;
            idRespostaManifestacao = null;
            arquivosComunicacaoExterna = new ArrayList<>();
            
            mountTabs();
            MensagemFaceUtil.info("Mensagem enviada com sucesso", null);
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro ao tentar enviar mensagem", e.getMessage());
        }
        
		// Se o usuário atual estiver logado aparece a modal com a opção de
		// continuar na atual página ou retornar para a página de listagem de
		// manifestações. Caso seja usuário não logado (acessou com os dados da
		// manifestação) recarrega a página atual.
        if(securityService.getUser() != null) {
        	showRedirectPageModal();
        } else {
        	JSFUtils.executeJavaScript("location.reload()");
        }
    }

    public void salvaTramite() {
        try {
            /*------- inicio - gravando tramite ------------*/
            //atualiza data de modificação da manifestação
            manifestacao.setDtUltimaAtualizacao(new Date());
            dao.edit(manifestacao);
            
            ManifestacaoTabView manifestacaoTabView = tabViewSelecionada != null ? tabViewSelecionada : tabs.get(0);
            TbTramite tramite = new TbTramite();
            tramite.setDsDescricao(dsMensagemAreaTecnica);
            tramite.setDtTramite(new Date());
            tramite.setIdEncaminhamento(manifestacaoTabView.getEncaminhamento());
            tramite.setIdUnidadeEnvio(unidadeDAO.find(idUnidadeTramite));
            tramite.setIdUsuarioEmissor(securityService.getUser());
            tramite.setStRetornada(BooleanEnum.NAO.getId());
            tramite.setStNotificacao(BooleanEnum.NAO.getId());
            tramite.setStTramitePublico(BooleanEnum.NAO.getId());
            
            // Envia Email
            if (ValidacaoHelper.isNotEmpty(idUsuarioTramite)) {
                TbUsuario usuarioReceptor = usuarioDAO.find(idUsuarioTramite);
                tramite.setIdUsuarioReceptor(usuarioReceptor);
                enviarEmailTramiteOperador(usuarioReceptor);
            } else {
            	enviarEmailsEncaminhamento(unidadeDAO.find(idUnidadeTramite), null);
            }
            
            tramiteDAO.create(tramite);
            /*------- fim - gravando tramite ------------*/
            
            //atualiza status do encaminhamento dependendo de para onde a msg foi enviada
            TbUnidade unidadeEnviou = manifestacaoTabView.getUnidadeEnviou();
            TbEncaminhamento encaminhamento = encaminhamentoDAO.find(manifestacaoTabView.getEncaminhamento().getIdEncaminhamento());
            if (unidadeEnviou.getIdUnidade().equals(idUnidadeTramite) && !(securityService.isOuvidor() || securityService.isAdministrador())) {
                encaminhamento.setStEncaminhamento(StatusEncaminhamentoEnum.RETORNADA.getId());
                encaminhamento.setDtRespostaTramite(new Date());
                
				// Se retornar a mensagem para a Unidade que realizou o encaminhamento
                // seta todos os tramites da unidade como retornado
                for (TbTramite t : encaminhamento.getTbTramiteCollection()) {
            		t.setStRetornada(BooleanEnum.SIM.getId());
            		tramiteDAO.edit(t);
            	}
            } else {
                encaminhamento.setStEncaminhamento(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
				if (!manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.EM_ANDAMENTO)
						&& EnumHelper.getFuncaoUsuarioEnum(securityService.getUser().getTpFuncao()) == FuncaoUsuarioEnum.OUVIDOR) {
					encaminhamento.setDtEnvioTramite(new Date());
				}
                
                manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
                dao.edit(manifestacao);
            }
            encaminhamentoDAO.edit(encaminhamento);
            
            // atualiza status dos tramites
            if (EnumHelper.getFuncaoUsuarioEnum(securityService.getUser().getTpFuncao()) == FuncaoUsuarioEnum.OPERADOR) {
            	int idOperador = securityService.getUser().getIdUsuario();
	        	for (TbTramite t : encaminhamento.getTbTramiteCollection()) {
	        		if(t.getIdUsuarioReceptor() != null && t.getIdUsuarioReceptor().getIdUsuario() == idOperador) {
	        			t.setStRetornada(BooleanEnum.SIM.getId());
	        			tramiteDAO.edit(t);
	        		}
	        	}
            }

            //----- gravando anexos ---------//
            ArrayList<TbTramitexAnexo> anexos = new ArrayList<>();
            for (TbAnexo anexo : arquivosTramite) {
                anexoDAO.create(anexo);

                TbTramitexAnexo tbTramitexAnexo = new TbTramitexAnexo();
                tbTramitexAnexo.setIdAnexo(anexo);
                tbTramitexAnexo.setIdTramite(tramite);
                anexos.add(tbTramitexAnexo);
            }
            tramite.setTbTramitexAnexoCollection(anexos);
            tramiteDAO.edit(tramite);

            //zerando variaveis
            mensagemAreaTecnica = null;
            dsMensagemAreaTecnica = null;
            idEncaminhamentoPadronizadoTramite = null;
            idUnidadeTramite = null;
            idUsuarioTramite = null;
            arquivosTramite = new ArrayList<>();

            showRedirectPageModal();
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro ao encaminhar a mensagem", e.getMessage());
        }
    }

    public Integer getIdRespostaManifestacao() {
        return idRespostaManifestacao;
    }

    public void setIdRespostaManifestacao(Integer idRespostaManifestacao) {
    	if(this.idRespostaManifestacao == idRespostaManifestacao) {
    		return;
    	}
    	
        this.idRespostaManifestacao = idRespostaManifestacao;
        if (getRespostaManifestacao() != null) {
            setDsMensagemAoManifestante(getRespostaManifestacao().getDsResposta());
        }
    }
    
    private String mensagemManifestante;

    public void ajustaDescricaoMensagemManifestante() {
        mensagemManifestante = PalavrasChavesHelper.converterPalavrasChaves(dsMensagemAoManifestante, manifestacao, false);
    }

    public String getDsMensagemAoManifestante() {
        return mensagemManifestante;
//        return dsMensagemAoManifestante;
    }

    public void setDsMensagemAoManifestante(String dsMensagemAoManifestante) {
        this.dsMensagemAoManifestante = dsMensagemAoManifestante;
    }

    public TbRespostaManifestacao getRespostaManifestacao() {
        return respostaManifestacaoDAO.find(idRespostaManifestacao == null ? 0 : idRespostaManifestacao);
    }
    
    private String mensagemAreaTecnica;

    public void ajustaDescricaoMensagemAreaTecnica() {
        mensagemAreaTecnica = PalavrasChavesHelper.converterPalavrasChaves(dsMensagemAreaTecnica, manifestacao, true);
    }
    
    public String getDsMensagemAreaTecnica() {
        return mensagemAreaTecnica;
    }

    public void setDsMensagemAreaTecnica(String dsMensagemAreaTecnica) {
        this.dsMensagemAreaTecnica = dsMensagemAreaTecnica;
    }

    public String getCodStatusManifetacao() {
        return codStatusManifetacao;
    }

    public void setCodStatusManifetacao(String codStatusManifetacao) {
        this.codStatusManifetacao = codStatusManifetacao;
    }
    private Boolean viewGrid = true;

    public void changeView(ActionEvent evt) {
        setViewGrid(!getViewGrid());
    }

    public Boolean getViewGrid() {
        return viewGrid;
    }

    public void setViewGrid(Boolean viewGrid) {
        this.viewGrid = viewGrid;
    }
    
    /**
     * Prepara a lista de unidades que podem ser áreas solucionadoras.
     * Apenas as unidades que já receberam encaminhamentos podem ser
     * áreas solucionadoras, além da Ouvidoria que sempre pode ser.
     */
    public void preparaListaAreasSolucionadoras() {
    	unidades = new ArrayList<>();
    	// Adiciona as unidades que já receberam encaminhamento
    	for (TbEncaminhamento referral : manifestacao.getTbEncaminhamentoCollection()) {
			if(StatusEncaminhamentoEnum.RETORNADA.getId().equals(referral.getStEncaminhamento())) {
				unidades.add(referral.getIdUnidadeRecebeu());
			}
    	}
    	
    	// Adiciona a Ouvidoria
    	if(!unidades.contains(new TbUnidade(UnidadeEnum.OUVIDORIA.getId()))) {
    		unidades.add(0, unidadeDAO.find(UnidadeEnum.OUVIDORIA.getId()));
    	}
    	
    	// Ordena as unidades pela sigla
		Collections.sort(unidades, new UnidadeSiglaComparator());
		unidadesCompleta = new ArrayList<TbUnidade>(unidades);
    	
    	// Unidades que ainda podem receber encaminhamento
		listaUnidades = null;
        listaUnidades = unidadeDAO.findAll();
        listaUnidades.removeAll(unidadeDAO.getPorEncaminhamentoManifestacao(manifestacao));
        listaUnidades.remove(new TbUnidade(UnidadeEnum.OUVIDORIA.getId()));
        Collections.sort(listaUnidades, new UnidadeSiglaComparator());
        listaUnidadesCompleta = new ArrayList<TbUnidade>(listaUnidades);
    }
    
    public Boolean habilitaUsuarioEncaminhamento() {
//        return !(idUnidadeEncaminhamento != null);
        return true;
    }

    public Boolean desabilitaUsuarioTramite() {
    	boolean desabilitar = true;
    	if(tabViewSelecionada != null) {
    		Integer idUnidadeEnviou = tabViewSelecionada.getUnidadeEnviou().getIdUnidade();
    		desabilitar = idUnidadeTramite.equals(idUnidadeEnviou);
    	}
    	return desabilitar;
    }
    
    public boolean desabilitarBotaoVisualizarTramite() {
    	boolean desabilitar = Boolean.TRUE;
    	
    	if (ValidacaoHelper.isNotEmpty(idUnidadeTramite)) {
    		if(idUnidadeTramite == UnidadeEnum.OUVIDORIA.getId()) {
    			desabilitar = Boolean.FALSE;
    		} else {
    			if(ValidacaoHelper.isNotEmpty(idUsuarioTramite)) {
    				desabilitar = Boolean.FALSE;
    			}
    		}
    	}
    	return desabilitar;
    }

    public Boolean desabilitaUnidadeTramite() {
        return idUnidadeTramite == null && (securityService.isInterlocutor() || securityService.isOperador());
    }
    
    public Boolean habilitaEnvioMensagemManifestante() {
    	if (manifestacao.getIdUsuarioAnalisador() != null) {
            if (manifestacao.getStStatusManifestacao() == null) {
                return false; //nao desabilita elemento
            } else if (manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.NOVA.getId())) {
                return true; //desabilita elemento
            } else if (manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.EM_ANALISE.getId()) && manifestacao.getIdUsuarioAnalisador().getIdUsuario() != securityService.getUser().getIdUsuario()) {
                return true;  //desabilita elemento
            } else {
                return false;  //nao desabilita elemento
            }
        } else {
            return true;  //desabilita elemento
        }
    }
    
    public Boolean habilitaEnvioTramite(ManifestacaoTabView tabView) {
    	this.tabViewSelecionada = tabView;
    	this.tabViewAtiva = tabView.getIndex();
    	return desabilitaEdicaoManifestacao();
    }
    
    public boolean desabilitaEncaminhamentoManifestacao() {
    	boolean desabilitar = false;
    	
    	if(!securityService.isInterlocutor()) {
			if (ValidacaoHelper.isEmpty(idOrgaoDestino, idClassificacao, idPrioridade)) {
				desabilitar = true;
			} else {
		        if (manifestacao.getIdUsuarioAnalisador() != null) {
		            if (StatusManifestacaoEnum.NOVA.getId().equals(manifestacao.getStStatusManifestacao())) {
		            	desabilitar = true;
		            } else if (StatusManifestacaoEnum.EM_ANALISE.getId().equals(manifestacao.getStStatusManifestacao()) && !isLoggedUserResponsibleForManifestation()) {
		            	desabilitar = true;
		            }
		        } else {
		        	desabilitar = true;
		        }
			}
    	}
		return desabilitar;
    }
    
    public boolean desabilitaComboStatusManifestacao() {
    	if (isLoggedUserResponsibleForManifestation()) {
    		return false;
    	}
		return true;
    }

    public boolean desabilitaEdicaoManifestacao() {
    	if(!securityService.isManifestante()) {
    		if (manifestacao.getIdUsuarioAnalisador() != null) {
    			if(securityService.isInterlocutor()) {
    				TbEncaminhamento enc = ManifestacaoUtils.getEncaminhamentoDoUsuarioLogado(manifestacao);
					if(UnidadeEnum.OUVIDORIA.getId() == enc.getIdUnidadeEnviou().getIdUnidade()) {
						return !mostrarBotaoResponderAOuvidoria();
    				} else {
    					return false;
    				}
    			}
    			
    			if (StatusManifestacaoEnum.NOVA.getId().equals(manifestacao.getStStatusManifestacao()) ||
    					StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {
    				return true;
    			} else if (StatusManifestacaoEnum.EM_ANALISE.getId().equals(manifestacao.getStStatusManifestacao()) && !isLoggedUserResponsibleForManifestation()) {
    				return true;
    			} else {
    				return false;
    			}
    		} else {
    			return true;
    		}
    	} else {
    		return false;
    	}
    }
    
    private boolean isLoggedUserResponsibleForManifestation() {
    	return securityService.getUser().equals(manifestacao.getIdUsuarioAnalisador());
    }

    public Collection<TbUsuario> getUsuarios() {
        int idUnidade = 0;
        
        if (ValidacaoHelper.isNotEmpty(idUnidadeTramite)) {
        	idUnidade = idUnidadeTramite;
        } else if (ValidacaoHelper.isNotEmpty(idUnidadeEncaminhamento)) {
        	idUnidade = idUnidadeEncaminhamento;
        } else if (idOrgaoDestino.size() == 1) {
        	idUnidade = Integer.parseInt(idOrgaoDestino.get(0));
        }
        
        if(idUnidade == 0) {
        	return new ArrayList<>();
        } else {
        	if(!(securityService.isAdministrador() || securityService.isOuvidor()) 
        			&& securityService.getUser().getIdUnidade().getIdUnidade() != idUnidade) {
        		idUnidade = 0;
        	}
        }
        
        List<TbUsuario> usuariosDaUnidade = new ArrayList<TbUsuario>(usuarioDAO.findByUnidade(idUnidade));
        
        if(idUnidade != UnidadeEnum.OUVIDORIA.getId()) {
        	for (Iterator<TbUsuario> it = usuariosDaUnidade.iterator(); it.hasNext();) {
        		TbUsuario usuario = it.next();
				if ((!usuario.getTpFuncao().equals(FuncaoUsuarioEnum.OPERADOR.getId()))
						|| usuario.isInativo()) {
					it.remove();
				}
        	}
        }

        Collections.sort(usuariosDaUnidade);
        return usuariosDaUnidade;
    }

    public Integer getIdUsuarioTramite() {
        return idUsuarioTramite;
    }

    public void setIdUsuarioTramite(Integer idUsuarioTramite) {
        this.idUsuarioTramite = idUsuarioTramite;
    }

    public Integer getIdUsuarioEncaminhamento() {
        return idUsuarioEncaminhamento;
    }

    public void setIdUsuarioEncaminhamento(Integer idUsuarioEncaminhamento) {
        this.idUsuarioEncaminhamento = idUsuarioEncaminhamento;
    }
    
    public void zerarValoresEncaminhamento() {
        //zerando variaveis
        idUnidadeEncaminhamento = null;
        idUsuarioEncaminhamento = null;
        mensagemEncaminhamento = null;
        idEncaminhamentoPadronizado = null;
        arquivosEncaminhamento = new ArrayList<>();
    }

    public void zerarValoresTramite() {
        //zerando variaveis

        mensagemAreaTecnica = null;
        idEncaminhamentoPadronizadoTramite = null;
        idUnidadeTramite = null;
        idUsuarioTramite = null;
        arquivosTramite = new ArrayList<>();
    }

    public void zerarValoresComunicacaoExterna() {
        //zerando variaveis
        mensagemManifestante = null;
        idRespostaManifestacao = null;
        arquivosComunicacaoExterna = new ArrayList<>();
    }
    
    public void baixarArquivo(TbAnexo anexo) throws IOException {
        try {
            File arquivo = new File(parametroDAO.getDiretorioAnexo() + anexo.getDsCaminhoAnexo());
            FileHelper.download(arquivo, anexo.getNmAnexo());
            MensagemFaceUtil.info("Baixando arquivo", "Enviando o arquivo " + anexo.getNmAnexo() + " para download");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao tentar baixar o arquivo");
        }
    }
    
    private List<StatusManifestacaoEnum> listaStatusManifestacao;

    public void ajustaListaStatusManifestacao() {
    	listaStatusManifestacao = null;
        listaStatusManifestacao = new ArrayList<>();
        StatusManifestacaoEnum status = EnumHelper.getStatusManifestacaoEnum(manifestacao.getStStatusManifestacao());
        
        // Verifica se e permitido encerrar com status encaminhado na preferencia dos sistema e se existe encaminhamento aberto
        Boolean encerrar = true;
        TbPreferenciaSistema preferenciasSistema = preferenciaSistemaDAO.findAll().get(0);
        String encerrarTramiteEncaminhada = preferenciasSistema.getEncerrarTramiteEncaminhada(); 
        if (BooleanEnum.NAO.getId().equals(encerrarTramiteEncaminhada) && checaEncaminhamentoAberto()) {
            encerrar = false;
        }

        switch (status) {
            case NOVA:
                listaStatusManifestacao.add(StatusManifestacaoEnum.NOVA);
                listaStatusManifestacao.add(StatusManifestacaoEnum.EM_ANALISE);
                break;
            case EM_ANALISE:
                listaStatusManifestacao.add(StatusManifestacaoEnum.EM_ANALISE);
                if (encerrar) { //disponibiliza status apenas se nao exisitr encaminhamento aberto
                    listaStatusManifestacao.add(StatusManifestacaoEnum.SOLUCIONADA);
                }
                break;
            case EM_ANDAMENTO:
                listaStatusManifestacao.add(StatusManifestacaoEnum.EM_ANDAMENTO);
                if (encerrar) { //disponibiliza status apenas se nao exisitr encaminhamento aberto
                    listaStatusManifestacao.add(StatusManifestacaoEnum.SOLUCIONADA);
                }
                break;
            case SOLUCIONADA:
                listaStatusManifestacao.add(StatusManifestacaoEnum.SOLUCIONADA);
                break;
            default: break;
        }
    }

    public Collection<StatusManifestacaoEnum> getListaStatusManifestacao() {
        return listaStatusManifestacao;
    }
    
    public Boolean checaEncaminhamentoAberto() {
        Collection<TbEncaminhamento> encaminhamento = encaminhamentoDAO.getEncaminhamentoAberto(manifestacao);
        return ValidacaoHelper.isNotEmpty(encaminhamento);
    }
    
    public void salvarEmailManifestante() {
    	try {
			dao.edit(manifestacao);
			MensagemFaceUtil.info("E-Mail alterado com sucesso!", "");
		} catch (InfrastructureException e) {
			e.printStackTrace();
		}
    }

    public void setIdOrgaoDestino(List<String> idOrgaoDestino) {
        this.idOrgaoDestino = idOrgaoDestino;
    }

    public List<String> getIdOrgaoDestino() {
        return idOrgaoDestino;
    }

	public Integer getIdManifestacao() {
		return idManifestacao;
	}

	public void setIdManifestacao(Integer idManifestacao) {
		this.idManifestacao = idManifestacao;
	}

	public Integer getNumeroManifestacao() {
		return numeroManifestacao;
	}

	public void setNumeroManifestacao(Integer numeroManifestacao) {
		this.numeroManifestacao = numeroManifestacao;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Gera uma nova senha para a manifestação e notifica o manifestante por
	 * e-mail
	 */
	public void generateNewPassword() {
		try {
			// Altera a senha no banco
			TbManifestacao newManifestation = dao.find(manifestacao.getIdManifestacao());
			String newPassword = PasswordUtils.generatePassword();
			newManifestation.setDsSenha(newPassword);
			manifestacao.setDsSenha(newPassword);
			dao.edit(newManifestation);
			
			// Notifica o manifestante por email
			List<String> emails = ManifestacaoUtils.converterEmListaDeEmails(manifestacao, true);
			for (String email : emails) {
				enviarEmail(manifestacao.getNmPessoa(), email, EmailAutomatizadoEnum.NOVA_SENHA_MANIFESTACAO, true);
			}
			MensagemFaceUtil.info("Nova senha gerada com sucesso", null);
		} catch (InfrastructureException e) {
			MensagemFaceUtil.erro("Não foi possível alterar a senha da manifestação", null);
			e.printStackTrace();
		} catch (EmailException e) {
			MensagemFaceUtil.erro("Não foi possível notificar o manifestante por email sobre a alteração da senha", null);
			e.printStackTrace();
		}
	}
	
	/**
	 * Marca a comunicação externa como pública ou privada. fazendo com que as
	 * unidades possam ver a mensagem e baixar os anexos
	 * 
	 * @param comunicacaoExterna
	 *            Comunicação Externa a ser marcada (ou desmarcada) como
	 *            pública
	 * @throws Exception
	 *             se acontecer algum problema ao tentar marcar a comunicação
	 *             externa como pública ou privada
	 */
	public void setComunicacaoExternaPublica(Object comunicacaoExterna) throws Exception {
		if(comunicacaoExterna != null && comunicacaoExterna instanceof TbComunicacaoExterna) {
			TbComunicacaoExterna comunicacao = (TbComunicacaoExterna) comunicacaoExterna;
			if(comunicacao.isComunicacaoPublica()) {
				comunicacao.setStComunicacaoPublica(BooleanEnum.NAO.getId());
			} else {
				comunicacao.setStComunicacaoPublica(BooleanEnum.SIM.getId());
			}
			
			try {
				comunicacaoExternaDAO.edit(comunicacao);
				String statusComunicacao = comunicacao.isComunicacaoPublica() ? "Público" : "Privado";
				MensagemFaceUtil.info("Alterado status da comunição externa para: " + statusComunicacao, null);
			} catch (Exception e) {
				MensagemFaceUtil.erro("Erro ao tentar alterar status da comunição externa", null);
			}
		}
	}
	
	/**
	 * @param comunicacaoExterna
	 *            Comunicação Externa
	 * @return retorna o nome do botão que deve ser apresentado na tela. Se a
	 *         comunicação externa tiver sido marcada como pública imprime
	 *         "Tornar Privado", caso contrário "Tornar Público"
	 */
	public String nomeBotaoComunicacaoExternaPublica(Object comunicacaoExterna) {
		String nomeBotao = "Tornar Público";
		if(comunicacaoExterna instanceof TbComunicacaoExterna) {
			TbComunicacaoExterna comunicacao = (TbComunicacaoExterna) comunicacaoExterna;
			if(comunicacao.isComunicacaoPublica()) {
				nomeBotao = "Tornar Privado";
			}
		}
		return nomeBotao;
	}
	
	/**
	 * Marca o trâmite como público ou privado. fazendo com que as
	 * unidades possam ver a mensagem e baixar os anexos
	 * 
	 * @param tramiteObj
	 *            Trâmite a ser marcado (ou desmarcado) como público
	 * @throws Exception
	 *             se acontecer algum problema ao tentar marcar a comunicação
	 *             externa como pública ou privada
	 */
	public void setTramitePublico(Object tramiteObj) throws Exception {
		if(tramiteObj != null && tramiteObj instanceof TbTramite) {
			TbTramite tramite = (TbTramite) tramiteObj;
			if(tramite.isTramitePublico()) {
				tramite.setStTramitePublico(BooleanEnum.NAO.getId());
			} else {
				tramite.setStTramitePublico(BooleanEnum.SIM.getId());
			}
			
			try {
				tramiteDAO.edit(tramite);
				String statusTramite = tramite.isTramitePublico() ? "Público" : "Privado";
				MensagemFaceUtil.info("Alterado status do trâmite para: " + statusTramite, null);
			} catch (Exception e) {
				MensagemFaceUtil.erro("Erro ao tentar alterar status do trâmite", null);
			}
		}
	}

	/**
	 * @param tramiteObj
	 *            Trâmite a ser marcado (ou desmarcado) como público
	 * @return retorna o nome do botão que deve ser apresentado na tela. Se o
	 *         trâmite tiver sido marcada como pública imprime "Tornar Privado",
	 *         caso contrário "Tornar Público"
	 */
	public String nomeBotaoTramitePublico(Object tramiteObj) {
		String nomeBotao = "Tornar Público";
		if(tramiteObj instanceof TbTramite) {
			TbTramite tramite = (TbTramite) tramiteObj;
			if(tramite.isTramitePublico()) {
				nomeBotao = "Tornar Privado";
			}
		}
		return nomeBotao;
	}
	
	/**
	 * Adiciona ou remove o trâmite como selecionado para que a mensagem seja
	 * copiada para o texto de solucionamento ou um novo trâmite.
	 * 
	 * @param idTramite
	 *            id do trâmite a ser adicionado ou removido
	 */
	public void addTramiteEscolhido(Integer idTramite) {
		if(ValidacaoHelper.isNotEmpty(idTramite)) {
			if(tramitesEscolhidos.contains(idTramite)) {
				tramitesEscolhidos.remove(idTramite);
			} else {
				tramitesEscolhidos.add(idTramite);
			}
		}
	}
	
	/**
	 * @param idTramite
	 *            id do trâmite
	 * @return retorna o nome do botão que deve ser apresentado na tela. Se o
	 *         trâmite tiver sido selecionado retorna "Selecionado", caso
	 *         contrário "Selecionar"
	 */
	public String nomeBotaoTramiteEscolhido(Integer idTramite) {
		String nomeBotao = "Selecionar";
		if (ValidacaoHelper.isNotEmpty(idTramite)
				&& tramitesEscolhidos.contains(idTramite)) {
			nomeBotao = "Selecionado";
		}
		return nomeBotao;
	}
	
	
	/**
	 * <p>
	 * Determina se o painel de cobrança de atraso por parte das undiades será
	 * mostrado ou não. Para mostrar as seguintes validações devem TODAS serem
	 * verdadeiras:
	 * </p>
	 * <ul>
	 * 	<li>Status da Manifestação deve ser diferente de SOLUCIONADA</li>
	 * 	<li>Usuário logado deve ser Administrador ou Ouvidor</li>
	 * 	<li>Deve existir encaminhamentos atrasados</li>
	 * </ul>
	 * 
	 * @return true ou false baseado na regra acima
	 */
	public boolean showDelayNotificationPanel() {
		boolean show = false;
		if ((!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao()))
				&& (securityService.isAdministrador() || securityService.isOuvidor())
				&& ValidacaoHelper.isNotEmpty(encaminhamentosAtrasados)) {
			show = true;
		}
		return show;
	}
	
	/**
	 * @return texto de atraso com o nome das unidades que já extouraram o prazo
	 *         de atendimento
	 */
	public String messageDelayNotification() {
		String nomeUnidades = null;
		String complemento = null;
		if(ValidacaoHelper.isNotEmpty(encaminhamentosAtrasados)) {
			if(encaminhamentosAtrasados.size() == 1) {
				complemento = "da unidade";
				TbEncaminhamento encaminhamento = encaminhamentosAtrasados.get(0);
				nomeUnidades = encaminhamento.getIdUnidadeRecebeu().getNomeFormatado();
			} else {
				complemento = "das unidades";
				for (TbEncaminhamento encaminhamento: encaminhamentosAtrasados) {
					String nomeUnidade = encaminhamento.getIdUnidadeRecebeu().getNomeFormatado();
					nomeUnidades = nomeUnidades == null ? nomeUnidade : nomeUnidades + ", " + nomeUnidade;
				}
			}
		}
		return String.format(MENSAGEM_ATRASO_UNIDADE, complemento, nomeUnidades);
	}
	
	public void sendDelayNotification() {
		for (TbEncaminhamento encaminhamento: encaminhamentosAtrasados) {
			TbUnidade unidade = encaminhamento.getIdUnidadeRecebeu();
			Collection<TbUsuario> usuarios = unidade.getTbUsuarioCollection();
			Collection<TbTramite> tramites = encaminhamento.getTbTramiteCollection();
			
			try {
				// Cria um trâmite sobre a cobrança
				criarTramiteNotificacaoAtraso(encaminhamento);
				
				// envia notificação para o email da unidade
				enviarEmail(unidade.getNomeFormatado(), unidade.getEeEmail(),
						EmailAutomatizadoEnum.ATRASO_INTERLOCUTOR, false);
				
				// envia notificação aos interlocutores
				for (TbUsuario usuario : usuarios) {
					FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
					if(funcao == FuncaoUsuarioEnum.INTERLOCUTOR && StatusUsuarioEnum.ATIVO.getId() == usuario.getStStatus()) {
						enviarEmail(usuario.getNmUsuario(),usuario.getEeEmail(),
								EmailAutomatizadoEnum.ATRASO_INTERLOCUTOR, false);
					}
				}
				
				// envia notificação aos operadores
				for (TbTramite tramite : tramites) {
					TbUsuario usuario = tramite.getIdUsuarioReceptor();
					if(usuario != null && StatusUsuarioEnum.ATIVO.getId() == usuario.getStStatus()) {
						FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
						if(funcao == FuncaoUsuarioEnum.OPERADOR && BooleanEnum.NAO.getId().equals(tramite.getStRetornada())) {
							enviarEmail(usuario.getNmUsuario(),usuario.getEeEmail(),
									EmailAutomatizadoEnum.ATRASO_OPERADOR, false);
						}
					}
				}
				MensagemFaceUtil.info("Os envolvidos foram notificados por email sobre o atraso", null);
			} catch (Exception e) {
				e.printStackTrace();
				MensagemFaceUtil.alerta("Ocorreu um erro ao tentar notificar os envolvidos por email sobre o atraso", null);
			}
		}
		showRedirectPageModal();
	}
	
	private void criarTramiteNotificacaoAtraso(TbEncaminhamento encaminhamento) throws Exception {
        // Atualiza data da última atualização da manifestação
        manifestacao.setDtUltimaAtualizacao(new Date());
        dao.edit(manifestacao);
        
        // Cria o Trâmite
        TbTramite tramite = new TbTramite();
        tramite.setDsDescricao(getTextoTramiteAtraso());
        tramite.setDtTramite(new Date());
        tramite.setIdEncaminhamento(encaminhamento);
        tramite.setIdUnidadeEnvio(encaminhamento.getIdUnidadeRecebeu());
        tramite.setIdUsuarioEmissor(securityService.getUser());
        tramite.setStRetornada(BooleanEnum.NAO.getId());
        tramite.setStNotificacao(BooleanEnum.SIM.getId());
        tramite.setStTramitePublico(BooleanEnum.NAO.getId());
        tramiteDAO.create(tramite);
    }
	
	/**
	 * @param userName nome do destinatário
	 * @param userEmail email do destinatário
	 * @param emailType padrão de email a ser utilizado
	 * @param isExternalEmail TRUE se o email for enviado para manifestantes (usuários externos)
	 * @throws EmailException 
	 */
	private void enviarEmail(String userName, String userEmail, EmailAutomatizadoEnum emailType, boolean isExternalEmail) throws EmailException {
        EmailService.Email email = emailService.newEmail();
        TbEmailAutomatizado emailTemplate = emailAutomatizadoDAO.findByTipo(emailType);
        String emailContent = PalavrasChavesHelper.converterPalavrasChaves(emailTemplate.getDsEmail(), manifestacao, isExternalEmail);
        
        email.addDestinatario(userName, userEmail);
        email.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailTemplate.getNmTituloEmail(), manifestacao, isExternalEmail));
        email.setTextoHtml(emailContent);
        email.setTextoSemFormatacao(emailContent);
        
        emailService.envia(email);
	}
	
	private String getTextoTramiteAtraso() {
		return new StringBuilder()
		.append("<p>Prezado (a) colaborador (a),</p>")
		.append("<p>Verificamos que a presente mensagem permanece inconclusa.</p>")
		.append("<p>Solicitamos que seja dada resposta, com brevidade, à mensagem em tela, tendo em vista que o prazo regimental de resposta a esta Ouvidoria já está expirado.</p>")
		.append("<p>Reiteramos que enviem a resposta com a maior brevidade possível, visto que o prazo estabelecido pela Ouvidoria para atendimento ao interessado já se esgotou (vide art. 57, § 1º, I, II e III, da Portaria n. 40 - Regimento Interno da Prefeitura)</p>") 
		.append("<p>Solicitamos que a resposta seja encaminhada à Ouvidoria, diretamente por meio deste sistema, para que possamos retransmiti-la ao interessado.</p>")
		.append("<p>Em caso de dúvidas, a Ouvidoria está à disposição.</p>")
		.append("<p>Atenciosamente,<br />") 
		.append("Ouvidoria<br />")
		.append("Prefeitura de Videira</p>").toString();
	}
	
	@Override
	public boolean disableSendMessageButton() {
		boolean disable = true;
		if(securityService.isManifestante()) {
			disable = false;
		} else {
			disable = isManifestacaoBloqueada() || habilitaEnvioMensagemManifestante();
		}
		return disable;
	}
	
	public void ajustaListaUnidadeTramite(ManifestacaoTabView manifestationTabView) {
		listaUnidadesTramite = new ArrayList<>();

		if (manifestationTabView == null) {
			listaUnidadesTramite.add(unidadeDAO.find(UnidadeEnum.OUVIDORIA.getId()));
			return;
		}

		TbUnidade userDepartment = securityService.getUser().getIdUnidade();
		TbUnidade departmentReceived = manifestationTabView.getUnidadeRecebeu();
		TbUnidade departmentSent = manifestationTabView.getUnidadeEnviou();
		// Seta a unidade da aba clicada como selecionada
		idUnidadeTramite = departmentReceived.getIdUnidade();
		// Adiciona a unidade que recebeu o trâmite
		listaUnidadesTramite.add(departmentReceived);

		// Se o usuário logado for Ouvidor, Administrador ou Interlocutor
		// adiciona a unidade que fez o encaminhamento, caso seja Operador
		// só adiciona se a unidade que fez o encaminhamento for a OUVIDORIA
		// e se a opção de operador devolver para a ouvidoria estiver habilitada
		if (securityService.isOperador()) {
			if (departmentSent.getIdUnidade() == UnidadeEnum.OUVIDORIA.getId()
					&& userDepartment.isRetornoOuvidoria()) {
				listaUnidadesTramite.add(departmentSent);
			}
		} else {
			listaUnidadesTramite.add(departmentSent);
		}
		
		mensagemAreaTecnica = carregarMensagensSelecionadas(false);
	}
	
	public boolean disableManifestationType() {
		return isManifestacaoBloqueada()
				|| (!desabilitaEdicaoManifestacao() 
						? !(securityService.isOuvidor() || securityService.isAdministrador())
						: desabilitaEdicaoManifestacao());
	}
	
	private void showRedirectPageModal() {
		// Foi solicitado que não apresentasse a modal com a opção e sim que
		// sempre retornasse a tela de listagem
//		JSFUtils.executeJavaScript("dlgRedirectToMainPage.show()");
		sendRedirectToMainPage();
	}
	
	public void sendRedirectToMainPage() {
		try {
			JSFUtils.redirect("/pages/manifestacao/listarmanifestacoes.xhtml");
		} catch (Exception e) {
			MensagemFaceUtil.erro("Erro ao tentar redirecionar para a página de gerenciar manifestações", null);
		}
	}
	
	
	
	
	public String getTxtFiltroOrgaoDestino() {
		return txtFiltroOrgaoDestino;
	}

	public void setTxtFiltroOrgaoDestino(String txtFiltroOrgaoDestino) {
		this.txtFiltroOrgaoDestino = txtFiltroOrgaoDestino;
	}

	public String getTxtFiltroClassificacao() {
		return txtFiltroClassificacao;
	}

	public void setTxtFiltroClassificacao(String txtFiltroClassificacao) {
		this.txtFiltroClassificacao = txtFiltroClassificacao;
	}

	public String getTxtFiltroSubclassificacao() {
		return txtFiltroSubclassificacao;
	}

	public void setTxtFiltroSubclassificacao(String txtFiltroSubclassificacao) {
		this.txtFiltroSubclassificacao = txtFiltroSubclassificacao;
	}

	public String getTxtFiltroAreaSolucionadora() {
		return txtFiltroAreaSolucionadora;
	}

	public void setTxtFiltroAreaSolucionadora(String txtFiltroAreaSolucionadora) {
		this.txtFiltroAreaSolucionadora = txtFiltroAreaSolucionadora;
	}

	public List<TbUnidade> getListaUnidades() {
		return listaUnidades;
	}

	public void setListaUnidades(List<TbUnidade> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
	
	public Collection<TbEncaminhamentoPadronizado> getEncaminhamentosPadronizados() {
        return encaminhamentosPadronizados;
    }
    
    public Collection<TbTipoManifestacao> getTipos() {
        return tiposManifestacao;
    }

    public List<TbAnexo> getArquivosEncaminhamento() {
        return arquivosEncaminhamento;
    }

    public List<TbAnexo> getArquivosComunicacaoExterna() {
        return arquivosComunicacaoExterna;
    }

    public List<TbAnexo> getArquivosTramite() {
        return arquivosTramite;
    }
    
    public Integer getIdUnidadeEncaminhamento() {
        return idUnidadeEncaminhamento;
    }

    public void setIdUnidadeEncaminhamento(Integer idUnidadeEncaminhamento) {
        this.idUnidadeEncaminhamento = idUnidadeEncaminhamento;
    }

	/**
	 * @return the monitorar
	 */
	public boolean isMonitorar() {
		return monitorar;
	}

	/**
	 * @param monitorar the monitorar to set
	 */
	public void setMonitorar(boolean monitorar) {
		this.monitorar = monitorar;
	}

	/**
	 * @return the dataMonitoramento
	 */
	public Date getDataMonitoramento() {
		return dataMonitoramento;
	}

	/**
	 * @param dataMonitoramento the dataMonitoramento to set
	 */
	public void setDataMonitoramento(Date dataMonitoramento) {
		this.dataMonitoramento = dataMonitoramento;
	}

	/**
	 * @return the quantidadeDias
	 */
	public int getQuantidadeDias() {
		return quantidadeDias;
	}

	/**
	 * @param quantidadeDias the quantidadeDias to set
	 */
	public void setQuantidadeDias(int quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

}