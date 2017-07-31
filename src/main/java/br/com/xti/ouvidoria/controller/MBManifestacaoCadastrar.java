package br.com.xti.ouvidoria.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.xti.ouvidoria.dao.AnexoDAO;
import br.com.xti.ouvidoria.dao.EmailAutomatizadoDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.dao.MeioRespostaDAO;
import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.dao.UfDAO;
import br.com.xti.ouvidoria.dao.UsuarioDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.helper.FileHelper;
import br.com.xti.ouvidoria.helper.PalavrasChavesHelper;
import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbAnexo;
import br.com.xti.ouvidoria.model.TbAreaEntrada;
import br.com.xti.ouvidoria.model.TbEmailAutomatizado;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbManifestacaoxAnexo;
import br.com.xti.ouvidoria.model.TbMeioEntrada;
import br.com.xti.ouvidoria.model.TbMeioResposta;
import br.com.xti.ouvidoria.model.TbMunicipio;
import br.com.xti.ouvidoria.model.TbPais;
import br.com.xti.ouvidoria.model.TbPrioridade;
import br.com.xti.ouvidoria.model.TbUF;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.AreaEntradaEnum;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;
import br.com.xti.ouvidoria.model.enums.MeioEntradaEnum;
import br.com.xti.ouvidoria.model.enums.MeioRespostaEnum;
import br.com.xti.ouvidoria.model.enums.PrioridadeEnum;
import br.com.xti.ouvidoria.model.enums.RacaEnum;
import br.com.xti.ouvidoria.model.enums.SexoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.TipoManifestanteEnum;
import br.com.xti.ouvidoria.model.enums.TipoPessoaEnum;
import br.com.xti.ouvidoria.security.SecurityService;
import br.com.xti.ouvidoria.util.EmailService;
import br.com.xti.ouvidoria.util.JSFUtils;
import br.com.xti.ouvidoria.util.PasswordUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Samuel Correia Guimarães
 */
