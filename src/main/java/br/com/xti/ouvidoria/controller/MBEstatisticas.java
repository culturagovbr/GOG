package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.PieChartModel;

import br.com.xti.ouvidoria.dao.VwEstatisticasManifestacaoDAO;
import br.com.xti.ouvidoria.model.VwEstatisticasManifestacao;

@SuppressWarnings("serial")
@ManagedBean(name = "mBEstatisticas")
@RequestScoped
public class MBEstatisticas implements Serializable {

    @EJB
    private VwEstatisticasManifestacaoDAO estatisticaDAO;
    // Descrição das Metricas
    private String dsStatistica01;
    private String dsStatistica02;
    private String dsStatistica03;
    private String dsStatistica04;
    private String dsStatistica05;
    private String dsStatistica06;
    private String dsStatistica07;
    private String dsStatistica08;
    private String dsStatistica09;
    private String dsStatistica10;
    private String dsStatistica11;
    //Quantidade
    private Integer nrStatistica01;
    private Integer nrStatistica02;
    private Integer nrStatistica03;
    private Integer nrStatistica04;
    private Integer nrStatistica05;
    private Integer nrStatistica06;
    private Integer nrStatistica07;
    private Integer nrStatistica08;
    private Integer nrStatistica09;
    private Integer nrStatistica10;
    private Integer nrStatistica11;
    private List<VwEstatisticasManifestacao> estatisticas;
    private HashMap<Integer, VwEstatisticasManifestacao> mapEstatisticas = new HashMap<>();
    private PieChartModel graficoComum = new PieChartModel();
    private PieChartModel graficoStatusManifestacao = new PieChartModel();
    private PieChartModel graficoStatusTramite = new PieChartModel();

    public PieChartModel getGraficoStatusTramite() {
        return graficoStatusTramite;
    }

    public PieChartModel getGraficoStatusManifestacao() {
        return graficoStatusManifestacao;
    }

    public PieChartModel getGraficoComum() {
        return graficoComum;
    }

    public void setGraficoComum(PieChartModel graficoComum) {
        this.graficoComum = graficoComum;
    }

    public void setGraficoStatusTramite(PieChartModel graficoStatusTramite) {
        this.graficoStatusTramite = graficoStatusTramite;
    }

    public void setGraficoStatusManifestacao(PieChartModel graficoStatusManifestacao) {
        this.graficoStatusManifestacao = graficoStatusManifestacao;
    }

    @PostConstruct
    public void init() {

        estatisticas = getEstatisticaDAO().findAll();
        if (estatisticas != null && !estatisticas.isEmpty()) {
            for (VwEstatisticasManifestacao vwEstatisticasManifestacao : estatisticas) {
               mapEstatisticas.put(vwEstatisticasManifestacao.getCodMetrica(), vwEstatisticasManifestacao);
            }


            graficoComum.set(getEstatistica(1).getDsMetrica(), getEstatistica(1).getQtdade());
            graficoComum.set(getEstatistica(2).getDsMetrica(), getEstatistica(2).getQtdade());
            graficoComum.set(getEstatistica(3).getDsMetrica(), getEstatistica(3).getQtdade());
            graficoComum.set(getEstatistica(4).getDsMetrica(), getEstatistica(4).getQtdade());
           //graficoComum.set(getEstatistica(5).getDsMetrica(), getEstatistica(5).getQtdade());
            graficoStatusManifestacao.set(getEstatistica(6).getDsMetrica(), getEstatistica(6).getQtdade());
            graficoStatusManifestacao.set(getEstatistica(7).getDsMetrica(), getEstatistica(7).getQtdade());
            graficoStatusManifestacao.set(getEstatistica(8).getDsMetrica(), getEstatistica(8).getQtdade());
            graficoStatusManifestacao.set(getEstatistica(9).getDsMetrica(), getEstatistica(9).getQtdade());

            graficoStatusTramite.set(getEstatistica(10).getDsMetrica(), getEstatistica(10).getQtdade());
            graficoStatusTramite.set(getEstatistica(11).getDsMetrica(), getEstatistica(11).getQtdade());
        }
        //  Map<String, Integer> estatisticas = manifestacaoDAO.getEstatisticas();
     /*   setDsStatistica01(estatisticas.get(1).getDsMetrica());
         setDsStatistica02(estatisticas.get(2).getDsMetrica());
         setDsStatistica03(estatisticas.get(3).getDsMetrica());
         setDsStatistica04(estatisticas.get(4).getDsMetrica());
         setDsStatistica05(estatisticas.get(5).getDsMetrica());
         setDsStatistica06(estatisticas.get(6).getDsMetrica());
         setDsStatistica07(estatisticas.get(7).getDsMetrica());
         setDsStatistica08(estatisticas.get(8).getDsMetrica());
         setDsStatistica09(estatisticas.get(9).getDsMetrica());
         setDsStatistica10(estatisticas.get(10).getDsMetrica());
         setDsStatistica11(estatisticas.get(11).getDsMetrica());

         setNrStatistica01(estatisticas.get(1).getQtdade());
         setNrStatistica02(estatisticas.get(2).getQtdade());
         setNrStatistica03(estatisticas.get(3).getQtdade());
         setNrStatistica04(estatisticas.get(4).getQtdade());
         setNrStatistica05(estatisticas.get(5).getQtdade());
         setNrStatistica06(estatisticas.get(6).getQtdade());
         setNrStatistica07(estatisticas.get(7).getQtdade());
         setNrStatistica08(estatisticas.get(8).getQtdade());
         setNrStatistica09(estatisticas.get(9).getQtdade());
         setNrStatistica10(estatisticas.get(10).getQtdade());
         setNrStatistica11(estatisticas.get(11).getQtdade());*/
    }

