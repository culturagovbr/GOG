package br.com.xti.ouvidoria.charts.pojo;

import java.util.TreeSet;

import br.com.xti.ouvidoria.model.TbUnidade;

public class DiasRespostaAOuvidoria extends AbstractChartResults implements IResultadoGrafico, Comparable<DiasRespostaAOuvidoria> {

	private TbUnidade unidade;
	private int quantidadeDias;

	public DiasRespostaAOuvidoria(TbUnidade unidade, int idManifestacao, int quantidadeDias) {
		this.unidade = unidade;
		this.quantidadeDias = quantidadeDias;
		this.idManifestacoes = new TreeSet<Integer>();
		this.idManifestacoes.add(idManifestacao);
	}
	
	public TbUnidade getUnidade() {
		return unidade;
	}
	
	public void addDias(int dias) {
		quantidadeDias += dias;
	}
	
	protected double getPorcentagem() {
		return porcentagem;
	}
	
	@Override
	public String getPorcentagemString() {
		if(porcentagem == 0) {
			calcularPorcetagem();
		}
		return super.getPorcentagemString();
	}
	
	public void calcularPorcetagem() {
		porcentagem = (double) quantidadeDias / getTotalCount();
	}

	@Override
	public int getQuantidade() {
		return quantidadeDias;
	}
	
	public int getQuantidadeManifestacoes() {
		return idManifestacoes.size();
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
		DiasRespostaAOuvidoria other = (DiasRespostaAOuvidoria) obj;
		if (unidade == null) {
			if (other.unidade != null)
				return false;
		} else if (!unidade.equals(other.unidade))
			return false;
		return true;
	}

	@Override
	public int compareTo(DiasRespostaAOuvidoria o) {
		return Double.valueOf(porcentagem).compareTo(o.getPorcentagem());
	}

}