@ManagedBean(name = "mBManifestacaoCadastrar")
@ViewScoped
public class MBManifestacaoCadastrar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityService securityService;

	@EJB
	private ManifestacaoDAO manifestacaoDAO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private AnexoDAO anexoDAO;
	
	@EJB
	private EmailAutomatizadoDAO emailAutomatizadoDAO;
	
	@EJB
	private EmailService emailService;
	
	@EJB
	private MeioRespostaDAO meioRespostaDAO;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@EJB
	private UfDAO ufDAO;
	
	@Inject
	private HttpServletRequest request;
	
	@ManagedProperty("#{localidadeBean}")
	private LocalidadeBean localidadeBean;
    
    // Outros Campos
    private Boolean cadastroSucesso = Boolean.FALSE;
    private Boolean anonimo = Boolean.FALSE;
    private Boolean campoObrigatorio = Boolean.FALSE;
    private Boolean tipoPessoaFisica = Boolean.TRUE;
    private TbManifestacao manifestacao = new TbManifestacao();
    private Integer idUf;
    private Integer idPais;
    private boolean emailCadastrado;
    
    private List<TbAnexo> arquivos = new ArrayList<>();
    private Collection<TbMunicipio> municipios = new ArrayList<>();
    
    private static final String MSG_MANIFESTACAO_ANONIMA;
    
    
    @PostConstruct
    public void init() {
 
        cadastroSucesso = Boolean.FALSE;
        campoObrigatorio = !(securityService.isOuvidor() || securityService.isAdministrador());
        String isAnonimo = request.getParameter("anonimo");
        anonimo = Boolean.FALSE;
		if(isAnonimo != null && isAnonimo.equals("true")) {
			anonimo = Boolean.TRUE;
		}
        
        tipoPessoaFisica = Boolean.TRUE;
        manifestacao = new TbManifestacao();
        TbMeioResposta objResposta = meioRespostaDAO.find(MeioRespostaEnum.EMAIL_ELETRONICO.getId());
        manifestacao.setIdMeioResposta(objResposta);
        manifestacao.setTpManifestante(TipoManifestanteEnum.CIDADAO.getId());
        
        preencherDadosUsuarioLogado();
    }
    
    /**
	 * Cadastra a manifestação na base de dados e envia e-mail para o
	 * manifestante e ouvidoria
	 */
    public void cadastrar() {
        try {
            manifestacao.setStResposta("1");
            manifestacao.setSiSigilo("1");
            
            //----- Número da Manifestação -------//
            // verifica se o número da Manifestação está com o valor zero e gera um número para ela
        	manifestacao.setNrManifestacao(gerarNrManifestacao());
            
            // Se já existir uma manifestação com esse número não cadastra de novo
            TbManifestacao man = manifestacaoDAO.getManifestacaoPorNumero(manifestacao.getNrManifestacao());
            if(man != null) {
            	manifestacao = man;
            	cadastroSucesso = Boolean.TRUE;
            	MensagemFaceUtil.alerta("Atenção!", "Manifestação já foi cadastrada");
            	return;
            }
            
            //----- Tipo Pessoa -------//
            if(tipoPessoaFisica) {
            	manifestacao.setTipoPessoa(TipoPessoaEnum.FISICA.getId());
            } else {
            	manifestacao.setTipoPessoa(TipoPessoaEnum.JURIDICA.getId());
            }
            	
            
            //----- pais e estado-------//
            // Seta o Brasil como País caso não seja selecionado um
            if(ValidacaoHelper.isEmpty(idPais)) {
            	manifestacao.setIdPais(new TbPais(37));
            } else {
	            manifestacao.setIdPais(new TbPais(idPais));
	            if(idPais == 37) { // Brasil
	            	manifestacao.setIdUF(ufDAO.find(idUf));
	            }
            }
            
            //----- prioridade -------//
            manifestacao.setIdPrioridade(new TbPrioridade(PrioridadeEnum.NORMAL.getId())); // 1 = Prioridade Normal

            //----- area entrada -------//
            if (manifestacao.getIdAreaEntrada() != null) {
                manifestacao.setIdAreaEntrada(manifestacao.getIdAreaEntrada());
            } else {
                manifestacao.setIdAreaEntrada(new TbAreaEntrada(AreaEntradaEnum.PAGINA_ELETRONICA.getId())); //1 = Página eletrônica da Cultura
            }

            //----- meio entrada -------//
            if (manifestacao.getIdMeioEntrada() != null) {
                manifestacao.setIdMeioEntrada(manifestacao.getIdMeioEntrada());
            } else {
                manifestacao.setIdMeioEntrada(new TbMeioEntrada(MeioEntradaEnum.FORMULARIO_ELETRONICO.getId())); //1=Formulario eletrônico (default)
            }

            //----- Usuário -------//
            if (securityService.getUser() != null) {
            	if(securityService.isManifestante()) {
            		manifestacao.setIdUsuarioManifestante(securityService.getUser());
            	} else if(securityService.isAdministrador() || securityService.isOuvidor()) {
            		manifestacao.setIdUsuarioCriador(securityService.getUser());
            	}
            }
            
            //----- Nome Manifestante -------//
            if(anonimo) {
            	manifestacao.setNmPessoa("Anônimo");
            }
            
            //----- Sexo -------//
            if(!ValidacaoHelper.isNotEmpty(manifestacao.getTpSexo())) {
            	manifestacao.setTpSexo(SexoEnum.NAO_INFORMADO.getId());
            }
            
            //----- Raça -------//
            if(!ValidacaoHelper.isNotEmpty(manifestacao.getTpRaca())) {
            	manifestacao.setTpRaca(RacaEnum.NAO_INFORMADO.getId());
            }
            
            //----- Email -------//
        	if(!ValidacaoHelper.isNotEmpty(manifestacao.getEeEmailUsuario())) {
        		manifestacao.setEeEmailUsuario("a@a.com");
        	}
            

            manifestacao.setDsSenha(PasswordUtils.generatePassword());
            manifestacao.setStStatusManifestacao(StatusManifestacaoEnum.NOVA.getId());
            manifestacao.setDtCadastro(new Date());
            manifestacao.setDtUltimaAtualizacao(new Date());

            //----- gravando manifestacao -------//
            manifestacaoDAO.create(manifestacao);

            //----- gravando anexos ---------//
            ArrayList<TbManifestacaoxAnexo> anexos = new ArrayList<>();
            for (TbAnexo anexo : arquivos) {
                anexoDAO.create(anexo);

                TbManifestacaoxAnexo tbManifestacaoxAnexo = new TbManifestacaoxAnexo();
                tbManifestacaoxAnexo.setIdAnexo(anexo);
                tbManifestacaoxAnexo.setIdManifestacao(manifestacao);
                anexos.add(tbManifestacaoxAnexo);
            }

            manifestacao.setTbManifestacaoxAnexoCollection(anexos);
            manifestacaoDAO.edit(manifestacao);

            cadastroSucesso = Boolean.TRUE;
            
            //----- enviando e-mail ---------//
            try {
                EmailService.Email email = emailService.newEmail();
                
                if (manifestacao.getEeEmailUsuario() != null && !manifestacao.getEeEmailUsuario().isEmpty() && "1".equals(manifestacao.getStResposta())) {

                	//adiciona email primario como destinatario
                    String nomeManifestante = ValidacaoHelper.isNotEmpty(manifestacao.getNmPessoa()) ? manifestacao.getNmPessoa() : "Manifestante";
                    email.addDestinatario(nomeManifestante, manifestacao.getEeEmailUsuario());

                    //adiciona emails secundarios com o copia
                    if (manifestacao.getEeEmailSecundario() != null && !manifestacao.getEeEmailSecundario().isEmpty()) {
                        for (String emailSecundario : manifestacao.getEeEmailSecundario().split("[,;]+")) {
                            email.addDestinatarioCc(nomeManifestante, emailSecundario);
                        }
                    }
                    
                    //recupera texto padrao para o email
                    TbEmailAutomatizado emailAutomatizado = emailAutomatizadoDAO.findByTipo(EmailAutomatizadoEnum.NOVO);

                    //adiciona texto e assunto de email padrao
                    StringBuilder emailTextoHtml = new StringBuilder();
                    StringBuilder emailTexto = new StringBuilder();

                    email.setAssunto(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getNmTituloEmail(), manifestacao, false));
                    emailTextoHtml.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, false));
                    emailTexto.append(PalavrasChavesHelper.converterPalavrasChaves(emailAutomatizado.getDsEmail(), manifestacao, false));

                    email.setTextoHtml(emailTextoHtml.toString());
                    email.setTextoSemFormatacao(emailTexto.toString());
                    
                    emailService.envia(email);
                }
            } catch (Exception e) {
                e.printStackTrace();
                MensagemFaceUtil.alerta("Atenção!", "Sua Manifestação foi cadastrada com sucesso porém, não foi possível enviar-lhe um e-mail com os dados de acesso a mesma.");
                return;
            }
            MensagemFaceUtil.info("Sucesso!", "Manifestação cadastrada com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            MensagemFaceUtil.erro("Erro ao cadastrar a Manifestação", "Ocorreu um erro ao cadastrar a Manifestação");
        }
    }
    
    /**
     * Efetua o upload do arquivo selecionado e salva em disco
     * 
     * @param event Arquivo selecionado
     * @throws IOException Se ocorrer algum problema ao tentar salvar o anexo em disco
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        try {
            if (uploadedFile != null) {
            	String uploadedFilename = uploadedFile.getFileName();
            	String normalizedFileName = FileHelper.normalizeName(uploadedFilename);
            	String pathName = FileHelper.getUploadedFilePathname(uploadedFilename, manifestacao.getNrManifestacao());
            	
                File file = new File(pathName);
                TbAnexo anexo = new TbAnexo();
                anexo.setNmAnexo(normalizedFileName);
                anexo.setDsCaminhoAnexo(file.getName());
                try {
                    FileUtils.writeByteArrayToFile(file, uploadedFile.getContents());
                    arquivos.add(anexo);
                    MensagemFaceUtil.info("Arquivo enviado", String.format("Arquivo enviado com sucesso: %s", normalizedFileName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JSFUtils.goToTopOfPage(); 
                    MensagemFaceUtil.erro("Erro", String.format("Erro ao enviar o arquivo: %s", normalizedFileName));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MensagemFaceUtil.erro("Erro", "Ocorreu um erro ao efetuar a operação: " + ex.getMessage());
        }
    }

    /**
     * Deleta um anexo previamente selecionado
     * 
     * @param anexo Anexo a ser removido
     */
    public void removerArquivo(TbAnexo anexo) {
        if (ValidacaoHelper.isNotEmpty(arquivos)) {
            String arquivo = anexo.getDsCaminhoAnexo();
            try {
            	File file = new File(parametroDAO.getDiretorioAnexo() + arquivo);
                if (file.exists()) {
                    file.delete();
                }
            } catch (InfrastructureException ex) {
                ex.printStackTrace();

            }
            arquivos.remove(anexo);
        }
    }
    
    /**
	 * Pesquisa as informações de endereço pelo cep e atualiza os campos com
	 * essa informação
	 */
    public void ajustaCep() {
        String cep = manifestacao.getNrCEP();
        if (ValidacaoHelper.isNotEmpty(cep)) {
            try {
                Document doc = Jsoup.connect(String.format("http://viacep.com.br/ws/%s/xml/", cep)).get();
                Element elem = doc.getElementsByTag("xmlcep").first();
                if (elem != null) {
                    manifestacao.setIdPais(localidadeBean.getBrasil());
                    manifestacao.setDsBairro(elem.getElementsByTag("bairro").first().text());
                    manifestacao.setEnEndereco(elem.getElementsByTag("logradouro").first().text());
                    TbUF uf = localidadeBean.getEstado(elem.getElementsByTag("uf").first().text());
                    setIdUf(uf.getIdUF());
                    manifestacao.setIdUF(uf);
                    setIdPais(localidadeBean.getBrasil().getIdPais());
                    manifestacao.setDsLocalidade(elem.getElementsByTag("localidade").first().text());
                } else {
                    manifestacao.setDsBairro(null);
                    manifestacao.setEnEndereco(null);
                    manifestacao.setIdUF(null);
                    manifestacao.setDsLocalidade(null);
                    manifestacao.setIdPais(null);
                    setIdPais(null);
                }
            } catch (Exception e) {
                // Do nothing
            }
        }
    }
    
	/**
	 * @param query
	 *            texto a ser usado na busca
	 * @return listagem das cidades que tenham no nome o texto enviado por
	 *         parâmetro. <b>Limitado a 15 municípios.</b>
	 */
    public List<String> completaMunicipio(String query) {
        List<String> results = new ArrayList<>();
        query = query.toUpperCase();
        for (TbMunicipio cidade : municipios) {
            String nmMunicipio = cidade.getNmMunicipio().toUpperCase();

            if (nmMunicipio.contains(query)) {
                results.add(nmMunicipio);
                
                if(results.size() >= 15) {
                	break;
                }
            }
        }
        return results;
    }
    
    /**
     * @param pais Pais a ser verificado
     * @return TRUE se o Pais informado for o brasil
     */
    public boolean isBrasil(TbPais pais) {
        pais = localidadeBean.getPais(pais.getIdPais());
        return (pais != null && pais.getNmPais().equalsIgnoreCase("brasil"));
    }

    /**
     * @return TRUE se o usuário puder escolher um estado
     */
    public boolean habilitaEstado() {
        return manifestacao.getIdPais() != null && isBrasil(manifestacao.getIdPais());
    }

    /**
     * @return TRUE se o usuário puder escolher um município
     */
    public boolean habilitaMunicipio() {
        return idUf != null && habilitaEstado();
    }
    
	/**
	 * Preenche os dados na tela de cadastro de manifestação com as informações
	 * do usuário logado
	 */
	public void preencherDadosUsuarioLogado() {
		TbUsuario user = securityService.getUser();
		
		if (user != null) {
			manifestacao.setNmPessoa(user.getNmUsuario());
			manifestacao.setEeEmailUsuario(user.getEeEmail());
			manifestacao.setNrTelefone(user.getNumTelefone());
		}
	}
    
	/**
	 * @return Próximo número de manifestação na sequência
	 * @throws InfrastructureException
	 */
	private Integer gerarNrManifestacao() throws InfrastructureException {
		try {
			return parametroDAO.getProximoNumeroManifestacao();
		} catch (InfrastructureException ex) {
			ex.printStackTrace();
			throw new InfrastructureException("Erro ao gerar número da manifestação");
		}
	}
    
	/**
	 * Alterna o formulário de cadastro de manifestação entre usuário anônimo e
	 * público. Se for escolhido anônimo preenche o campo de descrição da
	 * manifestação com a mensagem padrão
	 */
	public void setAnonimato() {
		if (anonimo) {
			manifestacao.setDsTextoManifestacao(MSG_MANIFESTACAO_ANONIMA);
		}
	}
    
    
    
    // GETTERS e SETTERS
    public List<TbAnexo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<TbAnexo> arquivos) {
        this.arquivos = arquivos;
    }

    public Boolean getCadastroSucesso() {
        return cadastroSucesso;
    }

    public void setCadastroSucesso(Boolean cadastroSucesso) {
        this.cadastroSucesso = cadastroSucesso;
    }
    
    public Boolean getAnonimo() {
		return anonimo;
	}

	public void setAnonimo(Boolean anonimo) {
		this.anonimo = anonimo;
	}
	
	public Boolean getTipoPessoaFisica() {
		return tipoPessoaFisica;
	}

	public void setTipoPessoaFisica(Boolean tipoPessoaFisica) {
		this.tipoPessoaFisica = tipoPessoaFisica;
	}

	public TbManifestacao getManifestacao() {
        return manifestacao;
    }

    public void setManifestacao(TbManifestacao manifestacao) {
        this.manifestacao = manifestacao;
    }

    public Integer getIdUf() {
        return idUf;
    }

    public void setIdUf(Integer idUf) {
        municipios = localidadeBean.buscaMunicipios(idUf);
        this.idUf = idUf;
    }

    public void setLocalidadeBean(LocalidadeBean localidadeBean) {
        this.localidadeBean = localidadeBean;
    }

    public void setIdPais(Integer idPais) {
        manifestacao.setIdPais(new TbPais(idPais));
        this.idPais = idPais;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public boolean isEmailCadastrado() {
        return emailCadastrado;
    }

    public void setEmailCadastrado(boolean emailCadastrado) {
        this.emailCadastrado = emailCadastrado;
    }
    
	public Boolean getCampoObrigatorio() {
		return campoObrigatorio;
	}

	public void setCampoObrigatorio(Boolean campoObrigatorio) {
		this.campoObrigatorio = campoObrigatorio;
	}





	static {
    	StringBuilder sb = new StringBuilder()
    	.append("ATENÇÃO<br /><br />")
    	.append("Caso queira informar um endereço de e-mail para o recebimento da sua ")
    	.append("resposta, não o faça no corpo da mensagem, indique-o no espaço a ele ")
    	.append("reservado. Tenha certeza de que isso não comprometerá seu anonimato. ")
    	.append("Caso contrário, pedimos que após 30 dias, contados a partir do ")
    	.append("encaminhamento desta manifestação, você verifique as providências tomadas, ")
    	.append("acessando o nosso site por meio do login e senha que você receberá ao final desta seção.");
    	
    	MSG_MANIFESTACAO_ANONIMA = sb.toString();
    }
    
}