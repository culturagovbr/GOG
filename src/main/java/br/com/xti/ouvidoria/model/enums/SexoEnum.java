/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum SexoEnum {
    MASCULINO("1", "Masculino"), FEMININO("2", "Feminino"), NAO_INFORMADO("3", "Não Informado");
    
    private String id;
    private String descricao;

    private SexoEnum(String id, String descricao) {
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
