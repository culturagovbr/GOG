package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import br.com.xti.ouvidoria.dao.FiltroPersonalizadoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.ConversorHelper;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.FileHelper;
import br.com.xti.ouvidoria.helper.FiltroHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbTramite;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.AtrasoManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.JSFUtils;
import br.com.xti.ouvidoria.util.ManifestacaoUtils;

import com.thoughtworks.xstream.XStream;

@SessionScoped
@SuppressWarnings("serial")
@Named("mBListarManifestacoes")
public class MBListarManifestacoes implements Serializable {
	
	@Inject
	private SecurityService securityService;
	
	@EJB
	private ManifestacaoDAO manifestacaoDAO;
	
	@EJB
	private FiltroPersonalizadoDAO filtroPersonalizadoDAO;
	
	
	public static final String URL_MANAGE_MANIFESTATION = "/pages/manifestacao/administrar.xhtml?num=%s&faces-redirect=true";
	
	// Nomes das Caixas Padrão
    private static final String CAIXA_ENTRADA = "Caixa de Entrada";
    private static final String SOLICITADA_INFORMACAO = "Solicitada Informação";
    private static final String SOLUCIONADA = "Solucionadas";
    private static final String EM_ANDAMENTO = "Em Andamento";
    private static final String RETORNADAS = "Retornadas";
    private static final String DEVOLVIDAS = "Devolvidas";
    private static final String COM_OUVIDORIA = "Com a Ouvidoria";
    
    private List<TbManifestacao> listaManifestacoes;
    private List<TbManifestacao> listaAntiga;
    
    private TbManifestacao selectedManifestation;
    private TbFiltroPersonalizado filtroEscolhido;
    private FiltroPersonalizado filtroAtual;
    
    private String nomeFiltro = CAIXA_ENTRADA;
    private String textoBuscaManifestacao;
    private String textoBuscaEncaminhamento;
    
    
    @PostConstruct
    public void init() {
    	if(securityService.getUser() != null) {
    		getCaixaEntrada(null);
    	} else {
    		Object manifestation = JSFUtils.getSessionAttribute("manifestation");
    		if(ValidacaoHelper.isNotEmpty(manifestation) && manifestation instanceof TbManifestacao) {
    			List<TbManifestacao> list = new ArrayList<TbManifestacao>();
    			list.add((TbManifestacao) manifestation);
    			setListaManifestacoes(list);
    		}
    	}
    }
    
    /**
     * Recarrega as manifestações que estavam abertas anteriormente
     */
    public void recarregarManifestacoes() {
    	if(filtroAtual != null) {
    		List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
    		switch (nomeFiltro) {
				case CAIXA_ENTRADA:
					ajustarCaixaEntrada(list);
					break;
				case DEVOLVIDAS:
					ajustarDevolvidas(list);
					break;
				case EM_ANDAMENTO:
					ajustarEmAndamento(list);
					break;
				case RETORNADAS:
					ajustarRetornadas(list);
					break;
			}
    		setListaManifestacoes(list);
    	} else {
    		getCaixaEntrada(null);
    	}
    }
    
    public void getSolicitadaInformacao(ActionEvent actionEvent) {
    	filtroEscolhido = new TbFiltroPersonalizado();
        
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("and");
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId());
        
        filtroAtual = filtro;
        nomeFiltro = SOLICITADA_INFORMACAO;
        
