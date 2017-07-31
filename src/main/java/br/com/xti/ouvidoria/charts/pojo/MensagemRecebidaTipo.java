package br.com.xti.ouvidoria.charts.pojo;

import java.util.TreeSet;

import br.com.xti.ouvidoria.model.TbTipoManifestacao;

public class MensagemRecebidaTipo extends AbstractChartResults implements IResultadoGrafico {
	
	private TbTipoManifestacao tipo;
	
	public MensagemRecebidaTipo(TbTipoManifestacao tipo, int idManifestacao, double porcentagem) {
		this.tipo = tipo;
		this.idManifestacoes = new TreeSet<Integer>();
		this.idManifestacoes.add(idManifestacao);
		this.porcentagem = porcentagem;
	}
	
	public TbTipoManifestacao getTipoManifestacao() {
		return tipo;
	}
	
	public void setTipoManifestacao(TbTipoManifestacao tipo) {
		this.tipo = tipo;
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
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		MensagemRecebidaTipo other = (MensagemRecebidaTipo) obj;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}
	
}
