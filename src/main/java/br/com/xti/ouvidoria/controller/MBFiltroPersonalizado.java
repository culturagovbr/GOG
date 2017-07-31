package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import br.com.xti.ouvidoria.dao.AreaEntradaDAO;
import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.FaixaEtariaDAO;
import br.com.xti.ouvidoria.dao.FiltroPersonalizadoDAO;
import br.com.xti.ouvidoria.dao.GrauInstrucaoDAO;
import br.com.xti.ouvidoria.dao.MeioEntradaDAO;
import br.com.xti.ouvidoria.dao.PaisDAO;
import br.com.xti.ouvidoria.dao.PrioridadeDAO;
import br.com.xti.ouvidoria.dao.SubClassificacaoDAO;
import br.com.xti.ouvidoria.dao.TipoManifestacaoDAO;
import br.com.xti.ouvidoria.dao.UfDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizadoComparator;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.helper.FiltroHelper;
import br.com.xti.ouvidoria.model.TbAreaEntrada;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbFaixaEtaria;
import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;
import br.com.xti.ouvidoria.model.TbGrauInstrucao;
import br.com.xti.ouvidoria.model.TbMeioEntrada;
import br.com.xti.ouvidoria.model.TbPais;
import br.com.xti.ouvidoria.model.TbPrioridade;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbTipoManifestacao;
import br.com.xti.ouvidoria.model.TbUF;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.RacaEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.security.SecurityService;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBFiltroPersonalizado")
@ViewScoped
public class MBFiltroPersonalizado implements Serializable {

    // Objeto usado para salvar o cache das consultas
    private Map<String, Object> mapCache = new HashMap<>();
    // ---------- Objetos para cadastrar um novo Filtro ----------
    private TbFiltroPersonalizado tbFiltroPersonalizadoNovo = new TbFiltroPersonalizado();
    private FiltroPersonalizado filtroPersonalizadoNovo = new FiltroPersonalizado();
    // Dual List Model's
    private DualListModel<TbTipoManifestacao> listaTipoNovo;
    private DualListModel<StatusManifestacaoEnum> listaStatusManifestacaoNovo;
    private DualListModel<TbUF> listaUfNovo;
    private DualListModel<TbFaixaEtaria> listaFaixaEtariaNovo;
    private DualListModel<TbGrauInstrucao> listaGrauInstrucaoNovo;
    private DualListModel<RacaEnum> listaRacaNovo;
    private DualListModel<TbMeioEntrada> listaMeioEntradaNovo;
    private DualListModel<TbAreaEntrada> listaAreaEntradaNovo;
    private DualListModel<TbUsuario> listaUsuarioCriadorNovo;
    private DualListModel<TbClassificacao> listaClassificacaoNovo;
    private DualListModel<TbSubClassificacao> listaSubClassificacaoNovo;
    private DualListModel<TbPrioridade> listaPrioridadeNovo;
    private DualListModel<TbUnidade> listaAreaSolucionadoraNovo;
    private DualListModel<TbUnidade> listaUnidadeEnvioNovo;
    private DualListModel<TbUsuario> listaOperadorNovo;
    // ---------- Objetos para alterar um filtro ----------
    private TbFiltroPersonalizado tbFiltroPersonalizado = new TbFiltroPersonalizado();
    private FiltroPersonalizado filtroPersonalizado = new FiltroPersonalizado();
    // Dual List Model's
    private DualListModel<TbTipoManifestacao> listaTipo;
    private DualListModel<StatusManifestacaoEnum> listaStatusManifestacao;
    private DualListModel<TbUF> listaUf;
    private DualListModel<TbFaixaEtaria> listaFaixaEtaria;
    private DualListModel<TbGrauInstrucao> listaGrauInstrucao;
    private DualListModel<RacaEnum> listaRaca;
    private DualListModel<TbMeioEntrada> listaMeioEntrada;
    private DualListModel<TbAreaEntrada> listaAreaEntrada;
    private DualListModel<TbUsuario> listaUsuarioCriador;
    private DualListModel<TbClassificacao> listaClassificacao;
    private DualListModel<TbSubClassificacao> listaSubClassificacao;
    private DualListModel<TbPrioridade> listaPrioridade;
    private DualListModel<TbUnidade> listaAreaSolucionadora;
    private DualListModel<TbUnidade> listaUnidadeEnvio;
    private DualListModel<TbUsuario> listaOperador;
    // Objetos DAO
    @EJB
    private FiltroPersonalizadoDAO dao;
    @EJB
    private TipoManifestacaoDAO tipoDAO;
    @EJB
    private UfDAO ufDAO;
    @EJB
    private FaixaEtariaDAO faixaEtariaDAO;
    @EJB
    private GrauInstrucaoDAO grauInstrucaoDAO;
    @EJB
    private MeioEntradaDAO meioEntradaDAO;
    @EJB
    private AreaEntradaDAO areaEntradaDAO;
    @EJB
    private ClassificacaoDAO classificacaoDAO;
    @EJB
    private SubClassificacaoDAO subClassificacaoDAO;
    @EJB
    private PrioridadeDAO prioridadeDAO;
    @EJB
    private UnidadeDAO unidadeDAO;
    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private PaisDAO paisDAO;
    
    @Inject
    private SecurityService securityService;
    
