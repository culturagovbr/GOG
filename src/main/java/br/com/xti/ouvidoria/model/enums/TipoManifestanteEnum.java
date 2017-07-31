package br.com.xti.ouvidoria.model.enums;

/**
 * Opções do campo "Tipo Manifestante" que define se o manifestante
 * que está criando a manifestação é externo ou interno (do MinC ou Vinculadas)
 * 
 * @author Samuel Correia Guimarães
 * @since v1.2 17/11/2014
 */
public enum TipoManifestanteEnum {
	
	CIDADAO("1","Cidadão"),
    SERVIDOR_MINC("2","Servidor MinC"),
    SERVIDOR_UNIDADE_VINCULADA("3","Servidor Unidade Vinculada");
    
    private String id;
    private String descricao;

    private TipoManifestanteEnum(String id, String descricao) {
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
