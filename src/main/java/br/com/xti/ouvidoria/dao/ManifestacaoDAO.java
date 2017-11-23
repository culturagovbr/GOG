package br.com.xti.ouvidoria.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.FiltroHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbAreaEntrada;
import br.com.xti.ouvidoria.model.TbAreaEntrada_;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbClassificacao_;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbEncaminhamento_;
import br.com.xti.ouvidoria.model.TbFaixaEtaria;
import br.com.xti.ouvidoria.model.TbFaixaEtaria_;
import br.com.xti.ouvidoria.model.TbGrauInstrucao;
import br.com.xti.ouvidoria.model.TbGrauInstrucao_;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbManifestacao_;
import br.com.xti.ouvidoria.model.TbMeioEntrada;
import br.com.xti.ouvidoria.model.TbMeioEntrada_;
import br.com.xti.ouvidoria.model.TbPais;
import br.com.xti.ouvidoria.model.TbPais_;
import br.com.xti.ouvidoria.model.TbPrioridade;
import br.com.xti.ouvidoria.model.TbPrioridade_;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao_;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;
import br.com.xti.ouvidoria.model.TbTipoManifestacao_;
import br.com.xti.ouvidoria.model.TbTramite;
import br.com.xti.ouvidoria.model.TbTramite_;
import br.com.xti.ouvidoria.model.TbUF;
import br.com.xti.ouvidoria.model.TbUF_;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUnidade_;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.TbUsuario_;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;

/**
 * @author renato
 */
@Stateless
public class ManifestacaoDAO extends AbstractDAO<TbManifestacao> {

    @EJB
    private VwUltimoTramiteDAO vwUltimoTramiteDAO;

