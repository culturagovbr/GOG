package br.com.xti.ouvidoria.negocio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import br.com.xti.ouvidoria.controller.PesquisaManifestacaoViewHelper;
import br.com.xti.ouvidoria.dao.ComunicacaoExternaDAO;
import br.com.xti.ouvidoria.dao.EncaminhamentoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDTODAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.dao.VwUltimoTramiteDAO;
import br.com.xti.ouvidoria.dto.manifestacao.DTOManifestacao;
import br.com.xti.ouvidoria.exception.PermissionDeniedException;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.FiltroHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbTramite;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.AtrasoManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;
import br.com.xti.ouvidoria.security.SecurityService;

import com.thoughtworks.xstream.XStream;

/**
 * @author Clelson
 */
@Stateless
public class ManifestacaoService {

	@Inject
	private SecurityService securityService;

    @EJB
    private ManifestacaoDTODAO manifestacaoDTODAO;
    @EJB
    private ManifestacaoDAO manifestacaoDAO;
    @EJB
    private EncaminhamentoDAO encaminhamentoDAO;
    @EJB
    private ComunicacaoExternaDAO comunicacaoExternaDAO;
    @EJB
    private UsuarioDAO usuarioDAO;
    
    
    @EJB
    private VwUltimoTramiteDAO vwUltimoTramiteDAO;

    public ManifestacaoService() {
    
    }

    
    /**
     * Oculta uma manifestação representada pelo atributo da classe DTOManifestacao.
     * 
     * 
     * @param manifestacaoSelecionada
     */
	public void ocultar(DTOManifestacao manifestacaoSelecionada) throws Exception{
		manifestacaoDTODAO.ocultar(manifestacaoSelecionada);
		
	}    
	
	
    /**
     * Desoculta uma manifestação representada pelo atributo da classe DTOManifestacao.
     * 
     * 
     * @param manifestacaoSelecionada
     */
	public void desocultar(DTOManifestacao manifestacaoSelecionada) throws Exception{
		manifestacaoDTODAO.desocultar(manifestacaoSelecionada);
		
	}    
 
    
    /**
     * Transforma uma lista de TbManifestacao em uma lista de DTOPesquisaManifestacao
     * 
     * @param list
     */
    private List<DTOManifestacao> transformarListaDTOManifestacao(
    		List<TbManifestacao> list) {
    	List<DTOManifestacao> retorno = new ArrayList<DTOManifestacao>();
    	for (TbManifestacao manifestacao : list) {
    		DTOManifestacao dto = new DTOManifestacao();
    		dto.setNumeroManifestacao(manifestacao.getNrManifestacao());
    		dto.setIdManifestacao(manifestacao.getIdManifestacao());
    		dto.setDataCadastro(manifestacao.getDtCadastro());
    		dto.setDataUltimaAtualizacao(manifestacao.getDtUltimaAtualizacao());
    		dto.setNomeManifestante(manifestacao.getNmPessoa());
    		dto.setNomePessoa(manifestacao.getNmPessoa());
    		
    		dto.setIdTipoManifestacao(manifestacao.getIdTipoManifestacao().getIdTipoManifestacao());
    		dto.setNomeTipoManifestacao(manifestacao.getIdTipoManifestacao().getNmTipoManifestacao());
    		dto.setNomePrioridade(manifestacao.getIdPrioridade().getNmPrioridade());
    		dto.setIdStatusManifestacao( manifestacao.getStStatusManifestacao());
    		dto.setSigilo(Boolean.valueOf(manifestacao.getSiSigilo()));
    		dto.setOculta(Boolean.valueOf( manifestacao.getStStatusOcultacao() ) );
    		dto.setTextoManifestacao( manifestacao.getDsTextoManifestacao() );
    		dto.setMotivoOcultacao(manifestacao.getDsMotivoOcultacao());
    		
    		dto.setPrazoEncaminhamento(manifestacao.getIdTipoManifestacao().getPrazoEntrada());
    		dto.setPrazoRespostaAOuvidoria(manifestacao.getIdTipoManifestacao().getPrazoAreaSolucionadora());
    		dto.setPrazoRespostaAoManifestante(manifestacao.getIdTipoManifestacao().getPrazoRespostaCidadao());
    		
    		dto.setDataMonitoramento(manifestacao.getDataMonitoramento());
    		
    		retorno.add(dto);
    	}
    	
    	// Carrega o resultado da pesquisa no DTO informado 
    	return retorno;
    }
    

