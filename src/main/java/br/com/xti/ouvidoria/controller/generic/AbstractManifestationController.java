package br.com.xti.ouvidoria.controller.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.xti.ouvidoria.dao.AnexoDAO;
import br.com.xti.ouvidoria.dao.ComunicacaoExternaDAO;
import br.com.xti.ouvidoria.dao.EncaminhamentoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.PrioridadeDAO;
import br.com.xti.ouvidoria.dao.TramiteDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.FiltroHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.manifestacao.view.ManifestacaoTabView;
import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbTramite;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.JSFUtils;

public abstract class AbstractManifestationController {

	@Inject
	protected SecurityService securityService;
	
	@EJB
	protected ManifestacaoDAO dao;
	
	@EJB
	protected AnexoDAO anexoDAO;

	@EJB
	protected ComunicacaoExternaDAO comunicacaoExternaDAO;

	@EJB
	protected EncaminhamentoDAO encaminhamentoDAO;

	@EJB
	protected TramiteDAO tramiteDAO;

	@EJB
	protected UnidadeDAO unidadeDAO;
	
	@EJB
	protected PrioridadeDAO prioridadeDAO;
	
	/** Manifestação que está sendo gerenciada */
	protected TbManifestacao manifestacao;
	protected List<ManifestacaoTabView> tabs;
	/** Lista de Unidades que o usuário pode realizar um trâmite/encaminhamento */
	protected List<TbUnidade> listaUnidadesTramite;
	/** Unidades que podem ser área solucionadora (Já tem encaminhamento) */
	protected List<TbUnidade> unidades;

	protected Integer idUnidadeTramite;
	protected Integer idUsuarioTramite;
	
	// Carregar Informações da Manifestação (GET Request)
	// Suporte para multi abas
	protected Integer manifestationId;
	protected Integer manifestationNumber;
	
	// Abas Trâmites
	protected ManifestacaoTabView tabViewSelecionada;
	protected Integer tabViewAtiva = 0;
	
	public void loadManifestation() {
		boolean redirectToHomePage = false;
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(!context.isValidationFailed()) {
			if(ValidacaoHelper.isEmpty(manifestationNumber)) {
				redirectToHomePage = true;
			} else {
				FiltroPersonalizado filtroPadrao = new FiltroPersonalizado();
	    		FuncaoUsuarioEnum funcao = securityService.getUserProfile();
	    		if(!(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR)) {
	    			filtroPadrao = FiltroHelper.getFiltrosPadrao(securityService.getUser());
	    		}
				
	    		FiltroPersonalizado filtroNumeroManifestacao = new FiltroPersonalizado();
	    		filtroNumeroManifestacao.setManIdRangeDe(manifestationNumber);
	    		filtroNumeroManifestacao.setManIdRangeAte(manifestationNumber);
	    		
	    		TbManifestacao oldManifestation = getOldManifestation();
	    		List<TbManifestacao> lista = dao.getManifestacoes(filtroPadrao, filtroNumeroManifestacao);
	    		if(ValidacaoHelper.isNotEmpty(lista)) {
	    			manifestacao = lista.get(0);
	    			if(ValidacaoHelper.isEmpty(oldManifestation)) {
	    				JSFUtils.setSessionAttribute("manifestation", manifestacao);
	    			}
	    		}
	    		
				if(manifestacao == null) {
					redirectToHomePage = true;
				} else {
					// Se for usuário não logado verifica se informou o id da manifestação
					if(securityService.getUser() == null && !manifestacao.getIdManifestacao().equals(manifestationId)) {
						if(!manifestacao.equals(oldManifestation)) {
							redirectToHomePage = true;
						}
					}
				}
			}
		}
		
		if(redirectToHomePage) {
			redirectToHomePage();
		}
	}

	private TbManifestacao getOldManifestation() {
		TbManifestacao oldManifestation = null;
		Object manifestation = JSFUtils.getSessionAttribute("manifestation");
		if(ValidacaoHelper.isNotEmpty(manifestation) && manifestation instanceof TbManifestacao) {
			oldManifestation = (TbManifestacao) manifestation;
		}
		return oldManifestation;
	}
	
