package br.com.xti.ouvidoria.extraction;

import java.util.ArrayList;
import java.util.List;

import br.com.xti.ouvidoria.model.TbManifestacao;

/**
 * @author felipe.fuchs
 */
public class ExtracaoHelper {

    public static List<ManifestacaoVO> convert(List<TbManifestacao> lista) {
        ArrayList<ManifestacaoVO> listaRes = new ArrayList<ManifestacaoVO>();
        for (TbManifestacao tbManifestacao : lista) {
            listaRes.add(new ManifestacaoVO(tbManifestacao));
        }
        return listaRes;
    }
}
