/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.xti.ouvidoria.helper.ValidacaoHelper;
/**
 *
 * @author Emanuel Melo
 */
@Entity
@Table(name = "tbRespostaQuestionario")
public class TbRespostaQuestionario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idResposta")
    private Integer idResposta;
    
    @ManyToOne
    @JoinColumn(name="idManifestacao", referencedColumnName = "idManifestacao")
    private TbManifestacao manifestacao;
    
    @ManyToOne
    @JoinColumn(name="idPergunta", referencedColumnName = "idPergunta")
    private TbPerguntaQuestionario pergunta;
    
    @Basic(optional = false)
    @Column(name = "dsResposta")
    private Integer dsResposta;
    
	//Construtores
    public TbRespostaQuestionario() {
    }

    //GETTERS e SETTERS
    public Integer getIdResposta() {
		return idResposta;
	}

	public void setIdResposta(Integer idResposta) {
		this.idResposta = idResposta;
	}

	public TbManifestacao getManifestacao() {
		return manifestacao;
	}

	public void setManifestacao(TbManifestacao manifestacao) {
		this.manifestacao = manifestacao;
	}

	public TbPerguntaQuestionario getPergunta() {
		return pergunta;
	}

	public void setPergunta(TbPerguntaQuestionario pergunta) {
		this.pergunta = pergunta;
	}

	public Integer getDsResposta() {
		return dsResposta;
	}

	public void setDsResposta(Integer dsResposta) {
		this.dsResposta = dsResposta;
	}
	
	
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idResposta != null ? idResposta.hashCode() : 0);
        return hash;
    }
	
	@Override
    public boolean equals(Object object) {
        if (!(object instanceof TbRespostaQuestionario)) {
            return false;
        }
        TbRespostaQuestionario other = (TbRespostaQuestionario) object;
        if ((this.idResposta == null && other.idResposta != null) || (this.idResposta != null && !this.idResposta.equals(other.idResposta))) {
            return false;
        }
        return true;
    }
    
	@Override
	public String toString() {
		return String.format("%s > %s", getEntidade(), getDescricao());
	}

	private String getEntidade() {
		return "Resposta Questionário";
	}

	private String getDescricao() {
		try {
			return String.format("[man=%s , pergunta=%s , resposta=%s]", manifestacao.getIdManifestacao(), pergunta.getIdPergunta(), dsResposta);
		} catch (Exception e) {
			return String.format("[idResposta=%s", ValidacaoHelper.isNotEmpty(idResposta) ? idResposta : 0);
		}
	}
    
}
