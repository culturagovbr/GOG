package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.com.xti.ouvidoria.dao.AreaEntradaDAO;
import br.com.xti.ouvidoria.dao.ClassificacaoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.PrioridadeDAO;
import br.com.xti.ouvidoria.dao.UnidadeDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbComunicacaoExterna;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbMunicipio;
import br.com.xti.ouvidoria.model.TbPrioridade;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbUF;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.AtrasoManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.RegiaoEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.TipoManifestanteEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.ManifestacaoUtils;

/**
 * @author Samuel Correia Guimarães
 */
@SuppressWarnings("serial")
@ManagedBean(name = "mBExtracaoDados")
@ViewScoped
public class MBExtracaoDados implements Serializable {
	
	@Inject
	private SecurityService securityService;

	@EJB
	private ManifestacaoDAO dao;
	@EJB
	private AreaEntradaDAO areaentradaDAO;
	@EJB
	private UnidadeDAO unidadeDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private ClassificacaoDAO classificacaoDAO;
	@EJB
	private PrioridadeDAO prioridadeDAO;

	@ManagedProperty("#{localidadeBean}")
	private LocalidadeBean localidadeBean;

    private Integer idTipoManifestacao;
    private Date dtCadastroInicio;
    private Date dtCadastroFim;
    private String statusOcorrencia;
    private Integer idClassificacao;
    private Integer idSubClassificacao;
    private Integer idAreaEntrada;
    private Integer idAreaSolucionada;
    private Integer idUnidade;
    private Integer idUsuario;
    private Integer idUsuarioCriador;
    private Integer idPrioridade;
    private String idSexo;
    private Integer idFaixaEtaria;
    private Integer idGrauInstrucao;
    private Integer idUf;
    private Integer idMunicipio;
    private String idRaca;
    private String tipoManifestacao;
    private String nmLocalidade;
    private Boolean atrasadas;
    private AtrasoManifestacaoEnum atrasoManifestacaoEnum;
    private StatusManifestacaoRelatorioEnum idStatus;
    private TipoManifestanteEnum tipoManifestanteEnum;
    private RegiaoEnum regiao;
    
    private List<TbPrioridade> listaPrioridades;
    private List<TbClassificacao> listaClassificacao;
    
    
    public void setLocalidadeBean(LocalidadeBean localidadeBean) {
        this.localidadeBean = localidadeBean;
    }
    
    public void setAtrasoManifestacaoEnum(AtrasoManifestacaoEnum atrasoManifestacaoEnum) {
        this.atrasoManifestacaoEnum = atrasoManifestacaoEnum;
    }

    public AtrasoManifestacaoEnum getAtrasoManifestacaoEnum() {
        return atrasoManifestacaoEnum;
    }

    public void setIdGrauInstrucao(Integer idGrauInstrucao) {
        this.idGrauInstrucao = idGrauInstrucao;
    }

    public Integer getIdGrauInstrucao() {
        return idGrauInstrucao;
    }

    public void setNmLocalidade(String nmLocalidade) {
        this.nmLocalidade = nmLocalidade;
    }

