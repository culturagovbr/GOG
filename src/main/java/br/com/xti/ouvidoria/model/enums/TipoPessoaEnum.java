package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum TipoPessoaEnum {

    FISICA("1","Física"), 
    JURIDICA("2","Jurídica");
    
    private String id;
    private String descricao;

    private TipoPessoaEnum(String id, String descricao) {
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