	private void redirectToHomePage() {
		try {
			JSFUtils.redirect("/login");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mountTabs() {
        int index = 0;
        setTabs(new ArrayList<ManifestacaoTabView>());
        // Monta a aba "Ouvidoria X Manifestante"
        ManifestacaoTabView comExt = new ManifestacaoTabView();
        comExt.setIndex(index);
        comExt.setTitulo("Ouvidoria x Manifestante");
        Collection<TbComunicacaoExterna> comunicacoes = comunicacaoExternaDAO.getPorManifestacao(manifestacao);
        comExt.setConteudo(comunicacoes);
        // Se o usuário for INTERLOCUTOR ou OPERADOR só visualiza as comunicações externas públicas
        if (securityService.isInterlocutor() || securityService.isOperador()) {
        	Iterator<TbComunicacaoExterna> comunicacoesIterator = comunicacoes.iterator();
        	for( ; comunicacoesIterator.hasNext() ; ) {
        		TbComunicacaoExterna comunicacao = comunicacoesIterator.next();
        		if(!comunicacao.isComunicacaoPublica()) {
        			comunicacoesIterator.remove();
        		}
        	}
        	
        	if(comunicacoes.size() > 0) {
        		getTabs().add(comExt);
        	}
        } else {
        	getTabs().add(comExt);
        }
        
        
        // Monta as abas das Unidades
        if(!securityService.isManifestante()) {
        	TbUnidade unidadeUsuarioLogado = securityService.getUser().getIdUnidade();
        	for (TbEncaminhamento enc : encaminhamentoDAO.getPorManifestacao(manifestacao)) {
				// Se o usuário for Interlocutor ou Operador só irá mostrar os
				// trâmites enviados ou recebidos pela sua área. A menos que
				// exista um trâmite de outra área marcado como público
        		if((securityService.isInterlocutor() || securityService.isOperador())	// Interlocutor ou Operador
					&& !(unidadeUsuarioLogado.equals(enc.getIdUnidadeEnviou())			// Unidade do usuário NÃO enviou...
					|| unidadeUsuarioLogado.equals(enc.getIdUnidadeRecebeu()))			// ...ou recebeu o encaminhamento
					&& !enc.temTramitePublico()) {										// Encaminhamento NÃO tem trâmite público
						continue;
        		}
        		
				TbUnidade departmentSent = enc.getIdUnidadeEnviou();
				TbUnidade departmentReceived = enc.getIdUnidadeRecebeu();
				String tabTitle = String.format("%s x %s", departmentSent.getNomeFormatado(), departmentReceived.getNomeFormatado());
				
				ManifestacaoTabView tabView = new ManifestacaoTabView();
				tabView.setIndex(++index);
				tabView.setTitulo(tabTitle);
				tabView.setConteudo(tramiteDAO.getPorEncaminhamento(enc));
				tabView.setEncaminhamento(enc);
				tabView.setUnidadeEnviou(departmentSent);
				tabView.setUnidadeRecebeu(departmentReceived);
				getTabs().add(tabView);
        	}
        }
    }
	
	public boolean mostrarTramite(Object tramiteObj) {
		boolean mostrarTramite = false;
		
		if(tramiteObj instanceof TbTramite) {
			if(securityService.isInterlocutor() || securityService.isOperador()) {
				TbTramite tramite = (TbTramite) tramiteObj;
				TbEncaminhamento enc = tramite.getIdEncaminhamento();
				TbUnidade unidade = securityService.getUser().getIdUnidade();
				
				if(unidade.equals(enc.getIdUnidadeEnviou()) || unidade.equals(enc.getIdUnidadeRecebeu())) {
					mostrarTramite = true;
				} else {
					mostrarTramite = tramite.isTramitePublico();
				}
			} else {
				mostrarTramite = true;
			}
		}
	
		return mostrarTramite;
	}
	
	public String mostrarPessoasEnvolvidasNaComunicacaoExterna(Object comunicacao) {
		StringBuilder sb = new StringBuilder(" - ");
		TbComunicacaoExterna ce = null;
		if (comunicacao instanceof TbComunicacaoExterna) {
			ce = (TbComunicacaoExterna) comunicacao;
			TbUsuario u = ce.getIdUsuario();
			if (u != null) {
				if (u.getIdUnidade() != null && UnidadeEnum.OUVIDORIA.getId().equals(u.getIdUnidade().getIdUnidade())) {
					sb.append("Ouvidoria disse:");
				} else {
					sb.append(u.getNmUsuario()).append(" disse:");
				}
			} else {
				String nomeManifestante = ValidacaoHelper.isNotEmpty(manifestacao.getNmPessoa()) 
						? manifestacao.getNmPessoa() : "Anônimo";
				sb.append(nomeManifestante).append(" disse:");
			}
		}
		return sb.toString();
	}

	public String mostrarAreaDeQuemRealizouComunicacao(Object comunicacao) {
		StringBuilder sb = new StringBuilder();
		TbComunicacaoExterna ce = null;
		if (comunicacao instanceof TbComunicacaoExterna) {
			ce = (TbComunicacaoExterna) comunicacao;
			TbUsuario u = ce.getIdUsuario();

			if (u != null && FuncaoUsuarioEnum.ADMINISTRADOR.getId().equals(u.getTpFuncao())) {
				sb.append("administrador");
			} else if (u != null && FuncaoUsuarioEnum.OUVIDOR.getId().equals(u.getTpFuncao())) {
				sb.append("ouvidoria");
			} else {
				sb.append("manifestante");
			}
		}
		return sb.toString();
	}
	
	public String mostrarPessoasEnvolvidasNoTramite(Object tramite) {
		StringBuilder sb = new StringBuilder(" - ");
		TbTramite t = null;
		if (tramite instanceof TbTramite) {
			t = (TbTramite) tramite;
			TbUnidade unidadeEnvio = t.getIdUnidadeEnvio();
			TbUsuario usuarioReceptor = t.getIdUsuarioReceptor();

			// Se foi um Ouvidor que fez o trâmite aparece o nome Ouvidoria e não o nome do usuário
			if (UnidadeEnum.OUVIDORIA.getId().equals(t.getIdUsuarioEmissor().getIdUnidade().getIdUnidade())) {
				sb.append("Ouvidoria disse para ");
			} else {
				sb.append(t.getIdUsuarioEmissor().getNmUsuario()).append(" disse para ");
			}

			if (unidadeEnvio != null && usuarioReceptor != null) {
				sb.append(usuarioReceptor.getNmUsuario())
				.append(" (").append(unidadeEnvio.getNmUnidade()).append(")");
			} else if (unidadeEnvio != null) {
				sb.append(unidadeEnvio.getNmUnidade());
			} else if (usuarioReceptor != null) {
				sb.append(usuarioReceptor.getNmUsuario());
			}
		}
		return sb.toString();
	}
	
	public String mostrarAreaDeQuemRealizouTramite(Object tramite) {
		try {
			StringBuilder sb = new StringBuilder();
			TbTramite t = null;
			if (tramite instanceof TbTramite) {
				t = (TbTramite) tramite;
				TbUsuario u = t.getIdUsuarioEmissor();

				if (FuncaoUsuarioEnum.ADMINISTRADOR.getId().equals(u.getTpFuncao())) {
					sb.append("administrador");
				} else if (FuncaoUsuarioEnum.OUVIDOR.getId().equals(u.getTpFuncao())) {
					sb.append("ouvidor");
				} else {
					sb.append("unidade");
				}
			}
			return sb.toString();
		} catch (NullPointerException npe) {
			return "";
		}
	}
	
	// Abas Trâmites
	public String getLabelSendMessage() {
		String labelName = "Enviar Mensagem ao ";
		if(isManifestante()) {
			labelName = labelName.concat("Ouvidor");
		} else {
			labelName = labelName.concat("Manifestante");
		}
		return labelName;
	}
	
	/**
	 * <p>Se o usuário for:</p>
	 * <ul>
	 * 	<li><b>ADMINISTRADOR ou OUVIDOR:</b> renderiza</li>
	 * 	<li><b>OPERADOR ou INTERLOCUTOR:</b> renderiza se o encaminhamento for para a área do usuário</li>
	 * 	<li><b>MANIFESTANTE:</b> não renderiza</li>
	 * </ul>
	 * 
	 * @param manifestationTabView Encaminhamento da Manifestação
	 * @return se vai ou não renderizar o botão de enviar mensagem a área do encaminhamento
	 */
	public boolean showSendButton(ManifestacaoTabView manifestationTabView) {
		boolean showSendButton = Boolean.FALSE;
		if(!isManifestante()) {
			if(securityService.isInterlocutor() || securityService.isOperador()) {
				TbUnidade userDepartment = securityService.getUser().getIdUnidade();
				TbUnidade departmentReceived = manifestationTabView.getUnidadeRecebeu();
				TbUnidade departmentSent = manifestationTabView.getUnidadeEnviou();
				
				if (userDepartment.equals(departmentReceived)
						|| userDepartment.equals(departmentSent)) {
					showSendButton = Boolean.TRUE;
				}
			} else {
				showSendButton = Boolean.TRUE;
			}
		}
		return showSendButton;
	}
	
	public String getStatusReferralName(TbEncaminhamento referral) {
		String referralStatusId = referral.getStEncaminhamento();
		StatusEncaminhamentoEnum referralStatus = EnumHelper.getStatusEncaminhamentoEnum(referralStatusId);
		return referralStatus.getDescricao();
	}
	
	public String getStatusReferralClass(TbEncaminhamento referral) {
		String referralStatusId = referral.getStEncaminhamento();
		StatusEncaminhamentoEnum referralStatus = EnumHelper.getStatusEncaminhamentoEnum(referralStatusId);
		String className = "";
		if(referralStatus == StatusEncaminhamentoEnum.ENCAMINHADA) {
			className = "statusTramiteEncaminhado";
		} else {
			className = "statusTramiteRetornado";
		}
		return className;
	}
	
	public String getUserProfileMessageClass(TbUsuario user) {
		String className = "notaAreaTecnica";
		if(user != null) {
			String userProfileId = user.getTpFuncao();
			FuncaoUsuarioEnum userProfile = EnumHelper.getFuncaoUsuarioEnum(userProfileId);
			if(userProfile == FuncaoUsuarioEnum.OUVIDOR || userProfile == FuncaoUsuarioEnum.ADMINISTRADOR) {
				className = "notaOuvidoria";
			}
		}
		return className;
	}
	
	public boolean disableSendMessageButton() {
		return false;
	}
	
	public boolean isManifestante() {
		boolean isManifestante = Boolean.FALSE;
		FuncaoUsuarioEnum profile = securityService.getUserProfile();
		if(profile == null || profile == FuncaoUsuarioEnum.MANIFESTANTE) {
			isManifestante = Boolean.TRUE;
		}
		return isManifestante;
	}
	
	// GETTES e SETTERS
	public TbManifestacao getManifestacao() {
		return manifestacao;
	}

	public void setManifestacao(TbManifestacao manifestacao) {
		this.manifestacao = manifestacao;
	}

	public List<ManifestacaoTabView> getTabs() {
		return tabs;
	}

	private void setTabs(List<ManifestacaoTabView> tabs) {
		this.tabs = tabs;
	}

	public Integer getTabViewAtiva() {
		return tabViewAtiva;
	}

	public void setTabViewAtiva(Integer tabViewAtiva) {
		this.tabViewAtiva = tabViewAtiva;
	}

	public ManifestacaoTabView getTabViewSelecionada() {
		return tabViewSelecionada;
	}

	public void setTabViewSelecionada(ManifestacaoTabView tabViewSelecionada) {
		this.tabViewSelecionada = tabViewSelecionada;
	}

	public List<TbUnidade> getListaUnidadesTramite() {
		return listaUnidadesTramite;
	}

	public void setListaUnidadesTramite(List<TbUnidade> listaUnidadesTramite) {
		this.listaUnidadesTramite = listaUnidadesTramite;
	}

	public Integer getIdUnidadeTramite() {
		return idUnidadeTramite;
	}

	public void setIdUnidadeTramite(Integer idUnidadeTramite) {
		this.idUnidadeTramite = idUnidadeTramite;
	}

	public Integer getIdUsuarioTramite() {
		return idUsuarioTramite;
	}

	public void setIdUsuarioTramite(Integer idUsuarioTramite) {
		this.idUsuarioTramite = idUsuarioTramite;
	}

	public Integer getManifestationNumber() {
		return manifestationNumber;
	}

	public void setManifestationNumber(Integer manifestationNumber) {
		this.manifestationNumber = manifestationNumber;
	}
	
	public Integer getManifestationId() {
		return manifestationId;
	}

	public void setManifestationId(Integer manifestationId) {
		this.manifestationId = manifestationId;
	}

	public List<TbUnidade> getUnidades() {
		return unidades;
	}
	
}