    @PostConstruct
    public void init() {
    	// ---------- Cria lista de dados ----------
    	List<TbTipoManifestacao> listaTipo = tipoDAO.findAll();
    	List<TbUF> listaUF = ufDAO.findAll();
    	List<TbFaixaEtaria> listaFaixaEtaria = faixaEtariaDAO.findAll();
    	List<TbGrauInstrucao> listaGrauInstrucao = grauInstrucaoDAO.findAll();
    	List<TbMeioEntrada> listaMeioEntrada = meioEntradaDAO.findAll();
    	List<TbAreaEntrada> listaAreaEntrada = areaEntradaDAO.findAll();
    	List<TbClassificacao> listaClassificacao = classificacaoDAO.findAll();
    	List<TbSubClassificacao> listaSubClassificacao = subClassificacaoDAO.findAll();
    	List<TbPrioridade> listaPrioridade = prioridadeDAO.findAll();
    	List<TbUnidade> listaUnidade = unidadeDAO.findAll();
    	List<TbUsuario> listaUsuario = usuarioDAO.getUsuariosInternos();
    	
    	// ---------- Ordena as listas ----------
    	Collections.sort(listaTipo);
    	Collections.sort(listaGrauInstrucao);
    	Collections.sort(listaMeioEntrada);
    	Collections.sort(listaAreaEntrada);
    	Collections.sort(listaClassificacao);
    	Collections.sort(listaSubClassificacao);
    	Collections.sort(listaPrioridade);
    	Collections.sort(listaUnidade);
    	Collections.sort(listaUsuario);
    	
        // ---------- Cria caches das consultas ----------
        mapCache.put("listaTipo", listaTipo);
        mapCache.put("listaUf", listaUF);
        mapCache.put("listaFaixaEtaria", listaFaixaEtaria);
        mapCache.put("listaGrauInstrucao", listaGrauInstrucao);
        mapCache.put("listaMeioEntrada", listaMeioEntrada);
        mapCache.put("listaAreaEntrada", listaAreaEntrada);
        mapCache.put("listaClassificacao", listaClassificacao);
        mapCache.put("listaSubClassificacao", listaSubClassificacao);
        mapCache.put("listaPrioridade", listaPrioridade);
        mapCache.put("listaUnidade", listaUnidade);
        mapCache.put("listaUsuario", listaUsuario);
        mapCache.put("listaStatusManifestacao", EnumHelper.getListaStatusManifestacaoOrdenada());
        mapCache.put("listaRaca", EnumHelper.getListaRacaOrdenada());

        // Inicia os objetos com seus valores padrão
        limpar();
    }

    public List<TbFiltroPersonalizado> getTodos() {
    	List<TbFiltroPersonalizado> list = dao.findByUsuario(securityService.getUser());
    	Collections.sort(list, new FiltroPersonalizadoComparator());
        return list;
    }

