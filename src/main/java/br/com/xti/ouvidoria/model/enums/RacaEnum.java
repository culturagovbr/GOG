package br.com.xti.ouvidoria.model.enums;

/**
 * @author marcos.ribeiro
 */
public enum RacaEnum {

    BRANCA("1","Branca"), 
    PRETA("2","Negra"), 
    AMARELA("3","Amarela"), 
    PARDA("4","Parda"), 
    INDIGENA("5","Indígena"), 
    NAO_INFORMADO("6", "Não Informado");
    
    private String id;
    private String descricao;

    private RacaEnum(String id, String descricao) {
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
