package br.com.xti.ouvidoria.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.SortOrder;

import br.com.xti.ouvidoria.dto.manifestacao.DTOManifestacao;
import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;
import br.com.xti.ouvidoria.negocio.ManifestacaoService;


public class PesquisaManifestacaoViewHelper extends PesquisaEntidadeViewHelper<DTOManifestacao> {
	private ManifestacaoService manifestacaoService;
	
	private String nomeFiltroPersonalizado;
	private TbFiltroPersonalizado filtroEscolhido;

	public PesquisaManifestacaoViewHelper(ManifestacaoService manifestacaoService){
		this.manifestacaoService = manifestacaoService;
		super.setFiltroPesquisa(new DTOManifestacao());
		//Configura a Caixa de Entrada como cenário de pesquisa padrão
		super.getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_CAIXA_ENTRADA);
	}
	
	private void DTOPesquisaManifestacao() {
		// TODO Auto-generated method stub

	}
	
	


	@Override
	public Class getClassResultado() {
		return DTOManifestacao.class;
	}
	
	
	
	@Override
	public List<DTOManifestacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		
		// Realiza a pesquisa de Manifestações
		try {
			// Configura a paginação da tabela de resultados
			if (getFiltroPesquisa().isReiniciarPaginacao()){
				getFiltroPesquisa().setPrimeiroRegistro(0);
				DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formListarManifestacoes:tabelaManifestacoes");
				if (dataTable != null)
					dataTable.setFirst(0);
				getFiltroPesquisa().setReiniciarPaginacao(false);
			}
			else{
				getFiltroPesquisa().setPrimeiroRegistro(first);
			}
			getFiltroPesquisa().setQuantidadeRegistros(pageSize);
			
			// Configura dados da ordenação das colunas na tabela de resultados
			if (StringUtils.isEmpty(sortField)){
				// Configura a ordenação padrão
				getFiltroPesquisa().setOrdenacaoCampo("numeroManifestacao");
				getFiltroPesquisa().setOrdenacaoForma("DESC");
				
			}else{
				// Configura a ordenação solicitada
				getFiltroPesquisa().setOrdenacaoCampo(sortField);
				if (sortOrder != null){
					if (sortOrder.equals(SortOrder.ASCENDING))
						getFiltroPesquisa().setOrdenacaoForma("ASC");
					else if (sortOrder.equals(SortOrder.DESCENDING))
						getFiltroPesquisa().setOrdenacaoForma("DESC");
					else  
						getFiltroPesquisa().setOrdenacaoForma("DESC");
				}
			}
		
			List<DTOManifestacao> retorno = manifestacaoService.pesquisaManifestacoes(this);
			this.setResultado(retorno);
			
		} catch (Exception e) {
			MensagemFaceUtil.erro("Problemas na execução da pesquisa", e.getMessage());
			e.printStackTrace();
			this.setResultado(new ArrayList<DTOManifestacao>());

		}
		//Configura a quantidade de registros pesquisados na paginação
		this.setRowCount(getFiltroPesquisa().getQuantidaLinhasPesquisadas());
    	
    	return this.getResultado();
	}
	
 
	@Override
    public Object getRowKey(DTOManifestacao dtoManifestacao) {
        return dtoManifestacao.getIdManifestacao();
    }
 
    @Override
    public DTOManifestacao getRowData(String dtoManifestacaoId) {
        Integer id = Integer.valueOf(dtoManifestacaoId);
 
        for (DTOManifestacao dtoManifestacao : getResultado()) {
            if(id.equals(dtoManifestacao.getIdManifestacao())){
                return dtoManifestacao;
            }
        }
 
        return null;
    }

    

    public void configuraCenarioPesquisaCaixaEntrada(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_CAIXA_ENTRADA);
    }

    public void configuraCenarioPesquisaSolicitadaInformacao(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_SOLICITADA_INFORMACAO);
    }

    public void configuraCenarioPesquisaEmAndamento(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_EM_ANDAMENTO);
    }

    public void configuraCenarioPesquisaEmMonitoramento(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_EM_MONITORAMENTO);
    }

    public void configuraCenarioPesquisaRetornadas(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_RETORNADAS);
    }

    public void configuraCenarioPesquisaSolucionadas(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_SOLUCIONADAS);
    }

    public void configuraCenarioPesquisaDevolvidas(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_DEVOLVIDAS);
    }

    public void configuraCenarioPesquisaComOuvidoria(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_COM_OUVIDORIA);
    }

    public void configuraCenarioPesquisaTodos(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_TODOS);
    }

    public void configuraCenarioPesquisaFiltroPersonalizado(){
    	getFiltroPesquisa().setCenarioPesquisa(DTOManifestacao.CENARIO_PESQUISA_FILTRO_PERSONALIZADO);
    }

    public boolean isCenarioPesquisaCaixaEntrada(){
    	return getFiltroPesquisa().isCenarioPesquisaCaixaEntrada();
    }

    public boolean isCenarioPesquisaSolicitadaInformacao(){
    	return getFiltroPesquisa().isCenarioPesquisaSolicitadaInformacao();
    }

    public boolean isCenarioPesquisaEmAndamento(){
    	return getFiltroPesquisa().isCenarioPesquisaEmAndamento();
    }

    public boolean isCenarioPesquisaEmMonitoramento(){
    	return getFiltroPesquisa().isCenarioPesquisaEmMonitoramento();
    }

    public boolean isCenarioPesquisaRetornadas(){
    	return getFiltroPesquisa().isCenarioPesquisaRetornadas();
    }

    public boolean isCenarioPesquisaSolucionadas(){
    	return getFiltroPesquisa().isCenarioPesquisaSolucionadas();
    }

    public boolean isCenarioPesquisaDevolvidas(){
    	return getFiltroPesquisa().isCenarioPesquisaDevolvidas();
    }

    public boolean isCenarioPesquisaComOuvidoria(){
    	return getFiltroPesquisa().isCenarioPesquisaComOuvidoria();
    }

    public boolean isCenarioPesquisaTodos(){
    	return getFiltroPesquisa().isCenarioPesquisaTodos();
    }

    public boolean isCenarioPesquisaFiltroPersonalizado(){
    	return getFiltroPesquisa().isCenarioPesquisaFiltroPersonalizado();
    }
    
	/**
	 * @return O nome do Cenário de Pesquisa
	 */
	public String getNomeCenarioPesquisa() {
		if (super.getFiltroPesquisa().getCenarioPesquisa() != null ){
			if (super.getFiltroPesquisa().isCenarioPesquisaFiltroPersonalizado())
				return super.getFiltroPesquisa().getCenarioPesquisa() + " - " + nomeFiltroPersonalizado;
			else
				return super.getFiltroPesquisa().getCenarioPesquisa();
		}
			
		return null;
	}

	/**
	 * @return the nomeFiltroPersonalizado
	 */
	public String getNomeFiltroPersonalizado() {
		return nomeFiltroPersonalizado;
	}

	/**
	 * @param nomeFiltroPersonalizado the nomeFiltroPersonalizado to set
	 */
	public void setNomeFiltroPersonalizado(String nomeFiltroPersonalizado) {
		this.nomeFiltroPersonalizado = nomeFiltroPersonalizado;
	}

	/**
	 * @return the filtroEscolhido
	 */
	public TbFiltroPersonalizado getFiltroEscolhido() {
		return filtroEscolhido;
	}

	/**
	 * @param filtroEscolhido the filtroEscolhido to set
	 */
	public void setFiltroEscolhido(TbFiltroPersonalizado filtroEscolhido) {
		this.filtroEscolhido = filtroEscolhido;
	}


	
}
