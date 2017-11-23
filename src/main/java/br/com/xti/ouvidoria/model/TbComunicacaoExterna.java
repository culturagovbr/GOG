package br.com.xti.ouvidoria.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.xti.ouvidoria.model.enums.BooleanEnum;

/**
 * @author Samuel Correia Guimarães
 */
@Entity
@Table(name = "tbComunicacaoExterna")
@NamedQueries({
    @NamedQuery(name = "TbComunicacaoExterna.findAll", query = "SELECT t FROM TbComunicacaoExterna t"),
    @NamedQuery(name = "TbComunicacaoExterna.findByIdComunicacaoExterna", query = "SELECT t FROM TbComunicacaoExterna t WHERE t.idComunicacaoExterna = :idComunicacaoExterna"),
    @NamedQuery(name = "TbComunicacaoExterna.findByDtComunicacao", query = "SELECT t FROM TbComunicacaoExterna t WHERE t.dtComunicacao = :dtComunicacao")})
public class TbComunicacaoExterna implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idComunicacaoExterna")
    private Integer idComunicacaoExterna;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtComunicacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtComunicacao;
    
    @Column(name = "dsComunicacao")
    private String dsComunicacao;
    
    @OneToMany(mappedBy = "idComunicacaoExterna", cascade=CascadeType.MERGE)
    private Collection<TbComunicacaoExternaxAnexo> tbComunicacaoExternaxAnexoCollection = new ArrayList<>();
   
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = true)
    private TbUsuario idUsuario;
    
    @JoinColumn(name = "idManifestacao", referencedColumnName = "idManifestacao")
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    private TbManifestacao idManifestacao;
    
    @Size(max = 1)
    @Column(name = "stRespostaFinal")
    private String stRespostaFinal;
    
    @Size(max = 1)
    @Column(name = "stComunicacaoPublica")
    private String stComunicacaoPublica;

    public TbComunicacaoExterna() {
    }

    public TbComunicacaoExterna(Integer idComunicacaoExterna) {
        this.idComunicacaoExterna = idComunicacaoExterna;
    }

    public TbComunicacaoExterna(Integer idComunicacaoExterna, Date dtComunicacao) {
        this.idComunicacaoExterna = idComunicacaoExterna;
        this.dtComunicacao = dtComunicacao;
    }

    public Integer getIdComunicacaoExterna() {
        return idComunicacaoExterna;
    }

    public void setIdComunicacaoExterna(Integer idComunicacaoExterna) {
        this.idComunicacaoExterna = idComunicacaoExterna;
    }

    public Date getDtComunicacao() {
        return dtComunicacao;
    }

    public void setDtComunicacao(Date dtComunicacao) {
        this.dtComunicacao = dtComunicacao;
    }

    public String getDsComunicacao() {
        return dsComunicacao;
    }

    public void setDsComunicacao(String dsComunicacao) {
        this.dsComunicacao = dsComunicacao;
    }

    public Collection<TbComunicacaoExternaxAnexo> getTbComunicacaoExternaxAnexoCollection() {
        return tbComunicacaoExternaxAnexoCollection;
    }

    public void setTbComunicacaoExternaxAnexoCollection(Collection<TbComunicacaoExternaxAnexo> tbComunicacaoExternaxAnexoCollection) {
        this.tbComunicacaoExternaxAnexoCollection = tbComunicacaoExternaxAnexoCollection;
    }

    public TbUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(TbUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TbManifestacao getIdManifestacao() {
        return idManifestacao;
    }

    public void setIdManifestacao(TbManifestacao idManifestacao) {
        this.idManifestacao = idManifestacao;
    }
    
    public String getStRespostaFinal() {
        return stRespostaFinal;
    }

    public void setStRespostaFinal(String stRespostaFinal) {
        this.stRespostaFinal = stRespostaFinal;
    }

    private String getEntidade() {
		return "Comunicação Externa";
	}

	private String getDescricao() {
		return dsComunicacao;
	}

	public String getStComunicacaoPublica() {
		return stComunicacaoPublica;
	}

	public void setStComunicacaoPublica(String stComunicacaoPublica) {
		this.stComunicacaoPublica = stComunicacaoPublica;
	}
	
	public boolean isComunicacaoPublica() {
		return BooleanEnum.SIM.getId().equals(stComunicacaoPublica);
	}

	@Override
    public int hashCode() {
    	int hash = 0;
    	hash += (idComunicacaoExterna != null ? idComunicacaoExterna.hashCode() : 0);
    	return hash;
    }
    
    @Override
    public boolean equals(Object object) {
    	if (!(object instanceof TbComunicacaoExterna)) {
    		return false;
    	}
    	TbComunicacaoExterna other = (TbComunicacaoExterna) object;
    	if ((this.idComunicacaoExterna == null && other.idComunicacaoExterna != null) || (this.idComunicacaoExterna != null && !this.idComunicacaoExterna.equals(other.idComunicacaoExterna))) {
    		return false;
    	}
    	return true;
    }
    
    @Override
    public String toString() {
    	return String.format("%s > %s", getEntidade(), getDescricao());
    }
    
}
