/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum PrioridadeEnum {

    NORMAL(1,"Normal"); //representa o registro no banco de dados referente ao valor padrao de PRIORIDADE para o cadastro de uma manifestacao
    
    private Integer id;
    private String descricao;

    private PrioridadeEnum(Integer id, String descricao) {
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
