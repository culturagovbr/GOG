package br.com.xti.ouvidoria.charts.pojo;

import java.util.HashSet;

import br.com.xti.ouvidoria.model.TbUnidade;

public class MensagemSolucionadaUnidadeSolucionadora extends AbstractChartResults implements IResultadoGrafico {
	
	private TbUnidade unidade;
	
	public MensagemSolucionadaUnidadeSolucionadora(TbUnidade unidade, int idManifestacao, double porcentagem) {
		this.unidade = unidade;
		this.porcentagem = porcentagem;
		idManifestacoes = new HashSet<Integer>();
		idManifestacoes.add(idManifestacao);
	}
	
	public TbUnidade getUnidade() {
		return unidade;
	}
	
	public void setUnidade(TbUnidade unidade) {
		this.unidade = unidade;
	}
	
	public void setPorcentagem(double porcentagem) {
		this.porcentagem = porcentagem;
	}
	
	public int getQuantidade() {
		return idManifestacoes.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(porcentagem);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		MensagemSolucionadaUnidadeSolucionadora other = (MensagemSolucionadaUnidadeSolucionadora) obj;
		if (Double.doubleToLongBits(porcentagem) != Double
				.doubleToLongBits(other.porcentagem))
			return false;
		if (unidade == null) {
			if (other.unidade != null)
				return false;
		} else if (!unidade.equals(other.unidade))
			return false;
		return true;
	}
	
}
