package br.com.xti.ouvidoria.filtropersonalizado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;

public class FiltroPersonalizado {
	public static final Integer REGISTRO_INICIAL = 0;
	public static final Integer QUANTIDADE_REGISTRO_PADRAO = 10;
	
	private Integer registroInicial;
	private Integer quantidadeRegistros;
	private boolean filtroPaginacao;

    private String metodoBusca;
    // Informações referentes a Manifestação
    private String manId;
    private Integer manIdRangeDe;
    private Integer manIdRangeAte;
    private List<Integer> manIdLista;
    private Date manDataCadastroDe;
    private Date manDataCadastroAte;
    private Date manDataAtualizacaoDe;
    private Date manDataAtualizacaoAte;
    private Date manDataFechamentoDe;
    private Date manDataFechamentoAte;
    private List<Integer> manIdTipo;
    private String manTipo;
    private List<String> manIdStatus;
    private Integer manIdPais;
    private String manPais;
    private List<Integer> manIdEstado;
    private List<Integer> manIdMunicipio;
    private String manEstado;
    private String manCidade;
    private List<Integer> manIdFaixaEtaria;
    private List<Integer> manIdGrauInstrucao;
    private List<String> manIdRaca;
    private String manSexo;
    private List<Integer> manIdMeioEntrada;
    private List<Integer> manIdAreaEntrada;
    private List<Integer> manIdClassificacao;
    private String manClassificacao;
    private List<Integer> manIdSubClassificacao;
    private String manSubClassificacao;
    private List<Integer> manIdPrioridade;
    private String manPrioridade;
    private List<Integer> manIdAreaSolucionadora;
    private String manAreaSolucionadora;
    private String manSigilo;
    private String manOculto;
    private String manResposta;
    private Integer manIdManifestante;
    private List<Integer> manUsuarioAnalizador;
    private List<Integer> manUsuarioCriador;
    private List<Integer> manUsuarioReativou;
    private String manManifestante;
    private String manTelefone;
    private String manEmail;
    private String manDesc;
    private String manEndereco;
    private String manNumPronac;
    private String manNumProcesso;
    private List<String> manIdTipoManifestante;
    // Informações referentes ao Encaminhamento/Trâmite
    private String encStatus;
    private String encDesc;
    private List<Integer> encIdUnidadeRecebeu;
    private List<Integer> encIdUnidadeEnviou;
    private String encUnidade;
    private List<Integer> encIdOuvidor;
    private List<Integer> encIdOperador;
    private Boolean encTramiteNaoRetornado;
    private Date encDataEnvioDe;
    private Date encDataEnvioAte;
    private Date encDataRespostaDe;
    private Date encDataRespostaAte;
    private String manLocalidade;
    
    public FiltroPersonalizado() {
		// TODO Auto-generated constructor stub
    	setRegistroInicial(REGISTRO_INICIAL);
    	setQuantidadeRegistros(QUANTIDADE_REGISTRO_PADRAO);
	}
    
    
    public String getManOculto() {
        return manOculto;
    }

    public void setManOculto(String manOculto) {
        this.manOculto = manOculto;
    }

    public void setManLocalidade(String manLocalidade) {
        this.manLocalidade = manLocalidade;
    }

    public String getManLocalidade() {
        return manLocalidade;
    }

    // GETTERS e SETTERS
    public String getMetodoBusca() {
        return metodoBusca;
    }

    public void setMetodoBusca(String metodoBusca) {
        this.metodoBusca = metodoBusca;
    }
    
    public String getManId() {
		return manId;
	}

	public void setManId(String manId) {
		this.manId = manId;
	}

	public Integer getManIdRangeDe() {
        return manIdRangeDe;
    }

    public void setManIdRangeDe(Integer manIdRangeDe) {
        this.manIdRangeDe = manIdRangeDe;
    }

    public Integer getManIdRangeAte() {
        return manIdRangeAte;
    }

    public void setManIdRangeAte(Integer manIdRangeAte) {
        this.manIdRangeAte = manIdRangeAte;
    }

    public List<Integer> getManIdLista() {
        return manIdLista;
    }

    public void setManIdLista(List<Integer> manIdLista) {
        this.manIdLista = manIdLista;
    }

    public void addManIdLista(Integer idManifestacao) {
        if (this.manIdLista == null) {
            this.manIdLista = new ArrayList<>();
        }
        this.manIdLista.add(idManifestacao);
    }

    public Date getManDataCadastroDe() {
        return manDataCadastroDe;
    }

    public void setManDataCadastroDe(Date manDataDe) {
        this.manDataCadastroDe = manDataDe;
    }

