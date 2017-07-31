package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum BooleanEnum {
	
    SIM("1", "Sim"), 
    NAO("2", "Não");
    
    private String id;
    private String descricao;

    private BooleanEnum(String id, String descricao) {
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
