package br.com.xti.ouvidoria.helper;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;

import br.com.xti.ouvidoria.controller.DominioCDIBean;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.PalavrasChavesEnum;

/**
 * @author Samuel Correia Guimarães
 */
public class PalavrasChavesHelper {
	
    public static String converterPalavrasChaves(String texto, TbManifestacao manifestacao, String respostaOuvidor, boolean isEmailInterno) {
        String t = converterPalavrasChaves(texto, manifestacao, isEmailInterno);
        // Adiciona resposta do Ouvidor
        t = substituirPorValor(t, "%RESPOSTA_OUVIDOR%", respostaOuvidor);
        return t;
    }
    
    @SuppressWarnings("incomplete-switch")
	public static String converterPalavrasChaves(String texto, TbManifestacao manifestacao, boolean isEmailInterno) {
        if(!ValidacaoHelper.isNotEmpty(texto) || manifestacao == null) {
            return "";
        }
        String t = texto;
        
        DominioCDIBean dominioBean = CdiHelper.getFacadeWithJNDI(DominioCDIBean.class);
        
        for (PalavrasChavesEnum palavra : PalavrasChavesEnum.values()) {
            try {
                switch(palavra) {
                    case HOJE: t = substituirPorValor(t, "%"+palavra+"%", DataHelper.converterData(new Date())); break;
                    // Dados da Manifestação
                    case NUMERO_MANIFESTACAO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getNrManifestacao()); break;
                    case SENHA_MANIFESTACAO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getDsSenha()); break;
                    case TIPO_MANIFESTACAO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdTipoManifestacao().getNmTipoManifestacao()); break;
                    case DATA_REGISTRO: t = substituirPorValor(t, "%"+palavra+"%", DataHelper.converterData(manifestacao.getDtCadastro())); break;
                    case ULTIMA_ATUALIZACAO: t = substituirPorValor(t, "%"+palavra+"%", DataHelper.converterData(manifestacao.getDtUltimaAtualizacao())); break;
                    case AREA_ENTRADA: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdAreaEntrada().getNmAreaEntrada()); break;
                    case MEIO_ENTRADA: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdMeioEntrada().getNmMeioEntrada()); break;
                    case CLASSIFICACAO: t = substituirPorValor(t, "%"+palavra+"%", getClassificacoes(manifestacao.getTbClassificacaoCollection())); break;
                    case SUBCLASSIFICACAO: t = substituirPorValor(t, "%"+palavra+"%", getSubClassificacoes(manifestacao.getTbSubClassificacaoCollection())); break;
                    case PRIORIDADE: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdPrioridade().getNmPrioridade()); break;
                    case STATUS: t = substituirPorValor(t, "%"+palavra+"%", EnumHelper.getStatusManifestacaoEnum(manifestacao.getStStatusManifestacao())); break;
                    case AREA_SOLUCIONADORA: t = substituirPorValor(t, "%"+palavra+"%", getAreaSolucionadora(manifestacao.getTbUnidadeAreaSolucionadoraCollection())); break;
                    // Dados do Manifestante
                    case NOME: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getNmPessoa()); break;
                    case EMAIL: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getEeEmailUsuario()); break;
                    case EMAIL_SECUNDARIO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getEeEmailSecundario()); break;
                    case ENDERECO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getEnEndereco()); break;
                    case PAIS: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdPais().getNmPais()); break;
                    case ESTADO: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getEnEndereco()); break;
                    case CIDADE: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getEnEndereco()); break;
                    case CEP: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getNrCEP()); break;
                    case FAIXA_ETARIA: t = substituirPorValor(t, "%"+palavra+"%", manifestacao.getIdFaixaEtaria().getNmFaixaEtaria()); break;
                    case SEXO: t = substituirPorValor(t, "%"+palavra+"%", EnumHelper.getSexoEnum(manifestacao.getTpSexo()).getDescricao()); break;
                    case RACA: t = substituirPorValor(t, "%"+palavra+"%", EnumHelper.getTipoRacaEnum(manifestacao.getTpRaca()).getDescricao()); break;
                    case URL_ACESSO_MANIFESTACAO: {
                    	String params = String.valueOf(manifestacao.getNrManifestacao());
                    	if(!isEmailInterno) {
                    		params = params.concat("&id=").concat(manifestacao.getIdManifestacao().toString()); 
                    	}
                    	
                    	t = substituirPorValor(t, "%"+palavra+"%", 
            				String.format("<a href='%s/pages/manifestacao/administrar.xhtml?num=%s'>Acesse sua manifestação</a>", 
        					dominioBean.getUrlBase(), params));
                    	break;
                    }
                }
            } catch(NullPointerException e) {
                t = substituirPorValor(t, "%"+palavra+"%", "...");
            }
        }

        return t;
    }
    
    @SuppressWarnings("incomplete-switch")
	public static String converterPalavrasChaves(String texto, TbUsuario usuario) {
        if(!ValidacaoHelper.isNotEmpty(texto) || usuario == null) {
            return "";
        }
        String t = texto;
        
        for (PalavrasChavesEnum palavra : PalavrasChavesEnum.values()) {
            try {
                switch(palavra) {
                    case NOME_USUARIO: t = substituirPorValor(t, "%"+palavra+"%", usuario.getNmUsuario()); break;
                    case EMAIL_USUARIO: t = substituirPorValor(t, "%"+palavra+"%", usuario.getEeEmail()); break;
                    case SENHA_USUARIO: t = substituirPorValor(t, "%"+palavra+"%", usuario.getNmSenha()); break;
                    case LOGIN_USUARIO: t = substituirPorValor(t, "%"+palavra+"%", usuario.getNmLogin()); break;
                    case TELEFONE_USUARIO: t = substituirPorValor(t, "%"+palavra+"%", usuario.getNumTelefone()); break;
                }
            } catch(NullPointerException e) {
                t = substituirPorValor(t, "%"+palavra+"%", "...");
            }
        }
        
        return t;
    }
    
    private static String substituirPorValor(String textoOriginal, String regex, Object replacement) {
        return textoOriginal.replaceAll(regex, Matcher.quoteReplacement(replacement.toString()));
    }
    
    private static String getClassificacoes(Collection<TbClassificacao> list) {
    	StringBuilder classificacoes = new StringBuilder();
    	for (TbClassificacao c : list)
			classificacoes.append(c.getDsClassificacao()).append(',');
    	
    	if(classificacoes.length() > 0)
        	classificacoes.deleteCharAt(classificacoes.length() - 1);
    	
    	return classificacoes.toString();
    }
    
    private static String getSubClassificacoes(Collection<TbSubClassificacao> list) {
    	StringBuilder subClassificacoes = new StringBuilder();
    	for (TbSubClassificacao c : list)
    		subClassificacoes.append(c.getDsSubClassificacao()).append(',');
    	
    	if(subClassificacoes.length() > 0)
    		subClassificacoes.deleteCharAt(subClassificacoes.length() - 1);
    	
    	return subClassificacoes.toString();
    }
    
    private static String getAreaSolucionadora(Collection<TbUnidade> list) {
    	StringBuilder areas = new StringBuilder();
    	for (TbUnidade u : list)
    		areas.append(u.getNmUnidade()).append(',');
    	
    	if(areas.length() > 0)
    		areas.deleteCharAt(areas.length() - 1);
    	
    	return areas.toString();
    }

}