    public Date getManDataCadastroAte() {
        return manDataCadastroAte;
    }

    public void setManDataCadastroAte(Date manDataAte) {
        this.manDataCadastroAte = manDataAte;
    }

    public List<Integer> getManIdTipo() {
        return manIdTipo;
    }

    public void setManIdTipo(List<Integer> manIdTipo) {
        this.manIdTipo = manIdTipo;
    }

    public void addManIdTipo(Integer manIdTipo) {
        if (this.manIdTipo == null) {
            this.manIdTipo = new ArrayList<>();
        }
        this.manIdTipo.add(manIdTipo);
    }

    public List<String> getManIdStatus() {
        return manIdStatus;
    }

    public void setManIdStatus(List<String> manIdStatus) {
        this.manIdStatus = manIdStatus;
    }

    public void addManIdStatus(String manIdStatus) {
        if (this.manIdStatus == null) {
            this.manIdStatus = new ArrayList<>();
        }
        this.manIdStatus.add(manIdStatus);
    }

    public Integer getManIdPais() {
        return manIdPais;
    }

    public void setManIdPais(Integer manIdPais) {
        this.manIdPais = manIdPais;
    }

    public List<Integer> getManIdEstado() {
        return manIdEstado;
    }

    public List<Integer> getManIdMunicipio() {
        return manIdMunicipio;
    }

    public void setManIdEstado(List<Integer> manIdEstado) {
        this.manIdEstado = manIdEstado;
    }

    public void setManIdMunicipio(List<Integer> manIdMunicipio) {
        this.manIdMunicipio = manIdMunicipio;
    }

    public void addManIdEstado(Integer manIdEstado) {
        if (this.manIdEstado == null) {
            this.manIdEstado = new ArrayList<>();
        }
        this.manIdEstado.add(manIdEstado);
    }

    public void addManIdMunicipio(Integer manIdMunicipio) {
        if (this.manIdMunicipio == null) {
            this.manIdMunicipio = new ArrayList<>();
        }
        this.manIdMunicipio.add(manIdMunicipio);
    }

    public List<Integer> getManIdGrauInstrucao() {
        return manIdGrauInstrucao;
    }

    public void setManIdGrauInstrucao(List<Integer> manIdGrauInstrucao) {
        this.manIdGrauInstrucao = manIdGrauInstrucao;
    }

    public List<Integer> getManIdFaixaEtaria() {
        return manIdFaixaEtaria;
    }

    public void setManIdFaixaEtaria(List<Integer> manIdFaixaEtaria) {
        this.manIdFaixaEtaria = manIdFaixaEtaria;
    }

    public void addManIdFaixaEtaria(Integer manIdFaixaEtaria) {
        if (this.manIdFaixaEtaria == null) {
            this.manIdFaixaEtaria = new ArrayList<>();
        }
        this.manIdFaixaEtaria.add(manIdFaixaEtaria);
    }

    public void addManIdGrauInstrucao(Integer manIdGrauInstrucao) {
        if (this.manIdGrauInstrucao == null) {
            this.manIdGrauInstrucao = new ArrayList<>();
        }
        this.manIdGrauInstrucao.add(manIdGrauInstrucao);
    }

    public List<String> getManIdRaca() {
        return manIdRaca;
    }

    public void setManIdRaca(List<String> manIdRaca) {
        this.manIdRaca = manIdRaca;
    }

    public void addManIdRaca(String manIdRaca) {
        if (this.manIdRaca == null) {
            this.manIdRaca = new ArrayList<>();
        }
        this.manIdRaca.add(manIdRaca);
    }

    public List<Integer> getManIdMeioEntrada() {
        return manIdMeioEntrada;
    }

    public void setManIdMeioEntrada(List<Integer> manIdMeioEntrada) {
        this.manIdMeioEntrada = manIdMeioEntrada;
    }

    public void addManIdMeioEntrada(Integer manIdMeioEntrada) {
        if (this.manIdMeioEntrada == null) {
            this.manIdMeioEntrada = new ArrayList<>();
        }
        this.manIdMeioEntrada.add(manIdMeioEntrada);
    }

    public List<Integer> getManIdAreaEntrada() {
        return manIdAreaEntrada;
    }

    public void setManIdAreaEntrada(List<Integer> manIdAreaEntrada) {
        this.manIdAreaEntrada = manIdAreaEntrada;
    }

    public void addManIdAreaEntrada(Integer manIdAreaEntrada) {
        if (this.manIdAreaEntrada == null) {
            this.manIdAreaEntrada = new ArrayList<>();
        }
        this.manIdAreaEntrada.add(manIdAreaEntrada);
    }

