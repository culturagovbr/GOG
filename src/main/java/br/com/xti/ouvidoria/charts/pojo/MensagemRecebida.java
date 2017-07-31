package br.com.xti.ouvidoria.charts.pojo;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;

public class MensagemRecebida extends AbstractChartResults implements IResultadoGrafico, Comparable<MensagemRecebida> {
	
	private String periodo;
	private Date data;
	private int quantidadeInterna;
	private int quantidadeVinculada;
	private int quantidadeTotal;
	private Set<Integer> idManifestacoesInternas;
	private Set<Integer> idManifestacoesVinculadas;
	
	public MensagemRecebida(String periodo, Date data,
			List<TbManifestacao> manifestacoesInternas,
			List<TbManifestacao> manifestacoesVinculadas, int quantidadeTotal) {
		this.periodo = periodo;
		this.data = data;
		this.idManifestacoes = new TreeSet<Integer>();
		
		this.idManifestacoesInternas = new TreeSet<Integer>();
		if(ValidacaoHelper.isNotEmpty(manifestacoesInternas)) {
			for (TbManifestacao manifestacao : manifestacoesInternas) {
				this.idManifestacoes.add(manifestacao.getIdManifestacao());
				this.idManifestacoesInternas.add(manifestacao.getIdManifestacao());
			}
			this.quantidadeInterna = manifestacoesInternas.size();
		}
		
		this.idManifestacoesVinculadas = new TreeSet<Integer>();
		if(ValidacaoHelper.isNotEmpty(manifestacoesVinculadas)) {
			for (TbManifestacao manifestacao : manifestacoesVinculadas) {
				this.idManifestacoes.add(manifestacao.getIdManifestacao());
				this.idManifestacoesVinculadas.add(manifestacao.getIdManifestacao());
			}
			this.quantidadeVinculada = manifestacoesVinculadas.size();
		}
		this.quantidadeTotal = quantidadeTotal;
	}
	
	public String getPeriodo() {
		return periodo;
	}
	
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getQuantidadeInterna() {
		return quantidadeInterna;
	}

	public void setQuantidadeInterna(int quantidadeInterna) {
		this.quantidadeInterna = quantidadeInterna;
	}

	public int getQuantidadeVinculada() {
		return quantidadeVinculada;
	}

	public void setQuantidadeVinculada(int quantidadeVinculada) {
		this.quantidadeVinculada = quantidadeVinculada;
	}
	
	public int getQuantidadeTotal() {
		return quantidadeTotal;
	}
	
	public String getIdManifestacoesInternas() {
		return joinManifestationIds(idManifestacoesInternas);
	}
	
	public String getIdManifestacoesVinculadas() {
		return joinManifestationIds(idManifestacoesVinculadas);
	}
	
	@Override
	public int getQuantidade() {
		return quantidadeTotal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
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
		MensagemRecebida other = (MensagemRecebida) obj;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		return true;
	}

	@Override
	public int compareTo(MensagemRecebida o) {
		return data.compareTo(o.getData());
	}
	
	
}
