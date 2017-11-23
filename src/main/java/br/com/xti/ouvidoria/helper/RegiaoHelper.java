/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.helper;

import br.com.xti.ouvidoria.model.enums.RegiaoEnum;

/**
 *
 * @author samuel.guimaraes
 */
public class RegiaoHelper {

    public static RegiaoEnum getRegiaoPorIdUf(int idUf) {
    	
    	switch (idUf) {
			case 1: case 3: case 4: case 14: case 21: case 22: case 27:
				return RegiaoEnum.NORTE;
			case 2: case 5: case 6: case 10: case 15: case 17: case 18: case 20: case 25:
				return RegiaoEnum.NORDESTE;
			case 7: case 9: case 12: case 13:
				return RegiaoEnum.CENTRO_OESTE;
			case 8: case 11: case 19: case 26:
				return RegiaoEnum.SUDESTE;
			case 16: case 23: case 24:
				return RegiaoEnum.SUL;
			default:
				return RegiaoEnum.NAO_ESPECIFICADO;
		}
    	
    }
    
}