    public List<Integer> getManIdClassificacao() {
        return manIdClassificacao;
    }

    public void setManIdClassificacao(List<Integer> manIdClassificacao) {
        this.manIdClassificacao = manIdClassificacao;
    }

    public void addManIdClassificacao(Integer manIdClassificacao) {
        if (this.manIdClassificacao == null) {
            this.manIdClassificacao = new ArrayList<>();
        }
        this.manIdClassificacao.add(manIdClassificacao);
    }

    public void setManClassificacao(List<TbClassificacao> listaClassificacao) {
        manIdClassificacao = new ArrayList<>();
        if (listaClassificacao != null) {
            for (TbClassificacao tbClassificacao : listaClassificacao) {
                manIdClassificacao.add(tbClassificacao.getIdClassificacao());
            }
        }
    }

    public List<Integer> getManIdSubClassificacao() {
        return manIdSubClassificacao;
    }

    public void setManIdSubClassificacao(List<Integer> manIdSubClassificacao) {
        this.manIdSubClassificacao = manIdSubClassificacao;
    }

    public void addManIdSubClassificacao(Integer manIdSubClassificacao) {
        if (this.manIdSubClassificacao == null) {
            this.manIdSubClassificacao = new ArrayList<>();
        }
        this.manIdSubClassificacao.add(manIdSubClassificacao);
    }

    public void setManSubClassificacao(List<TbSubClassificacao> listaSubClassificacao) {
        manIdSubClassificacao = new ArrayList<>();
        if (listaSubClassificacao != null) {
            for (TbSubClassificacao tbSubClassificacao : listaSubClassificacao) {
                manIdSubClassificacao.add(tbSubClassificacao.getIdSubClassificacao());
            }
        }
    }

    public List<Integer> getManIdPrioridade() {
        return manIdPrioridade;
    }

    public void setManIdPrioridade(List<Integer> manIdPrioridade) {
        this.manIdPrioridade = manIdPrioridade;
    }

    public void addManIdPrioridade(Integer manIdPrioridade) {
        if (this.manIdPrioridade == null) {
            this.manIdPrioridade = new ArrayList<>();
        }
        this.manIdPrioridade.add(manIdPrioridade);
    }

    public List<Integer> getManIdAreaSolucionadora() {
        return manIdAreaSolucionadora;
    }

    public void setManIdAreaSolucionadora(List<Integer> manIdAreaSolucionadora) {
        this.manIdAreaSolucionadora = manIdAreaSolucionadora;
    }

    public void addManIdAreaSolucionadora(Integer manIdAreaSolucionadora) {
        if (this.manIdAreaSolucionadora == null) {
            this.manIdAreaSolucionadora = new ArrayList<>();
        }
        this.manIdAreaSolucionadora.add(manIdAreaSolucionadora);
    }

    public List<Integer> getEncIdUnidadeRecebeu() {
        return encIdUnidadeRecebeu;
    }

    public void setEncIdUnidadeRecebeu(List<Integer> encIdUnidadeRecebeu) {
        this.encIdUnidadeRecebeu = encIdUnidadeRecebeu;
    }

    public void addEncIdUnidadeRecebeu(Integer encIdUnidadeRecebeu) {
        if (this.encIdUnidadeRecebeu == null) {
            this.encIdUnidadeRecebeu = new ArrayList<>();
        }
        this.encIdUnidadeRecebeu.add(encIdUnidadeRecebeu);
    }
    
    public List<Integer> getEncIdUnidadeEnviou() {
		return encIdUnidadeEnviou;
	}

	public void setEncIdUnidadeEnviou(List<Integer> encIdUnidadeEnviou) {
		this.encIdUnidadeEnviou = encIdUnidadeEnviou;
	}
	
	public void addEncIdUnidadeEnviou(Integer encIdUnidadeEnviou) {
		if (this.encIdUnidadeEnviou == null) {
			this.encIdUnidadeEnviou = new ArrayList<>();
		}
		this.encIdUnidadeEnviou.add(encIdUnidadeEnviou);
	}

	public List<Integer> getEncIdOuvidor() {
        return encIdOuvidor;
    }

    public void setEncIdOuvidor(List<Integer> encIdOuvidor) {
        this.encIdOuvidor = encIdOuvidor;
    }

    public void addEncIdOuvidor(Integer encIdOuvidor) {
        if (this.encIdOuvidor == null) {
            this.encIdOuvidor = new ArrayList<>();
        }
        this.encIdOuvidor.add(encIdOuvidor);
    }

