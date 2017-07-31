/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author felipe.fuchs
 */
@Entity

@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwEstatisticasManifestacao.findAll", query = "SELECT v FROM VwEstatisticasManifestacao v"),
    @NamedQuery(name = "VwEstatisticasManifestacao.findByCodMetrica", query = "SELECT v FROM VwEstatisticasManifestacao v WHERE v.codMetrica = :codMetrica"),
    @NamedQuery(name = "VwEstatisticasManifestacao.findByDsMetrica", query = "SELECT v FROM VwEstatisticasManifestacao v WHERE v.dsMetrica = :dsMetrica"),
    @NamedQuery(name = "VwEstatisticasManifestacao.findByQtdade", query = "SELECT v FROM VwEstatisticasManifestacao v WHERE v.qtdade = :qtdade")})
public class VwEstatisticasManifestacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Id
    private int codMetrica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 31)
    @Column(nullable = false, length = 31)
    private String dsMetrica;
    private Integer qtdade;

    public VwEstatisticasManifestacao() {
    }

    public int getCodMetrica() {
        return codMetrica;
    }

    public void setCodMetrica(int codMetrica) {
        this.codMetrica = codMetrica;
    }

    public String getDsMetrica() {
        return dsMetrica;
    }

    public void setDsMetrica(String dsMetrica) {
        this.dsMetrica = dsMetrica;
    }

    public Integer getQtdade() {
        return qtdade;
    }

    public void setQtdade(Integer qtdade) {
        this.qtdade = qtdade;
    }
    
}
