package br.com.xti.ouvidoria.dto;

/**
 * Classe que carrega atributos de controle da paginação de pesquisas 
 * 
 * @author clelson
 *
 */
public class DTOPesquisaPaginacao {
	
	private Integer primeiroRegistro = 0;
	private Integer quantidadeRegistros = 10;
	private String ordenacaoCampo;
	private String ordenacaoForma;
	private boolean reiniciarPaginacao;
	
	private Integer quantidaLinhasPesquisadas = 0;
	
	
	/**
	 * @return the primeiroRegistro
	 */
	public Integer getPrimeiroRegistro() {
		return primeiroRegistro;
	}
	/**
	 * @param primeiroRegistro the primeiroRegistro to set
	 */
	public void setPrimeiroRegistro(Integer primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
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
	 * @return the ordenacaoCampo
	 */
	public String getOrdenacaoCampo() {
		return ordenacaoCampo;
	}
	/**
	 * @param ordenacaoCampo the ordenacaoCampo to set
	 */
	public void setOrdenacaoCampo(String ordenacaoCampo) {
		this.ordenacaoCampo = ordenacaoCampo;
	}
	/**
	 * @return the ordenacaoForma
	 */
	public String getOrdenacaoForma() {
		return ordenacaoForma;
	}
	/**
	 * @param ordenacaoForma the ordenacaoForma to set
	 */
	public void setOrdenacaoForma(String ordenacaoForma) {
		this.ordenacaoForma = ordenacaoForma;
	}
	/**
	 * @return the reiniciarPaginacao
	 */
	public boolean isReiniciarPaginacao() {
		return reiniciarPaginacao;
	}
	/**
	 * @param reiniciarPaginacao the reiniciarPaginacao to set
	 */
	public void setReiniciarPaginacao(boolean reiniciarPaginacao) {
		this.reiniciarPaginacao = reiniciarPaginacao;
	}
	/**
	 * @return the quantidaLinhasPesquisadas
	 */
	public Integer getQuantidaLinhasPesquisadas() {
		return quantidaLinhasPesquisadas;
	}
	/**
	 * @param quantidaLinhasPesquisadas the quantidaLinhasPesquisadas to set
	 */
	public void setQuantidaLinhasPesquisadas(Integer quantidaLinhasPesquisadas) {
		this.quantidaLinhasPesquisadas = quantidaLinhasPesquisadas;
	}

	
	

}