    public String getNmLocalidade() {
        return nmLocalidade;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public void setIdUf(Integer idUF) {
        this.idUf = idUF;
        if (idUF != null) {
            TbUF uf = localidadeBean.getEstado(idUf);
            if (uf != null) {
                municipios = uf.getTbMunicipioCollection();
            }
        }
    }
    
    public Boolean getAtrasadas() {
		return atrasadas;
	}

	public void setAtrasadas(Boolean atrasadas) {
		this.atrasadas = atrasadas;
	}

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public Integer getIdUf() {
        municipios = localidadeBean.buscaMunicipios(idUf);
        return idUf;
    }

    public void setIdFaixaEtaria(Integer idFaixaEtaria) {
        this.idFaixaEtaria = idFaixaEtaria;
    }

    public Integer getIdFaixaEtaria() {
        return idFaixaEtaria;
    }

    public void setIdRaca(String idRaca) {
        this.idRaca = idRaca;
    }

    public void setIdSexo(String idSexo) {
        this.idSexo = idSexo;
    }

    public String getIdRaca() {
        return idRaca;
    }

    public String getIdSexo() {
        return idSexo;
    }

    public void setIdStatus(StatusManifestacaoRelatorioEnum idStatus) {
        this.idStatus = idStatus;
    }

    public StatusManifestacaoRelatorioEnum getIdStatus() {
        return idStatus;
    }
    public List<TbManifestacao> manifestacoes = new ArrayList<>();

    public List<TbManifestacao> getManifestacoes() {
        return manifestacoes;
    }

    public String getTipoManifestacao() {
        return tipoManifestacao;
    }

    public void setTipoManifestacao(String tipoManifestacao) {
        this.tipoManifestacao = tipoManifestacao;
    }

    public List<TbPrioridade> getListaPrioridades() {
        listaPrioridades = prioridadeDAO.findAll();
        Collections.sort(listaPrioridades);
        return listaPrioridades;
    }

    public void setListaPrioridades(List<TbPrioridade> listaPrioridades) {
        this.listaPrioridades = listaPrioridades;
    }

    public List<TbClassificacao> getListaClassificacao() {
        listaClassificacao = classificacaoDAO.findAll();
        Collections.sort(listaClassificacao);
        return listaClassificacao;
    }

    public void setListaClassificacao(List<TbClassificacao> listaClassificacao) {
        this.listaClassificacao = listaClassificacao;
    }
    
    public void download() throws Exception {
    	ManifestacaoUtils.downloadReport("Relatorio", manifestacoes);
    }
    
    public void consultarManifestacoes() {
        List<TbManifestacao> listaManifestacoes;
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        
        if(regiao != null) {
            filtro.setManIdEstado(regiao.getIdUFs());
        }
        
        // Setando filtros
        if (ValidacaoHelper.isNotEmpty(idPrioridade)) {
            filtro.addManIdPrioridade(idPrioridade);
        }
        if (ValidacaoHelper.isNotEmpty(idTipoManifestacao)) {
            filtro.addManIdTipo(idTipoManifestacao);
        }
        if (ValidacaoHelper.isNotEmpty(idClassificacao)) {
            filtro.addManIdClassificacao(idClassificacao);
        }
        if (ValidacaoHelper.isNotEmpty(idSubClassificacao)) {
            filtro.addManIdSubClassificacao(idSubClassificacao);
        }
        if (ValidacaoHelper.isNotEmpty(idAreaEntrada)) {
            filtro.addManIdAreaEntrada(idAreaEntrada);
        }
        if (ValidacaoHelper.isNotEmpty(idAreaSolucionada)) {
            filtro.addManIdAreaSolucionadora(idAreaSolucionada);
        }
        if (ValidacaoHelper.isNotEmpty(idUsuarioCriador)) {
        	filtro.addManUsuarioCriador(idUsuarioCriador);
        }
        if (ValidacaoHelper.isNotEmpty(idUnidade)) {
            filtro.addEncIdUnidadeRecebeu(idUnidade);
        }
        if (ValidacaoHelper.isNotEmpty(idUsuario)) {
            filtro.addEncIdOperador(idUsuario);
        }
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }

        if (idStatus != null) {
            switch (idStatus) {
                case NOVA_ANALISE:
                    filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
                    filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
                    break;
                case EM_ANDAMENTO:
                    filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
                    break;
                case SOLUCIONADA_ENCERRADA:
                    filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
                    break;
                case INATIVADA:
                    filtro.addManIdStatus(StatusManifestacaoEnum.INATIVADA.getId());
                    break;
                case EM_ANALISE:
                    filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
                    break;
            }

        }
        if (ValidacaoHelper.isNotEmpty(idSexo)) {
            filtro.setManSexo(idSexo);
        }
        if (ValidacaoHelper.isNotEmpty(idFaixaEtaria)) {
            filtro.addManIdFaixaEtaria(idFaixaEtaria);
        }
        if (ValidacaoHelper.isNotEmpty(idGrauInstrucao)) {
            filtro.addManIdGrauInstrucao(idGrauInstrucao);
        }
        if (ValidacaoHelper.isNotEmpty(idRaca)) {
            filtro.addManIdRaca(idRaca);
        }
        if (ValidacaoHelper.isNotEmpty(idUf)) {
            filtro.addManIdEstado(idUf);
        }
        if (ValidacaoHelper.isNotEmpty(nmLocalidade)) {
            filtro.setManLocalidade(nmLocalidade);
        }
        if (tipoManifestanteEnum != null) {
        	filtro.addManIdTipoManifestante(tipoManifestanteEnum.getId());
        }

        listaManifestacoes = dao.getManifestacoes(filtro);
        if (listaManifestacoes == null) {
            listaManifestacoes = new ArrayList<>();
        } else {
            if (atrasoManifestacaoEnum != null) {
                listaManifestacoes = filtroAtraso(atrasoManifestacaoEnum, listaManifestacoes);
            }
        }

        manifestacoes = listaManifestacoes;
        
        if(atrasadas != null)
        	filtrarAtraso();
        	
    }
    