	private List<DTOManifestacao> pesquisaManifestacoesFiltroPersonalizado(PesquisaManifestacaoViewHelper filtroManifestacao) {
		boolean filtraOcultas = filtroManifestacao.getFiltroPesquisa().isOculta();

        FiltroPersonalizado filtroPadrao = new FiltroPersonalizado();
		
		FuncaoUsuarioEnum funcao = securityService.getUserProfile();
		if(!(funcao == FuncaoUsuarioEnum.ADMINISTRADOR || funcao == FuncaoUsuarioEnum.OUVIDOR)) {
			filtroPadrao.setMetodoBusca("and");
			filtroPadrao.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
			filtroPadrao.addEncIdUnidadeRecebeu(securityService.getUser().getIdUnidade().getIdUnidade());
		}
    	
        XStream xs = new XStream();
        FiltroPersonalizado filtroAtual = (FiltroPersonalizado) xs.fromXML(filtroManifestacao.getFiltroEscolhido().getDsParticao());

    	List<TbManifestacao> list = manifestacaoDTODAO.getManifestacoes(filtroManifestacao.getFiltroPesquisa(), filtraOcultas, filtroPadrao, filtroAtual);

        List<DTOManifestacao> retorno = transformarListaDTOManifestacao(list);
        complementaDadosManifestacao(retorno);
        
        return retorno;
    }

    
    @SuppressWarnings("unchecked")
    public List<DTOManifestacao> pesquisaManifestacoes(PesquisaManifestacaoViewHelper filtroManifestacao) throws Exception{
    	
    	List<DTOManifestacao> retorno = new ArrayList<DTOManifestacao>();
    	
    	TbUsuario usuario = usuarioDAO.find(securityService.getUser().getIdUsuario());
    	// Garantir a verificação de permissão de acesso na camada de serviços
    	this.validaPermissaoAcessoPesquisa(filtroManifestacao, usuario);
    	

    	if(filtroManifestacao.isCenarioPesquisaFiltroPersonalizado()){
    		//Realiza a pesquisa de manifestações utilizando o FILTRO PERSONALIZADO
    		retorno = pesquisaManifestacoesFiltroPersonalizado(filtroManifestacao);
    	}else{
    		//Realiza a pesquisa de manifestações no banco de dados
    		retorno = manifestacaoDTODAO.pesquisaManifestacoes(filtroManifestacao.getFiltroPesquisa(), usuario);
    		complementaDadosManifestacao(retorno);
    	}
    	
    	return retorno;

    }


    /**
     * Valida as permissões de acesso aos cenários de pesquisa de manifestações.
     * Vale destacar que estas verificações estão replicadas na interface de usuário (tela/ xhtml), entretanto estão replicadas
     * nesta classe de serviço, para garantir a alta coesão da classe de negócio
     * 
     * @param filtroManifestacao
     * @param usuario
     * @throws PermissionDeniedException
     */
	private void validaPermissaoAcessoPesquisa(
			PesquisaManifestacaoViewHelper filtroManifestacao, TbUsuario usuario)
			throws PermissionDeniedException {
		FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
    
    	// TODO: Verficiar o tipo de usuário e validar se o cenário de pesquisa é permitido
    	if (filtroManifestacao.isCenarioPesquisaCaixaEntrada()){
    		/* 
    		 * Permitido o acesso de todos os perfis à Caixa de Entrada. 
    		 * Configurar os registros da caixa de entrada conforme o perfil do usuário
    		 */
    		
    	}
    	if (filtroManifestacao.isCenarioPesquisaSolicitadaInformacao())
    		// Permitido o acesso aos perfis ADMINISTRADOR e OUVIDOR
    		validaPermissaoAcessoPesquisa(true, funcao, FuncaoUsuarioEnum.ADMINISTRADOR, FuncaoUsuarioEnum.OUVIDOR);
    	else if (filtroManifestacao.isCenarioPesquisaEmAndamento())
    		// Permitido o acesso aos perfis que NÃO são OPERADOR ou MANIFESTANTE
    		validaPermissaoAcessoPesquisa(false, funcao, FuncaoUsuarioEnum.OPERADOR, FuncaoUsuarioEnum.MANIFESTANTE);
    	else if (filtroManifestacao.isCenarioPesquisaEmMonitoramento())
    		// Permitido o acesso aos perfis ADMINISTRADOR e OUVIDOR
    		validaPermissaoAcessoPesquisa(true, funcao, FuncaoUsuarioEnum.ADMINISTRADOR, FuncaoUsuarioEnum.OUVIDOR);
    	else if (filtroManifestacao.isCenarioPesquisaRetornadas())
    		// Permitido o acesso aos perfis que NÃO são OPERADOR ou MANIFESTANTE
    		validaPermissaoAcessoPesquisa(false, funcao, FuncaoUsuarioEnum.OPERADOR, FuncaoUsuarioEnum.MANIFESTANTE);
    	else if (filtroManifestacao.isCenarioPesquisaDevolvidas())
    		// Permitido o acesso ao perfil OPERADOR 
    		validaPermissaoAcessoPesquisa(true, funcao, FuncaoUsuarioEnum.OPERADOR, FuncaoUsuarioEnum.MANIFESTANTE);
    	else if (filtroManifestacao.isCenarioPesquisaComOuvidoria())
    		// Permitido o acesso aos perfis INTERLOCUTOR e OPERADOR
    		validaPermissaoAcessoPesquisa(true, funcao, FuncaoUsuarioEnum.INTERLOCUTOR, FuncaoUsuarioEnum.OPERADOR);
    	else if (filtroManifestacao.isCenarioPesquisaSolucionadas())
    		// Permitido o acesso aos perfis que NÃO MANIFESTANTE
    		validaPermissaoAcessoPesquisa(false, funcao, FuncaoUsuarioEnum.MANIFESTANTE);
    	else if (filtroManifestacao.isCenarioPesquisaTodos())
    		// Permitido o acesso aos perfis ADMINISTRADOR e OUVIDOR
    		validaPermissaoAcessoPesquisa(true, funcao, FuncaoUsuarioEnum.ADMINISTRADOR, FuncaoUsuarioEnum.OUVIDOR);
	}
    

