/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import javax.ejb.Stateless;

import br.com.xti.ouvidoria.model.TbGrauInstrucao;

/**
 *
 * @author renato
 */
@Stateless
public class GrauInstrucaoDAO extends AbstractDAO<TbGrauInstrucao> {
    public GrauInstrucaoDAO() {
        super(TbGrauInstrucao.class);
    }
    
      @Override
    public String getNomeEntidade() {
        return "Grau de Instrução";
    }
}
