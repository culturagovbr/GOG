package br.com.xti.ouvidoria.controller;

import java.util.List;

import org.primefaces.model.LazyDataModel;

public abstract class PesquisaEntidadeViewHelper<T> extends LazyDataModel<T>{
	

	private List<T> resultado;
	private T filtroPesquisa;
	
	/**
	 * Informa a classe carregada na lista de resultados do DTO
	 */
	public abstract Class getClassResultado();
	
 	/**
	 * @return the resultado
	 */
	public List<T> getResultado() {
		return resultado;
	}
	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(List<T> resultado) {
		this.resultado = resultado;
	}


	/**
	 * @return the filtroPesquisa
	 */
	public T getFiltroPesquisa() {
		return filtroPesquisa;
	}


	/**
	 * @param filtroPesquisa the filtroPesquisa to set
	 */
	public void setFiltroPesquisa(T filtroPesquisa) {
		this.filtroPesquisa = filtroPesquisa;
	}

	
}
