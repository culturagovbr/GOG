/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.filtropersonalizado;

import java.util.Comparator;

import br.com.xti.ouvidoria.model.TbFiltroPersonalizado;

/**
 *
 * @author Samuel Correia Guimarães
 */
public class FiltroPersonalizadoComparator implements Comparator<TbFiltroPersonalizado>{

    @Override
    public int compare(TbFiltroPersonalizado filtro, TbFiltroPersonalizado outroFiltro) {
        return filtro.getNmFiltroPersonalizado().compareToIgnoreCase(outroFiltro.getNmFiltroPersonalizado());
    }
    
}