    public VwEstatisticasManifestacao getEstatistica(Integer codigo) {
        return mapEstatisticas.get(codigo);
    }

    /**
     * @return the estatisticaDAO
     */
    public VwEstatisticasManifestacaoDAO getEstatisticaDAO() {
        return estatisticaDAO;
    }

    /**
     * @param estatisticaDAO the estatisticaDAO to set
     */
    public void setEstatisticaDAO(VwEstatisticasManifestacaoDAO estatisticaDAO) {
        this.estatisticaDAO = estatisticaDAO;
    }

    /**
     * @return the dsStatistica01
     */
    public String getDsStatistica01() {
        return dsStatistica01;
    }

    /**
     * @param dsStatistica01 the dsStatistica01 to set
     */
    public void setDsStatistica01(String dsStatistica01) {
        this.dsStatistica01 = dsStatistica01;
    }

    /**
     * @return the dsStatistica02
     */
    public String getDsStatistica02() {
        return dsStatistica02;
    }

    /**
     * @param dsStatistica02 the dsStatistica02 to set
     */
    public void setDsStatistica02(String dsStatistica02) {
        this.dsStatistica02 = dsStatistica02;
    }

    /**
     * @return the dsStatistica03
     */
    public String getDsStatistica03() {
        return dsStatistica03;
    }

    /**
     * @param dsStatistica03 the dsStatistica03 to set
     */
    public void setDsStatistica03(String dsStatistica03) {
        this.dsStatistica03 = dsStatistica03;
    }

    /**
     * @return the dsStatistica04
     */
    public String getDsStatistica04() {
        return dsStatistica04;
    }

    /**
     * @param dsStatistica04 the dsStatistica04 to set
     */
    public void setDsStatistica04(String dsStatistica04) {
        this.dsStatistica04 = dsStatistica04;
    }

    /**
     * @return the dsStatistica05
     */
    public String getDsStatistica05() {
        return dsStatistica05;
    }

    /**
     * @param dsStatistica05 the dsStatistica05 to set
     */
    public void setDsStatistica05(String dsStatistica05) {
        this.dsStatistica05 = dsStatistica05;
    }

    /**
     * @return the dsStatistica06
     */
    public String getDsStatistica06() {
        return dsStatistica06;
    }

    /**
     * @param dsStatistica06 the dsStatistica06 to set
     */
    public void setDsStatistica06(String dsStatistica06) {
        this.dsStatistica06 = dsStatistica06;
    }

    /**
     * @return the dsStatistica07
     */
    public String getDsStatistica07() {
        return dsStatistica07;
    }

    /**
     * @param dsStatistica07 the dsStatistica07 to set
     */
    public void setDsStatistica07(String dsStatistica07) {
        this.dsStatistica07 = dsStatistica07;
    }

    /**
     * @return the dsStatistica08
     */
    public String getDsStatistica08() {
        return dsStatistica08;
    }

    /**
     * @param dsStatistica08 the dsStatistica08 to set
     */
    public void setDsStatistica08(String dsStatistica08) {
        this.dsStatistica08 = dsStatistica08;
    }

    /**
     * @return the dsStatistica09
     */
    public String getDsStatistica09() {
        return dsStatistica09;
    }

