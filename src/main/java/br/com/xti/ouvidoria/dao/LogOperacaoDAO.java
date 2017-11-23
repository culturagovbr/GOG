/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.xti.ouvidoria.model.TbLogOperacao;
import br.com.xti.ouvidoria.model.enums.LogOperacaoEnum;
import br.com.xti.ouvidoria.security.SecurityService;

/**
 * @author renato
 */
@Stateless
public class LogOperacaoDAO extends AbstractDAO<TbLogOperacao> {
	
	@Inject
	private SecurityService securityService;

    public LogOperacaoDAO() {
        super(TbLogOperacao.class);
    }

    @Override
    public String getNomeEntidade() {
        return "Log Operação";
    }

    public List<TbLogOperacao> searchOperacoes(LogOperacaoEnum tipo, Integer idUsuario, Date dataInicial, Date dataFinal) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT t FROM TbLogOperacao t where "
                + "     t.idUsuario.idUsuario=:idUsuario and "
                + "     t.dtDataLog >= :dataInicial and "
                + "     t.dtDataLog <= :dataFinal  ";


        if (tipo != null) {
            sql += "and  t.tpOperacao = :tipo";
            params.put("tipo", tipo.getId());
        }


        params.put("idUsuario", idUsuario);

        if (dataFinal == null) {
            dataFinal = new Date();
        }

        params.put("dataInicial", dataInicial);
        params.put("dataFinal", dataFinal);

        return selectList(sql, params);
    }

    public void saveLog(LogOperacaoEnum tipo, String operacao) {
        TbLogOperacao op = new TbLogOperacao();
        op.setDtDataLog(new Date());
        op.setDsOperacao(operacao);
        op.setTpOperacao(tipo.getId());
        op.setIdUsuario(securityService.getUser());

        getEntityManager().persist(op);
    }
}
