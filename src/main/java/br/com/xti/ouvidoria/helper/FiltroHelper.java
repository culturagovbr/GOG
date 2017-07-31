/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.helper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.model.TbUsuario;
import br.com.xti.ouvidoria.model.enums.BooleanEnum;
import br.com.xti.ouvidoria.model.enums.FuncaoUsuarioEnum;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;

/**
 *
 * @author samuel.guimaraes
 */
public class FiltroHelper {

    public static FiltroPersonalizado getFiltrosPadrao(TbUsuario usuario) {
        FiltroPersonalizado filtro = new FiltroPersonalizado();
        filtro.setMetodoBusca("and");
        
        if(usuario != null) {
        	// Recupera a Função do usuário
        	FuncaoUsuarioEnum funcao = EnumHelper.getFuncaoUsuarioEnum(usuario.getTpFuncao());
        	
        	switch (funcao) {
        	case OPERADOR:
        		filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        		filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        		filtro.addEncIdUnidadeRecebeu(usuario.getIdUnidade().getIdUnidade());
        		filtro.addEncIdOperador(usuario.getIdUsuario());
        		filtro.setEncTramiteNaoRetornado(true);
        		break;
        		
        	case INTERLOCUTOR:
        		filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANDAMENTO.getId());
        		// Comentamos essa linha para o Intelocutor consiga visualizar manifestações com situação "Retornada"
        		//filtro.setEncStatus(StatusEncaminhamentoEnum.ENCAMINHADA.getId());
        		filtro.addEncIdUnidadeRecebeu(usuario.getIdUnidade().getIdUnidade());
        		break;
        		
        	case MANIFESTANTE:
        		filtro.setManIdManifestante(usuario.getIdUsuario());
        		break;
        		
        	case ADMINISTRADOR:
        	case OUVIDOR:
        		filtro.addManIdStatus(StatusManifestacaoEnum.NOVA.getId());
        		filtro.addManIdStatus(StatusManifestacaoEnum.EM_ANALISE.getId());
        		filtro.addManIdStatus(StatusManifestacaoEnum.SOLICITACAO_RESPONDIDA.getId());
        		break;
        	}
        	
        	if(funcao != FuncaoUsuarioEnum.ADMINISTRADOR){
        		filtro.setManOculto(BooleanEnum.NAO.getId());
        	}
        }
        
        return filtro;
    }

    public static FiltroPersonalizado getFiltroCaixaEntrada(TbUsuario usuario) {
        FiltroPersonalizado filtro = getFiltrosPadrao(usuario);
        return filtro;
    }

    public static <ENTIDADE> List<Integer> getIdsFromEntityList(List<ENTIDADE> list) {
        // Lista de Ids que será retornada
        List<Integer> listIds = new ArrayList<>();
        if (list == null) {
            return listIds;
        }

        try {
            for (ENTIDADE e : list) {
                listIds.add(ReflectionHelper.getValorID(e));
            }
        } catch (Exception ex) {
            Logger.getLogger(ListHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listIds;
    }

    public static <ENUM> List<String> getIdsFromEnumList(List<ENUM> list) {
        // Lista de Ids que será retornada
        List<String> listIds = new ArrayList<>();
        if (list == null) {
            return listIds;
        }

        try {
            for (ENUM e : list) {
                Method m = e.getClass().getMethod("getId");
                Object o = m.invoke(e);
                listIds.add(String.valueOf(o));
            }
        } catch (Exception ex) {
            Logger.getLogger(ListHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listIds;
    }
}
