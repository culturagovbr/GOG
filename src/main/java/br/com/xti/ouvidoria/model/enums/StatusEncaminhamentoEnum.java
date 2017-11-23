package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum StatusEncaminhamentoEnum {

	ENCAMINHADA("1", "Encaminhada"),
	RETORNADA("2", "Retornada");

	private String id;
	private String descricao;

	private StatusEncaminhamentoEnum(String id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
}
