package br.com.xti.ouvidoria.model.enums;

/**
 * @author renato
 */
public enum StatusUsuarioEnum {
	
    INATIVO(0, "Inativo"), 
    ATIVO(1, "Ativo"),
    NOVA_SENHA(2,"Cadastrar nova senha");
    
    private Integer id;
    private String descricao;

    private StatusUsuarioEnum(Integer id, String descricao) {
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