    public void cadastrar(ActionEvent actionEvent) {
        try {
            TbUsuario usuario = securityService.getUser();

            // Seta as opções dos componentes PickList no Filtro
            // ----- Manifestação -----
            filtroPersonalizadoNovo.setManIdTipo(FiltroHelper.getIdsFromEntityList(listaTipoNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdEstado(FiltroHelper.getIdsFromEntityList(listaUfNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdFaixaEtaria(FiltroHelper.getIdsFromEntityList(listaFaixaEtariaNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdGrauInstrucao(FiltroHelper.getIdsFromEntityList(listaGrauInstrucaoNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdStatus(FiltroHelper.getIdsFromEnumList(listaStatusManifestacaoNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdRaca(FiltroHelper.getIdsFromEnumList(listaRacaNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdMeioEntrada(FiltroHelper.getIdsFromEntityList(listaMeioEntradaNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdAreaEntrada(FiltroHelper.getIdsFromEntityList(listaAreaEntradaNovo.getTarget()));
            filtroPersonalizadoNovo.setManUsuarioCriador(FiltroHelper.getIdsFromEntityList(listaUsuarioCriadorNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdClassificacao(FiltroHelper.getIdsFromEntityList(listaClassificacaoNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdSubClassificacao(FiltroHelper.getIdsFromEntityList(listaSubClassificacaoNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdPrioridade(FiltroHelper.getIdsFromEntityList(listaPrioridadeNovo.getTarget()));
            filtroPersonalizadoNovo.setManIdAreaSolucionadora(FiltroHelper.getIdsFromEntityList(listaAreaSolucionadoraNovo.getTarget()));
            // ----- Encaminhamento -----
            filtroPersonalizadoNovo.setEncIdUnidadeRecebeu(FiltroHelper.getIdsFromEntityList(listaUnidadeEnvioNovo.getTarget()));
            filtroPersonalizadoNovo.setEncIdOperador(FiltroHelper.getIdsFromEntityList(listaOperadorNovo.getTarget()));

            // Cria o XML do filtro
            XStream xs = new XStream();
            String filtroXML = xs.toXML(filtroPersonalizadoNovo);
            tbFiltroPersonalizadoNovo.setIdUsuario(usuario);
            tbFiltroPersonalizadoNovo.setDsParticao(filtroXML);
            getDao().create(tbFiltroPersonalizadoNovo);
            MensagemFaceUtil.info("Sucesso", "Alteração realizada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void alterar(ActionEvent actionEvent) {
        try {
            // Seta as opções dos componentes PickList no Filtro
            // ----- Manifestação -----
            filtroPersonalizado.setManIdTipo(FiltroHelper.getIdsFromEntityList(listaTipo.getTarget()));
            filtroPersonalizado.setManIdEstado(FiltroHelper.getIdsFromEntityList(listaUf.getTarget()));
            filtroPersonalizado.setManIdFaixaEtaria(FiltroHelper.getIdsFromEntityList(listaFaixaEtaria.getTarget()));
            filtroPersonalizado.setManIdGrauInstrucao(FiltroHelper.getIdsFromEntityList(listaGrauInstrucao.getTarget()));
            filtroPersonalizado.setManIdStatus(FiltroHelper.getIdsFromEnumList(listaStatusManifestacao.getTarget()));
            filtroPersonalizado.setManIdRaca(FiltroHelper.getIdsFromEnumList(listaRaca.getTarget()));
            filtroPersonalizado.setManIdMeioEntrada(FiltroHelper.getIdsFromEntityList(listaMeioEntrada.getTarget()));
            filtroPersonalizado.setManIdAreaEntrada(FiltroHelper.getIdsFromEntityList(listaAreaEntrada.getTarget()));
            filtroPersonalizado.setManUsuarioCriador(FiltroHelper.getIdsFromEntityList(listaUsuarioCriador.getTarget()));
            filtroPersonalizado.setManIdClassificacao(FiltroHelper.getIdsFromEntityList(listaClassificacao.getTarget()));
            filtroPersonalizado.setManIdSubClassificacao(FiltroHelper.getIdsFromEntityList(listaSubClassificacao.getTarget()));
            filtroPersonalizado.setManIdPrioridade(FiltroHelper.getIdsFromEntityList(listaPrioridade.getTarget()));
            filtroPersonalizado.setManIdAreaSolucionadora(FiltroHelper.getIdsFromEntityList(listaAreaSolucionadora.getTarget()));
            // ----- Encaminhamento -----
            filtroPersonalizado.setEncIdUnidadeRecebeu(FiltroHelper.getIdsFromEntityList(listaUnidadeEnvio.getTarget()));
            filtroPersonalizado.setEncIdOperador(FiltroHelper.getIdsFromEntityList(listaOperador.getTarget()));

            // Cria o XML do filtro
            XStream xs = new XStream();
            String filtroXML = xs.toXML(filtroPersonalizado);
            tbFiltroPersonalizado.setDsParticao(filtroXML);
            getDao().edit(tbFiltroPersonalizado);
            MensagemFaceUtil.info("Sucesso", "Inclusão realizada com sucesso");
        } catch (Exception e) {
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    public void remover(ActionEvent actionEvent) {
        try {
            if (tbFiltroPersonalizado != null && tbFiltroPersonalizado.getIdFiltroPersonalizado() != null) {
                getDao().remove(tbFiltroPersonalizado);
                MensagemFaceUtil.info("Sucesso", "Filtro Personalizado removido com sucesso");
            }
        } catch (Exception e) {
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro. Entre em contato com o administrador do sistema.");
        }
        limpar();
    }

    @SuppressWarnings("unchecked")
	public void prepararAlterar(TbFiltroPersonalizado filtro) throws Exception {
        XStream xs = new XStream();
        filtroPersonalizado = (FiltroPersonalizado) xs.fromXML(filtro.getDsParticao());
        // Tipo Manifestação
        List<TbTipoManifestacao> tiposDisponiveis = new ArrayList<>((List<TbTipoManifestacao>) mapCache.get("listaTipo"));
        List<TbTipoManifestacao> tiposSelecionados = tipoDAO.findIn(filtroPersonalizado.getManIdTipo());
        tiposDisponiveis.removeAll(tiposSelecionados);
        listaTipo = new DualListModel<>(tiposDisponiveis, tiposSelecionados);
        // UF
        List<TbUF> ufsDisponiveis = new ArrayList<>((List<TbUF>) mapCache.get("listaUf"));
        List<TbUF> ufsSelecionados = ufDAO.findIn(filtroPersonalizado.getManIdEstado());
        ufsDisponiveis.removeAll(ufsSelecionados);
        listaUf = new DualListModel<>(ufsDisponiveis, ufsSelecionados);
        // Faixa Etária
        List<TbFaixaEtaria> faixaDisponiveis = new ArrayList<>((List<TbFaixaEtaria>) mapCache.get("listaFaixaEtaria"));
        List<TbFaixaEtaria> faixaSelecionados = faixaEtariaDAO.findIn(filtroPersonalizado.getManIdFaixaEtaria());
        faixaDisponiveis.removeAll(faixaSelecionados);
        listaFaixaEtaria = new DualListModel<>(faixaDisponiveis, faixaSelecionados);
        // Grau Instrução
        List<TbGrauInstrucao> grauInstrucaoDisponiveis = new ArrayList<>((List<TbGrauInstrucao>) mapCache.get("listaGrauInstrucao"));
        List<TbGrauInstrucao> grauInstrucaoSelecionados = grauInstrucaoDAO.findIn(filtroPersonalizado.getManIdGrauInstrucao());
        grauInstrucaoDisponiveis.removeAll(grauInstrucaoSelecionados);
        listaGrauInstrucao = new DualListModel<>(grauInstrucaoDisponiveis, grauInstrucaoSelecionados);
        // Meio Entrada
        List<TbMeioEntrada> meioDisponiveis = new ArrayList<>((List<TbMeioEntrada>) mapCache.get("listaMeioEntrada"));
        List<TbMeioEntrada> meioSelecionados = meioEntradaDAO.findIn(filtroPersonalizado.getManIdMeioEntrada());
        meioDisponiveis.removeAll(meioSelecionados);
        listaMeioEntrada = new DualListModel<>(meioDisponiveis, meioSelecionados);
        // Meio Entrada
        List<TbAreaEntrada> areaDisponiveis = new ArrayList<>((List<TbAreaEntrada>) mapCache.get("listaAreaEntrada"));
        List<TbAreaEntrada> areaSelecionados = areaEntradaDAO.findIn(filtroPersonalizado.getManIdAreaEntrada());
        areaDisponiveis.removeAll(areaSelecionados);
        listaAreaEntrada = new DualListModel<>(areaDisponiveis, areaSelecionados);
        // Usuario Criador
        List<TbUsuario> usuarioCriadorDisponiveis = new ArrayList<>((List<TbUsuario>) mapCache.get("listaUsuario"));
        List<TbUsuario> usuarioCriadorSelecionados = usuarioDAO.findIn(filtroPersonalizado.getManUsuarioCriador());
        usuarioCriadorDisponiveis.removeAll(usuarioCriadorSelecionados);
        listaUsuarioCriador = new DualListModel<>(usuarioCriadorDisponiveis, usuarioCriadorSelecionados);
        // Classificacao
        List<TbClassificacao> classificacaoDisponiveis = new ArrayList<>((List<TbClassificacao>) mapCache.get("listaClassificacao"));
        List<TbClassificacao> classificacaoSelecionados = classificacaoDAO.findIn(filtroPersonalizado.getManIdClassificacao());
        classificacaoDisponiveis.removeAll(classificacaoSelecionados);
        listaClassificacao = new DualListModel<>(classificacaoDisponiveis, classificacaoSelecionados);
        // Sub Classificacao
        List<TbSubClassificacao> subDisponiveis = new ArrayList<>();
        for (TbClassificacao tbClassificacao : classificacaoSelecionados) {
            subDisponiveis.addAll(tbClassificacao.getTbSubClassificacaoCollection());
        }
        List<TbSubClassificacao> subSelecionados = subClassificacaoDAO.findIn(filtroPersonalizado.getManIdSubClassificacao());
        subDisponiveis.removeAll(subSelecionados);
        listaSubClassificacao = new DualListModel<>(subDisponiveis, subSelecionados);
        // Prioridade
        List<TbPrioridade> prioridadeDisponiveis = new ArrayList<>((List<TbPrioridade>) mapCache.get("listaPrioridade"));
        List<TbPrioridade> prioridadeSelecionados = prioridadeDAO.findIn(filtroPersonalizado.getManIdPrioridade());
        prioridadeDisponiveis.removeAll(prioridadeSelecionados);
        listaPrioridade = new DualListModel<>(prioridadeDisponiveis, prioridadeSelecionados);
        // Area Solucionadora
        List<TbUnidade> areaSolucionadoraDisponiveis = new ArrayList<>((List<TbUnidade>) mapCache.get("listaUnidade"));
        List<TbUnidade> areaSolucionadoraSelecionados = unidadeDAO.findIn(filtroPersonalizado.getManIdAreaSolucionadora());
        areaSolucionadoraDisponiveis.removeAll(areaSolucionadoraSelecionados);
        listaAreaSolucionadora = new DualListModel<>(areaSolucionadoraDisponiveis, areaSolucionadoraSelecionados);
        // Unidade de Envio
        List<TbUnidade> unidadeEnvioDisponiveis = new ArrayList<>((List<TbUnidade>) mapCache.get("listaUnidade"));
        List<TbUnidade> unidadeEnvioSelecionados = unidadeDAO.findIn(filtroPersonalizado.getEncIdUnidadeRecebeu());
        unidadeEnvioDisponiveis.removeAll(unidadeEnvioSelecionados);
        listaUnidadeEnvio = new DualListModel<>(unidadeEnvioDisponiveis, unidadeEnvioSelecionados);
        // Operador
        List<TbUsuario> operadorDisponiveis = new ArrayList<>((List<TbUsuario>) mapCache.get("listaUsuario"));
        List<TbUsuario> operadorSelecionados = usuarioDAO.findIn(filtroPersonalizado.getEncIdOperador());
        operadorDisponiveis.removeAll(operadorSelecionados);
        listaOperador = new DualListModel<>(operadorDisponiveis, operadorSelecionados);
        // Status manifestação - ENUM
        MBEnum mBEnum = new MBEnum();
        List<StatusManifestacaoEnum> statusDisponiveis = new ArrayList<StatusManifestacaoEnum>(EnumHelper.getListaStatusManifestacaoOrdenada());
        List<StatusManifestacaoEnum> statusSelecionados = new ArrayList<>();
        List<String> statusSelecionadosString = filtroPersonalizado.getManIdStatus();
        for (String status : statusSelecionadosString) {
            statusSelecionados.add(mBEnum.getStatusManifestacaoEnum(status));
        }
        statusDisponiveis.removeAll(statusSelecionados);
        listaStatusManifestacao = new DualListModel<>(statusDisponiveis, statusSelecionados);
        // Raça - ENUM
        List<RacaEnum> racaDisponiveis = new ArrayList<RacaEnum>(EnumHelper.getListaRacaOrdenada());
        List<RacaEnum> racaSelecionados = new ArrayList<>();
        List<String> racaSelecionadosString = filtroPersonalizado.getManIdRaca();
        for (String raca : racaSelecionadosString) {
            racaSelecionados.add(mBEnum.getTipoRacaEnum(raca));
        }
        racaDisponiveis.removeAll(racaSelecionados);
        listaRaca = new DualListModel<>(racaDisponiveis, racaSelecionados);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void limpar() {
        // Limpa os objetos Novo
        tbFiltroPersonalizadoNovo = new TbFiltroPersonalizado();
        filtroPersonalizadoNovo = new FiltroPersonalizado();
        filtroPersonalizadoNovo.setMetodoBusca("and");
        listaTipoNovo = new DualListModel<>((List<TbTipoManifestacao>) mapCache.get("listaTipo"), new ArrayList());
        listaStatusManifestacaoNovo = new DualListModel<>((List<StatusManifestacaoEnum>) mapCache.get("listaStatusManifestacao"), new ArrayList());
        
        if(securityService.isInterlocutor() || securityService.isOperador()){
            ArrayList lista = new ArrayList(listaStatusManifestacaoNovo.getSource());
            lista.remove(StatusManifestacaoEnum.NOVA);
            lista.remove(StatusManifestacaoEnum.EM_ANALISE);
            listaStatusManifestacaoNovo.setSource(lista);
        }
        
        listaUfNovo = new DualListModel<>((List<TbUF>) mapCache.get("listaUf"), new ArrayList());
        listaFaixaEtariaNovo = new DualListModel<>((List<TbFaixaEtaria>) mapCache.get("listaFaixaEtaria"), new ArrayList());
        listaGrauInstrucaoNovo = new DualListModel<>((List<TbGrauInstrucao>) mapCache.get("listaGrauInstrucao"), new ArrayList());
        listaRacaNovo = new DualListModel<>((List<RacaEnum>) mapCache.get("listaRaca"), new ArrayList());
        listaMeioEntradaNovo = new DualListModel<>((List<TbMeioEntrada>) mapCache.get("listaMeioEntrada"), new ArrayList());
        listaAreaEntradaNovo = new DualListModel<>((List<TbAreaEntrada>) mapCache.get("listaAreaEntrada"), new ArrayList());
        listaUsuarioCriadorNovo = new DualListModel<>((List<TbUsuario>) mapCache.get("listaUsuario"), new ArrayList());
        listaClassificacaoNovo = new DualListModel<>((List<TbClassificacao>) mapCache.get("listaClassificacao"), new ArrayList());
        listaSubClassificacaoNovo = new DualListModel<>(new ArrayList(), new ArrayList());
        listaPrioridadeNovo = new DualListModel<>((List<TbPrioridade>) mapCache.get("listaPrioridade"), new ArrayList());
        listaAreaSolucionadoraNovo = new DualListModel<>((List<TbUnidade>) mapCache.get("listaUnidade"), new ArrayList());
        listaUnidadeEnvioNovo = new DualListModel<>((List<TbUnidade>) mapCache.get("listaUnidade"), new ArrayList());
        listaOperadorNovo = new DualListModel<>((List<TbUsuario>) mapCache.get("listaUsuario"), new ArrayList());

        // Limpa os objetos de Alteração
        tbFiltroPersonalizado = new TbFiltroPersonalizado();
        filtroPersonalizado = new FiltroPersonalizado();
        listaTipo = new DualListModel<>(new ArrayList(), new ArrayList());
        listaStatusManifestacao = new DualListModel<>(new ArrayList(), new ArrayList());
        listaUf = new DualListModel<>(new ArrayList(), new ArrayList());
        listaFaixaEtaria = new DualListModel<>(new ArrayList(), new ArrayList());
        listaGrauInstrucao = new DualListModel<>(new ArrayList(), new ArrayList());
        listaRaca = new DualListModel<>(new ArrayList(), new ArrayList());
        listaMeioEntrada = new DualListModel<>(new ArrayList(), new ArrayList());
        listaAreaEntrada = new DualListModel<>(new ArrayList(), new ArrayList());
        listaUsuarioCriador = new DualListModel<>(new ArrayList(), new ArrayList());
        listaClassificacao = new DualListModel<>(new ArrayList(), new ArrayList());
        listaSubClassificacao = new DualListModel<>(new ArrayList(), new ArrayList());
        listaPrioridade = new DualListModel<>(new ArrayList(), new ArrayList());
        listaAreaSolucionadora = new DualListModel<>(new ArrayList(), new ArrayList());
        listaUnidadeEnvio = new DualListModel<>(new ArrayList(), new ArrayList());
        listaOperador = new DualListModel<>(new ArrayList(), new ArrayList());
    }

    public Boolean isBrasil(int idPais) {
        TbPais pais = paisDAO.find(idPais);
        return (pais != null && pais.getNmPais().equalsIgnoreCase("brasil"));
    }

    @SuppressWarnings("unchecked")
	public void onTransferNovo(TransferEvent event) {
        List<TbClassificacao> classificacoesEscolhidas = (List<TbClassificacao>) event.getItems();
        List<TbSubClassificacao> subClassificacoes = new ArrayList<>(listaSubClassificacaoNovo.getSource());
        List<TbSubClassificacao> subClassificacoesEscolhidas = new ArrayList<>(listaSubClassificacaoNovo.getTarget());

        if (event.isAdd()) {
            for (TbClassificacao tbClassificacao : classificacoesEscolhidas) {
                subClassificacoes.addAll(tbClassificacao.getTbSubClassificacaoCollection());
            }
            listaSubClassificacaoNovo.setSource(subClassificacoes);
        } else if (event.isRemove()) {
            for (TbClassificacao tbClassificacao : classificacoesEscolhidas) {
                subClassificacoes.removeAll(tbClassificacao.getTbSubClassificacaoCollection());
                subClassificacoesEscolhidas.removeAll(tbClassificacao.getTbSubClassificacaoCollection());
            }
            listaSubClassificacaoNovo.setSource(subClassificacoes);
            listaSubClassificacaoNovo.setTarget(subClassificacoesEscolhidas);
        }
    }

    @SuppressWarnings("unchecked")
	public void onTransfer(TransferEvent event) {
        List<TbClassificacao> classificacoesEscolhidas = (List<TbClassificacao>) event.getItems();
        List<TbSubClassificacao> subClassificacoes = new ArrayList<>(listaSubClassificacao.getSource());
        List<TbSubClassificacao> subClassificacoesEscolhidas = new ArrayList<>(listaSubClassificacao.getTarget());

        if (event.isAdd()) {
            for (TbClassificacao tbClassificacao : classificacoesEscolhidas) {
                subClassificacoes.addAll(tbClassificacao.getTbSubClassificacaoCollection());
            }
            listaSubClassificacao.setSource(subClassificacoes);
        } else if (event.isRemove()) {
            for (TbClassificacao tbClassificacao : classificacoesEscolhidas) {
                subClassificacoes.removeAll(tbClassificacao.getTbSubClassificacaoCollection());
                subClassificacoesEscolhidas.removeAll(tbClassificacao.getTbSubClassificacaoCollection());
            }
            listaSubClassificacao.setSource(subClassificacoes);
            listaSubClassificacao.setTarget(subClassificacoesEscolhidas);
        }
    }

    // ---------- GETTERS e SETTERS ----------
    public TbFiltroPersonalizado getTbFiltroPersonalizadoNovo() {
        return tbFiltroPersonalizadoNovo;
    }

    public void setTbFiltroPersonalizadoNovo(TbFiltroPersonalizado tbFiltroPersonalizadoNovo) {
        this.tbFiltroPersonalizadoNovo = tbFiltroPersonalizadoNovo;
    }

    public FiltroPersonalizado getFiltroPersonalizadoNovo() {
        return filtroPersonalizadoNovo;
    }

    public void setFiltroPersonalizadoNovo(FiltroPersonalizado filtroPersonalizadoNovo) {
        this.filtroPersonalizadoNovo = filtroPersonalizadoNovo;
    }

    public DualListModel<TbTipoManifestacao> getListaTipoNovo() {
        return listaTipoNovo;
    }

    public void setListaTipoNovo(DualListModel<TbTipoManifestacao> listaTipoNovo) {
        this.listaTipoNovo = listaTipoNovo;
    }

    public DualListModel<StatusManifestacaoEnum> getListaStatusManifestacaoNovo() {
        return listaStatusManifestacaoNovo;
    }

    public void setListaStatusManifestacaoNovo(DualListModel<StatusManifestacaoEnum> listaStatusManifestacaoNovo) {
        this.listaStatusManifestacaoNovo = listaStatusManifestacaoNovo;
    }

    public DualListModel<TbUF> getListaUfNovo() {
        return listaUfNovo;
    }

    public void setListaUfNovo(DualListModel<TbUF> listaUfNovo) {
        this.listaUfNovo = listaUfNovo;
    }
    
    public DualListModel<TbFaixaEtaria> getListaFaixaEtariaNovo() {
        return listaFaixaEtariaNovo;
    }

    public void setListaFaixaEtariaNovo(DualListModel<TbFaixaEtaria> listaFaixaEtariaNovo) {
        this.listaFaixaEtariaNovo = listaFaixaEtariaNovo;
    }
    
     public DualListModel<TbGrauInstrucao> getListaGrauInstrucaoNovo() {
        return listaGrauInstrucaoNovo;
    }

    public void setListaGrauInstrucaoNovo(DualListModel<TbGrauInstrucao> listaGrauInstrucaoNovo) {
        this.listaGrauInstrucaoNovo = listaGrauInstrucaoNovo;
    }

    public DualListModel<RacaEnum> getListaRacaNovo() {
        return listaRacaNovo;
    }

    public void setListaRacaNovo(DualListModel<RacaEnum> listaRacaNovo) {
        this.listaRacaNovo = listaRacaNovo;
    }

    public DualListModel<TbMeioEntrada> getListaMeioEntradaNovo() {
        return listaMeioEntradaNovo;
    }

    public void setListaMeioEntradaNovo(DualListModel<TbMeioEntrada> listaMeioEntradaNovo) {
        this.listaMeioEntradaNovo = listaMeioEntradaNovo;
    }

    public DualListModel<TbAreaEntrada> getListaAreaEntradaNovo() {
        return listaAreaEntradaNovo;
    }

    public void setListaAreaEntradaNovo(DualListModel<TbAreaEntrada> listaAreaEntradaNovo) {
        this.listaAreaEntradaNovo = listaAreaEntradaNovo;
    }

    public DualListModel<TbClassificacao> getListaClassificacaoNovo() {
        return listaClassificacaoNovo;
    }

    public void setListaClassificacaoNovo(DualListModel<TbClassificacao> listaClassificacaoNovo) {
        this.listaClassificacaoNovo = listaClassificacaoNovo;
    }

    public DualListModel<TbSubClassificacao> getListaSubClassificacaoNovo() {
        return listaSubClassificacaoNovo;
    }

    public void setListaSubClassificacaoNovo(DualListModel<TbSubClassificacao> listaSubClassificacaoNovo) {
        this.listaSubClassificacaoNovo = listaSubClassificacaoNovo;
    }

    public DualListModel<TbPrioridade> getListaPrioridadeNovo() {
        return listaPrioridadeNovo;
    }

    public void setListaPrioridadeNovo(DualListModel<TbPrioridade> listaPrioridadeNovo) {
        this.listaPrioridadeNovo = listaPrioridadeNovo;
    }

    public DualListModel<TbUnidade> getListaAreaSolucionadoraNovo() {
        return listaAreaSolucionadoraNovo;
    }

    public void setListaAreaSolucionadoraNovo(DualListModel<TbUnidade> listaAreaSolucionadoraNovo) {
        this.listaAreaSolucionadoraNovo = listaAreaSolucionadoraNovo;
    }

    public DualListModel<TbUnidade> getListaUnidadeEnvioNovo() {
        return listaUnidadeEnvioNovo;
    }

    public void setListaUnidadeEnvioNovo(DualListModel<TbUnidade> listaUnidadeEnvioNovo) {
        this.listaUnidadeEnvioNovo = listaUnidadeEnvioNovo;
    }

    public DualListModel<TbUsuario> getListaOperadorNovo() {
        return listaOperadorNovo;
    }

    public void setListaOperadorNovo(DualListModel<TbUsuario> listaOperadorNovo) {
        this.listaOperadorNovo = listaOperadorNovo;
    }

    public TbFiltroPersonalizado getTbFiltroPersonalizado() {
        return tbFiltroPersonalizado;
    }

    public void setTbFiltroPersonalizado(TbFiltroPersonalizado tbFiltroPersonalizado) {
        this.tbFiltroPersonalizado = tbFiltroPersonalizado;
    }

    public FiltroPersonalizado getFiltroPersonalizado() {
        return filtroPersonalizado;
    }

    public void setFiltroPersonalizado(FiltroPersonalizado filtroPersonalizado) {
        this.filtroPersonalizado = filtroPersonalizado;
    }

    public DualListModel<TbTipoManifestacao> getListaTipo() {
        return listaTipo;
    }

    public void setListaTipo(DualListModel<TbTipoManifestacao> listaTipo) {
        this.listaTipo = listaTipo;
    }

    public DualListModel<StatusManifestacaoEnum> getListaStatusManifestacao() {
        return listaStatusManifestacao;
    }

    public void setListaStatusManifestacao(DualListModel<StatusManifestacaoEnum> listaStatusManifestacao) {
        this.listaStatusManifestacao = listaStatusManifestacao;
    }

    public DualListModel<TbUF> getListaUf() {
        return listaUf;
    }

    public void setListaUf(DualListModel<TbUF> listaUf) {
        this.listaUf = listaUf;
    }

    public DualListModel<TbFaixaEtaria> getListaFaixaEtaria() {
        return listaFaixaEtaria;
    }

    public void setListaFaixaEtaria(DualListModel<TbFaixaEtaria> listaFaixaEtaria) {
        this.listaFaixaEtaria = listaFaixaEtaria;
    }

    public DualListModel<TbGrauInstrucao> getListaGrauInstrucao() {
        return listaGrauInstrucao;
    }

    public void setListaGrauInstrucao(DualListModel<TbGrauInstrucao> listaGrauInstrucao) {
        this.listaGrauInstrucao = listaGrauInstrucao;
    }
    
    public DualListModel<RacaEnum> getListaRaca() {
        return listaRaca;
    }

    public void setListaRaca(DualListModel<RacaEnum> listaRaca) {
        this.listaRaca = listaRaca;
    }

    public DualListModel<TbMeioEntrada> getListaMeioEntrada() {
        return listaMeioEntrada;
    }

    public void setListaMeioEntrada(DualListModel<TbMeioEntrada> listaMeioEntrada) {
        this.listaMeioEntrada = listaMeioEntrada;
    }

    public DualListModel<TbAreaEntrada> getListaAreaEntrada() {
        return listaAreaEntrada;
    }

    public void setListaAreaEntrada(DualListModel<TbAreaEntrada> listaAreaEntrada) {
        this.listaAreaEntrada = listaAreaEntrada;
    }
    
	public DualListModel<TbClassificacao> getListaClassificacao() {
        return listaClassificacao;
    }

    public void setListaClassificacao(DualListModel<TbClassificacao> listaClassificacao) {
        this.listaClassificacao = listaClassificacao;
    }

    public DualListModel<TbSubClassificacao> getListaSubClassificacao() {
        return listaSubClassificacao;
    }

    public void setListaSubClassificacao(DualListModel<TbSubClassificacao> listaSubClassificacao) {
        this.listaSubClassificacao = listaSubClassificacao;
    }

    public DualListModel<TbPrioridade> getListaPrioridade() {
        return listaPrioridade;
    }

    public void setListaPrioridade(DualListModel<TbPrioridade> listaPrioridade) {
        this.listaPrioridade = listaPrioridade;
    }

    public DualListModel<TbUnidade> getListaAreaSolucionadora() {
        return listaAreaSolucionadora;
    }

    public void setListaAreaSolucionadora(DualListModel<TbUnidade> listaAreaSolucionadora) {
        this.listaAreaSolucionadora = listaAreaSolucionadora;
    }

    public DualListModel<TbUnidade> getListaUnidadeEnvio() {
        return listaUnidadeEnvio;
    }

    public void setListaUnidadeEnvio(DualListModel<TbUnidade> listaUnidadeEnvio) {
        this.listaUnidadeEnvio = listaUnidadeEnvio;
    }

    public DualListModel<TbUsuario> getListaOperador() {
        return listaOperador;
    }

    public void setListaOperador(DualListModel<TbUsuario> listaOperador) {
        this.listaOperador = listaOperador;
    }
    
    public DualListModel<TbUsuario> getListaUsuarioCriadorNovo() {
		return listaUsuarioCriadorNovo;
	}

	public void setListaUsuarioCriadorNovo(
			DualListModel<TbUsuario> listaUsuarioCriadorNovo) {
		this.listaUsuarioCriadorNovo = listaUsuarioCriadorNovo;
	}

	public DualListModel<TbUsuario> getListaUsuarioCriador() {
		return listaUsuarioCriador;
	}

	public void setListaUsuarioCriador(DualListModel<TbUsuario> listaUsuarioCriador) {
		this.listaUsuarioCriador = listaUsuarioCriador;
	}

	public FiltroPersonalizadoDAO getDao() {
        return dao;
    }

    public void setDao(FiltroPersonalizadoDAO dao) {
        this.dao = dao;
    }

    public TipoManifestacaoDAO getTipoDAO() {
        return tipoDAO;
    }

    public void setTipoDAO(TipoManifestacaoDAO tipoDAO) {
        this.tipoDAO = tipoDAO;
    }

    public Map<String, Object> getMapCache() {
        return mapCache;
    }

    public void setMapCache(Map<String, Object> mapCache) {
        this.mapCache = mapCache;
    }

    public UfDAO getUfDAO() {
        return ufDAO;
    }

    public void setUfDAO(UfDAO ufDAO) {
        this.ufDAO = ufDAO;
    }

    public FaixaEtariaDAO getFaixaEtariaDAO() {
        return faixaEtariaDAO;
    }

    public void setFaixaEtariaDAO(FaixaEtariaDAO faixaEtariaDAO) {
        this.faixaEtariaDAO = faixaEtariaDAO;
    }

    public GrauInstrucaoDAO getGrauInstrucaoDAO() {
        return grauInstrucaoDAO;
    }

    public void setGrauInstrucaoDAO(GrauInstrucaoDAO grauInstrucaoDAO) {
        this.grauInstrucaoDAO = grauInstrucaoDAO;
    }

    public MeioEntradaDAO getMeioEntradaDAO() {
        return meioEntradaDAO;
    }

    public void setMeioEntradaDAO(MeioEntradaDAO meioEntradaDAO) {
        this.meioEntradaDAO = meioEntradaDAO;
    }

    public AreaEntradaDAO getAreaEntradaDAO() {
        return areaEntradaDAO;
    }

    public void setAreaEntradaDAO(AreaEntradaDAO areaEntradaDAO) {
        this.areaEntradaDAO = areaEntradaDAO;
    }

    public ClassificacaoDAO getClassificacaoDAO() {
        return classificacaoDAO;
    }

    public void setClassificacaoDAO(ClassificacaoDAO classificacaoDAO) {
        this.classificacaoDAO = classificacaoDAO;
    }

    public SubClassificacaoDAO getSubClassificacaoDAO() {
        return subClassificacaoDAO;
    }

    public void setSubClassificacaoDAO(SubClassificacaoDAO subClassificacaoDAO) {
        this.subClassificacaoDAO = subClassificacaoDAO;
    }

    public PrioridadeDAO getPrioridadeDAO() {
        return prioridadeDAO;
    }

    public void setPrioridadeDAO(PrioridadeDAO prioridadeDAO) {
        this.prioridadeDAO = prioridadeDAO;
    }

    public UnidadeDAO getUnidadeDAO() {
        return unidadeDAO;
    }

    public void setUnidadeDAO(UnidadeDAO unidadeDAO) {
        this.unidadeDAO = unidadeDAO;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
}