        List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        setListaManifestacoes(list);
    }
    
    public void getCaixaEntrada(ActionEvent actionEvent) {
        filtroEscolhido = new TbFiltroPersonalizado();
        nomeFiltro = CAIXA_ENTRADA;
        filtroAtual = manifestacaoDAO.getFiltroCaixaEntrada(securityService.getUser());
        List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        
        ajustarCaixaEntrada(list);
        setListaManifestacoes(list);
    }
    
    private void ajustarCaixaEntrada(List<TbManifestacao> list) {
    	switch (securityService.getUserProfile()) {
			case INTERLOCUTOR: // remover as manifestações que ele já encaminhou para um operador.
	        	TbUnidade unidadeInterlocutor = securityService.getUser().getIdUnidade();
	        	
	        	ListIterator<TbManifestacao> listIterator = list.listIterator();
	        	for( ; listIterator.hasNext() ; ) {
	        		enc: for (TbEncaminhamento e : listIterator.next().getTbEncaminhamentoCollection()) {
	        			if(e.getIdUnidadeEnviou().equals(unidadeInterlocutor)) {
	        				listIterator.remove();
	        				break enc;
	        			} else if(e.getIdUnidadeRecebeu().equals(unidadeInterlocutor)) {
							// Verifica o último trâmite para determinar se deve estar ou não na caixa de entrada
							List<TbTramite> listTramites = new ArrayList<TbTramite>(e.getTbTramiteCollection());
							TbTramite ultimoTramite = listTramites.get(listTramites.size() -1);
							if(!UnidadeEnum.OUVIDORIA.getId().equals(ultimoTramite.getIdUsuarioEmissor().getIdUnidade().getIdUnidade())) {
								if(!securityService.getUser().getIdUnidade().equals(ultimoTramite.getIdUnidadeEnvio())) {
									listIterator.remove();
									break enc;
								}
							}
							
							for (TbTramite t : listTramites) {
								if(t.getIdUsuarioReceptor() != null 
										&& FuncaoUsuarioEnum.OPERADOR.getId().equals(t.getIdUsuarioReceptor().getTpFuncao())
										&& BooleanEnum.NAO.getId().equals(t.getStRetornada())) {
									listIterator.remove();
									break enc;
								}
							}
						}
					}
	        	}
		        break;
			default:
				break;
		}
    }

    public void getEmAndamento(ActionEvent actionEvent) {
        filtroEscolhido = new TbFiltroPersonalizado();
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("and");
        // -- ADMIN ou OUVIDOR
        if(securityService.isAdministrador() || securityService.isOuvidor()) {
	        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
	        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
//	        filtro.addManIdStatus(StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId());
        } else if(securityService.isInterlocutor()) {
        	filtro = FiltroHelper.getFiltrosPadrao(securityService.getUser());
        }

        nomeFiltro = EM_ANDAMENTO;
        filtroAtual = filtro;
        List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        if(securityService.isInterlocutor()) {
        	FiltroPersonalizado filtro2 = new FiltroPersonalizado();
        	filtro2.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        	filtro2.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        	filtro2.addEncIdUnidadeEnviou(securityService.getUser().getIdUnidade().getIdUnidade());
        	list.addAll(manifestacaoDAO.getManifestacoes(filtro2));
        }
        
        ajustarEmAndamento(list);
        setListaManifestacoes(list);
    }
    
    private void ajustarEmAndamento(List<TbManifestacao> list) {
    	switch (securityService.getUserProfile()) {
			case INTERLOCUTOR: // remover as manifestações que não foram encaminhadas para um operador.
	        	TbUnidade unidadeInterlocutor = securityService.getUser().getIdUnidade();
	        	
	        	ListIterator<TbManifestacao> listIterator = list.listIterator();
	        	for( ; listIterator.hasNext() ; ) {
	        		boolean deletarEnviou = Boolean.FALSE;
	        		boolean deletarRecebeu = Boolean.FALSE;
	        		enc: for (TbEncaminhamento e : listIterator.next().getTbEncaminhamentoCollection()) {
        				if(e.getIdUnidadeEnviou().equals(unidadeInterlocutor)) {
        					if(StatusEncaminhamentoEnum.RETORNADA.getId().equals(e.getStEncaminhamento())) {
        						deletarEnviou = Boolean.TRUE;
        					} else {
        						break enc;
        					}
        				} else if(e.getIdUnidadeRecebeu().equals(unidadeInterlocutor)) {
        					for (TbTramite t : e.getTbTramiteCollection()) {
        						if(t.getIdUsuarioReceptor() != null 
        								&& FuncaoUsuarioEnum.OPERADOR.getId().equals(t.getIdUsuarioReceptor().getTpFuncao())
        								&& BooleanEnum.NAO.getId().equals(t.getStRetornada())) {
        							break enc;
        						} else {
        							deletarRecebeu = Boolean.TRUE;
        						}
        					}
        				}
        				
        				if(deletarEnviou && deletarRecebeu) {
        					listIterator.remove();
        				}
					}
	        	}
		        break;
			default:
				break;
		}
    }
    
    public void getRetornadas(ActionEvent actionEvent) {
        filtroEscolhido = new TbFiltroPersonalizado();
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("and");
        
        if(securityService.isAdministrador() || securityService.isOuvidor()) {
        	filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        	filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        } else if(securityService.isInterlocutor()) {
        	filtro = FiltroHelper.getFiltrosPadrao(securityService.getUser());
        }
        
        nomeFiltro = RETORNADAS;
        filtroAtual = filtro;
        List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        
        ajustarRetornadas(list);
        setListaManifestacoes(list);
    }
    
    private void ajustarRetornadas(List<TbManifestacao> list) {
    	switch (securityService.getUserProfile()) {
			case INTERLOCUTOR: // remover as manifestações que não foram retornadas pelo operador.
	        	TbUnidade unidadeInterlocutor = securityService.getUser().getIdUnidade();
	        	
	        	ListIterator<TbManifestacao> listIterator = list.listIterator();
	        	for( ; listIterator.hasNext() ; ) {
	        		enc: for (TbEncaminhamento e : listIterator.next().getTbEncaminhamentoCollection()) {
						if(e.getIdUnidadeRecebeu().equals(unidadeInterlocutor)) {
							boolean deletar = Boolean.TRUE;
							// Recupera o último trâmite da ouvidria para a área
							Date dtUltimoTramiteDaOuvidoria = null;
							ListIterator<TbTramite> listTramites = new ArrayList<TbTramite>(e.getTbTramiteCollection()).listIterator(e.getTbTramiteCollection().size());
							// Percorre a lista em ordem inversa para pegar o último trâmite da Ouvidoria
							for ( ; listTramites.hasPrevious() ; ) {
								TbTramite t = listTramites.previous();
								if(t.getIdUsuarioEmissor() != null && UnidadeEnum.OUVIDORIA.getId().equals(t.getIdUsuarioEmissor().getIdUnidade().getIdUnidade())) {
									dtUltimoTramiteDaOuvidoria = t.getDtTramite();
									break;
								}
							}
							
							if(dtUltimoTramiteDaOuvidoria == null) {
								dtUltimoTramiteDaOuvidoria = e.getDtEnvioTramite();
							}
							
							for (TbTramite t : e.getTbTramiteCollection()) {
								if(!(t.getIdUsuarioReceptor() != null && FuncaoUsuarioEnum.OPERADOR.getId().equals(t.getIdUsuarioReceptor().getTpFuncao()))) {
									continue;
								}
								
								if(BooleanEnum.NAO.getId().equals(t.getStRetornada()) && t.getDtTramite().compareTo(dtUltimoTramiteDaOuvidoria) >= 0) {
//								if(BooleanEnum.NAO.getId().equals(t.getStRetornada())) { //TODO
									listIterator.remove();
									break enc;
								} else {
									deletar = Boolean.FALSE;
								}
							}
							if(deletar)
								listIterator.remove();
						}
					}
	        	}
		        break;
			default:
				break;
		}
    }
    
    public void getSolucionadas(ActionEvent actionEvent) {
        filtroEscolhido = new TbFiltroPersonalizado();
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        switch (securityService.getUserProfile()) {
        	case OPERADOR:
        		filtro.addEncIdOperador(securityService.getUser().getIdUsuario());
	        case INTERLOCUTOR:
	            filtro.addEncIdUnidadeRecebeu(securityService.getUser().getIdUnidade().getIdUnidade());
			case ADMINISTRADOR:
			case OUVIDOR:
				filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
				break;
			default: break;
		}

        nomeFiltro = SOLUCIONADA;
        filtroAtual = filtro;
        setListaManifestacoes(manifestacaoDAO.getManifestacoes(filtroAtual));
    }
    
    public void getDevolvidas(ActionEvent actionEvent) {
    	filtroEscolhido = new TbFiltroPersonalizado();
    	FiltroPersonalizado filtro = new FiltroPersonalizado();
    	filtro.setMetodoBusca("and");
    	filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
    	filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
    	filtro.addEncIdUnidadeRecebeu(securityService.getUser().getIdUnidade().getIdUnidade());
        filtro.addEncIdOperador(securityService.getUser().getIdUsuario());
        filtro.setEncTramiteNaoRetornado(false);
    	
    	nomeFiltro = DEVOLVIDAS;
    	filtroAtual = filtro;
    	List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        
    	ajustarDevolvidas(list);
        setListaManifestacoes(list);
    }
    
    private void ajustarDevolvidas(List<TbManifestacao> list) {
    	switch (securityService.getUserProfile()) {
			case OPERADOR: // remover as manifestações que foram retornadas mas voltou para o operador
	        	TbUnidade unidadeOperador = securityService.getUser().getIdUnidade();
	        	
	        	ListIterator<TbManifestacao> listIterator = list.listIterator();
	        	for( ; listIterator.hasNext() ; ) {
	        		enc: for (TbEncaminhamento e : listIterator.next().getTbEncaminhamentoCollection()) {
						if(e.getIdUnidadeRecebeu().equals(unidadeOperador)) {
							for (TbTramite t : e.getTbTramiteCollection()) {
								if(t.getIdUsuarioReceptor() != null 
										&& t.getIdUsuarioReceptor().equals(securityService.getUser())
										&& FuncaoUsuarioEnum.OPERADOR.getId().equals(t.getIdUsuarioReceptor().getTpFuncao())
										&& BooleanEnum.NAO.getId().equals(t.getStRetornada())) {
									listIterator.remove();
									break enc;
								}
							}
						}
					}
	        	}
		        break;
			default:
				break;
		}
    }
    
    public void getComAOuvidoria(ActionEvent actionEvent) {
    	filtroEscolhido = new TbFiltroPersonalizado();
    	FiltroPersonalizado filtro = new FiltroPersonalizado();
    	filtro.setMetodoBusca("and");
    	filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        filtro.addEncIdUnidadeRecebeu(securityService.getUser().getIdUnidade().getIdUnidade());
    	
    	nomeFiltro = COM_OUVIDORIA;
    	filtroAtual = filtro;
    	List<TbManifestacao> list = manifestacaoDAO.getManifestacoes(filtroAtual);
        
        setListaManifestacoes(list);
    }
    
    public void getFiltroPersonalizado(AjaxBehaviorEvent ajaxBehaviorEvent) {
    	FiltroPersonalizado filtroPadrao = new FiltroPersonalizado();
		
		FuncaoUsuarioEnum funcao = securityService.getUserProfile();
		if(!(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR)) {
			filtroPadrao.setMetodoBusca("and");
			filtroPadrao.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
			filtroPadrao.addEncIdUnidadeRecebeu(securityService.getUser().getIdUnidade().getIdUnidade());
		}
    	
        filtroEscolhido = filtroPersonalizadoDAO.find(filtroEscolhido.getIdFiltroPersonalizado());

        XStream xs = new XStream();
        filtroAtual = (FiltroPersonalizado) xs.fromXML(filtroEscolhido.getDsParticao());

        nomeFiltro = filtroEscolhido.getNmFiltroPersonalizado();
        setListaManifestacoes(manifestacaoDAO.getManifestacoes(filtroPadrao, filtroAtual));
    }
    
    public void buscarManifestacao(ActionEvent actionEvent) {
    	if (!textoBuscaManifestacao.isEmpty()) {
    		FiltroPersonalizado filtroPadrao = new FiltroPersonalizado();
    		
    		FuncaoUsuarioEnum funcao = securityService.getUserProfile();
    		if(!(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR)) {
    			filtroPadrao = FiltroHelper.getFiltrosPadrao(securityService.getUser());
    		}
    		
    		filtroAtual = ConversorHelper.converterTextoEmFiltroManifestacao(textoBuscaManifestacao);
    		setListaManifestacoes(manifestacaoDAO.getManifestacoes(filtroPadrao, filtroAtual));
    		nomeFiltro = "Busca em manifestação por: " + textoBuscaManifestacao;
    		textoBuscaManifestacao = "";
    		textoBuscaEncaminhamento = "";
    	} else {
    		if(securityService.isManifestante()) {
    			getCaixaEntrada(null);
    		}
    	}
    }
    
    public void buscarEncaminhamento(ActionEvent actionEvent) {
        if (!textoBuscaEncaminhamento.isEmpty()) {
        	FiltroPersonalizado filtroPadrao = new FiltroPersonalizado();
        	
        	FuncaoUsuarioEnum funcao = securityService.getUserProfile();
        	if(!(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR))
        		filtroPadrao = FiltroHelper.getFiltrosPadrao(securityService.getUser());
        	
        	filtroAtual = ConversorHelper.converterTextoEmFiltroEncaminhamento(textoBuscaEncaminhamento);
            setListaManifestacoes(manifestacaoDAO.getManifestacoes(filtroPadrao, filtroAtual));
            nomeFiltro = "Busca em encaminhamentos por: " + textoBuscaEncaminhamento;
            textoBuscaManifestacao = "";
            textoBuscaEncaminhamento = "";
        }
    }
    
    public String getManifestationUrl(TbManifestacao manifestation) {
    	return String.format(URL_MANAGE_MANIFESTATION, manifestation.getNrManifestacao());
    }
    
    public void hideManifestation() {
        try {
            selectedManifestation.setStStatusOcultacao(BooleanEnum.SIM.getId());
            manifestacaoDAO.edit(selectedManifestation);
            MensagemFaceUtil.info("Manifestação foi ocultada", "");
            JSFUtils.closeDialog("dlgOcultarManifestacao");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Ocorreu um erro ao ocultar manifestação", "");
        }
    }
    
    public void showManifestation() {
        try {
        	selectedManifestation.setStStatusOcultacao(BooleanEnum.NAO.getId());
        	selectedManifestation.setDsMotivoOcultacao("");
        	manifestacaoDAO.edit(selectedManifestation);
            MensagemFaceUtil.info("Manifestação está visível novamente", "");
            JSFUtils.closeDialog("dlgDesocultarManifestacao");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Ocorreu um erro ao desocultar manifestação", "");
        }

    }
    
    public String getUnidadesEncaminhadas(TbManifestacao manifestacao) {
        StringBuilder unidades = new StringBuilder();
        Collection<TbEncaminhamento> listaEncaminhamentos = manifestacao.getTbEncaminhamentoCollection();
        if (listaEncaminhamentos != null) {
            for (TbEncaminhamento encaminhamento : listaEncaminhamentos) {
                if (unidades.length() == 0) {
                    unidades.append(encaminhamento.getIdUnidadeRecebeu().getSgUnidade());
                } else {
                    unidades.append(", ").append(encaminhamento.getIdUnidadeRecebeu().getSgUnidade());
                }
            }
        }
        return unidades.toString();
    }
    
    public Date getPrazoAtendimento(TbManifestacao manifestacao) {
    	return ManifestacaoUtils.getDataLimiteAtendimentoUnidadeUsuarioLogado(manifestacao);
    }
    
    public void filtraAtrasoEncaminhamento() {
        filtroAtraso(AtrasoManifestacaoEnum.ATRASO_ENCAMINHAMENTO);
    }

    public void filtraAtrasoRespostaOuvidoria() {
        filtroAtraso(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA);
    }

    public void filtraAtrasoRespostaManifestante() {
        filtroAtraso(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE);
    }

    public void filtraSemAtraso() {
        filtroAtraso(AtrasoManifestacaoEnum.SEM_ATRASO);
    }
    
    public void downloadReport() throws Exception {
    	ManifestacaoUtils.downloadReport(FileHelper.normalizeName(nomeFiltro), listaManifestacoes);
    }

    public void filtraOcultas() {
        listaManifestacoes = listaAntiga;
        ArrayList<TbManifestacao> filtradas = new ArrayList<>();
        for (TbManifestacao tbManifestacao : listaManifestacoes) {
            if (tbManifestacao.getStStatusOcultacao() != null) {
                if (BooleanEnum.SIM.getId().equals(tbManifestacao.getStStatusOcultacao())) {
                    filtradas.add(tbManifestacao);
                }
            }
        }
        listaManifestacoes = filtradas;
    }
    
    public boolean showManifestationFilter() {
    	boolean showManifestationFilter = Boolean.TRUE;
    	
    	if(securityService.isManifestante()) {
    		if(!securityService.isManifestanteLogged()) {
    			showManifestationFilter = Boolean.FALSE;
    		}
    	}
    	
    	return showManifestationFilter;
    }
    
    public boolean showSecretData(TbManifestacao manifestation) {
    	boolean showSecretData = Boolean.FALSE;
    	if(securityService.hasRole(FuncionalidadeEnum.VISUALIZAR_DADOS_SIGILOSOS.toString())) {
    		showSecretData = Boolean.TRUE;
    	} else {
	    	String secrecy = manifestation.getSiSigilo();
	        if (BooleanEnum.SIM.getId().equals(secrecy)) {
	            showSecretData = securityService.isOuvidor() || securityService.isAdministrador();
	        }
    	}
        return showSecretData;
    }

    private void filtroAtraso(AtrasoManifestacaoEnum tipoAtraso) {
        listaManifestacoes = listaAntiga;
        ArrayList<TbManifestacao> filtradas = new ArrayList<>();
        for (TbManifestacao tbManifestacao : listaManifestacoes) {
            AtrasoManifestacaoEnum tipo = verificaAtraso(tbManifestacao);
            if (tipo.equals(tipoAtraso)) {
                filtradas.add(tbManifestacao);
            }
        }
        listaManifestacoes = filtradas;
    }
    
    public int diasAtrasoAoManifestante(TbManifestacao manifestation) {
		int diasEmAtraso = 0;
		if (manifestation != null
				&& !StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestation.getStStatusManifestacao())) {
    		if(securityService.isInterlocutor() || securityService.isOperador()) {
    			Date dateLimitToAnswer = getPrazoAtendimento(manifestation);
    			DateTimeZone BRAZIL = DateTimeZone.forID("America/Sao_Paulo");
    			DateTime start = new LocalDate(dateLimitToAnswer.getTime(), BRAZIL).toDateTimeAtStartOfDay();
    			DateTime end = new LocalDate(new Date().getTime(), BRAZIL).toDateTimeAtStartOfDay();
    			Days days = Days.daysBetween(start, end);
    			
    			if(days.isGreaterThan(null)) {
    				diasEmAtraso = days.getDays();
    			}
    		} else {
    			Integer prazoEncaminhamento = manifestation.getIdTipoManifestacao().getPrazoEntrada();
    			Integer prazoRespostaAOuvidoria = manifestation.getIdTipoManifestacao().getPrazoAreaSolucionadora();
    			Integer prazoRespostaAoManifestante = manifestation.getIdTipoManifestacao().getPrazoRespostaCidadao();
    			int diasParaResponderManifestante = prazoEncaminhamento + prazoRespostaAoManifestante + prazoRespostaAOuvidoria;
    			
    			Date dataManifestacao = manifestation.getDtCadastro();
    			Date dataAtual = new Date();
    			int diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
    			
    			if (diasTranscorridos > ++diasParaResponderManifestante) {
    				diasEmAtraso = diasTranscorridos - diasParaResponderManifestante;
    			}
    		}
    		
    	}
    	return diasEmAtraso;
    }
    
    public String verificaAtrasoStyleClass(TbManifestacao manifestacao) {
    	AtrasoManifestacaoEnum tipoAtraso = verificaAtraso(manifestacao);
    	String styleClass = tipoAtraso.getStyleClass();
    	// As linhas abaixo são para que as linhas das manifestações em atraso fiquem piscando 
//		if ((securityService.isInterlocutor() || securityService.isOperador()) && tipoAtraso != AtrasoManifestacaoEnum.SEM_ATRASO) {
//    		styleClass = styleClass.concat(" " + styleClass + "2");
//    	}
		return styleClass;
    }
    
    public AtrasoManifestacaoEnum verificaAtraso(TbManifestacao manifestacao) {
    	int prazoEncaminhamento = manifestacao.getIdTipoManifestacao().getPrazoEntrada();
    	int prazoRespostaAOuvidoria = manifestacao.getIdTipoManifestacao().getPrazoAreaSolucionadora();
    	int prazoRespostaAoManifestante = manifestacao.getIdTipoManifestacao().getPrazoRespostaCidadao();
    	
        int diasTranscorridos = 0;
        Date dataManifestacao = manifestacao.getDtCadastro();
        Date dataAtual = new Date();

        if (securityService.isOuvidor() || securityService.isAdministrador()) {

            if (!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {
            	// VERIFICA ATRASO - ENCAMINHAMENTO
            	diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
            	Collection<TbEncaminhamento> listaEncaminhamentos = manifestacao.getTbEncaminhamentoCollection();
            	if (listaEncaminhamentos != null) {
            		if (prazoEncaminhamento > 0 && diasTranscorridos > prazoEncaminhamento && listaEncaminhamentos.isEmpty()) {
            			return AtrasoManifestacaoEnum.ATRASO_ENCAMINHAMENTO;
            		}
            	}

                // VERIFICA ATRASO - RESPOSTA À OUVIDORIA
            	boolean todasAreasRetornaram = Boolean.TRUE;
                if (listaEncaminhamentos != null) {
                    for (TbEncaminhamento tbEncaminhamento : listaEncaminhamentos) {
                        Date dataEncaminhamento = tbEncaminhamento.getDtEnvioTramite();
                        String statusTramite = tbEncaminhamento.getStEncaminhamento();
                        if(StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(statusTramite)) {
                        	todasAreasRetornaram = Boolean.FALSE;
                        }

                        diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataEncaminhamento);

                        if (prazoRespostaAOuvidoria > 0 && diasTranscorridos > prazoRespostaAOuvidoria && StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(statusTramite)) {
                            return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA;
                        }
                    }
                }
                
            	// VERIFICA ATRASO - RESPOSTA AO MANIFESTANTE
                if(todasAreasRetornaram) {
                	diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
                	Collection<TbComunicacaoExterna> listaComunicacao = manifestacao.getTbComunicacaoExternaCollection();
                	
                	if (ValidacaoHelper.isEmpty(listaComunicacao)) {
                		if (prazoRespostaAoManifestante > 0 && diasTranscorridos > prazoRespostaAoManifestante) {
                			return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE;
                		}
                	} else {
	        			//pega a ultima comunicacao e verifica se ela possui resposta final
                		List<TbComunicacaoExterna> comunicacoes = new ArrayList<TbComunicacaoExterna>(listaComunicacao);
	        			TbComunicacaoExterna com = comunicacoes.listIterator(comunicacoes.size() - 1).next();
	        			if (prazoRespostaAoManifestante > 0 && diasTranscorridos > prazoRespostaAoManifestante && !"1".equals(com.getStRespostaFinal())) {
	        				return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE;
	        			}
                	}
                }
            }
            
            return AtrasoManifestacaoEnum.SEM_ATRASO;
        } else if (securityService.isInterlocutor() || securityService.isOperador()) {
            // VERIFICA ATRASO - RESPOSTA À OUVIDORIA
            if (!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao())) {

                if (manifestacao.getTbEncaminhamentoCollection() != null) {
                	TbEncaminhamento tbEncaminhamento = ManifestacaoUtils.getEncaminhamentoDoUsuarioLogado(manifestacao);

                    Date dataEncaminhamento = tbEncaminhamento.getDtEnvioTramite();
                    String statusTramite = tbEncaminhamento.getStEncaminhamento();

                    diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataEncaminhamento);

                    if (prazoRespostaAOuvidoria > 0 && diasTranscorridos > prazoRespostaAOuvidoria && StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(statusTramite)) {
                        return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA;
                    }
                }
            }
        }
            
        return AtrasoManifestacaoEnum.SEM_ATRASO;
    }
    
    public String getNomeOperadoresComManifestacao(TbManifestacao man) {
    	StringBuilder nomeOperadores = new StringBuilder();
    	TbUnidade unidadeInterlocutor = securityService.getUser().getIdUnidade();
    	Set<TbUsuario> operadores = new TreeSet<>();
    	
		for (TbEncaminhamento e : man.getTbEncaminhamentoCollection()) {
			if(e.getIdUnidadeRecebeu().equals(unidadeInterlocutor)) {
				for (TbTramite t : e.getTbTramiteCollection()) {
					if(t.getIdUsuarioReceptor() != null && FuncaoUsuarioEnum.OPERADOR.getId().equals(t.getIdUsuarioReceptor().getTpFuncao())) {
						operadores.add(t.getIdUsuarioReceptor());
					}
				}
			}
		}
		
		for (TbUsuario u : operadores) {
			if(nomeOperadores.length() == 0) {
				nomeOperadores.append(u.getNmUsuario());
			} else {
				nomeOperadores.append(", ").append(u.getNmUsuario());
			}
		}
		
		return nomeOperadores.toString();
    }
    
    public String getBotaoCaixaCssClass(String textoBotao) {
    	if(nomeFiltro.equals(textoBotao))
    		return "botaoCaixaSelecionado";
    	
    	return "";
    }

    public void salvar(TbManifestacao manifestacao) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().getSessionMap().put("manifestacaoAtual", manifestacao);
    }
    
    public boolean isNotOperador() {
        return !securityService.isOperador();
    }
    
    public boolean isAdministradorOuOuvidor() {
    	return securityService.isAdministrador() || securityService.isOuvidor();
    }
    
    public boolean isInterlocutorOuOperador() {
    	return securityService.isInterlocutor() || securityService.isOperador();
    }

    // GETTERS e SETTERS
    public List<TbManifestacao> getListaManifestacoes() {
        return listaManifestacoes;
    }

    public void setListaManifestacoes(List<TbManifestacao> listaManifestacoes) {
        this.listaManifestacoes = listaManifestacoes;
        this.listaAntiga = listaManifestacoes;
    }

    public String getNomeFiltro() {
        Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        String nomeFiltroSession;
        if ((nomeFiltroSession = (String) session.get("filtroNome")) != null) {
            nomeFiltro = nomeFiltroSession;
            session.remove("filtroNome");
        }
        return nomeFiltro;
    }

    public void setNomeFiltro(String nomeFiltro) {
        this.nomeFiltro = nomeFiltro;
    }

    public TbFiltroPersonalizado getFiltroEscolhido() {
        return filtroEscolhido;
    }

    public void setFiltroEscolhido(TbFiltroPersonalizado filtroEscolhido) {
        this.filtroEscolhido = filtroEscolhido;
    }

    public String getTextoBuscaManifestacao() {
        return textoBuscaManifestacao;
    }

    public void setTextoBuscaManifestacao(String textoBuscaManifestacao) {
        this.textoBuscaManifestacao = textoBuscaManifestacao;
    }

    public String getTextoBuscaEncaminhamento() {
        return textoBuscaEncaminhamento;
    }

    public void setTextoBuscaEncaminhamento(String textoBuscaEncaminhamento) {
        this.textoBuscaEncaminhamento = textoBuscaEncaminhamento;
    }

	public TbManifestacao getSelectedManifestation() {
		return selectedManifestation;
	}

	public void setSelectedManifestation(TbManifestacao selectedManifestation) {
		this.selectedManifestation = selectedManifestation;
	}
    
}