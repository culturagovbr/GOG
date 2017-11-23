package br.com.xti.ouvidoria.charts.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.xti.ouvidoria.dao.AbstractDAO;
import br.com.xti.ouvidoria.dao.ManifestacaoDAO;
import br.com.xti.ouvidoria.filtropersonalizado.FiltroPersonalizado;
import br.com.xti.ouvidoria.helper.DataHelper;
import br.com.xti.ouvidoria.model.TbManifestacao;
import br.com.xti.ouvidoria.model.TbRespostaQuestionario;
import br.com.xti.ouvidoria.model.enums.StatusEncaminhamentoEnum;
import br.com.xti.ouvidoria.model.enums.StatusManifestacaoEnum;


@Stateless
public class ChartDao extends AbstractDAO<TbManifestacao> {
	
	@EJB
	private ManifestacaoDAO dao;

	public ChartDao() {
		super(TbManifestacao.class);
	}

	@Override
	public String getNomeEntidade() {
		return "CHART";
	}
	
	public List<TbManifestacao> getManifestacoesEntreDatas(Date dataDe, Date dataAte) {
		FiltroPersonalizado filtroPersonalizado = getFiltroPersonalizadoEntreDatas(dataDe, dataAte);
		return dao.getManifestacoes(filtroPersonalizado);
	}
	
	public List<TbManifestacao> getManifestacoesEntreDatas(Date dataDe,
			Date dataAte, int idUnidadeEnviouEncaminhamento,
			List<Integer> idUnidadesRecebeuEncaminhamento) {
		FiltroPersonalizado filtroPersonalizado = getFiltroPersonalizadoEntreDatas(dataDe, dataAte);
		filtroPersonalizado.addEncIdUnidadeEnviou(idUnidadeEnviouEncaminhamento);
		filtroPersonalizado.setEncIdUnidadeRecebeu(idUnidadesRecebeuEncaminhamento);
		filtroPersonalizado.setEncStatus(StatusEncaminhamentoEnum.RETORNADA.getId());
		return dao.getManifestacoes(filtroPersonalizado);
	}
	
	public List<TbManifestacao> getManifestacoesEntreDatasPorClassificacao(
			Date dataDe, Date dataAte, int idClassificacao) {
		FiltroPersonalizado filtroPersonalizado = getFiltroPersonalizadoEntreDatas(dataDe, dataAte);
		filtroPersonalizado.addManIdClassificacao(idClassificacao);
		return dao.getManifestacoes(filtroPersonalizado);
	}
	
	public List<TbManifestacao> getManifestacoesSolucionadasEntreDatas(Date dataDe, Date dataAte) {
		FiltroPersonalizado filtroPersonalizado = new FiltroPersonalizado();
		filtroPersonalizado.setManDataFechamentoDe(DataHelper.getDataMin(dataDe));
		filtroPersonalizado.setManDataFechamentoAte(DataHelper.getDataMax(dataAte));
		filtroPersonalizado.addManIdStatus(StatusManifestacaoEnum.SOLUCIONADA.getId());
		return dao.getManifestacoes(filtroPersonalizado);
	}
	
	public List<TbRespostaQuestionario> getRespsotasQuestionario(int idQuestionario) {
		List<TbRespostaQuestionario> res = new ArrayList<TbRespostaQuestionario>();
		
		try {
			String select = "SELECT r FROM TbRespostaQuestionario r WHERE r.manifestacao.idQuestionario.idQuestionario = :idQuestionario";
			TypedQuery<TbRespostaQuestionario> query = getEntityManager().createQuery(select, TbRespostaQuestionario.class);
			query.setParameter("idQuestionario", idQuestionario);
			
			res.addAll(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private FiltroPersonalizado getFiltroPersonalizadoEntreDatas(Date dataDe, Date dataAte) {
		FiltroPersonalizado filtroPersonalizado = new FiltroPersonalizado();
		filtroPersonalizado.setManDataCadastroDe(DataHelper.getDataMin(dataDe));
		filtroPersonalizado.setManDataCadastroAte(DataHelper.getDataMax(dataAte));
		return filtroPersonalizado;
	}

}
