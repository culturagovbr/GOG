/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum UnidadeEnum {

    OUVIDORIA(1,"Ouvidoria"); //representa o registro no banco de dados referente à unidade OUVIDORIA
    
    private Integer id;
    private String descricao;

    private UnidadeEnum(Integer id, String descricao) {
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
