package br.com.xti.ouvidoria.dao;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import br.com.xti.ouvidoria.dto.manifestacao.DTOManifestacao;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbManifestacao_;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.UnidadeEnum;

/**
 * @author Clelson
 */
@Stateless
public class ManifestacaoDTODAO extends AbstractDAO<TbManifestacao> {

    @EJB
    private VwUltimoTramiteDAO vwUltimoTramiteDAO;
    @EJB
    private ManifestacaoDAO manifestacaoDAO;
    
    
    private Map<String, StringBuffer> 	mapaEntidades 		= new HashMap<String, StringBuffer>();
    private Map<String, Boolean> 		mapaUsoEntidades 	= new HashMap<String, Boolean>();
    private Map<String, Object> 		mapaQueryParameter 	= new HashMap<String, Object>();

    
    private static final String ASSOCIACAO_ENCAMINHAMENTOS					=	"encaminhamentos"; 
    private static final String ASSOCIACAO_TRAMITE							=	"tramite"; 
    private static final String ASSOCIACAO_USUARIO_RECEPTOR					=	"usuarioReceptor"; 
    private static final String ASSOCIACAO_USUARIO_EMISSOR					=	"usuarioEmissor"; 
    private static final String ASSOCIACAO_ULTIMO_TRAMITE					=	"ultimoTramite"; 