    public Date getEncDataEnvioDe() {
        return encDataEnvioDe;
    }

    public void setEncDataEnvioDe(Date encDataEnvioDe) {
        this.encDataEnvioDe = encDataEnvioDe;
    }

    public Date getEncDataEnvioAte() {
        return encDataEnvioAte;
    }

    public void setEncDataEnvioAte(Date encDataEnvioAte) {
        this.encDataEnvioAte = encDataEnvioAte;
    }

    public Date getEncDataRespostaDe() {
        return encDataRespostaDe;
    }

    public void setEncDataRespostaDe(Date encDataRespostaDe) {
        this.encDataRespostaDe = encDataRespostaDe;
    }

    public Date getEncDataRespostaAte() {
        return encDataRespostaAte;
    }

    public void setEncDataRespostaAte(Date encDataRespostaAte) {
        this.encDataRespostaAte = encDataRespostaAte;
    }

    public Integer getManIdManifestante() {
        return manIdManifestante;
    }

    public void setManIdManifestante(Integer manIdManifestante) {
        this.manIdManifestante = manIdManifestante;
    }
    
    public List<Integer> getManUsuarioAnalizador() {
		return manUsuarioAnalizador;
	}

	public void setManUsuarioAnalizador(List<Integer> manUsuarioAnalizador) {
		this.manUsuarioAnalizador = manUsuarioAnalizador;
	}
	
	public void addManUsuarioAnalizador(Integer idUsuarioAnalizador) {
		if(manUsuarioAnalizador == null)
			manUsuarioAnalizador = new ArrayList<>();
			
		manUsuarioAnalizador.add(idUsuarioAnalizador);
	}

	public List<Integer> getManUsuarioCriador() {
		return manUsuarioCriador;
	}

	public void setManUsuarioCriador(List<Integer> manUsuarioCriador) {
		this.manUsuarioCriador = manUsuarioCriador;
	}

	public void addManUsuarioCriador(Integer idUsuarioCriador) {
		if(manUsuarioCriador == null)
			manUsuarioCriador = new ArrayList<>();
			
		manUsuarioCriador.add(idUsuarioCriador);
	}
	
	public List<Integer> getManUsuarioReativou() {
		return manUsuarioReativou;
	}

	public void setManUsuarioReativou(List<Integer> manUsuarioReativou) {
		this.manUsuarioReativou = manUsuarioReativou;
	}
	
	public void addManUsuarioReativou(Integer idUsuarioReativou) {
		if(manUsuarioReativou == null)
			manUsuarioReativou = new ArrayList<>();
			
			manUsuarioReativou.add(idUsuarioReativou);
	}

	public List<Integer> getEncIdOperador() {
        return encIdOperador;
    }

    public void setEncIdOperador(List<Integer> encIdOperador) {
        this.encIdOperador = encIdOperador;
    }

    public void addEncIdOperador(Integer encIdOperador) {
        if (this.encIdOperador == null) {
            this.encIdOperador = new ArrayList<>();
        }
        this.encIdOperador.add(encIdOperador);
    }
    
    public Boolean isEncTramiteNaoRetornado() {
		return encTramiteNaoRetornado;
	}

	public void setEncTramiteNaoRetornado(boolean encTramiteRetornado) {
		this.encTramiteNaoRetornado = encTramiteRetornado;
	}

	public String getManSexo() {
        return manSexo;
    }

    public void setManSexo(String manSexo) {
        this.manSexo = manSexo;
    }

    public String getManSigilo() {
        return manSigilo;
    }

    public void setManSigilo(String manSigilo) {
        this.manSigilo = manSigilo;
    }

    public String getManResposta() {
        return manResposta;
    }

    public void setManResposta(String manResposta) {
        this.manResposta = manResposta;
    }

    public String getEncStatus() {
        return encStatus;
    }

    public void setEncStatus(String encStatus) {
        this.encStatus = encStatus;
    }

    public Date getManDataAtualizacaoDe() {
        return manDataAtualizacaoDe;
    }

    public void setManDataAtualizacaoDe(Date manDataAtualizacaoDe) {
        this.manDataAtualizacaoDe = manDataAtualizacaoDe;
    }

    public Date getManDataAtualizacaoAte() {
        return manDataAtualizacaoAte;
    }

    public void setManDataAtualizacaoAte(Date manDataAtualizacaoAte) {
        this.manDataAtualizacaoAte = manDataAtualizacaoAte;
    }

    public Date getManDataFechamentoDe() {
        return manDataFechamentoDe;
    }

