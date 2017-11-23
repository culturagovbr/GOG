package br.com.xti.ouvidoria.controller;

import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.com.xti.ouvidoria.comparator.RacaNomeComparator;
import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.enums.AtrasoManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.EmailAutomatizadoEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.LogOperacaoEnum;
import br.com.xti.ouvidoria.model.enums.PalavrasChavesEnum;
import br.com.xti.ouvidoria.model.enums.RacaEnum;
import br.com.xti.ouvidoria.model.enums.RegiaoEnum;
import br.com.xti.ouvidoria.model.enums.RespostasQuestionariosEnum;
import br.com.xti.ouvidoria.model.enums.SexoEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.TipoManifestanteEnum;
import br.com.xti.ouvidoria.model.enums.TipoPessoaEnum;

/**
 * @author Samuel Correia Guimarães
 */
@Named("mBEnum")
@ApplicationScoped
public class MBEnum {
	
	private String codStatusManifestacaoNOVA = StatusManifestacaoEnum.NOVA.getId();
    private String codStatusManifestacaoEM_ANALISE = StatusManifestacaoEnum.EM_ANALISE.getId();
    private String codStatusManifestacaoEM_ANDAMENTO = StatusManifestacaoEnum.EM_ANDAMENTO.getId();
    private String codStatusManifestacaoSOLUCIONADA = StatusManifestacaoEnum.SOLUCIONADA.getId();
    private String codStatusManifestacaoEM_MONITORAMENTO = StatusManifestacaoEnum.EM_MONITORAMENTO.getId();
    
    
    
    
    public RegiaoEnum[] getRegiaoEnums() {
        return RegiaoEnum.values();
    }
    
    public RespostasQuestionariosEnum[] getRespostasQuestionariosEnums() {
    	return RespostasQuestionariosEnum.values();
    }
    
    public SexoEnum[] getSexoEnums() {
        return SexoEnum.values();
    }
    
    public TipoManifestanteEnum[] getTipoManifestanteEnums() {
    	return TipoManifestanteEnum.values();
    }

    public BooleanEnum[] getBooleanEnums() {
        return BooleanEnum.values();
    }

    public RacaEnum[] getRacaEnums() {
    	RacaEnum[] racas = RacaEnum.values();
    	Arrays.sort(racas, new RacaNomeComparator());
        return racas;
    }

    public StatusManifestacaoEnum[] getStatusManifestacaoEnums() {
        return StatusManifestacaoEnum.values();
    }

    public StatusEncaminhamentoEnum[] getStatusEncaminhamentoEnums() {
        return StatusEncaminhamentoEnum.values();
    }

    public FuncaoUsuarioEnum[] getFuncaoUsuarioEnums() {
        return FuncaoUsuarioEnum.values();
    }

    public PalavrasChavesEnum[] getPalavrasChavesEnums() {
        return PalavrasChavesEnum.values();
    }

    public PalavrasChavesEnum[] getPalavrasChavesUsuariosEnums() {
        return new PalavrasChavesEnum[]{PalavrasChavesEnum.HOJE,
            PalavrasChavesEnum.NOME_USUARIO,
            PalavrasChavesEnum.EMAIL_USUARIO,
            PalavrasChavesEnum.SENHA_USUARIO,
            PalavrasChavesEnum.LOGIN_USUARIO,
            PalavrasChavesEnum.TELEFONE_USUARIO};
    }

    public SexoEnum getSexoEnum(String id) throws Exception {
        return EnumHelper.getSexoEnum(id);
    }
    
    public RespostasQuestionariosEnum getRespostaQuestionarioEnum(Integer id) throws Exception {
    	return getRespostaQuestionarioEnum(String.valueOf(id));
    }
    
    public RespostasQuestionariosEnum getRespostaQuestionarioEnum(String id) throws Exception {
    	return EnumHelper.getRespostaQuestionarioEnum(id);
    }

    public RacaEnum getTipoRacaEnum(String id) throws Exception {
        return EnumHelper.getTipoRacaEnum(id);
    }
    
    public TipoPessoaEnum getTipoPessoaEnum(String id) throws Exception {
    	return EnumHelper.getTipoPessoaEnum(id);
    }

    public StatusManifestacaoEnum getStatusManifestacaoEnum(String id) throws Exception {
        return EnumHelper.getStatusManifestacaoEnum(id);
    }

    public StatusEncaminhamentoEnum getStatusEncaminhamentoEnum(String id) throws Exception {
        return EnumHelper.getStatusEncaminhamentoEnum(id);
    }

    public EmailAutomatizadoEnum getEmailAutomatizadoEnum(String id) throws Exception {
        return EnumHelper.getEmailAutomatizadoEnum(id);
    }

    public LogOperacaoEnum getLogOperacaoEnum(String id) throws Exception {
        return EnumHelper.getLogOperacaoEnum(id);
    }

    public BooleanEnum getBooleanEnum(String id) throws Exception {
        return EnumHelper.getBooleanEnum(id);
    }

    public FuncaoUsuarioEnum getFuncaoUsuarioEnum(String id) {
        return EnumHelper.getFuncaoUsuarioEnum(id);
    }

    public AtrasoManifestacaoEnum[] getAtrasoManifestacaoEnum() {
        return AtrasoManifestacaoEnum.values();
    }

	public String getCodStatusManifestacaoNOVA() {
		return codStatusManifestacaoNOVA;
	}

	public String getCodStatusManifestacaoEM_ANALISE() {
		return codStatusManifestacaoEM_ANALISE;
	}

	public String getCodStatusManifestacaoEM_ANDAMENTO() {
		return codStatusManifestacaoEM_ANDAMENTO;
	}

	public String getCodStatusManifestacaoSOLUCIONADA() {
		return codStatusManifestacaoSOLUCIONADA;
	}

	public String getCodStatusManifestacaoEM_MONITORAMENTO() {
		return codStatusManifestacaoEM_MONITORAMENTO;
	}
	
	public StatusManifestacaoEnum getStatusManifestacaoEM_MONITORAMENTO() {
		return StatusManifestacaoEnum.EM_MONITORAMENTO;
	}

	public StatusManifestacaoEnum getStatusManifestacaoSOLUCIONADA() {
		return StatusManifestacaoEnum.SOLUCIONADA;
	}

}
