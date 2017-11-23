/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author Samuel Correia Guimarães
 */
public enum MeioRespostaEnum {

    EMAIL_ELETRONICO(1,"Email Eletrônico"); //representa o registro no banco de dados referente ao valor padrao de MEIO RESPOSTA para o cadastro de uma manifestacao
    
    private Integer id;
    private String descricao;

    private MeioRespostaEnum(Integer id, String descricao) {
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
