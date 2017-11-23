package br.com.xti.ouvidoria.model.enums;

/**
 * @author samuel.guimaraes
 */
public enum PalavrasChavesEnum {

    HOJE("Data atual"),
    // Dados da Manifestação
    NUMERO_MANIFESTACAO("Número da Manifestação"),
    SENHA_MANIFESTACAO("Senha da Manifestação"),
    TIPO_MANIFESTACAO("Tipo da Manifestação"),
    DATA_REGISTRO("Data de Cadastro da Manifestação"),
    ULTIMA_ATUALIZACAO("Data da última atualização"),
    AREA_ENTRADA("Area de entrada"),
    MEIO_ENTRADA("Meio de entrada"),
    CLASSIFICACAO("Classificação"),
    SUBCLASSIFICACAO("Subclassificação"),
    PRIORIDADE("Prioridade"),
    STATUS("Status"),
    AREA_SOLUCIONADORA("Nome da Área Solucionadora"),
    //    UNIDADE("Unidade que recebeu encaminhamento"),
    // Dados do Manifestante
    NOME("Nome do Manifestante"),
    EMAIL("Email do Manifestante"),
    EMAIL_SECUNDARIO("Email Secundário do Manifestante"),
    ENDERECO("Endereço do Manifestante"),
    PAIS("País do Manifestante"),
    ESTADO("Estado do Manifestante"),
    CIDADE("Cidade do Manifestante"),
    CEP("CEP do Manifestante"),
    FAIXA_ETARIA("Faixa Etária do Manifestante"),
    SEXO("Sexo do Manifestante"),
    RACA("Raça do Manifestante"),
    //Dados do Usuario
    NOME_USUARIO("Nome do Usuário"),
    EMAIL_USUARIO("E-mail do Usuário"),
    SENHA_USUARIO("Senha do Usuário"),
    LOGIN_USUARIO("Login do Usuário"),
    TELEFONE_USUARIO("Telefone do Usuário"),
    
    //Parametro
    URL_ACESSO_MANIFESTACAO("URL de acesso à manifestação");
    
    private String descricao;

    private PalavrasChavesEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