    private void filtrarAtraso() {
    	for (TbManifestacao t : manifestacoes) {
    		int diasAtraso = diasAtraso(t);
    		if(atrasadas) {
    			if(diasAtraso == 0)
    				manifestacoes.remove(t);
    		} else {
    			if(diasAtraso > 0)
    				manifestacoes.remove(t);
    		}
		}
    }
    
    private int diasAtraso(TbManifestacao manifestacao) {
    	if(manifestacao != null) {
    		if(StatusManifestacaoEnum.SOLUCIONADA.getId().equals(manifestacao.getStStatusManifestacao()))
    			return 0;
    		
    		Integer prazoEncaminhamento = manifestacao.getIdTipoManifestacao().getPrazoEntrada();
    		Integer prazoRespostaAOuvidoria = manifestacao.getIdTipoManifestacao().getPrazoAreaSolucionadora();
    		Integer prazoRespostaAoManifestante = manifestacao.getIdTipoManifestacao().getPrazoRespostaCidadao();
    		int diasParaResponderManifestante = prazoEncaminhamento + prazoRespostaAoManifestante + prazoRespostaAOuvidoria;
    		
    		Date dataManifestacao = manifestacao.getDtCadastro();
    		Date dataAtual = new Date();
    		int diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
    		
    		if (diasTranscorridos > ++diasParaResponderManifestante)
    			return diasTranscorridos - diasParaResponderManifestante;
    	}
    	return 0;
    }

    public Collection<TbSubClassificacao> getSubClassificacaoClassificacao() {
        if (idClassificacao != null) {
            TbClassificacao classificacao = classificacaoDAO.find(idClassificacao);
            if (classificacao != null) {
                return classificacao.getTbSubClassificacaoCollection();
            }
        }
        return new ArrayList<>();
    }

    public Collection<TbUsuario> getOperadores() {

        return operadores;
    }

    /**
     * @return the idTipoManifestacao
     */
    public Integer getIdTipoManifestacao() {
        return idTipoManifestacao;
    }

    /**
     * @param idTipoManifestacao the idTipoManifestacao to set
     */
    public void setIdTipoManifestacao(Integer idTipoManifestacao) {
        this.idTipoManifestacao = idTipoManifestacao;
    }

    /**
     * @return the dtCadastroInicio
     */
    public Date getDtCadastroInicio() {
        return dtCadastroInicio;
    }

    /**
     * @param dtCadastroInicio the dtCadastroInicio to set
     */
    public void setDtCadastroInicio(Date dtCadastroInicio) {
        this.dtCadastroInicio = dtCadastroInicio;
    }

    /**
     * @return the dtCadastroFim
     */
    public Date getDtCadastroFim() {
        return dtCadastroFim;
    }

    /**
     * @param dtCadastroFim the dtCadastroFim to set
     */
    public void setDtCadastroFim(Date dtCadastroFim) {
        this.dtCadastroFim = dtCadastroFim;
    }

    /**
     * @return the statusOcorrencia
     */
    public String getStatusOcorrencia() {
        return statusOcorrencia;
    }

    /**
     * @param statusOcorrencia the statusOcorrencia to set
     */
    public void setStatusOcorrencia(String statusOcorrencia) {
        this.statusOcorrencia = statusOcorrencia;
    }

    /**
     * @return the idClassificacao
     */
    public Integer getIdClassificacao() {
        return idClassificacao;
    }

