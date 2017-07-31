/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum TipoPerfilEnum {
    INTERNO("1", "Interno"), EXTERNO("2", "Externo");
    
    private String id;
    private String descricao;

    private TipoPerfilEnum(String id, String descricao) {
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