    public void setManDataFechamentoDe(Date manDataFechamentoDe) {
        this.manDataFechamentoDe = manDataFechamentoDe;
    }

    public Date getManDataFechamentoAte() {
        return manDataFechamentoAte;
    }

    public void setManDataFechamentoAte(Date manDataFechamentoAte) {
        this.manDataFechamentoAte = manDataFechamentoAte;
    }

    public String getManTipo() {
        return manTipo;
    }

    public void setManTipo(String manTipo) {
        this.manTipo = manTipo;
    }

    public String getManPais() {
        return manPais;
    }

    public void setManPais(String manPais) {
        this.manPais = manPais;
    }

    public String getManEstado() {
        return manEstado;
    }

    public void setManEstado(String manEstado) {
        this.manEstado = manEstado;
    }

    public String getManCidade() {
        return manCidade;
    }

    public void setManCidade(String manCidade) {
        this.manCidade = manCidade;
    }

    public String getManClassificacao() {
        return manClassificacao;
    }

    public void setManClassificacao(String manClassificacao) {
        this.manClassificacao = manClassificacao;
    }

    public String getManSubClassificacao() {
        return manSubClassificacao;
    }

    public void setManSubClassificacao(String manSubClassificacao) {
        this.manSubClassificacao = manSubClassificacao;
    }

    public String getManPrioridade() {
        return manPrioridade;
    }

    public void setManPrioridade(String manPrioridade) {
        this.manPrioridade = manPrioridade;
    }

    public String getManAreaSolucionadora() {
        return manAreaSolucionadora;
    }

    public void setManAreaSolucionadora(String manAreaSolucionadora) {
        this.manAreaSolucionadora = manAreaSolucionadora;
    }

    public String getManManifestante() {
        return manManifestante;
    }

    public void setManManifestante(String manManifestante) {
        this.manManifestante = manManifestante;
    }

    public String getManTelefone() {
        return manTelefone;
    }

    public void setManTelefone(String manTelefone) {
        this.manTelefone = manTelefone;
    }

    public String getManEmail() {
        return manEmail;
    }

    public void setManEmail(String manEmail) {
        this.manEmail = manEmail;
    }

    public String getManDesc() {
        return manDesc;
    }

    public void setManDesc(String manDesc) {
        this.manDesc = manDesc;
    }

    public String getManEndereco() {
        return manEndereco;
    }

    public void setManEndereco(String manEndereco) {
        this.manEndereco = manEndereco;
    }
    
    public String getManNumPronac() {
		return manNumPronac;
	}

	public void setManNumPronac(String manNumPronac) {
		this.manNumPronac = manNumPronac;
	}

	public String getManNumProcesso() {
		return manNumProcesso;
	}

	public void setManNumProcesso(String manNumProcesso) {
		this.manNumProcesso = manNumProcesso;
	}
	
	// Encaminhamento / Trâmite
	public String getEncDesc() {
        return encDesc;
    }

    public void setEncDesc(String encDesc) {
        this.encDesc = encDesc;
    }

    public String getEncUnidade() {
        return encUnidade;
    }

    public void setEncUnidade(String encUnidade) {
        this.encUnidade = encUnidade;
    }
    
	public List<String> getManIdTipoManifestante() {
		return manIdTipoManifestante;
	}

	public void setManIdTipoManifestante(List<String> manIdTipoManifestante) {
		this.manIdTipoManifestante = manIdTipoManifestante;
	}
	
	public void addManIdTipoManifestante(String idTipoManifestante) {
		if(this.manIdTipoManifestante == null) {
			this.manIdTipoManifestante = new ArrayList<String>();
		}
		this.manIdTipoManifestante.add(idTipoManifestante);
	}

	/**
	 * @return the registroInicial
	 */
	public Integer getRegistroInicial() {
		return registroInicial;
	}

	/**
	 * @param registroInicial the registroInicial to set
	 */
	public void setRegistroInicial(Integer registroInicial) {
		this.registroInicial = registroInicial;
	}

	/**
	 * @return the quantidadeRegistros
	 */
	public Integer getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	/**
	 * @param quantidadeRegistros the quantidadeRegistros to set
	 */
	public void setQuantidadeRegistros(Integer quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}


	/**
	 * @return the filtroPaginacao
	 */
	public boolean isFiltroPaginacao() {
		return filtroPaginacao;
	}


	/**
	 * @param filtroPaginacao the filtroPaginacao to set
	 */
	public void setFiltroPaginacao(boolean filtroPaginacao) {
		this.filtroPaginacao = filtroPaginacao;
	}

}
