package br.com.xti.ouvidoria.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.FuncionalidadeEnum;
import br.com.xti.ouvidoria.model.enums.LogOperacaoEnum;
import br.com.xti.ouvidoria.model.enums.RacaEnum;
import br.com.xti.ouvidoria.model.enums.RespostasQuestionariosEnum;
import br.com.xti.ouvidoria.model.enums.SexoEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.StatusUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.TipoManifestanteEnum;
import br.com.xti.ouvidoria.model.enums.TipoPessoaEnum;

/**
 * @author Samuel Correia Guimarães
 */
public class EnumHelper {

    public static SexoEnum getSexoEnum(String id) {
        switch (id) {
            case "1": return SexoEnum.MASCULINO;
            case "2": return SexoEnum.FEMININO;
            case "3": return SexoEnum.NAO_INFORMADO;
            default: return null;
        }
    }
    
    public static TipoManifestanteEnum getTipoManifestanteEnum(String id) {
    	switch (id) {
	    	case "1": return TipoManifestanteEnum.SERVIDOR_PREFEITURA;
	    	case "2": return TipoManifestanteEnum.SERVIDOR_UNIDADE_VINCULADA;
	    	case "3": return TipoManifestanteEnum.CIDADAO;
	    	default: return null;
    	}
    }
    
    public static RespostasQuestionariosEnum getRespostaQuestionarioEnum(String id) {
    	switch (id) {
	    	case "1": return RespostasQuestionariosEnum.CONCORDO_TOTALMENTE;
	    	case "2": return RespostasQuestionariosEnum.CONCORDO;
	    	case "3": return RespostasQuestionariosEnum.DISCORDO;
	    	case "4": return RespostasQuestionariosEnum.DISCORDO_TOTALMENTE;
	    	default: return null;
    	}
    }

    public static RacaEnum getTipoRacaEnum(String id) {
        switch (id) {
            case "1": return RacaEnum.BRANCA;
            case "2": return RacaEnum.PRETA;
            case "3": return RacaEnum.AMARELA;
            case "4": return RacaEnum.PARDA;
            case "5": return RacaEnum.INDIGENA;
            case "6": return RacaEnum.NAO_INFORMADO;
            default: return null;
        }
    }

    public static TipoPessoaEnum getTipoPessoaEnum(String id) {
    	switch (id) {
	    	case "1": return TipoPessoaEnum.FISICA;
	    	case "2": return TipoPessoaEnum.JURIDICA;
	    	default: return null;
    	}
    }
    
    public static StatusManifestacaoEnum getStatusManifestacaoEnum(String id) {
        switch (id) {
            case "1": return StatusManifestacaoEnum.NOVA;
            case "2": return StatusManifestacaoEnum.EM_ANALISE;
            case "3": return StatusManifestacaoEnum.EM_ANDAMENTO;
            case "4": return StatusManifestacaoEnum.SOLUCIONADA;
            case "6": return StatusManifestacaoEnum.INATIVADA;
            case "7": return StatusManifestacaoEnum.SOLICITADA_INFORMACAO;
            case "8": return StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA;
            case "9": return StatusManifestacaoEnum.EM_MONITORAMENTO;
            default: return null;
        }
    }

    public static StatusEncaminhamentoEnum getStatusEncaminhamentoEnum(String id) {
        switch (id) {
            case "1": return StatusEncaminhamentoEnum.ENCAMINHADA;
            case "2": return StatusEncaminhamentoEnum.RETORNADA;
            default: return null;
        }
    }

    public static EmailAutomatizadoEnum getEmailAutomatizadoEnum(String id) {
        switch (id) {
            case "1": return EmailAutomatizadoEnum.NOVO;
            case "2": return EmailAutomatizadoEnum.PRIMEIRO_TRAMITE;
            case "3": return EmailAutomatizadoEnum.RESPOSTA_FINAL;
            default: return null;
        }
    }

    public static LogOperacaoEnum getLogOperacaoEnum(String id) {
        switch (id) {
            case "1": return LogOperacaoEnum.INSERIR;
            case "2": return LogOperacaoEnum.ALTERAR;
            case "3": return LogOperacaoEnum.EXCLUIR;
            case "4": return LogOperacaoEnum.CONSULTAR;
            default: return null;
        }
    }

    public static BooleanEnum getBooleanEnum(String id) {
        switch (id) {
            case "1": return BooleanEnum.SIM;
            case "2": return BooleanEnum.NAO;
            default: return null;
        }
    }

    public static FuncaoUsuarioEnum getFuncaoUsuarioEnum(String id) {
        switch (id) {
            case "1": return FuncaoUsuarioEnum.OUVIDOR;
            case "2": return FuncaoUsuarioEnum.INTERLOCUTOR;
            case "3": return FuncaoUsuarioEnum.OPERADOR;
            case "4": return FuncaoUsuarioEnum.MANIFESTANTE;
            case "5": return FuncaoUsuarioEnum.ADMINISTRADOR;
            default: return null;
        }
    }

    public static StatusUsuarioEnum getStatusUsuarioEnum(Integer id) {
        switch (id) {
            case 0: return StatusUsuarioEnum.INATIVO;
            case 1: return StatusUsuarioEnum.ATIVO;
            case 2: return StatusUsuarioEnum.NOVA_SENHA;
            default: return null;
        }
    }

