package br.com.xti.ouvidoria.charts.pojo;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;

public class MensagemSolucionada extends AbstractChartResults implements IResultadoGrafico,Comparable<MensagemSolucionada> {
	
	private String periodo;
	private Date data;
	
	public MensagemSolucionada(String periodo, Date data, List<TbManifestacao> manifestacoes) {
		this.periodo = periodo;
		this.data = data;
		this.idManifestacoes = new TreeSet<Integer>();
		if(ValidacaoHelper.isNotEmpty(manifestacoes)) {
			for (TbManifestacao manifestacao : manifestacoes) {
				this.idManifestacoes.add(manifestacao.getIdManifestacao());
			}
		}
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

	public int getQuantidade() {
		return idManifestacoes.size();
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
		MensagemSolucionada other = (MensagemSolucionada) obj;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		return true;
	}

	@Override
	public int compareTo(MensagemSolucionada o) {
		return data.compareTo(o.getData());
	}
	
}
