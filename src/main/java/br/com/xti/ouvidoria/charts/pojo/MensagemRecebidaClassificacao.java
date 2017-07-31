package br.com.xti.ouvidoria.charts.pojo;

import java.util.TreeSet;

import br.com.xti.ouvidoria.model.TbClassificacao;

public class MensagemRecebidaClassificacao extends AbstractChartResults implements IResultadoGrafico, Comparable<MensagemRecebidaClassificacao> {
	
	private TbClassificacao classificacao;
	
	public MensagemRecebidaClassificacao(TbClassificacao classificacao, int idManifestacao, double porcentagem) {
		this.classificacao = classificacao;
		this.idManifestacoes = new TreeSet<Integer>();
		this.idManifestacoes.add(idManifestacao);
		this.porcentagem = porcentagem;
	}
	
	public TbClassificacao getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(TbClassificacao classificacao) {
		this.classificacao = classificacao;
	}

	public int getQuantidade() {
		return idManifestacoes.size();
	}
	
	public void setPorcentagem(double porcentagem) {
		this.porcentagem = porcentagem;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificacao == null) ? 0 : classificacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MensagemRecebidaClassificacao other = (MensagemRecebidaClassificacao) obj;
		if (classificacao == null) {
			if (other.classificacao != null)
				return false;
		} else if (!classificacao.equals(other.classificacao))
			return false;
		return true;
	}

	@Override
	public int compareTo(MensagemRecebidaClassificacao o) {
		return new Integer(o.getQuantidade()).compareTo(getQuantidade());
	}
	
	@Override
	public String toString() {
		return classificacao.toString() + ": " + getQuantidade();
	}
	
}
