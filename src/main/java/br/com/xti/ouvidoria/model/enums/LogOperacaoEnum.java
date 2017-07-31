package br.com.xti.ouvidoria.model.enums;

/**
 * @author renato
 */
public enum LogOperacaoEnum {

    INSERIR("1", "Inserção", "inseriu"),
    ALTERAR("2", "Alteração", "alterou"),
    EXCLUIR("3", "Exclusão", "excluiu"),
    CONSULTAR("4", "Consulta", "consultou");
    
    private String acao;
    private String id;
    private String descricao;

    private LogOperacaoEnum(String id, String descricao, String acao) {
        this.acao = acao;
        this.id = id;
        this.descricao = descricao;
    }

    public String getAcao() {
        return acao;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    
}
