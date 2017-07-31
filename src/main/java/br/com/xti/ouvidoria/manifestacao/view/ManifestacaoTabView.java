package br.com.xti.ouvidoria.manifestacao.view;

import java.io.Serializable;
import java.util.Collection;

import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbUnidade;

/**
 * @author marcos.ribeiro
 * @author Samuel Correia Guimarães
 */
@SuppressWarnings("serial")
public class ManifestacaoTabView implements Serializable {

	private Integer index;
	private String titulo;
	private Collection<?> conteudo;
	private TbEncaminhamento encaminhamento;
	private TbUnidade unidadeRecebeu;
	private TbUnidade unidadeEnviou;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getTitulo() {
		return titulo;
	}

	public Collection<?> getConteudo() {
		return conteudo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setConteudo(Collection<?> conteudo) {
		this.conteudo = conteudo;
	}

	public TbEncaminhamento getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(TbEncaminhamento encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public TbUnidade getUnidadeRecebeu() {
		return unidadeRecebeu;
	}

	public void setUnidadeRecebeu(TbUnidade unidadeRecebeu) {
		this.unidadeRecebeu = unidadeRecebeu;
	}

	public TbUnidade getUnidadeEnviou() {
		return unidadeEnviou;
	}

	public void setUnidadeEnviou(TbUnidade unidadeEnviou) {
		this.unidadeEnviou = unidadeEnviou;
	}

}
