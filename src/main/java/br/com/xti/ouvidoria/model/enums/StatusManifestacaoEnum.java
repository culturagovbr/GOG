/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

/**
 *
 * @author marcos.ribeiro
 */
public enum StatusManifestacaoEnum {

    NOVA("1", "Nova"), 
    EM_ANALISE("2", "Em Análise"), 
    EM_ANDAMENTO("3", "Em Andamento"), 
    SOLUCIONADA("4", "Solucionada"), 
    INATIVADA("6", "Inativa"),
    SOLICITADA_INFORMACAO("7", "Solicitada Informação"), 
    SOLICITACAO_RESPONDIDA("8", "Solicitação Respondida"),
    EM_MONITORAMENTO("9", "Em Monitoramento"); 
    
    private String id;
    private String descricao;

    private StatusManifestacaoEnum(String id, String descricao) {
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
