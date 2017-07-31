package br.com.xti.ouvidoria.comparator;

import java.util.Comparator;

import br.com.xti.ouvidoria.model.TbPerguntaQuestionario;

public class PerguntaQuestionarioPosicaoComparator implements Comparator<TbPerguntaQuestionario> {

	@Override
	public int compare(TbPerguntaQuestionario o1, TbPerguntaQuestionario o2) {
		return o1.getPosicaoPergunta().compareTo(o2.getPosicaoPergunta());
	}

}