	/**
	 * Verifica se a função do usuário informada é igual a uma das funções permitidas.
	 * 
	 * @param acessoPermitido Flag para informar se o acesso deve ser permitido  
	 * @param funcaoUsuario
	 * @param funcoesVerificaveis
	 * @throws PermissionDeniedException Exceção disparada caso a função do usuário não seja igual ao menos a uma das funções permitidas 
	 */
	private void validaPermissaoAcessoPesquisa(boolean acessoPermitido, FuncaoUsuarioEnum funcaoUsuario, FuncaoUsuarioEnum... funcoesVerificaveis) throws PermissionDeniedException{
		for (FuncaoUsuarioEnum funcao : funcoesVerificaveis) {
			if (acessoPermitido){
				// Encerra a validação se o acesso for permitido para uma das funções informadas
				if (funcao.equals(funcaoUsuario))
					return;
			}else{
				// Dispara exceção caso a função verificável é a igual a função do usuário E (and) caso o acesso não seja permitido para as funções informadas 
				if (funcao.equals(funcaoUsuario))
					throw new PermissionDeniedException();
			}
		}
		
		if (acessoPermitido)
			// Dispara exceção caso a função do usuário NÃO seja igual a qualquer uma das funções informadas
			throw new PermissionDeniedException();
	}
 
       
	/**
     * Complementa os dados da manifestação com atributos resultadas dos relacionamentos com demais entidades.
     * 
     * @param resultado
     */
	private void complementaDadosManifestacao(List<DTOManifestacao> resultado){
        for (DTOManifestacao dtoManifestacao : resultado) {
        	//Recupera a lista de encaminhamentos da manifestação
        	Collection<TbEncaminhamento> listaEncaminhamentos = encaminhamentoDAO.getPorIdManifestacao(dtoManifestacao.getIdManifestacao());
        	dtoManifestacao.setEncaminhamentos(listaEncaminhamentos);
        	// Recupera o encaminhamento do usuário logado para a manifestação
        	recuperaEncaminhamentoDoUsuarioLogado(dtoManifestacao, listaEncaminhamentos);
        	//Recupera a lista de comunicações externas da manifestação
        	Collection<TbComunicacaoExterna> listaComunicacao = comunicacaoExternaDAO.getPorIdManifestacao(dtoManifestacao.getIdManifestacao());
        	dtoManifestacao.setComunicacaoExterna(listaComunicacao);

        	// Recuperar o prazo para atendimento da manifestação e a data limite para atendimento do usuário logado
        	recuperaPrazoAtendimento(dtoManifestacao);
        	// Definir o tipo de atraso em cada manifestação recuperada
        	recuperaTipoAtraso(dtoManifestacao);
        	// Recuperar a lista com o nome dos operadores que atuaram na manifestação
        	recuperaNomeOperadoresComManifestacao(dtoManifestacao);
        	// Recuperar a quantidade de dias em atraso da manifestação
        	recuperaDiasAtrasoAoManifestante(dtoManifestacao);
        	// Recuperar o nome das unidades encaminhadas
        	recuperaUnidadesEncaminhadas(dtoManifestacao);

		}
	}

