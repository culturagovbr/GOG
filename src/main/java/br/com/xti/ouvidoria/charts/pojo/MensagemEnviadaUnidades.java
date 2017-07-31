package br.com.xti.ouvidoria.charts.pojo;

import java.util.TreeSet;

import br.com.xti.ouvidoria.model.TbUnidade;

public class MensagemEnviadaUnidades extends AbstractChartResults implements IResultadoGrafico {
	
	private TbUnidade unidade;
	private int quantidade;
	
	public MensagemEnviadaUnidades(TbUnidade unidade, int idManifestacao, int quantidade, double porcentagem) {
		this.unidade = unidade;
		this.idManifestacoes = new TreeSet<Integer>();
		this.idManifestacoes.add(idManifestacao);
		this.quantidade = quantidade;
		this.porcentagem = porcentagem;
	}
	
	public TbUnidade getUnidade() {
		return unidade;
	}
	
	public void setUnidade(TbUnidade unidade) {
		this.unidade = unidade;
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public void addQuantidade() {
		addQuantidade(1);
	}
	
	public void addQuantidade(int quantidade) {
		this.quantidade += quantidade;
	}

	public void setPorcentagem(double porcentagem) {
		this.porcentagem = porcentagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
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
		MensagemEnviadaUnidades other = (MensagemEnviadaUnidades) obj;
		if (unidade == null) {
			if (other.unidade != null)
				return false;
		} else if (!unidade.equals(other.unidade))
			return false;
		return true;
	}
	
}