    /**
     * @param idClassificacao the idClassificacao to set
     */
    public void setIdClassificacao(Integer idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    /**
     * @return the idSubClassificacao
     */
    public Integer getIdSubClassificacao() {
        return idSubClassificacao;
    }

    /**
     * @param idSubClassificacao the idSubClassificacao to set
     */
    public void setIdSubClassificacao(Integer idSubClassificacao) {
        this.idSubClassificacao = idSubClassificacao;
    }

    /**
     * @return the idAreaEntrada
     */
    public Integer getIdAreaEntrada() {
        return idAreaEntrada;
    }

    /**
     * @param idAreaEntrada the idAreaEntrada to set
     */
    public void setIdAreaEntrada(Integer idAreaEntrada) {
        this.idAreaEntrada = idAreaEntrada;
    }

    /**
     * @return the idAreaSolucionada
     */
    public Integer getIdAreaSolucionada() {
        return idAreaSolucionada;
    }

    /**
     * @param idAreaSolucionada the idAreaSolucionada to set
     */
    public void setIdAreaSolucionada(Integer idAreaSolucionada) {
        this.idAreaSolucionada = idAreaSolucionada;
    }

    /**
     * @return the idUnidade
     */
    public Integer getIdUnidade() {
        return idUnidade;
    }

    /**
     * @param idUnidade the idUnidade to set
     */
    public void setIdUnidade(Integer idUnidade) {
        this.idUnidade = idUnidade;
        if (idUnidade != null) {
            TbUnidade unidade = unidadeDAO.find(idUnidade);
            if (unidade != null) {
                operadores = new ArrayList<TbUsuario>(unidade.getTbUsuarioCollection());
                Collections.sort(operadores);
            }
        }
    }

    /**
     * @return the idUsuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Integer getIdUsuarioCriador() {
		return idUsuarioCriador;
	}

	public void setIdUsuarioCriador(Integer idUsuarioCriador) {
		this.idUsuarioCriador = idUsuarioCriador;
	}

	public RegiaoEnum getRegiao() {
        return regiao;
    }

    public void setRegiao(RegiaoEnum regiao) {
        this.regiao = regiao;
    }
    
    public TipoManifestanteEnum getTipoManifestanteEnum() {
		return tipoManifestanteEnum;
	}

	public void setTipoManifestanteEnum(TipoManifestanteEnum tipoManifestanteEnum) {
		this.tipoManifestanteEnum = tipoManifestanteEnum;
	}

	public Integer getIdPrioridade() {
        return idPrioridade;
    }
    
    public void setIdPrioridade(Integer idPrioridade) {
        this.idPrioridade = idPrioridade;
    }
    
    public AreaEntradaDAO getAreaentradaDAO() {
        return areaentradaDAO;
    }
    
    public void setAreaentradaDAO(AreaEntradaDAO areaentradaDAO) {
        this.areaentradaDAO = areaentradaDAO;
    }
    
    private Collection<TbMunicipio> municipios = new ArrayList<>();
    private List<TbUsuario> operadores = new ArrayList<>();

    public Collection<TbMunicipio> getMunicipios() {
        return municipios;
    }

    public List<String> completaMunicipio(String query) {
        List<String> results = new ArrayList<>();
        query = query.toUpperCase();
        for (TbMunicipio cidade : municipios) {
            String nmMunicipio = cidade.getNmMunicipio().toUpperCase();

            if (nmMunicipio.startsWith(query)) {
                results.add(nmMunicipio);
            }
        }
        return results;
    }
    
    private ArrayList<TbManifestacao> filtroAtraso(AtrasoManifestacaoEnum tipoAtraso, List<TbManifestacao> listaManifestacoes) {
        ArrayList<TbManifestacao> filtradas = new ArrayList<>();
        for (TbManifestacao tbManifestacao : listaManifestacoes) {
            AtrasoManifestacaoEnum tipo = verificaAtraso(tbManifestacao);
            
            if(tipoAtraso.equals(AtrasoManifestacaoEnum.QUALQUER_ATRASO)) {
            	if(AtrasoManifestacaoEnum.SEM_ATRASO != tipo)
            		filtradas.add(tbManifestacao);
            } else {
	            if (tipo.equals(tipoAtraso)) {
	                filtradas.add(tbManifestacao);
	            }
            }
        }
        return filtradas;
    }

    @SuppressWarnings("unused")
	public AtrasoManifestacaoEnum verificaAtraso(TbManifestacao manifestacao) {


        Integer prazoEncaminhamento = manifestacao.getIdTipoManifestacao().getPrazoEntrada();
        Integer prazoRespostaAOuvidoria = manifestacao.getIdTipoManifestacao().getPrazoAreaSolucionadora();
        Integer prazoRespostaAoManifestante = manifestacao.getIdTipoManifestacao().getPrazoRespostaCidadao();

        if (manifestacao != null) {

            int diasTranscorridos = 0; //
            Date dataManifestacao = manifestacao.getDtCadastro();
            Date dataAtual = new Date();
            diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);


            if (securityService.isOuvidor() || securityService.isAdministrador()) {

                if ((manifestacao.getStStatusManifestacao() == null ? StatusManifestacaoEnum.SOLUCIONADA.getId() != null : !manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.SOLUCIONADA.getId()))) {

                    //VERIFICA ATRASO - RESPOSTA AO MANIFESTANTE - TELA DO OUVIDOR
                    diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
                    Collection<TbComunicacaoExterna> listaComunicacao = manifestacao.getTbComunicacaoExternaCollection();

                    if (listaComunicacao == null || listaComunicacao.isEmpty()) {
                        if (prazoRespostaAoManifestante != 0 && diasTranscorridos > prazoRespostaAoManifestante) {
                            return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE;
                        }
                    } else {
                        if (listaComunicacao != null) {
                            //pega a ultima comunicacao e verifica se ela possui resposta final
                            TbComunicacaoExterna com = listaComunicacao.iterator().next();
                            String stRespostaFinal = com.getStRespostaFinal() != null ? com.getStRespostaFinal() : "0";
                            if (prazoRespostaAoManifestante != 0 && diasTranscorridos > prazoRespostaAoManifestante && !"1".equals(stRespostaFinal)) {
                                return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE;
                            }
                        }
                    }

                    //VERIFICA ATRASO - RESPOSTA À OUVIDORIA - TELA DO OUVIDOR]
                    if (manifestacao.getTbEncaminhamentoCollection() != null) {
                        for (TbEncaminhamento tbEncaminhamento : manifestacao.getTbEncaminhamentoCollection()) {

                            diasTranscorridos = 0;

                            Date dataEncaminhamento = tbEncaminhamento.getDtEnvioTramite();
                            String statusTramite = tbEncaminhamento.getStEncaminhamento();

                            diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataEncaminhamento);

                            if (prazoRespostaAOuvidoria != 0 && diasTranscorridos > prazoRespostaAOuvidoria && (statusTramite == null ? StatusEncaminhamentoEnum.ENCAMINHADA.getId() == null : statusTramite.equals(StatusEncaminhamentoEnum.ENCAMINHADA.getId()))) {
                                return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA;
                            }
                        }
                    }

                    //VERIFICA ATRASO - ENCAMINHAMENTO - TELA DO OUVIDOR
                    diasTranscorridos = 0;
                    diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataManifestacao);
                    Collection<TbEncaminhamento> listaEncaminhamentos = manifestacao.getTbEncaminhamentoCollection();
                    if (listaEncaminhamentos != null) {
                        if (prazoEncaminhamento != 0 && diasTranscorridos > prazoEncaminhamento && listaEncaminhamentos.isEmpty()) {
                            return AtrasoManifestacaoEnum.ATRASO_ENCAMINHAMENTO;
                        }
                    }
                }
                return AtrasoManifestacaoEnum.SEM_ATRASO;

            } else if (securityService.isInterlocutor() || securityService.isOperador()) {

                Integer idUnidadeUsuario = securityService.getUser().getIdUnidade().getIdUnidade();

                //VERIFICA ATRASO - RESPOSTA À OUVIDORIA - TELA DO OUVIDOR
                if ((manifestacao.getStStatusManifestacao() == null ? StatusManifestacaoEnum.SOLUCIONADA.getId() != null : !manifestacao.getStStatusManifestacao().equals(StatusManifestacaoEnum.SOLUCIONADA.getId()))) {

                    if (manifestacao.getTbEncaminhamentoCollection() != null) {
                        for (TbEncaminhamento tbEncaminhamento : manifestacao.getTbEncaminhamentoCollection()) {

                            Integer idUnidadeEncaminhamento = tbEncaminhamento.getIdUnidadeRecebeu().getIdUnidade();

                            diasTranscorridos = 0;

                            if (idUnidadeUsuario.equals(idUnidadeEncaminhamento)) {

                                Date dataEncaminhamento = tbEncaminhamento.getDtEnvioTramite();
                                String statusTramite = tbEncaminhamento.getStEncaminhamento();

                                diasTranscorridos = DataHelper.getDiferencaEntreDatasEmDias(dataAtual, dataEncaminhamento);

                                if (prazoRespostaAOuvidoria != 0 && diasTranscorridos > prazoRespostaAOuvidoria && (statusTramite == null ? StatusEncaminhamentoEnum.ENCAMINHADA.getId() == null : statusTramite.equals(StatusEncaminhamentoEnum.ENCAMINHADA.getId()))) {
                                    return AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA;
                                }
                            }
                        }
                    }
                }
                return AtrasoManifestacaoEnum.SEM_ATRASO;

            } else {
                return AtrasoManifestacaoEnum.SEM_ATRASO;
            }
        }
        
		return AtrasoManifestacaoEnum.SEM_ATRASO;
    }

    public StatusManifestacaoRelatorioEnum[] getStatusManifestacaoRelatorioEnums() {
        return new StatusManifestacaoRelatorioEnum[]{
            StatusManifestacaoRelatorioEnum.NOVA_ANALISE,
            StatusManifestacaoRelatorioEnum.EM_ANDAMENTO,
            StatusManifestacaoRelatorioEnum.SOLUCIONADA_ENCERRADA,
            StatusManifestacaoRelatorioEnum.INATIVADA};
    }

    public enum StatusManifestacaoRelatorioEnum {
        NOVA_ANALISE("Nova / Em Análise"),
        EM_ANDAMENTO("Em Andamento"),
        SOLUCIONADA_ENCERRADA("Solucionada / Encerrada"),
        INATIVADA("Inativa"),
        EM_ANALISE("Em Análise");
        
        private String descricao;

        private StatusManifestacaoRelatorioEnum(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public void atualizaOuvidores() {
        try {
            operadores = new ArrayList<TbUsuario>(usuarioDAO.findOuvidorByUnidade(idUnidade));
        } catch (Exception ex) {
            Logger.getLogger(MBExtracaoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultarManifestacoesRetornadasPorUnidade() {
        consultarManifestacoesPorStatus(idUnidade, null, StatusEncaminhamentoEnum.RETORNADA);
    }

    public void consultarManifestacoesRetornadasPorTecnico() {
        consultarManifestacoesPorStatus(null, idUsuario, StatusEncaminhamentoEnum.RETORNADA);
    }

    public void consultarManifestacoesEncaminhadasPorUnidade() {
        consultarManifestacoesPorStatus(idUnidade, null, StatusEncaminhamentoEnum.ENCAMINHADA);
    }

    public void consultarManifestacoesEncaminhadasPorTecnico() {
        consultarManifestacoesPorStatus(null, idUsuario, StatusEncaminhamentoEnum.ENCAMINHADA);
    }

    private void consultarManifestacoesPorStatus(Integer unidade, Integer usuario, StatusEncaminhamentoEnum status) {
        manifestacoes = dao.getManifestacoesPorTramite(dtCadastroInicio,dtCadastroFim,unidade, usuario, status);
    }

    public void consultarManifestacoesSolucionadasPorUnidade() {
        idStatus = StatusManifestacaoRelatorioEnum.SOLUCIONADA_ENCERRADA;
        consultarManifestacoes();
    }

    public void consultarManifestacoesEmAnalisePorOuvidor() {
        idStatus = StatusManifestacaoRelatorioEnum.EM_ANALISE;
        consultarManifestacoes();
    }

    public void consultarManifestacoesOuvidorXUnidade() {
        idAreaSolucionada = idUnidade;
        idUnidade = null;
        consultarManifestacoes();
        idUnidade = idAreaSolucionada;
    }

    public void consultarManifestacoesAtrasoOuvidoria() {
        atrasoManifestacaoEnum = AtrasoManifestacaoEnum.ATRASO_RESPOSTA_OUVIDORIA;
        consultarManifestacoes();
    }

    public void consultarManifestacoesAtrasoEncaminhamento() {
        atrasoManifestacaoEnum = AtrasoManifestacaoEnum.ATRASO_ENCAMINHAMENTO;
        consultarManifestacoes();

    }

    public void consultarManifestacoesAtrasoResposta() {
        atrasoManifestacaoEnum = AtrasoManifestacaoEnum.ATRASO_RESPOSTA_MANIFESTANTE;
        consultarManifestacoes();
    }
    
    // ---------------------------------------------------------------- //
    // ---------- Novos Relatórios 08 e 09 de Julho de 2013 ----------- //
    // ---------------------------------------------------------------- //
    public void consultarRetornadasPorTipoManifestacao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        if(ValidacaoHelper.isNotEmpty(tipoManifestacao)) {
            filtro.addManIdTipo(new Integer(tipoManifestacao));
        }
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncaminhadasPorTipoManifestacao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        if(ValidacaoHelper.isNotEmpty(tipoManifestacao)) {
            filtro.addManIdTipo(new Integer(tipoManifestacao));
        }
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarRetornadasPorPrioridade() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        filtro.addManIdPrioridade(idPrioridade);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncaminhadasPorPrioridade() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        filtro.addManIdPrioridade(idPrioridade);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarRetornadasPorClassificacao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        filtro.addManIdClassificacao(idClassificacao);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncaminhadasPorClassificacao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        filtro.addManIdClassificacao(idClassificacao);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarRetornadasPorOperador() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        filtro.addEncIdOperador(idUsuario);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncaminhadasPorOperador() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        filtro.addEncIdOperador(idUsuario);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarPorAreaSolucionadora() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdAreaSolucionadora(idAreaSolucionada);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEmAnalisePorOuvidor() {
        manifestacoes = dao.getManifestacoesEmAnalisePorUsuarioAnalisador(idUsuario);
    }
    
    public void consultarEmAndamentoPorUnidade() {
        manifestacoes = dao.getManifestacoesEncaminhadasSemAcaoPorUnidade(idUnidade);
    }
    
    public void consultarEncerradasPorGrauInstrucao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
        filtro.addManIdGrauInstrucao(idGrauInstrucao);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarNaoEncerradasPorGrauInstrucao() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        filtro.addManIdGrauInstrucao(idGrauInstrucao);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncerradasPorFaixaEtaria() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
        filtro.addManIdFaixaEtaria(idFaixaEtaria);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarNaoEncerradasPorFaixaEtaria() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        filtro.addManIdFaixaEtaria(idFaixaEtaria);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarRetornadasPorUF() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
        filtro.addManIdEstado(idUf);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncaminhadasPorUF() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        filtro.addManIdEstado(idUf);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncerradasPorUF() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
        filtro.addManIdEstado(idUf);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarNaoEncerradasPorUF() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        filtro.addManIdEstado(idUf);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncerradasPorSexo() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
        filtro.setManSexo(idSexo);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarNaoEncerradasPorSexo() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        filtro.setManSexo(idSexo);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarEncerradasPorRaca() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
        filtro.addManIdRaca(idRaca);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarNaoEncerradasPorRaca() {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        filtro.addManIdRaca(idRaca);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    // ---------------------------------------------------------------- //
    // --------- Novos Relatórios 16 e 17 de Outubro de 2013 ---------- //
    // ---------------------------------------------------------------- //
    public void consultarManifestacoesCadastradasPorOuvidor() {
    	FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.addManUsuarioCriador(idUsuarioCriador);
        if (dtCadastroInicio != null) {
            filtro.setManDataCadastroDe(dtCadastroInicio);
        }
        if (dtCadastroFim != null) {
            filtro.setManDataCadastroAte(dtCadastroFim);
        }
        manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarManifestacoesSolucionadasPorOuvidor() {
    	FiltroPersonalizado filtro = new FiltroPersonalizado();
    	filtro.addManUsuarioAnalizador(idUsuario);
    	if (dtCadastroInicio != null) {
    		filtro.setManDataCadastroDe(dtCadastroInicio);
    	}
    	if (dtCadastroFim != null) {
    		filtro.setManDataCadastroAte(dtCadastroFim);
    	}
    	manifestacoes = dao.getManifestacoes(filtro);
    }
    
    public void consultarManifestacoesSolucionadasSemEncaminhamentoPorOuvidor() {
    	manifestacoes = dao.getManifestacoesSolucionadasSemEncaminhamento(idUsuario);
    }
    
    public void consultarManifestacoesReativadasPorOuvidor() {
    	FiltroPersonalizado filtro = new FiltroPersonalizado();
    	filtro.addManUsuarioReativou(idUsuario);
    	if (dtCadastroInicio != null) {
    		filtro.setManDataCadastroDe(dtCadastroInicio);
    	}
    	if (dtCadastroFim != null) {
    		filtro.setManDataCadastroAte(dtCadastroFim);
    	}
    	manifestacoes = dao.getManifestacoes(filtro);
    }
    
}