    /**
     * Recupera o nome das unidades para as quais foram encaminhadas a manifestação 
     * 
     * @param manifestacao
     * @return
     */
    private void recuperaUnidadesEncaminhadas(DTOManifestacao dtoManifestacao) {
        StringBuilder unidades = new StringBuilder();
        Collection<TbEncaminhamento> listaEncaminhamentos = dtoManifestacao.getEncaminhamentos();
        if (listaEncaminhamentos != null) {
            for (TbEncaminhamento encaminhamento : listaEncaminhamentos) {
                if (unidades.length() == 0) {
                    unidades.append(encaminhamento.getIdUnidadeRecebeu().getSgUnidade());
                } else {
                    unidades.append(", ").append(encaminhamento.getIdUnidadeRecebeu().getSgUnidade());
                }
            }
        }
        
        dtoManifestacao.setNomesUnidades(unidades.toString());
    }

    
    
    private void recuperaDiasAtrasoAoManifestante(DTOManifestacao dtoManifestacao) {
		int diasEmAtraso = 0;
		if (dtoManifestacao != null
				&& !StatusManifestacaoEnum.SOLUCIONADA.getId().equals(dtoManifestacao.getIdStatusManifestacao())) {
    		if(securityService.isInterlocutor() || securityService.isOperador()) {
    			Date dateLimitToAnswer = dtoManifestacao.getPrazoAtendimento();
    			DateTimeZone BRAZIL = DateTimeZone.forID("America/Sao_Paulo");
    			DateTime start = new LocalDate(dateLimitToAnswer.getTime(), BRAZIL).toDateTimeAtStartOfDay();
    			DateTime end = new LocalDate(new Date().getTime(), BRAZIL).toDateTimeAtStartOfDay();
    			Days days = Days.daysBetween(start, end);
    			
    			if(days.isGreaterThan(null)) {
    				diasEmAtraso = days.getDays();
    			}
    		} else {
    			Integer prazoEncaminhamento = dtoManifestacao.getPrazoEncaminhamento();
    			Integer prazoRespostaAOuvidoria = dtoManifestacao.getPrazoRespostaAOuvidoria();
    			Integer prazoRespostaAoManifestante = dtoManifestacao.getPrazoRespostaAoManifestante();
    			int diasParaResponderManifestante = prazoEncaminhamento + prazoRespostaAoManifestante + prazoRespostaAOuvidoria;
    			
    			Date dataManifestacao = dtoManifestacao.getDataCadastro();
    			Date dataAtual = new Date();
    			int diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
    			
    			if (diasTranscorridos > ++diasParaResponderManifestante) {
    				diasEmAtraso = diasTranscorridos - diasParaResponderManifestante;
    			}
    		}
    		
    	}
		dtoManifestacao.setDiasAtraso(diasEmAtraso);
//    	return diasEmAtraso;
    }

    /**
     * Recupera o prazo de atendimento para a unidade do usuário logado
     * 
     * @param dtoManifestacao
     */
	private void recuperaPrazoAtendimento(DTOManifestacao dtoManifestacao) {
    	Date dataFinalAtendimento = null;
		TbEncaminhamento encaminhamento = dtoManifestacao.getEncaminhamentoUsuario();
    	if(encaminhamento != null) {
	    	int diasParaResponder = dtoManifestacao.getPrazoRespostaAOuvidoria();
	    	
    		Calendar c = Calendar.getInstance();
    		c.setTime(encaminhamento.getDtEnvioTramite());
    		c.add(Calendar.DAY_OF_MONTH, diasParaResponder);
    		dataFinalAtendimento = c.getTime();
    	}
    	dtoManifestacao.setPrazoAtendimento(dataFinalAtendimento);
    }
	
	


