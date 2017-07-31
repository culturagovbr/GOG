/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author marcos.ribeiro
 */
public enum RegiaoEnum {
    
    NORTE(1,"Norte",1,3,4,14,21,22,27),
    NORDESTE(2,"Nordeste",2,5,6,10,15,17,18,20,25),
    CENTRO_OESTE(3,"Centro Oeste",7,9,12,13),
    SUDESTE(4,"Sudeste",8,11,19,26),
    SUL(5,"Sul",16,23,24),
    NAO_ESPECIFICADO(6,"Não Especificado");
    
    private Integer id;
    private String nome;
    private List<Integer> idUFs;
    
    private RegiaoEnum(Integer id, String nome, Integer... idUFs) {
        this.id = id;
        this.nome = nome;
        this.idUFs = Arrays.asList(idUFs);
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public List<Integer> getIdUFs() {
        return idUFs;
    }
    
}
