package br.com.xti.ouvidoria.model.enums;

/**
 * @author Samuel Correia Guimarães
 */
public enum EmailAutomatizadoEnum {

    NOVO(1, "Esse e-mail será enviado ao cliente quando o usuário cria uma nova Manifestação ou recuperar o link da Manifestação"),
    PRIMEIRO_TRAMITE(2, "Esse e-mail será enviado ao cliente quando ocorrer o primeiro trâmite na Manifestação"),
    RESPOSTA_FINAL(3, "Esse e-mail será enviado ao cliente quando um técnico fecha uma Manifestação no painel de administração"),
    RECUPERACAO_SENHA(4, "Esse e-mail será enviado ao usuário que solicitar a recuperação de senha"),
    RESPONDER_QUESTIONARIO(5, "Esse e-mail será enviado ao usuário caso haja um questionário ativo. Informando-lhe de responder o mesmo"),
    INTERLOCUTOR_OUVIDORIA(6, "Esse e-mail será enviado ao interlocutor sempre que houver um novo trâmite para a sua área"),
    OPERADOR(7, "Esse e-mail será enviado para o operador sempre que houver trâmites que o operador seja envolvido"),
    MANIFESTANTE(8, "Esse e-mail será enviado para a ouvidoria sempre que o manifestante enviar uma comunicação externa"),
    OUVIDORIA(9, "Esse e-mail será enviado a ouvidoria sempre que uma manifestação retornar a ela, seja por um interlocutor ou operador"),
    INTERLOCUTOR_OPERADOR(10, "Esse e-mail será enviado do operador para o interlocutor sempre que houver trâmite do operador para o interlocutor"),
    ATRASO_INTERLOCUTOR(11, "Esse e-mail será enviado aos interlocutores da área que foi enviado notificação de atraso"),
    ATRASO_OPERADOR(12, "Esse e-mail será enviado aos operadores da área que foi enviado notificação de atraso"),
    NOVA_SENHA_MANIFESTACAO(13, "Esse e-mail será enviado ao manifestante quando for solicitada uma nova senha para a manifestação");
    
	private Integer id;
	private String descricao;

	private EmailAutomatizadoEnum(Integer id, String descricao) {
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
