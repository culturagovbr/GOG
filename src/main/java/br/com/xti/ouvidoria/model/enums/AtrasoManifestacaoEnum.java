package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum AtrasoManifestacaoEnum {

    QUALQUER_ATRASO("0","Qualquer Atraso", ""),
    ATRASO_ENCAMINHAMENTO("1","Atraso para Encaminhamento às Unidades","atrasoEncaminhamento"),
    ATRASO_RESPOSTA_OUVIDORIA("2","Atraso de Resposta à Ouvidoria","atrasoRespostaOuvidoria"),
    ATRASO_RESPOSTA_MANIFESTANTE("3","Atraso para Resposta ao Cidadão","atrasoRespostaManifestante"),
    SEM_ATRASO("4","Sem atraso","");
    
	private String id;
	private String descricao;
	private String styleClass;

	private AtrasoManifestacaoEnum(String id, String descricao, String styleClass) {
		this.id = id;
		this.descricao = descricao;
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getStyleClass() {
		return styleClass;
	}

}
