package br.com.xti.ouvidoria.comparator;

import java.util.Comparator;

import br.com.xti.ouvidoria.model.enums.RacaEnum;

public class RacaNomeComparator implements Comparator<RacaEnum> {

	@Override
	public int compare(RacaEnum raca1, RacaEnum raca2) {
		return raca1.getDescricao().compareTo(raca2.getDescricao());
	}

}