    /**
     * Recupera o nome dos operadores que atuaram na manifestação
     * @param dtoManifestacao
     */
    private void recuperaNomeOperadoresComManifestacao(DTOManifestacao dtoManifestacao) {
    	StringBuilder nomeOperadores = new StringBuilder();
    	TbUnidade unidadeInterlocutor = securityService.getUser().getIdUnidade();
    	Set<TbUsuario> operadores = new TreeSet<>();
    	
		for (TbEncaminhamento e : dtoManifestacao.getEncaminhamentos()) {
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
		
		dtoManifestacao.setNomesOperadores(nomeOperadores.toString());
    }

    
    
    /**
     * Recupera o tipo de atraso da manifestação, conforme regra estabelecida
     * 
     * @param dtoManifestacao
     */
    private void recuperaTipoAtraso(DTOManifestacao dtoManifestacao) {
    	
        int diasTranscorridos = 0;
        Date dataManifestacao = dtoManifestacao.getDataCadastro();
        Date dataAtual = new Date();

        if (securityService.isOuvidor() || securityService.isAdministrador()) {

            if (!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(dtoManifestacao.getIdStatusManifestacao())) {
            	// VERIFICA ATRASO - ENCAMINHAMENTO
            	diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
            	
            	Collection<TbEncaminhamento> listaEncaminhamentos = dtoManifestacao.getEncaminhamentos();
            	if (listaEncaminhamentos != null) {
            		if (dtoManifestacao.getPrazoEncaminhamento() > 0 && 
            				diasTranscorridos > dtoManifestacao.getPrazoEncaminhamento() && 
            				listaEncaminhamentos.isEmpty()) {
            			dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.ATRASO_ENCAMINHAMENTO);
            			return;
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

                        if (dtoManifestacao.getPrazoRespostaAOuvidoria() > 0 && 
                        		diasTranscorridos > dtoManifestacao.getPrazoRespostaAOuvidoria() && 
                        		StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(statusTramite)) {
                        	dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA);
                        	return;
                        }
                    }
                }
                
            	// VERIFICA ATRASO - RESPOSTA AO MANIFESTANTE
                if(todasAreasRetornaram) {
                	diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
                	Collection<TbComunicacaoExterna> listaComunicacao = dtoManifestacao.getComunicacaoExterna(); 
                	
                	if (ValidacaoHelper.isEmpty(listaComunicacao)) {
                		if (dtoManifestacao.getPrazoRespostaAoManifestante() > 0 && 
                				diasTranscorridos > dtoManifestacao.getPrazoRespostaAoManifestante()) {
                			dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE);
                			return;
                		}
                	} else {
	        			//pega a ultima comunicacao e verifica se ela possui resposta final
                		List<TbComunicacaoExterna> comunicacoes = new ArrayList<TbComunicacaoExterna>(listaComunicacao);
	        			TbComunicacaoExterna com = comunicacoes.listIterator(comunicacoes.size() - 1).next();
	        			if (dtoManifestacao.getPrazoRespostaAoManifestante() > 0 && 
	        					diasTranscorridos > dtoManifestacao.getPrazoRespostaAoManifestante() && 
	        					!"1".equals(com.getStRespostaFinal())) {
	        				dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE);
	        				return;
	        			}
                	}
                }
            }
            
            dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.SEM_ATRASO);
            return;
        } else if (securityService.isInterlocutor() || securityService.isOperador()) {
            // VERIFICA ATRASO - RESPOSTA À OUVIDORIA
            if (!StatusManifestacaoEnum.SOLUCIONADA.getId().equals(dtoManifestacao.getIdStatusManifestacao())) {

               	TbEncaminhamento tbEncaminhamento = dtoManifestacao.getEncaminhamentoUsuario();
               	if (tbEncaminhamento != null) {

                    Date dataEncaminhamento = tbEncaminhamento.getDtEnvioTramite();
                    String statusTramite = tbEncaminhamento.getStEncaminhamento();

                    diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataEncaminhamento);

                    if (dtoManifestacao.getPrazoRespostaAOuvidoria() > 0 && 
                    		diasTranscorridos > dtoManifestacao.getPrazoRespostaAOuvidoria() && 
                    		StatusEncaminhamentoEnum.ENCAMINHADA.getId().equals(statusTramite)) {
                    	dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA);
                    	return;
                    }
                }
            }
        }
            
        dtoManifestacao.setTipoAtrasoManifestacao(AtrasoManifestacaoEnum.SEM_ATRASO);
    }
    
    
	/**
	 * Recupera o encaminhamento realizado a unidade do usuário logado
	 * 
	 * @param manifestacao manifestação
	 */
	private void recuperaEncaminhamentoDoUsuarioLogado(DTOManifestacao dtoManifestacao, Collection<TbEncaminhamento> listaEncaminhamentos) {
		TbUnidade unidadeUsuario = securityService.getUser().getIdUnidade();
		if (listaEncaminhamentos != null) {
			for (TbEncaminhamento encaminhamento : listaEncaminhamentos) {
				if (encaminhamento.getIdUnidadeRecebeu().equals(unidadeUsuario)) {
					dtoManifestacao.setEncaminhamentoUsuario(encaminhamento);
					return;
				}
			}
		}
	}



}
