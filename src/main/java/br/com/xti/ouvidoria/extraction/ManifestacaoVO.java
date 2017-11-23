package br.com.xti.ouvidoria.extraction;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.jsoup.Jsoup;

import br.com.xti.ouvidoria.helper.EnumHelper;
import br.com.xti.ouvidoria.model.TbClassificacao;
import br.com.xti.ouvidoria.model.TbEncaminhamento;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbSubClassificacao;
import br.com.xti.ouvidoria.model.TbUnidade;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.SexoEnum;

/**
 * @author felipe.fuchs
 */
public class ManifestacaoVO {

    private TbManifestacao manifestacao;

    public ManifestacaoVO(TbManifestacao manifestacao) {
        this.manifestacao = manifestacao;
    }

    public String getNrManifestacao() {
        return manifestacao.getNrManifestacao() + "";
    }

    public String getTipoManifestacao() {
        return manifestacao.getIdTipoManifestacao().getNmTipoManifestacao();
    }

    public String getDataCadastro() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(manifestacao.getDtCadastro());
    }

    public String getDataConclusao() {
        if (manifestacao.getDtFechamento() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(manifestacao.getDtFechamento());
        }
        return "";
    }

    public String getNomePessoa() {
        return manifestacao.getNmPessoa();
    }

    public String getNrTelefone() {
        return manifestacao.getNrTelefone();
    }

    public String getEmail() {
        return manifestacao.getEeEmailUsuario();
    }

    public String getPais() {
        return manifestacao.getIdPais().getNmPais();
    }

    public String getCidade() {
        return manifestacao.getDsLocalidade();
    }

    public String getEstado() {
        return manifestacao.getIdUF().getNmUF();
    }

    public String getFaixaEtaria() {
        return manifestacao.getIdFaixaEtaria().getNmFaixaEtaria();
    }

    public String getSexo() {
        if (manifestacao.getTpSexo().equals(SexoEnum.MASCULINO.getId().toString())) {
            return SexoEnum.MASCULINO.getDescricao();
        } else if (manifestacao.getTpSexo().equals(SexoEnum.FEMININO.getId().toString())) {
            return SexoEnum.FEMININO.getDescricao();
        }
        return "";
    }

    public String getReceberResposta() {
        if (manifestacao.getStResposta() == null ? BooleanEnum.SIM.getId() == null : manifestacao.getStResposta().equals(BooleanEnum.SIM.getId())) {
            return BooleanEnum.SIM.getDescricao();
        } else if (manifestacao.getStResposta() == null ? BooleanEnum.NAO.getId() == null : manifestacao.getStResposta().equals(BooleanEnum.NAO.getId())) {
            return BooleanEnum.NAO.getDescricao();
        }
        return "";
    }

    public String getMeioResposta() {
        return manifestacao.getIdMeioResposta().getNmMeioResposta();
    }

    public String getAreaEntrada() {
        return manifestacao.getIdAreaEntrada().getNmAreaEntrada();
    }

    public String getMeioEntrada() {
        return manifestacao.getIdMeioEntrada().getNmMeioEntrada();
    }

    public String getRaca() {
    	return EnumHelper.getTipoRacaEnum(manifestacao.getTpRaca()).getDescricao();
    }

    public String getSigilo() {
        if (manifestacao.getSiSigilo() != null) {
            if (manifestacao.getSiSigilo() == null ? BooleanEnum.SIM.getId() == null : manifestacao.getSiSigilo().equals(BooleanEnum.SIM.getId())) {
                return BooleanEnum.SIM.getDescricao();
            } else if (manifestacao.getSiSigilo() == null ? BooleanEnum.NAO.getId() == null : manifestacao.getSiSigilo().equals(BooleanEnum.NAO.getId())) {
                return BooleanEnum.NAO.getDescricao();
            }
        }
        return "";
    }

    public String getStatusManifestacao() {
    	return EnumHelper.getStatusManifestacaoEnum(manifestacao.getStStatusManifestacao()).getDescricao();
    }

    public String getAreaSolucionadora() {
    	StringBuilder areas = new StringBuilder();
        Collection<TbUnidade> listaEncaminhamento = manifestacao.getTbUnidadeAreaSolucionadoraCollection();
        
        for (TbUnidade unidade : listaEncaminhamento)
        	areas.append(unidade.getNmUnidade()).append(",");
        
        if(areas.length() > 0)
        	areas.deleteCharAt(areas.length() - 1);
        
        return areas.toString();
    }

    public String getClassificacao() {
    	StringBuilder classificacoes = new StringBuilder();
        Collection<TbClassificacao> listaClassificacoes = manifestacao.getTbClassificacaoCollection();
        
        for (TbClassificacao c : listaClassificacoes)
        	classificacoes.append(c.getDsClassificacao()).append(",");
        
        if(classificacoes.length() > 0)
        	classificacoes.deleteCharAt(classificacoes.length() - 1);
        
        return classificacoes.toString();
    }

    public String getSubClassificacao() {
    	StringBuilder subClassificacoes = new StringBuilder();
    	Collection<TbSubClassificacao> listaSubClassificacoes = manifestacao.getTbSubClassificacaoCollection();
    	
    	for (TbSubClassificacao sub : listaSubClassificacoes)
    		subClassificacoes.append(sub.getDsSubClassificacao()).append(",");
    	
    	if(subClassificacoes.length() > 0)
    		subClassificacoes.deleteCharAt(subClassificacoes.length() - 1);
    	
    	return subClassificacoes.toString();
    }

    public String getPrioridade() {
        return manifestacao.getIdPrioridade().getNmPrioridade();
    }

    public String getAreaEnviada() {
    	StringBuilder areas = new StringBuilder();
        Collection<TbEncaminhamento> listaEncaminhamento = manifestacao.getTbEncaminhamentoCollection();
        
    	for (TbEncaminhamento enc : listaEncaminhamento)
        	areas.append(enc.getIdUnidadeRecebeu().getNmUnidade()).append(",");
        
        if(areas.length() > 0)
        	areas.deleteCharAt(areas.length() - 1);
        
        return areas.toString();
    }

    public String getTexto() {
    	return Jsoup.parse(manifestacao.getDsTextoManifestacao()).text();
    }
}