    public ManifestacaoDAO() {
        super(TbManifestacao.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Manifestação";
    }

    public FiltroPersonalizado getFiltroCaixaEntrada(TbUsuario usuario) {
    	return FiltroHelper.getFiltroCaixaEntrada(usuario);
    }
    
    public List<TbManifestacao> getCaixaEntrada(TbUsuario usuario) {
        FiltroPersonalizado filtro = FiltroHelper.getFiltroCaixaEntrada(usuario);
        return getManifestacoes(filtro);
    }

	public TbManifestacao getManifestacaoPorNumero(int numeroManifestacao) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("nrManifestacao", numeroManifestacao);
            return getObject("findByNrManifestacao", map);
        } catch (Exception ex) {
            Logger.getLogger(ManifestacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

	public TbManifestacao getManifestacaoPorNumeroSenha(int numeroManifestacao, String senha) throws Exception {
        String select = "SELECT t FROM TbManifestacao t WHERE t.nrManifestacao = :nrManifestacao AND t.dsSenha = :dsSenha";

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nrManifestacao", numeroManifestacao);
        map.put("dsSenha", senha);
        TbManifestacao tbManifestacao = selectObject(select, map);

        return tbManifestacao;
    }
	
	
    /**
     * Retorna uma lista de manifestações que tenham um encaminhamento feito para 
     * uma unidade específica e que não tenha sido transferida para nenhum operador
     * pelo interlocutor responsável pela Unidade. Ou seja, o encaminhamento está parado
     * na unidade porém sem nenhum responsável por ela.
     * 
     * @param idUnidade Id da Unidade
     * @return Lista de Manifestações
     */
    @SuppressWarnings("unchecked")
	public List<TbManifestacao> getManifestacoesEncaminhadasSemAcaoPorUnidade(int idUnidade) {
        StringBuilder nativeQuery = new StringBuilder("SELECT DISTINCT m.idManifestacao FROM tbManifestacao m ");
        nativeQuery.append("INNER JOIN tbEncaminhamento e ON e.idManifestacao = m.idManifestacao ");
        nativeQuery.append("INNER JOIN vwUltimoTramite ut ON ut.idManifestacao = m.idManifestacao ");
        nativeQuery.append("WHERE e.idUnidadeEnvio = ? AND ut.idUsuarioReceptor IS NULL");
        
        Query query = getEntityManager().createNativeQuery(nativeQuery.toString());
        query.setParameter(1, idUnidade);
        
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setManIdLista(query.getResultList());
        return getManifestacoes(filtro);
    }

    public List<TbManifestacao> getManifestacoes(FiltroPersonalizado... filtrosPersonalizados) {
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
                Predicate[] predicates = getFiltros(cb, m, filtro);
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
            query.where(restricoesLista.toArray(new Predicate[restricoesLista.size()]));

            // Retorna as manifestações encontradas
            TypedQuery<TbManifestacao> selectQuery = getEntityManager().createQuery(query);
            return selectQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    public Predicate[] getFiltros(CriteriaBuilder cb, Root<TbManifestacao> m, FiltroPersonalizado filtro) {
    	// Lista que recebe todas as restrições do filtro escolhido
    	List<Predicate> restricoesLista = new ArrayList<>();

    	// Define os JOINs com as tabelas para ser realizado os filtros
    	
        Join<TbManifestacao, TbUsuario> joinManifestante = m.join(TbManifestacao_.idUsuarioManifestante, JoinType.LEFT);
        Join<TbManifestacao, TbUnidade> joinAreaSolucionadora = m.join(TbManifestacao_.tbUnidadeAreaSolucionadoraCollection, JoinType.LEFT);
        Join<TbManifestacao, TbTipoManifestacao> joinTipoManifestacao = m.join(TbManifestacao_.idTipoManifestacao, JoinType.LEFT);
        Join<TbManifestacao, TbSubClassificacao> joinSubClassificacao = m.join(TbManifestacao_.tbSubClassificacaoCollection, JoinType.LEFT);
        Join<TbManifestacao, TbClassificacao> joinClassificacao = m.join(TbManifestacao_.tbClassificacaoCollection, JoinType.LEFT);
        Join<TbManifestacao, TbPrioridade> joinPrioridade = m.join(TbManifestacao_.idPrioridade, JoinType.LEFT);
        Join<TbManifestacao, TbPais> joinPais = m.join(TbManifestacao_.idPais, JoinType.LEFT);
        Join<TbManifestacao, TbUF> joinUF = m.join(TbManifestacao_.idUF, JoinType.LEFT);
        Join<TbManifestacao, TbMeioEntrada> joinMeioEntrada = m.join(TbManifestacao_.idMeioEntrada, JoinType.LEFT);
        Join<TbManifestacao, TbFaixaEtaria> joinFaixaEtaria = m.join(TbManifestacao_.idFaixaEtaria, JoinType.LEFT);
        Join<TbManifestacao, TbGrauInstrucao> joinGrauInstrucao = m.join(TbManifestacao_.idGrauInstrucao, JoinType.LEFT);
        Join<TbManifestacao, TbAreaEntrada> joinAreaEntrada = m.join(TbManifestacao_.idAreaEntrada, JoinType.LEFT);

        Join<TbManifestacao, TbEncaminhamento> joinEncaminhamento = m.join(TbManifestacao_.tbEncaminhamentoCollection, JoinType.LEFT);
        Join<TbEncaminhamento, TbUnidade> joinUnidadeEnviou = joinEncaminhamento.join(TbEncaminhamento_.idUnidadeEnviou, JoinType.LEFT);
        Join<TbEncaminhamento, TbUnidade> joinUnidadeRecebeu = joinEncaminhamento.join(TbEncaminhamento_.idUnidadeRecebeu, JoinType.LEFT);
        Join<TbEncaminhamento, TbUsuario> joinUsuarioEnviou = joinEncaminhamento.join(TbEncaminhamento_.idUsuarioEnviou, JoinType.LEFT);
        Join<TbEncaminhamento, TbTramite> joinTramite = joinEncaminhamento.join(TbEncaminhamento_.tbTramiteCollection, JoinType.LEFT);
        

        // Id da Manifestação
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdRangeDe()) && ValidacaoHelper.isNotEmpty(filtro.getManIdRangeAte())) {
            restricoesLista.add(cb.between(m.get(TbManifestacao_.nrManifestacao), filtro.getManIdRangeDe(), filtro.getManIdRangeAte()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdLista())) {
            restricoesLista.add(m.get(TbManifestacao_.idManifestacao).in(filtro.getManIdLista()));
        }
        // Entre Datas
        if (filtro.getManDataCadastroDe() != null && filtro.getManDataCadastroAte() != null) {
            restricoesLista.add(cb.between(m.get(TbManifestacao_.dtCadastro), DataHelper.getDataMin(filtro.getManDataCadastroDe()), DataHelper.getDataMax(filtro.getManDataCadastroAte())));
        }
        if (filtro.getManDataAtualizacaoDe() != null && filtro.getManDataAtualizacaoAte() != null) {
            restricoesLista.add(cb.between(m.get(TbManifestacao_.dtUltimaAtualizacao), DataHelper.getDataMin(filtro.getManDataAtualizacaoDe()), DataHelper.getDataMax(filtro.getManDataAtualizacaoAte())));
        }
        if (filtro.getManDataFechamentoDe() != null && filtro.getManDataFechamentoAte() != null) {
            restricoesLista.add(cb.between(m.get(TbManifestacao_.dtFechamento), DataHelper.getDataMin(filtro.getManDataFechamentoDe()), DataHelper.getDataMax(filtro.getManDataFechamentoAte())));
        }
        // Tipo Manifestação
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdTipo())) {
            restricoesLista.add(joinTipoManifestacao.get(TbTipoManifestacao_.idTipoManifestacao).in(filtro.getManIdTipo()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManTipo())) {
            restricoesLista.add(cb.like(cb.upper(joinTipoManifestacao.get(TbTipoManifestacao_.nmTipoManifestacao)), "%" + filtro.getManTipo().toUpperCase() + "%"));
        }
        // Status Manifestação
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdStatus())) {
            restricoesLista.add(m.get(TbManifestacao_.stStatusManifestacao).in(filtro.getManIdStatus()));
        }
        // País
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdPais())) {
            restricoesLista.add(cb.equal(joinPais.get(TbPais_.idPais), filtro.getManIdPais()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManPais())) {
            restricoesLista.add(cb.like(cb.upper(joinPais.get(TbPais_.nmPais)), "%" + filtro.getManPais().toUpperCase() + "%"));
        }
        // Estado
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdEstado())) {
            restricoesLista.add(joinUF.get(TbUF_.idUF).in(filtro.getManIdEstado()));
        }

        /*if (ValidacaoHelper.isNotEmpty(filtro.getManLocalidade())) {
            restricoesLista.add(m.get(TbManifestacao_.dsLocalidade).in(filtro.getManLocalidade()));
        }*/

        // Faixa Etária
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdFaixaEtaria())) {
            restricoesLista.add(joinFaixaEtaria.get(TbFaixaEtaria_.idFaixaEtaria).in(filtro.getManIdFaixaEtaria()));
        }
        // Grau de Instrução
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdGrauInstrucao())) {
            restricoesLista.add(joinGrauInstrucao.get(TbGrauInstrucao_.idGrauInstrucao).in(filtro.getManIdGrauInstrucao()));
        }
        // Raça
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdRaca())) {
            restricoesLista.add(m.get(TbManifestacao_.tpRaca).in(filtro.getManIdRaca()));
        }
        // Sexo
        if (ValidacaoHelper.isNotEmpty(filtro.getManSexo()) && !filtro.getManSexo().equals("0")) {
            restricoesLista.add(cb.equal(m.get(TbManifestacao_.tpSexo), filtro.getManSexo()));
        }
        // Meio Entrada
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdMeioEntrada())) {
            restricoesLista.add(joinMeioEntrada.get(TbMeioEntrada_.idMeioEntrada).in(filtro.getManIdMeioEntrada()));
        }
        // Área Entrada
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdAreaEntrada())) {
            restricoesLista.add(joinAreaEntrada.get(TbAreaEntrada_.idAreaEntrada).in(filtro.getManIdAreaEntrada()));
        }
        // Classificacao
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdClassificacao())) {
            restricoesLista.add(joinClassificacao.get(TbClassificacao_.idClassificacao).in(filtro.getManIdClassificacao()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManClassificacao())) {
            restricoesLista.add(cb.like(cb.upper(joinClassificacao.get(TbClassificacao_.dsClassificacao)), "%" + filtro.getManClassificacao().toUpperCase() + "%"));
        }
        // Sub-Classificacao
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdSubClassificacao())) {
            restricoesLista.add(joinSubClassificacao.get(TbSubClassificacao_.idSubClassificacao).in(filtro.getManIdSubClassificacao()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManSubClassificacao())) {
            restricoesLista.add(cb.like(cb.upper(joinSubClassificacao.get(TbSubClassificacao_.dsSubClassificacao)), "%" + filtro.getManClassificacao().toUpperCase() + "%"));
        }
        // Prioridade
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdPrioridade())) {
            restricoesLista.add(joinPrioridade.get(TbPrioridade_.idPrioridade).in(filtro.getManIdPrioridade()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManPrioridade())) {
            restricoesLista.add(cb.like(cb.upper(joinPrioridade.get(TbPrioridade_.nmPrioridade)), "%" + filtro.getManPrioridade().toUpperCase() + "%"));
        }
        // Área Solucionadora
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdAreaSolucionadora())) {
            restricoesLista.add(joinAreaSolucionadora.get(TbUnidade_.idUnidade).in(filtro.getManIdAreaSolucionadora()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getManAreaSolucionadora())) {
            restricoesLista.add(cb.or(
                    cb.like(cb.upper(joinAreaSolucionadora.get(TbUnidade_.nmUnidade)), "%" + filtro.getManAreaSolucionadora().toUpperCase() + "%"),
                    cb.like(cb.upper(joinAreaSolucionadora.get(TbUnidade_.sgUnidade)), "%" + filtro.getManAreaSolucionadora().toUpperCase() + "%")));
        }
        // Sigilo (dos dados)
        if (ValidacaoHelper.isNotEmpty(filtro.getManSigilo())) {
            restricoesLista.add(cb.equal(m.get(TbManifestacao_.siSigilo), filtro.getManSigilo()));
        }

        // Status da ocultação da manifestacao
        if (filtro.getManOculto() != null) {
            restricoesLista.add(cb.equal(m.get(TbManifestacao_.stStatusOcultacao), filtro.getManOculto()));
        }
        // Resposta (deseja ser notificado)
        if (ValidacaoHelper.isNotEmpty(filtro.getManResposta())) {
            restricoesLista.add(cb.equal(m.get(TbManifestacao_.stResposta), filtro.getManResposta()));
        }
        // Manifestante
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdManifestante())) {
            restricoesLista.add(cb.equal(joinManifestante.get(TbUsuario_.idUsuario), filtro.getManIdManifestante()));
        }
        // Usuário Criador
        if (ValidacaoHelper.isNotEmpty(filtro.getManUsuarioCriador())) {
        	restricoesLista.add(m.get(TbManifestacao_.idUsuarioCriador).in(filtro.getManUsuarioCriador()));
        }
        // Usuário Reativou
        if (ValidacaoHelper.isNotEmpty(filtro.getManUsuarioReativou())) {
        	restricoesLista.add(m.get(TbManifestacao_.idUsuarioReativou).in(filtro.getManUsuarioReativou()));
        }
        // Telefone
        if (ValidacaoHelper.isNotEmpty(filtro.getManTelefone())) {
            restricoesLista.add(cb.like(cb.upper(m.get(TbManifestacao_.nrTelefone)), "%" + filtro.getManTelefone().toUpperCase() + "%"));
        }
        // Email
        if (ValidacaoHelper.isNotEmpty(filtro.getManEmail())) {
            restricoesLista.add(cb.or(
                    cb.like(cb.upper(m.get(TbManifestacao_.eeEmailUsuario)), "%" + filtro.getManEmail().toUpperCase() + "%"),
                    cb.like(cb.upper(m.get(TbManifestacao_.eeEmailSecundario)), "%" + filtro.getManEmail().toUpperCase() + "%")));
        }
        // Endereço
        if (ValidacaoHelper.isNotEmpty(filtro.getManEndereco())) {
            restricoesLista.add(cb.like(cb.upper(m.get(TbManifestacao_.enEndereco)), "%" + filtro.getManEndereco().toUpperCase() + "%"));
        }
        // Texto Manifestação
        if (ValidacaoHelper.isNotEmpty(filtro.getManDesc())) {
            restricoesLista.add(cb.like(cb.upper(m.get(TbManifestacao_.dsTextoManifestacao)), "%" + filtro.getManDesc().toUpperCase() + "%"));
        }
        // Número Pronac
        if (ValidacaoHelper.isNotEmpty(filtro.getManNumPronac())) {
        	restricoesLista.add(cb.like(cb.upper(m.get(TbManifestacao_.nrPronac)), "%" + filtro.getManNumPronac().toUpperCase() + "%"));
        }
        // Número Processo
        if (ValidacaoHelper.isNotEmpty(filtro.getManNumProcesso())) {
        	restricoesLista.add(cb.like(cb.upper(m.get(TbManifestacao_.nrProcesso)), "%" + filtro.getManNumProcesso().toUpperCase() + "%"));
        }
        // Tipo Manifestante
        if (ValidacaoHelper.isNotEmpty(filtro.getManIdTipoManifestante())) {
            restricoesLista.add(m.get(TbManifestacao_.tpManifestante).in(filtro.getManIdTipoManifestante()));
        }

        // --------------- ENCAMINHAMENTO/TRÂMITE ---------------
        // Texto Encaminhamento e Trâmite
        if (ValidacaoHelper.isNotEmpty(filtro.getEncDesc())) {
            restricoesLista.add(cb.like(cb.upper(joinEncaminhamento.get(TbEncaminhamento_.dsDescricao)), "%" + filtro.getEncDesc().toUpperCase() + "%"));
            restricoesLista.add(cb.like(cb.upper(joinTramite.get(TbTramite_.dsDescricao)), "%" + filtro.getEncDesc().toUpperCase() + "%"));
        }
        // Status do Encaminhamento
        if (ValidacaoHelper.isNotEmpty(filtro.getEncStatus()) && !filtro.getEncStatus().equals("0")) {
            restricoesLista.add(cb.equal(joinEncaminhamento.get(TbEncaminhamento_.stEncaminhamento), filtro.getEncStatus()));
        }
        // Unidade Realizou Encaminhamento
        if (ValidacaoHelper.isNotEmpty(filtro.getEncIdUnidadeEnviou())) {
        	restricoesLista.add(joinUnidadeEnviou.get(TbUnidade_.idUnidade).in(filtro.getEncIdUnidadeEnviou()));
        }
        // Unidade Recebeu Encaminhamento
        if (ValidacaoHelper.isNotEmpty(filtro.getEncIdUnidadeRecebeu())) {
            restricoesLista.add(joinUnidadeRecebeu.get(TbUnidade_.idUnidade).in(filtro.getEncIdUnidadeRecebeu()));
        }
        if (ValidacaoHelper.isNotEmpty(filtro.getEncUnidade())) {
            restricoesLista.add(cb.or(
                    cb.like(cb.upper(joinUnidadeRecebeu.get(TbUnidade_.nmUnidade)), "%" + filtro.getEncUnidade().toUpperCase() + "%"),
                    cb.like(cb.upper(joinUnidadeRecebeu.get(TbUnidade_.sgUnidade)), "%" + filtro.getEncUnidade().toUpperCase() + "%")));
        }
        // Ouvidor que fez o Encaminhamento
        if (ValidacaoHelper.isNotEmpty(filtro.getEncIdOuvidor())) {
            restricoesLista.add(joinUsuarioEnviou.get(TbUsuario_.idUsuario).in(filtro.getEncIdOuvidor()));
        }
        // Usuário que recebeu o Trâmite
        if (ValidacaoHelper.isNotEmpty(filtro.getEncIdOperador())) {
        	restricoesLista.add(joinTramite.get(TbTramite_.idUsuarioReceptor).get(TbUsuario_.idUsuario).in(filtro.getEncIdOperador()));
        	
        	if(filtro.isEncTramiteNaoRetornado() != null) {
	        	if(filtro.isEncTramiteNaoRetornado())
	        		restricoesLista.add(cb.equal(joinTramite.get(TbTramite_.stRetornada), BooleanEnum.NAO.getId()));
	        	else
	        		restricoesLista.add(cb.equal(joinTramite.get(TbTramite_.stRetornada), BooleanEnum.SIM.getId()));
        	}
        }
        // Entre Datas
        if (filtro.getEncDataEnvioDe() != null && filtro.getEncDataEnvioAte() != null) {
            restricoesLista.add(cb.between(joinEncaminhamento.get(TbEncaminhamento_.dtCriacaoEncaminhamento), DataHelper.getDataMin(filtro.getEncDataEnvioDe()), DataHelper.getDataMax(filtro.getEncDataEnvioAte())));
        }
        if (filtro.getEncDataRespostaDe() != null && filtro.getEncDataRespostaAte() != null) {
            restricoesLista.add(cb.between(joinEncaminhamento.get(TbEncaminhamento_.dtRespostaTramite), DataHelper.getDataMin(filtro.getEncDataRespostaDe()), DataHelper.getDataMax(filtro.getEncDataRespostaAte())));
        }
        
        Predicate[] restricoesArray = restricoesLista.toArray(new Predicate[restricoesLista.size()]);
        return restricoesArray;
    }

	public List<TbManifestacao> getManifestacoesPorTramite(Date dataInicial, Date dataFinal, Integer idUnidade, Integer idUsuario, StatusEncaminhamentoEnum status) {
        String select = "select distinct(enc.idManifestacao) from TbEncaminhamento enc where enc.stEncaminhamento = :status";
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (idUnidade != null && idUnidade != 0) {
            select += " and enc.idUnidadeEnvio.idUnidade=:unidade ";
            params.put("unidade", idUnidade);
        }
        if (idUsuario != null && idUsuario != 0) {

            select += " and enc.idUsuarioOuvidor.idUsuario=:usuario";
            params.put("usuario", idUsuario);
        }
        params.put("status", status.getId());

        if (dataInicial != null) {
            params.put("dataInicial", dataInicial);
            select += " and enc.idManifestacao.dtCadastro >= :dataInicial";
        }

        if (dataFinal != null) {    
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataFinal);
            cal.add(Calendar.DATE, 1);
            params.put("dataFinal", cal.getTime());
            
            select += " and enc.idManifestacao.dtCadastro <= :dataFinal";
        }
        
        List<TbManifestacao> res = new ArrayList<>();
        try {
            res = selectList(select, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res;
    }
    
	public List<TbManifestacao> getManifestacoesEmAnalisePorUsuarioAnalisador(Integer idUsuarioAnalisador) {
        try {
            String select = "SELECT t FROM TbManifestacao t WHERE t.idUsuarioAnalisador.idUsuario = :idUsuario AND t.stStatusManifestacao = :status";
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("idUsuario", idUsuarioAnalisador);
            map.put("status", StatusManifestacaoEnum.EM_ANALISE.getId());
            
            return selectList(select, map);
        } catch(Exception e) {
        }
        return new ArrayList<>();
    }
    
    public List<TbManifestacao> getManifestacoesSolucionadasSemEncaminhamento(Integer idUsuario) {
    	try {
    		StringBuilder select = new StringBuilder()
            .append("SELECT m FROM TbManifestacao m ")
            .append("WHERE SIZE(m.tbEncaminhamentoCollection) = 0 ")
            .append("AND m.stStatusManifestacao = :status ");
    		
    		HashMap<String, Object> map = new HashMap<>();
    		map.put("status", StatusManifestacaoEnum.SOLUCIONADA.getId());
    		
            if(ValidacaoHelper.isNotEmpty(idUsuario)) {
            	select.append("AND m.idUsuarioAnalisador.idUsuario = :idUsuario");
            	map.put("idUsuario", idUsuario);
            }
            
            return selectList(select.toString(), map);
        } catch(Exception e) {
        }
        return new ArrayList<>();
    }
    
}
