package br.com.xti.ouvidoria.charts.pojo;

import java.util.TreeSet;

import br.com.xti.ouvidoria.model.enums.RegiaoEnum;


public class MensagemRecebidaRegiaoEconomica extends AbstractChartResults implements IResultadoGrafico {
	
	private RegiaoEnum regiao;
	
	private MensagemRecebidaRegiaoEconomica(RegiaoEnum regiao) {
		this.regiao = regiao;
		this.idManifestacoes = new TreeSet<Integer>();
	}
	
	public RegiaoEnum getRegiao() {
		return regiao;
	}
	
	public void setRegiao(RegiaoEnum regiao) {
		this.regiao = regiao;
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
		result = prime * result + ((regiao == null) ? 0 : regiao.hashCode());
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
		MensagemRecebidaRegiaoEconomica other = (MensagemRecebidaRegiaoEconomica) obj;
		if (regiao == null) {
			if (other.regiao != null)
				return false;
		} else if (!regiao.equals(other.regiao))
			return false;
		return true;
	}
	
	// Métodos para criar instâncias
	public static MensagemRecebidaRegiaoEconomica regiaoNorte() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.NORTE);
	}
	
	public static MensagemRecebidaRegiaoEconomica regiaoNordeste() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.NORDESTE);
	}
	
	public static MensagemRecebidaRegiaoEconomica regiaoCentroOeste() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.CENTRO_OESTE);
	}
	
	public static MensagemRecebidaRegiaoEconomica regiaoSudeste() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.SUDESTE);
	}
	
	public static MensagemRecebidaRegiaoEconomica regiaoSul() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.SUL);
	}
	
	public static MensagemRecebidaRegiaoEconomica regiaoNaoEspecificada() {
		return new MensagemRecebidaRegiaoEconomica(RegiaoEnum.NAO_ESPECIFICADO);
	}
	
}
