package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum FuncaoUsuarioEnum {
	
    OUVIDOR("1","Ouvidor"),
    INTERLOCUTOR("2","Interlocutor"),
    OPERADOR("3","Operador"),
    MANIFESTANTE("4","Manifestante"),
    ADMINISTRADOR("5","Administrador");
    
    private String id;
    private String descricao;

    private FuncaoUsuarioEnum(String id, String descricao) {
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
