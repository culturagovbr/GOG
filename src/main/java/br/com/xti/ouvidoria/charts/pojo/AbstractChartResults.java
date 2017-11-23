package br.com.xti.ouvidoria.charts.pojo;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractChartResults {
	
	protected Set<Integer> idManifestacoes;
	protected double porcentagem;
	
	public String getIdManifestacoes() {
		return joinManifestationIds(idManifestacoes);
	}

	public void addIdManifestacao(int idManifestacao) {
		this.idManifestacoes.add(idManifestacao);
	}
	
	public int getTotalCount() {
		return idManifestacoes.size();
	}
	
	public String getPorcentagemString() {
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(2);
		return numberFormat.format(porcentagem);
	}
	
	protected String joinManifestationIds(Set<Integer> idManifestacoes) {
		StringBuilder ids = new StringBuilder();
		for (Integer integer : idManifestacoes) {
			if(ids.length() == 0) {
				ids.append(integer);
			} else {
				ids.append(",").append(integer);
			}
		}
		
		return ids.toString();
	}

}
