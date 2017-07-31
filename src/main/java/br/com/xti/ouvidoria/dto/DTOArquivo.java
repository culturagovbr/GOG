package br.com.xti.ouvidoria.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que carrega atributos para manipulação de arquivos 
 * 
 * @author clelson
 *
 */
public class DTOArquivo {

	private String titulo;
	private String nome;
	private String caminho;

	// Lista de arquivos que um arquivo do tipo diretório pode conter
	private List<DTOArquivo> arquivos = new ArrayList<DTOArquivo>();

	/**
	 * Carrega o arquivo informada na lista de arquivos 
	 * 
	 * @param arquivo
	 */
	public void adicionaArquivo(DTOArquivo arquivo){
		if (arquivos == null || arquivos.isEmpty())
			arquivos = new ArrayList<DTOArquivo>();
		
		if (arquivo != null)
			arquivos.add(arquivo);
	}
	
	
	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the caminho
	 */
	public String getCaminho() {
		return caminho;
	}

	/**
	 * @param caminho the caminho to set
	 */
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	/**
	 * @return the arquivos
	 */
	public List<DTOArquivo> getArquivos() {
		return arquivos;
	}

	/**
	 * @param arquivos the arquivos to set
	 */
	public void setArquivos(List<DTOArquivo> arquivos) {
		this.arquivos = arquivos;
	}
	

}
