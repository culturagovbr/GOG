/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum MeioEntradaEnum {

    FORMULARIO_ELETRONICO(1,"Formulário Eletrônico"); //representa o registro no banco de dados referente ao valor padrao de MEIO ENTRADA para o cadastro de uma manifestacao
    
    private Integer id;
    private String descricao;

    private MeioEntradaEnum(Integer id, String descricao) {
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