    public static FuncionalidadeEnum getFuncionalidadeEnum(Integer id) throws Exception {
        switch (id) {
            case 1: return FuncionalidadeEnum.ATUALIZAR_MINHAS_INFORMACOES;
            case 2: return FuncionalidadeEnum.CONFIGURAR_PREFERENCIAS_DE_SISTEMA;
            case 3: return FuncionalidadeEnum.CONSULTAR_PROJETOS_DO_SALIC;
            case 4: return FuncionalidadeEnum.CONSULTAR_PROPOSTAS_DO_SALIC;
            case 5: return FuncionalidadeEnum.GERENCIAR_MANIFESTACAO;
            case 6: return FuncionalidadeEnum.MANTER_AJUDA;
            case 7: return FuncionalidadeEnum.MANTER_AREA_DE_ENTRADA;
            case 8: return FuncionalidadeEnum.MANTER_AVISO;
            case 9: return FuncionalidadeEnum.MANTER_CLASSIFICACOES;
            case 10: return FuncionalidadeEnum.MANTER_EMAILS_DE_NOTIFICACAO;
            case 11: return FuncionalidadeEnum.MANTER_ENCAMINHAMENTO_PADRONIZADO;
            case 12: return FuncionalidadeEnum.MANTER_FAIXA_ETARIA;
            case 13: return FuncionalidadeEnum.MANTER_FILTROS_PERSONALIZADOS;
            case 14: return FuncionalidadeEnum.MANTER_FILTROS_SPAM;
            case 15: return FuncionalidadeEnum.MANTER_GRUPO;
            case 16: return FuncionalidadeEnum.MANTER_LISTA_MANIFESTACAO;
            case 17: return FuncionalidadeEnum.MANTER_MEIO_DE_ENTRADA;
            case 18: return FuncionalidadeEnum.MANTER_MEIO_DE_RESPOSTA;
            case 19: return FuncionalidadeEnum.MANTER_PERFIL;
            case 20: return FuncionalidadeEnum.MANTER_PERFIL_X_FUNCIONALIDADES;
            case 21: return FuncionalidadeEnum.MANTER_PERFIL_X_GRUPO;
            case 22: return FuncionalidadeEnum.MANTER_PRIORIDADE;
            case 23: return FuncionalidadeEnum.MANTER_RESPOSTAS_MANIFESTACOES;
            case 24: return FuncionalidadeEnum.MANTER_SUBCLASSIFICACOES;
            case 25: return FuncionalidadeEnum.MANTER_TIPO_MANIFESTACAO;
            case 26: return FuncionalidadeEnum.MANTER_UNIDADE;
            case 27: return FuncionalidadeEnum.MANTER_USUARIO_X_PERFIL;
            case 28: return FuncionalidadeEnum.MANTER_USUARIOS;
            case 29: return FuncionalidadeEnum.PAINEL_DE_NOTIFICACAO_CONTROLE_PRAZOS;
            case 30: return FuncionalidadeEnum.REALIZAR_ENCAMINHAMENTO_UNIDADE;
            case 31: return FuncionalidadeEnum.REALIZAR_EXTRACAO_DE_DADOS;
            case 32: return FuncionalidadeEnum.REALIZAR_PESQUISA_NAS_MANIFESTACOES;
            case 33: return FuncionalidadeEnum.REALIZAR_PESQUISA_NOS_TRAMITES;
            case 34: return FuncionalidadeEnum.REGISTRAR_COMUNICACAO_EXTERNA;
            case 35: return FuncionalidadeEnum.REGISTRAR_MANIFESTACAO;
            case 36: return FuncionalidadeEnum.REGISTRAR_TRAMITE;
            case 37: return FuncionalidadeEnum.VISUALIZAR_ESTATISTICAS_DE_MANIFESTACAO;
            case 38: return FuncionalidadeEnum.VISUALIZAR_LOG_DE_OPERACOES;
            case 39: return FuncionalidadeEnum.MANTER_GRUPO_AJUDA;
            case 40: return FuncionalidadeEnum.VISUALIZAR_DADOS_SIGILOSOS;
            case 41: return FuncionalidadeEnum.MANTER_GRAU_INSTRUCAO;
            case 42: return FuncionalidadeEnum.GERAR_GRAFICOS;
            case 43: return FuncionalidadeEnum.MANTER_QUESTIONARIO;
        }
        throw new Exception("Funcionalidade não listada.");
    }
    
    public static List<StatusManifestacaoEnum> getListaStatusManifestacaoOrdenada() {
    	List<StatusManifestacaoEnum> lista = Arrays.asList(StatusManifestacaoEnum.values());
    	Collections.sort(lista, new Comparator<StatusManifestacaoEnum>() {
    		@Override
    		public int compare(StatusManifestacaoEnum staus1, StatusManifestacaoEnum status2) {
    			return staus1.getDescricao().compareToIgnoreCase(status2.getDescricao());
    		}
		});
    	return lista;
    }
    
    public static List<RacaEnum> getListaRacaOrdenada() {
    	List<RacaEnum> lista = Arrays.asList(RacaEnum.values());
    	Collections.sort(lista, new Comparator<RacaEnum>() {
    		@Override
    		public int compare(RacaEnum raca1, RacaEnum raca2) {
    			return raca1.getDescricao().compareToIgnoreCase(raca2.getDescricao());
    		}
    	});
    	return lista;
    }
    
}