    /**
     * @param dsStatistica09 the dsStatistica09 to set
     */
    public void setDsStatistica09(String dsStatistica09) {
        this.dsStatistica09 = dsStatistica09;
    }

    /**
     * @return the dsStatistica10
     */
    public String getDsStatistica10() {
        return dsStatistica10;
    }

    /**
     * @param dsStatistica10 the dsStatistica10 to set
     */
    public void setDsStatistica10(String dsStatistica10) {
        this.dsStatistica10 = dsStatistica10;
    }

    /**
     * @return the dsStatistica11
     */
    public String getDsStatistica11() {
        return dsStatistica11;
    }

    /**
     * @param dsStatistica11 the dsStatistica11 to set
     */
    public void setDsStatistica11(String dsStatistica11) {
        this.dsStatistica11 = dsStatistica11;
    }

    /**
     * @return the nrStatistica01
     */
    public Integer getNrStatistica01() {
        return nrStatistica01;
    }

    /**
     * @param nrStatistica01 the nrStatistica01 to set
     */
    public void setNrStatistica01(Integer nrStatistica01) {
        this.nrStatistica01 = nrStatistica01;
    }

    /**
     * @return the nrStatistica02
     */
    public Integer getNrStatistica02() {
        return nrStatistica02;
    }

    /**
     * @param nrStatistica02 the nrStatistica02 to set
     */
    public void setNrStatistica02(Integer nrStatistica02) {
        this.nrStatistica02 = nrStatistica02;
    }

    /**
     * @return the nrStatistica03
     */
    public Integer getNrStatistica03() {
        return nrStatistica03;
    }

    /**
     * @param nrStatistica03 the nrStatistica03 to set
     */
    public void setNrStatistica03(Integer nrStatistica03) {
        this.nrStatistica03 = nrStatistica03;
    }

    /**
     * @return the nrStatistica04
     */
    public Integer getNrStatistica04() {
        return nrStatistica04;
    }

    /**
     * @param nrStatistica04 the nrStatistica04 to set
     */
    public void setNrStatistica04(Integer nrStatistica04) {
        this.nrStatistica04 = nrStatistica04;
    }

    /**
     * @return the nrStatistica05
     */
    public Integer getNrStatistica05() {
        return nrStatistica05;
    }

    /**
     * @param nrStatistica05 the nrStatistica05 to set
     */
    public void setNrStatistica05(Integer nrStatistica05) {
        this.nrStatistica05 = nrStatistica05;
    }

    /**
     * @return the nrStatistica06
     */
    public Integer getNrStatistica06() {
        return nrStatistica06;
    }

    /**
     * @param nrStatistica06 the nrStatistica06 to set
     */
    public void setNrStatistica06(Integer nrStatistica06) {
        this.nrStatistica06 = nrStatistica06;
    }

    /**
     * @return the nrStatistica07
     */
    public Integer getNrStatistica07() {
        return nrStatistica07;
    }

    /**
     * @param nrStatistica07 the nrStatistica07 to set
     */
    public void setNrStatistica07(Integer nrStatistica07) {
        this.nrStatistica07 = nrStatistica07;
    }

    /**
     * @return the nrStatistica08
     */
    public Integer getNrStatistica08() {
        return nrStatistica08;
    }

    /**
     * @param nrStatistica08 the nrStatistica08 to set
     */
    public void setNrStatistica08(Integer nrStatistica08) {
        this.nrStatistica08 = nrStatistica08;
    }

    /**
     * @return the nrStatistica09
     */
    public Integer getNrStatistica09() {
        return nrStatistica09;
    }

    /**
     * @param nrStatistica09 the nrStatistica09 to set
     */
    public void setNrStatistica09(Integer nrStatistica09) {
        this.nrStatistica09 = nrStatistica09;
    }

    /**
     * @return the nrStatistica10
     */
    public Integer getNrStatistica10() {
        return nrStatistica10;
    }

    /**
     * @param nrStatistica10 the nrStatistica10 to set
     */
    public void setNrStatistica10(Integer nrStatistica10) {
        this.nrStatistica10 = nrStatistica10;
    }

    /**
     * @return the nrStatistica11
     */
    public Integer getNrStatistica11() {
        return nrStatistica11;
    }

    /**
     * @param nrStatistica11 the nrStatistica11 to set
     */
    public void setNrStatistica11(Integer nrStatistica11) {
        this.nrStatistica11 = nrStatistica11;
    }
}