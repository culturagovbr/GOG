package br.com.xti.ouvidoria.helper;

import java.util.Date;

import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.model.enums.RacaEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;
import br.com.xti.ouvidoria.model.enums.TipoManifestanteEnum;

/**
 * @author Samuel Correia Guimarães
 */
public class ConversorHelper {
	
    public static FiltroPersonalizado converterTextoEmFiltroManifestacao(String texto) {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("or");
        
        // Tenta converter o texto em Data
        Date data;
        if((data = DataHelper.converterData(texto)) != null) {
            filtro.setManDataCadastroDe(data);
            filtro.setManDataCadastroAte(data);
            
            filtro.setManDataAtualizacaoDe(data);
            filtro.setManDataAtualizacaoAte(data);
            
            filtro.setManDataFechamentoDe(data);
            filtro.setManDataFechamentoAte(data);
        } else {
            // Número da Manifestação
            try {
                filtro.setManIdRangeDe(Integer.parseInt(texto));
                filtro.setManIdRangeAte(Integer.parseInt(texto));
            } catch (NumberFormatException e) {}
            
            filtro.setManTipo(texto);
            filtro.setManClassificacao(texto);
            filtro.setManSubClassificacao(texto);
            filtro.setManPrioridade(texto);
            filtro.setManAreaSolucionadora(texto);
            filtro.setManManifestante(texto);
            filtro.setManTelefone(texto);
            filtro.setManEmail(texto);
            filtro.setManDesc(texto);
            filtro.setManEndereco(texto);
            filtro.setManPais(texto);
            filtro.setManCidade(texto);
            filtro.setManNumProcesso(texto);
            filtro.setManNumPronac(texto);
            
            // Status
            for (StatusManifestacaoEnum status : StatusManifestacaoEnum.values()) {
                if(status.getDescricao().toUpperCase().contains(texto.toUpperCase())) {
                    filtro.addManIdStatus(status.getId().toString());
                }
            }
            // Raça
            for (RacaEnum raca : RacaEnum.values()) {
                if(raca.getDescricao().toUpperCase().contains(texto.toUpperCase())) {
                    filtro.addManIdRaca(raca.getId());
                }
            }
            
            // Encaminhamento
            filtro.setEncDesc(texto);
            filtro.setEncUnidade(texto);

            // Status
            for (StatusEncaminhamentoEnum status : StatusEncaminhamentoEnum.values()) {
            	if(status.getDescricao().toUpperCase().contains(texto.toUpperCase())) {
            		filtro.setEncStatus(status.getId());
            	}
            }
            
            // Tipo Manifestante
            for (TipoManifestanteEnum tipoManifestante : TipoManifestanteEnum.values()) {
                if(tipoManifestante.getDescricao().toUpperCase().contains(texto.toUpperCase())) {
                    filtro.addManIdTipoManifestante(tipoManifestante.getId());
                }
            }
        }
        
        return filtro;
    }
    
    public static FiltroPersonalizado converterTextoEmFiltroEncaminhamento(String texto) {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("or");
        
        // Tenta converter o texto em Data
        Date data;
        if((data = DataHelper.converterData(texto)) != null) {
            filtro.setEncDataEnvioDe(data);
            filtro.setEncDataEnvioAte(data);
            
            filtro.setEncDataRespostaDe(data);
            filtro.setEncDataRespostaAte(data);
        }
        filtro.setEncDesc(texto);
        filtro.setEncUnidade(texto);

        // Status
        for (StatusEncaminhamentoEnum status : StatusEncaminhamentoEnum.values()) {
            if(status.getDescricao().toUpperCase().contains(texto.toUpperCase())) {
                filtro.setEncStatus(status.getId());
            }
        }
        
        return filtro;
    }
    
}
