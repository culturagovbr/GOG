package br.com.xti.ouvidoria.comparator;

import java.util.Comparator;

import br.com.xti.ouvidoria.model.TbUnidade;

public class UnidadeSiglaComparator implements Comparator<TbUnidade> {

	@Override
	public int compare(TbUnidade unidade1, TbUnidade unidade2) {
		return unidade1.getSgUnidade().toUpperCase().compareTo(unidade2.getSgUnidade().toUpperCase());
	}

}
