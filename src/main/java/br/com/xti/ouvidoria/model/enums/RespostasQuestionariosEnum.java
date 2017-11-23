package br.com.xti.ouvidoria.model.enums;

/**
 * @author Samuel Correia Guimarães
 */
public enum RespostasQuestionariosEnum {

	CONCORDO_TOTALMENTE(1, "Concordo Totalmente"), 
	CONCORDO(2, "Concordo"), 
	DISCORDO(3, "Discordo"), 
	DISCORDO_TOTALMENTE(4, "Discordo Totalmente");

	private Integer id;
	private String descricao;

	private RespostasQuestionariosEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

}