    public ManifestacaoDTODAO() {
        super(TbManifestacao.class);
        
        //Carrega os mapas para controlar as associações de entidades 
        mapaEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, 
        		new StringBuffer ("	LEFT JOIN TbEncaminhamento en				ON en.idManifestacao					= m.idManifestacao "));
        mapaUsoEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, false);
        

        mapaEntidades.put(ASSOCIACAO_TRAMITE, 
        		new StringBuffer ("	LEFT JOIN TbTramite tra						ON tra.idEncaminhamento					= en.idEncaminhamento " ));
        mapaUsoEntidades.put(ASSOCIACAO_TRAMITE,  false);

        mapaEntidades.put(ASSOCIACAO_USUARIO_RECEPTOR, 
        		new StringBuffer ("	LEFT JOIN TbUsuario usuarioReceptor			ON usuarioReceptor.idUsuario			= tra.idUsuarioReceptor " +
        						  "	LEFT JOIN TbUnidade unidadeUsuarioReceptor	ON unidadeUsuarioReceptor.idUnidade 	= usuarioReceptor.idUnidade " ));
        mapaUsoEntidades.put(ASSOCIACAO_USUARIO_RECEPTOR,  false);
    	
        mapaEntidades.put(ASSOCIACAO_USUARIO_EMISSOR, 
        		new StringBuffer ("	LEFT JOIN TbUsuario usuarioEmissor			ON usuarioEmissor.idUsuario				= tra.idUsuarioEmissor " +
        						  "	LEFT JOIN TbUnidade unidadeUsuarioEmissor	ON unidadeUsuarioEmissor.idUnidade 		= usuarioEmissor.idUnidade " ));
        mapaUsoEntidades.put(ASSOCIACAO_USUARIO_EMISSOR,  false);
    	
        mapaEntidades.put(ASSOCIACAO_ULTIMO_TRAMITE, 
        		new StringBuffer ("	LEFT JOIN vwUltimoTramite vwUltTram		ON m.idManifestacao						= vwUltTram.idManifestacao " +
        						  "	LEFT JOIN TbTramite ultimoTramite			ON vwUltTram.idTramite			 		= ultimoTramite.idTramite " ));
        mapaUsoEntidades.put(ASSOCIACAO_ULTIMO_TRAMITE,  false);

    }

    /**
     * Recupera o SQL para complementar a associação de entidades
     * @return
     */
    private String recuperaComplementoAssociacaoEntidades(){
    	String sqlAssociacao = "";

    	sqlAssociacao += mapaUsoEntidades.get(ASSOCIACAO_ENCAMINHAMENTOS) 					? mapaEntidades.get(ASSOCIACAO_ENCAMINHAMENTOS) : "";
    	sqlAssociacao += mapaUsoEntidades.get(ASSOCIACAO_TRAMITE) 							? mapaEntidades.get(ASSOCIACAO_TRAMITE) : "";
    	sqlAssociacao += mapaUsoEntidades.get(ASSOCIACAO_USUARIO_RECEPTOR) 					? mapaEntidades.get(ASSOCIACAO_USUARIO_RECEPTOR) : "";
    	sqlAssociacao += mapaUsoEntidades.get(ASSOCIACAO_USUARIO_EMISSOR) 					? mapaEntidades.get(ASSOCIACAO_USUARIO_EMISSOR) : "";
    	sqlAssociacao += mapaUsoEntidades.get(ASSOCIACAO_ULTIMO_TRAMITE) 					? mapaEntidades.get(ASSOCIACAO_ULTIMO_TRAMITE) : "";

    	return sqlAssociacao;
    }
    
    /**
     * Configura a mapa de utilização de associações de entidades 
     * @param associacaoEntidade
     */
    private void configuraComplementoAssociacaoEntidade(String associacaoEntidade){
    	if (associacaoEntidade.equals(ASSOCIACAO_ENCAMINHAMENTOS))
    		mapaUsoEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, true);
    	
    	else if (associacaoEntidade.equals(ASSOCIACAO_TRAMITE)){
    		mapaUsoEntidades.put(ASSOCIACAO_TRAMITE, true);
    		mapaUsoEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, true);

    	}else if (associacaoEntidade.equals(ASSOCIACAO_USUARIO_RECEPTOR)){
    		mapaUsoEntidades.put(ASSOCIACAO_USUARIO_RECEPTOR, true);
    		mapaUsoEntidades.put(ASSOCIACAO_TRAMITE, true);
    		mapaUsoEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, true);
    	}else if (associacaoEntidade.equals(ASSOCIACAO_USUARIO_EMISSOR)){
    		mapaUsoEntidades.put(ASSOCIACAO_USUARIO_EMISSOR, true);
    		mapaUsoEntidades.put(ASSOCIACAO_TRAMITE, true);
    		mapaUsoEntidades.put(ASSOCIACAO_ENCAMINHAMENTOS, true);
    	}else if (associacaoEntidade.equals(ASSOCIACAO_ULTIMO_TRAMITE)){
    		mapaUsoEntidades.put(ASSOCIACAO_ULTIMO_TRAMITE, true);
    	}
    }
    
    @Override
    public String getNomeEntidade() {
        return "Manifestação";
    }
    
    /**
     * Recupera as manifestações marcadas para Monitoramento e que tenham a 
     * data de monitoramento menor ou igual à data informada
     * 
     * @param dataMonitoramento
     * @return
     */
    public List<TbManifestacao> recuperaManifestacoesEmMonitoramento(Date dataMonitoramento) throws Exception{
        String select = "SELECT t FROM TbManifestacao t WHERE "
        		+ " t.stStatusManifestacao = :status"
        		// Recupera os monitoramentos com a data de monitoramento menor ou igual à data informada
        		+ " AND t.dataMonitoramento <= :data ";
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", StatusManifestacaoEnum.EM_MONITORAMENTO.getId());
        params.put("data", dataMonitoramento);

        Query selectQuery = getEntityManager().createQuery(select);
        selectQuery.setParameter("status", StatusManifestacaoEnum.EM_MONITORAMENTO.getId());
        selectQuery.setParameter("data", dataMonitoramento);
        
		List<TbManifestacao> results = selectQuery.getResultList();
        
        return results;

    }
    
    /**
     * Configura a tabela de manifestações: 
     * Verifica a existência da coluna 'dtMonitoramento' na tabela 'tbManifestacao'
     * Altera a tabela 'tbManifestacao', incluindo a coluna 'dtMonitoramento', caso não exista
     * 
     */
    @SuppressWarnings("unchecked")
    public void configuraTabelaManifestacao() throws Exception{
    	try {
    		// Verifica a existência da coluna 'dtMonitoramento' na tabela 'tbManifestacao'
    		String queryVerificaColuna = " SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS where table_name = 'tbManifestacao' and column_name = 'dtMonitoramento'; ";
    		Query selectQuery = getEntityManager().createNativeQuery(queryVerificaColuna);
    		List results = selectQuery.getResultList();
    		if (results == null || results.isEmpty()){
    			// Altera a tabela 'tbManifestacao', incluindo a coluna 'dtMonitoramento', caso não exista
    			String queryIncluiColuna = "alter table tbManifestacao add dtMonitoramento timestamp;";
    			Query alterQuery = getEntityManager().createNativeQuery(queryIncluiColuna);
    			alterQuery.executeUpdate();
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
        } 
    	
    	
    }
    

	/**
	 * Recupera as manifestações conforme os dados do DTO de pesquisa 'DTOPesquisaManifestacao' e dos filtros personalizados
	 * 
	 * @param filtroManifestacao
	 * @param filtrosPersonalizados
	 * @return
	 */
    public List<TbManifestacao> getManifestacoes(DTOManifestacao filtroManifestacao, boolean filtraOcultas, FiltroPersonalizado... filtrosPersonalizados) {
        try {
            // Criando Query para filtro
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<TbManifestacao> query = cb.createQuery(TbManifestacao.class);
            Root<TbManifestacao> m = query.from(TbManifestacao.class);
            query.select(m);
            query.distinct(true);
//            query.orderBy(cb.desc(m.get(TbManifestacao_.nrManifestacao)));

            List<Predicate> restricoesLista = new ArrayList<>();
            for (FiltroPersonalizado filtro : filtrosPersonalizados) {
            	// Delegar para a implementação de ManifestacaoDAO a recuperação de filtros com JPAModel
                Predicate[] predicates = manifestacaoDAO.getFiltros(cb, m, filtro);
                if (predicates.length > 0) {
                    if ("or".equals(filtro.getMetodoBusca())) {
                        restricoesLista.add(cb.or(predicates));
                    } else if ("not".equals(filtro.getMetodoBusca())) {
                    	for (Predicate predicate : predicates) {
							restricoesLista.add(predicate.not());
						}
                    } else {
                        restricoesLista.add(cb.and(predicates));
                    }
                }
            }
            //Acrescenta filtro de manifestações ocultas 
            if (filtraOcultas){
                restricoesLista.add(cb.equal(m.get(TbManifestacao_.stStatusOcultacao), BooleanEnum.SIM.getId()));
            }

            query.where(restricoesLista.toArray(new Predicate[restricoesLista.size()]));
            
        	// Carrega a quantidade de registros da pesquisa no DTO informado
            CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);

            Root<TbManifestacao> root = queryCount.from(TbManifestacao.class);
            Expression<Long> expression = cb.countDistinct(root.get("idManifestacao"));
            queryCount.select(expression);
            List<Predicate> restricoesListaCount = new ArrayList<>();
            for (FiltroPersonalizado filtro : filtrosPersonalizados) {
            	// Delegar para a implementação de ManifestacaoDAO a recuperação de filtros com JPAModel
                Predicate[] predicates = manifestacaoDAO.getFiltros(cb, root, filtro);
                if (predicates.length > 0) {
                    if ("or".equals(filtro.getMetodoBusca())) {
                    	restricoesListaCount.add(cb.or(predicates));
                    } else if ("not".equals(filtro.getMetodoBusca())) {
                    	for (Predicate predicate : predicates) {
                    		restricoesListaCount.add(predicate.not());
						}
                    } else {
                    	restricoesListaCount.add(cb.and(predicates));
                    }
                }
            }
            if (filtraOcultas){
            	restricoesListaCount.add(cb.equal(root.get(TbManifestacao_.stStatusOcultacao), BooleanEnum.SIM.getId()));
            }
            queryCount.where(restricoesListaCount.toArray(new Predicate[restricoesListaCount.size()]));
        	filtroManifestacao.setQuantidaLinhasPesquisadas(getEntityManager().createQuery(queryCount).getSingleResult().intValue());

        	
        	// Configura parâmetros da ordenação
        	if (filtroManifestacao.getOrdenacaoCampo() != null){
        		if (filtroManifestacao.getOrdenacaoCampo().equals("nrManifestacao")){
        			if (filtroManifestacao.getOrdenacaoForma().equals("DESC"))
        				query.orderBy(cb.desc(m.get(TbManifestacao_.nrManifestacao)));
        			else if (filtroManifestacao.getOrdenacaoForma().equals("ASC"))
        				query.orderBy(cb.asc(m.get(TbManifestacao_.nrManifestacao)));
        		}
        		if (filtroManifestacao.getOrdenacaoCampo().equals("dtUltimaAtualizacao")){
        			if (filtroManifestacao.getOrdenacaoForma().equals("DESC"))
        				query.orderBy(cb.desc(m.get(TbManifestacao_.dtUltimaAtualizacao)));
        			else if (filtroManifestacao.getOrdenacaoForma().equals("ASC"))
        				query.orderBy(cb.asc(m.get(TbManifestacao_.dtUltimaAtualizacao)));
        		}
        		if (filtroManifestacao.getOrdenacaoCampo().equals("nomePessoa")){
        			if (filtroManifestacao.getOrdenacaoForma().equals("DESC"))
        				query.orderBy(cb.desc(m.get(TbManifestacao_.nmPessoa)));
        			else if (filtroManifestacao.getOrdenacaoForma().equals("ASC"))
        				query.orderBy(cb.asc(m.get(TbManifestacao_.nmPessoa)));
        		}
        		
        	}

            
            // Retorna as manifestações encontradas
            TypedQuery<TbManifestacao> selectQuery = getEntityManager().createQuery(query);
            
            // Define posições na paginação da pesquisa
            selectQuery.setFirstResult(filtroManifestacao.getPrimeiroRegistro());
            selectQuery.setMaxResults(filtroManifestacao.getQuantidadeRegistros());
            
            return selectQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    
    @SuppressWarnings("unchecked")
    public List<DTOManifestacao> pesquisaManifestacoes(DTOManifestacao filtroManifestacao, TbUsuario usuario) throws Exception{

    	StringBuffer camposQuery = new StringBuffer(
	    	" SELECT distinct " + 
	    	"  m.nrManifestacao" + 
	    	", m.idManifestacao" + 
	    	", m.dtCadastro" +  
	    	", m.dtUltimaAtualizacao" + 
	    	", m.nmPessoa" +
	    	", tp.idTipoManifestacao" + 
	    	", tp.nmTipoManifestacao" + 
	    	", pr.nmPrioridade" + 
	    	", m.stStatusManifestacao" + 
	    	", m.siSigilo" + 
	    	", m.stStatusOcultacao" +  
	    	", m.dsTextoManifestacao" + 
	    	", m.dsMotivoOcultacao" + 
	    	", m.dtMonitoramento" + 
	
	    	
	    	", tp.prazoEntrada" +  
	    	", tp.prazoAreaSolucionadora" +  
	    	", tp.prazoRespostaCidadao " );  
    	

    	StringBuffer fonteDadosQuery = new StringBuffer(
    		" FROM tbManifestacao m " + 
	    	"	INNER JOIN tbTipoManifestacao tp		ON tp.idTipoManifestacao 		= m.idTipoManifestacao " + 
	    	"	INNER JOIN TbPrioridade pr				ON pr.idPrioridade 				= m.idPrioridade " );
    	

    	StringBuffer filtroQuery = geraClausulasWherePesquisaManifestacoes(
				filtroManifestacao, usuario);

		// Carrega os complementos das associações de entidades requeridas
    	fonteDadosQuery.append( recuperaComplementoAssociacaoEntidades() );

    	
    	StringBuffer ordenacaoQuery = new StringBuffer();
    	
    	String campoOrdenacao = filtroManifestacao.getOrdenacaoCampo();
    	String formaOrdenacao = filtroManifestacao.getOrdenacaoForma();
    	if (!StringUtils.isEmpty(campoOrdenacao) && !StringUtils.isEmpty(formaOrdenacao)){
    		ordenacaoQuery.append(" ORDER BY " );
			switch (campoOrdenacao) {
				case "numeroManifestacao":
		    		ordenacaoQuery.append(" m.nrManifestacao " + formaOrdenacao);
					break;
					
				case "dataUltimaAtualizacao":
		    		ordenacaoQuery.append("  m.dtUltimaAtualizacao " + formaOrdenacao);
					break;
	
				case "nomePessoa":
		    		ordenacaoQuery.append("  m.nmPessoa " + formaOrdenacao);
					break;
	
				case "nomeTipoManifestacao":
		    		ordenacaoQuery.append("  tp.nmTipoManifestacao " + formaOrdenacao);
					break;
	
				case "nomePrioridade":
		    		ordenacaoQuery.append("  pr.nmPrioridade " + formaOrdenacao);
					break;
	
				default:
					ordenacaoQuery.append(campoOrdenacao + " " + formaOrdenacao);
					break;
			}

    	}

    	// Carrega a quantidade de registros da pesquisa no DTO informado
    	StringBuffer queryCount = new StringBuffer("SELECT count(distinct m.nrManifestacao) ");
    	queryCount.append(fonteDadosQuery);
    	//Acrescenta clásula WHERE quando tem filtros informados
    	queryCount.append(filtroQuery);
    	
    	Set<String> chavesMapaQuery = mapaQueryParameter.keySet();
    	Query selectQueryCount = getEntityManager().createNativeQuery(queryCount.toString());
    	// Carrega os valores das cláusulas WHERE
    	for (String chave : chavesMapaQuery) {
    		selectQueryCount.setParameter(chave, mapaQueryParameter.get(chave));
    	}
    	filtroManifestacao.setQuantidaLinhasPesquisadas(Integer.parseInt("" + selectQueryCount.getSingleResult()));

    	// Realiza a pesquisa 
    	StringBuffer query = new StringBuffer(camposQuery);
    	query.append(fonteDadosQuery);
    	//Acrescenta clásula WHERE quando tem filtros informados
    	query.append(filtroQuery);
    	query.append(ordenacaoQuery);
    	Query selectQuery = getEntityManager().createNativeQuery(query.toString());
    	// Carrega os valores das cláusulas WHERE
    	for (String chave : chavesMapaQuery) {
    		selectQuery.setParameter(chave, mapaQueryParameter.get(chave));
    	}
    	
    	// Define posições na paginação da pesquisa
        selectQuery.setFirstResult(filtroManifestacao.getPrimeiroRegistro());
        selectQuery.setMaxResults(filtroManifestacao.getQuantidadeRegistros());
        
        // Executa a pesquisa e transforma os objetos em um bean do DTO informado
        List<Object[]> resultList = selectQuery.getResultList();
//      ResultTransformer resultTransformer  = Transformers.aliasToBean(filtroManifestacao.getClassResultado());
//      List<DTOManifestacao> retorno = resultTransformer.transformList(resultList);
        
        List<DTOManifestacao> retorno = new ArrayList<DTOManifestacao>();
        for (Object[] obj : resultList) {
        	DTOManifestacao dto = new DTOManifestacao();
        	dto.setNumeroManifestacao((Integer) obj[0]);
        	dto.setIdManifestacao((Integer) obj[1]);
        	dto.setDataCadastro(new Date( ((Timestamp) obj[2]).getTime() ));
        	dto.setDataUltimaAtualizacao(new Date( ((Timestamp) obj[3]).getTime() ));
        	dto.setNomeManifestante((String) obj[4]);
        	dto.setNomePessoa((String) obj[4]);
        	
        	dto.setIdTipoManifestacao((Integer) obj[5]);
        	dto.setNomeTipoManifestacao((String) obj[6]);
        	dto.setNomePrioridade((String) obj[7]);

        	//Configura os dados de campos Character, conforme o modelo no Banco
    		dto.setIdStatusManifestacao((obj[8]) instanceof Character ? 
    			((Character) obj[8]).toString():((String) obj[8]));
    		dto.setSigilo((obj[9]) instanceof Character ? 
    			Boolean.valueOf( ((Character) obj[9]).toString()):Boolean.valueOf( ((String) obj[9]))  );
    		dto.setOculta((obj[10]) instanceof Character ?  
    			((Character) obj[10]).toString().equals("1"):((String) obj[10]).equals("1") ) ;
    		String textoManifestacao;
    		
    		if (obj[11] instanceof Clob) {    			
    			Reader in = ((Clob) obj[11]).getCharacterStream();
    			StringWriter w = new StringWriter();
    			IOUtils.copy(in, w);
    			
    			textoManifestacao = w.toString();
    		} else {
    			textoManifestacao = (String) obj[11];
    		}
    		
    		dto.setTextoManifestacao(textoManifestacao);
        	
        	dto.setMotivoOcultacao((String) obj[12]);
        	
        	dto.setDataMonitoramento(obj[13] != null ?
        			new Date( ((Timestamp) obj[13]).getTime()) : 
        				null); 
        	
        	dto.setPrazoEncaminhamento((Integer) obj[14]);
        	dto.setPrazoRespostaAOuvidoria((Integer) obj[15]);
        	dto.setPrazoRespostaAoManifestante((Integer) obj[16]);

        	retorno.add(dto);
		}
        
        // Carrega o resultado da pesquisa no DTO informado 
        return retorno;
        

    }

    
    /**
     * Gera texto para a cláusula WHERE da pesquisa de manifestações, utilizando dados do filtro informado 
     * 
     * @param filtroManifestacao
     * @param usuario
     * @return
     */
	private StringBuffer geraClausulasWherePesquisaManifestacoes(
			DTOManifestacao filtroManifestacao, TbUsuario usuario) {
		mapaQueryParameter 	= new HashMap<String, Object>();
		StringBuffer filtroQuery = new StringBuffer("");
		boolean filtraOcultas = filtroManifestacao.isOculta();
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
    	if (funcao == FuncaoUsuarioEnum.ADMINISTRADOR && filtraOcultas){//Filtra ocultas apenas para o Administrador
    		filtroQuery.append(" WHERE m.stStatusOcultacao = " + BooleanEnum.SIM.getId());
    	}

    	// Configura os filtros de pesquisa conforme o cenário de pesquisa
    	//configuraFiltroPesquisa(filtroManifestacao, filtroQuery, usuario);
		if (filtroManifestacao.isCenarioPesquisaCaixaEntrada())
			configuraFiltroCaixaEntrada(filtroQuery, usuario);
		else if (filtroManifestacao.isCenarioPesquisaSolicitadaInformacao())
			configuraFiltroSolicitadaInformacao(filtroQuery);
		else if (filtroManifestacao.isCenarioPesquisaEmMonitoramento())
			configuraFiltroEmMonitoramento(filtroQuery);
		else if (filtroManifestacao.isCenarioPesquisaDevolvidas())
			configuraFiltroDevolvidas(filtroQuery, usuario);
		else if (filtroManifestacao.isCenarioPesquisaEmAndamento())
			configuraFiltroEmAndamento(filtroQuery, usuario);
		else if (filtroManifestacao.isCenarioPesquisaRetornadas())
			configuraFiltroRetornadas(filtroQuery, usuario);
		else if (filtroManifestacao.isCenarioPesquisaSolucionadas())
			configuraFiltroSolucionadas(filtroQuery, usuario);
		else if (filtroManifestacao.isCenarioPesquisaComOuvidoria())
			configuraFiltroComOuvidoria(filtroQuery, usuario);

		// Configura a pesquisa de manifestações
		Integer filtroNumeroManifestacao = filtroManifestacao.getNumeroManifestacao();
		if (filtroNumeroManifestacao != null && filtroNumeroManifestacao != 0){
			adicionaClausulaWHERE(filtroQuery, " m.nrManifestacao = :numeroManifestacao ");
			mapaQueryParameter.put("numeroManifestacao", filtroNumeroManifestacao);
		}
		String nomeManifestante = filtroManifestacao.getNomeManifestante();
		if (nomeManifestante != null && !StringUtils.isEmpty(nomeManifestante)){
			adicionaClausulaWHERE(filtroQuery, " upper(m.nmPessoa) like :nomePessoa ");
			mapaQueryParameter.put("nomePessoa", "%" + nomeManifestante.toUpperCase() + "%");
		}

		if (filtroManifestacao.getIdTipoManifestacao() != null && 
				filtroManifestacao.getIdTipoManifestacao() != 0){
			adicionaClausulaWHERE(filtroQuery, " tp.idTipoManifestacao = :idTipoManifestacao ");
			mapaQueryParameter.put("idTipoManifestacao", filtroManifestacao.getIdTipoManifestacao());
		}
		if (filtroManifestacao.getIdPrioridade() != null && 
				filtroManifestacao.getIdPrioridade() != 0){
			adicionaClausulaWHERE(filtroQuery, " pr.idPrioridade = :idPrioridade ");
			mapaQueryParameter.put("idPrioridade", filtroManifestacao.getIdPrioridade());
		}
		if (filtroManifestacao.getIdStatusManifestacao() != null  && 
				!filtroManifestacao.getIdStatusManifestacao().trim().equals("0")){
			adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao = :idStatusManifestacao ");
			mapaQueryParameter.put("idStatusManifestacao", filtroManifestacao.getIdStatusManifestacao());
		}
		if (filtroManifestacao.getDataCadastro() != null){
			adicionaClausulaWHERE(filtroQuery, " m.dtCadastro BETWEEN :dataCadastroInicial AND  :dataCadastroFinal ");

			registraDataQueryParameter(filtroManifestacao.getDataCadastro(), 
					"dataCadastroInicial", "dataCadastroFinal");
		}
		if (filtroManifestacao.getDataUltimaAtualizacao() != null){
			adicionaClausulaWHERE(filtroQuery, " m.dtUltimaAtualizacao BETWEEN :dataUltimaAtualizacaoInicial AND  :dataUltimaAtualizacaoFinal ");

			registraDataQueryParameter(filtroManifestacao.getDataUltimaAtualizacao(), 
					"dataUltimaAtualizacaoInicial", "dataUltimaAtualizacaoFinal");
		}
		
		return filtroQuery;
	}
	
	
	/**
	 * Registra no mapa de parâmetros da query os valores dos filtros de data inicial e final a serem utilizados
	 *  
	 * @param dataFiltro A data a ser processada
	 * @param nomeParametroInicial o nome do parâmetro para o filtro inicial
	 * @param nomeParametroFinal o nome do parâmetro para o filtro final
	 */
	private void registraDataQueryParameter(Date dataFiltro, String nomeParametroInicial, String nomeParametroFinal){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataFiltro);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		// Define o valor para a data inicial do filtro a ser utilizado
		Date dataFiltroInicial = calendar.getTime();

		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		// Define o valor para a data final do filtro a ser utilizado
		Date dataFiltroFinal = calendar.getTime();

		mapaQueryParameter.put(nomeParametroInicial, dataFiltroInicial);
		mapaQueryParameter.put(nomeParametroFinal, dataFiltroFinal);
		
	}
    
    
    private void adicionaClausulaWHERE(StringBuffer filtroQuery, String clausulaWHERE){
		filtroQuery.append(filtroQuery.length() > "WHERE ".length() ? " and " : " WHERE ");
		filtroQuery.append(clausulaWHERE);
    	
    }

    
	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com o status igual a SOLUCIONADA
	 * Apenas manifestações que estão SOLUCIONADAS, considerando a unidade do usuário (caso seja um OPERADOR ou um INTERLOCUTOR)
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroSolucionadas(StringBuffer filtroQuery,
			TbUsuario usuario) {
    	// Recupera a Função do usuário
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());

		
		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao				= '" + StatusManifestacaoEnum.SOLUCIONADA.getId() + "' ");

    	switch (funcao) {
	    	case OPERADOR:
	    		adicionaClausulaWHERE(filtroQuery, " tra.idUsuarioReceptor		= " + usuario.getIdUsuario());
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_TRAMITE);
	
	    	case INTERLOCUTOR:
	    		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 	= " + usuario.getIdUnidade().getIdUnidade());
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);

	    	break;
    	}

	}


	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com a Ouvidoria
	 * Apenas manifestações que estão RETORNADAS para a Ouvidoria e que foram encaminhadas para a Unidade do usuário
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroComOuvidoria(StringBuffer filtroQuery,
			TbUsuario usuario) {
    	// Recupera a Função do usuário
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
        
        adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao				= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
    	adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.RETORNADA.getId() + "' ");
    	adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 			= " + usuario.getIdUnidade().getIdUnidade());

    	switch (funcao) {
	    	case OPERADOR:
	    		adicionaClausulaWHERE(filtroQuery, " tra.idUsuarioReceptor		= " + usuario.getIdUsuario());
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_TRAMITE);
	
	    	break;
		}

    	configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
	}


    
	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com o status igual a RETORNADA
	 * Apenas manifestações que estão RETORNADA, considerando a unidade do usuário (caso seja um INTERLOCUTOR)
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroRetornadas(StringBuffer filtroQuery,
			TbUsuario usuario) {
    	// Recupera a Função do usuário
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
		
    	adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao				= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
    	adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.RETORNADA.getId() + "' ");

    	switch (funcao) {
	    	case INTERLOCUTOR:
	    		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 	= " + usuario.getIdUnidade().getIdUnidade());

	    	break;
    	}

    	configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
	}

	
	
	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com o status igual a EM_ANDAMENTO
	 * Apenas manifestações que estão EM_ANDAMENTO, considerando a unidade do usuário (caso seja um INTERLOCUTOR)
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroEmAndamento(StringBuffer filtroQuery,
			TbUsuario usuario) {
    	// Recupera a Função do usuário
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
    	
    	adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao				= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
    	adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.ENCAMINHADA.getId() + "' ");

    	switch (funcao) {
	    	case INTERLOCUTOR:
	    		// Encaminhamento para a unidade do INTERLOCUTOR
	    		adicionaClausulaWHERE(filtroQuery, 
	    			"( 	en.idUnidadeRecebeu	 									=  " + usuario.getIdUnidade().getIdUnidade()
	    			+ " OR 	en.idUnidadeEnviou		 							=  " + usuario.getIdUnidade().getIdUnidade()
	    			+ ")" );
	    		// TRÂMITE já tenha sido enviado para um usuário receptor 
	    		adicionaClausulaWHERE(filtroQuery, " tra.idUsuarioReceptor	is not null ");
	    		// TRÂMITE não tenha sido retornado 
	    		adicionaClausulaWHERE(filtroQuery, " tra.stRetornada			=   '" + BooleanEnum.NAO.getId() + "' ");
        		
    		break;
    	}

    	configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
		configuraComplementoAssociacaoEntidade(ASSOCIACAO_TRAMITE);
		configuraComplementoAssociacaoEntidade(ASSOCIACAO_USUARIO_RECEPTOR);
	}

	
	/**
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroDevolvidas(StringBuffer filtroQuery,
			TbUsuario usuario) {
    	// Recupera a Função do usuário
    	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());

    	switch (funcao) {
	    	// A caixa DEVOLVIDAS deve ser exibida apenas para o OPERADOR
    		case OPERADOR:
    			// Manifestações ainda em andamento
	    		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao 			= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
	    		adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.RETORNADA.getId() + "' ");
	    		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 			= " + usuario.getIdUnidade().getIdUnidade());

	    		// Que tenha um trâmite em que o usuário logado tenha recebido e esteja retornado 
	    		adicionaClausulaWHERE(filtroQuery, 
	    				"( tra.idUsuarioReceptor				= " + usuario.getIdUsuario()
	    				+ " AND tra.stRetornada					= '" + BooleanEnum.SIM.getId() + "' "
	    				+ " ) " );

	    		// Que tenha o último trâmite em que o usuário logado tenha enviado para a OUVIDORIA e que ainda esteja sem retorno 
	    		adicionaClausulaWHERE(filtroQuery, 
			    		"( ultimoTramite.idUnidadeEnvio 					= " + UnidadeEnum.OUVIDORIA.getId()   
			    		+ "	AND ultimoTramite.idUsuarioEmissor 				= " + usuario.getIdUsuario()
			    		+ " AND ultimoTramite.stRetornada					=   '" + BooleanEnum.NAO.getId() + "' )" );

	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_TRAMITE);
	    		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ULTIMO_TRAMITE);
	    		
			break;
    	}
		
	}

	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com o status igual a EM_MONITORAMENTO
	 * Apenas manifestações que estão EM_MONITORAMENTO
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroEmMonitoramento(StringBuffer filtroQuery) {
		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao = '" + 
				StatusManifestacaoEnum.EM_MONITORAMENTO.getId() + "' ");
		
	}

	/**
	 * Configura o filtro de pesquisa para recuperar as manifestação que estão com o status igual a SOLICITADA_INFORMACAO
	 * Apenas manifestações que estão SOLICITADA_INFORMACAO
	 * 
	 * @param filtroQuery o texto gerado para o filtro de pesquisa 
	 * @param usuario o usuário que está solicitando a pesquisa
	 */
	private void configuraFiltroSolicitadaInformacao(StringBuffer filtroQuery) {
		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao = '" + 
				StatusManifestacaoEnum.SOLICITADA_INFORMACAO.getId() + "' ");
		
	}

	/**
     * Configura os filtros para carregar a Caixa de Entrada
     * 
     * @param filtroQuery
     * @param usuario
     */
	private void configuraFiltroCaixaEntrada(StringBuffer filtroQuery, TbUsuario usuario) {
        if(usuario != null) {
        	// Recupera a Função do usuário
        	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
        	
        	switch (funcao) {
        	case OPERADOR:
        		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao				= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
        		adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.ENCAMINHADA.getId() + "' ");
        		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 			= " + usuario.getIdUnidade().getIdUnidade());
        		adicionaClausulaWHERE(filtroQuery, " tra.idUsuarioReceptor				= " + usuario.getIdUsuario());
        		adicionaClausulaWHERE(filtroQuery, " tra.stRetornada	 				= '" + BooleanEnum.NAO.getId() + "' ");
        		
        		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
        		configuraComplementoAssociacaoEntidade(ASSOCIACAO_TRAMITE);
        		break;
        		
        	case INTERLOCUTOR:
        		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao 			= '" + StatusManifestacaoEnum.EM_ANDAMENTO.getId() + "' ");
        		adicionaClausulaWHERE(filtroQuery, " en.stEncaminhamento 				= '" + StatusEncaminhamentoEnum.ENCAMINHADA.getId() + "' ");
        		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeEnviou		 			<> " + usuario.getIdUnidade().getIdUnidade());
        		adicionaClausulaWHERE(filtroQuery, " en.idUnidadeRecebeu	 			=  " + usuario.getIdUnidade().getIdUnidade());
        		
        		configuraComplementoAssociacaoEntidade(ASSOCIACAO_ENCAMINHAMENTOS);
        		/*
        		 * Complementa a query INCLUIDO todos os encaminhamento para os quais 
        		 * - o ÚLTIMO TRÂMITE não tenha sido retornado e que
        		 * - o ÚLTIMO TRÂMITE ainda não tenha sido enviado para um usuário receptor (UM OPERADOR DA UNIDADE) 
        		 */
        		StringBuffer complementoSQLUltimoTramiteAberto = new StringBuffer();
        		complementoSQLUltimoTramiteAberto.append( 
        				"( SELECT distinct tram.idEncaminhamento "
        				+ " FROM TbTramite tram "
        				+ " WHERE tram.idTramite in( "
        				// Seleciona o último Trâmite
        				+ " 	SELECT ultimoTramite.idTramite "
        				+ "    		FROM vwUltimoTramite ultimoTramite  "
        				+ "			WHERE ultimoTramite.idUsuarioReceptor is null "
        				+ "	) "   
        				+ " and tram.stRetornada	=   '" + BooleanEnum.NAO.getId() + "' )");

        		adicionaClausulaWHERE(filtroQuery, " en.idEncaminhamento 				in " + complementoSQLUltimoTramiteAberto);
        		
        		break;
        		
        	case MANIFESTANTE:
        		adicionaClausulaWHERE(filtroQuery, " m.idUsuarioManifestante 			= " + usuario.getIdUsuario());
        		break;
        		
        	case ADMINISTRADOR:
        	case OUVIDOR:
        		adicionaClausulaWHERE(filtroQuery, " m.stStatusManifestacao 		in ('" + 
        				StatusManifestacaoEnum.NOVA.getId() + "', '" +
        				StatusManifestacaoEnum.EM_ANALISE.getId() + "', '" +
        				StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA.getId() + "') ");
        		break;
        	}
        	
        }

	}

	/**
     * Oculta uma manifestação representada pelo atributo da classe DTOManifestacao.
     * 
     * 
     * @param manifestacaoSelecionada
     */
	public void ocultar(DTOManifestacao manifestacaoSelecionada) throws Exception{
		// Recuperar a manifestação
		TbManifestacao manifestacao =  manifestacaoDAO.getManifestacaoPorNumero(manifestacaoSelecionada.getNumeroManifestacao());
		// Configurar a propriedade StStatusOcultacao da manifestação para SIM
        manifestacao.setStStatusOcultacao(BooleanEnum.SIM.getId());
        // Configurar o texto do motivo da ocultação
        manifestacao.setDsMotivoOcultacao(manifestacaoSelecionada.getMotivoOcultacao());
        // Atualizar a manifestação informada
        edit(manifestacao);
        
	}    
	
	
    /**
     * Oculta uma manifestação representada pelo atributo da classe DTOManifestacao.
     * 
     * 
     * @param manifestacaoSelecionada
     */
	public void desocultar(DTOManifestacao manifestacaoSelecionada) throws Exception{
		// Recuperar a manifestação
		TbManifestacao manifestacao =  manifestacaoDAO.getManifestacaoPorNumero(manifestacaoSelecionada.getNumeroManifestacao());
		// Configurar a propriedade StStatusOcultacao da manifestação PARA NÃO
        manifestacao.setStStatusOcultacao(BooleanEnum.NAO.getId());
		// Limpar o texto do motivo da ocultação
        manifestacao.setDsMotivoOcultacao("");
        // Atualizar a manifestação informada
        edit(manifestacao);
        
	}    

}
