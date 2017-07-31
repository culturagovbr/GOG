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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/** *
 * @author samuel.guimaraes
 */
@Entity
@Table(name = "tbComunicacaoExternaxAnexo")
@NamedQueries({
    @NamedQuery(name = "TbComunicacaoExternaxAnexo.findAll", query = "SELECT t FROM TbComunicacaoExternaxAnexo t"),
    @NamedQuery(name = "TbComunicacaoExternaxAnexo.findByIdComunicacaoExternaxAnexo", query = "SELECT t FROM TbComunicacaoExternaxAnexo t WHERE t.idComunicacaoExternaxAnexo = :idComunicacaoExternaxAnexo")})
public class TbComunicacaoExternaxAnexo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idComunicacaoExternaxAnexo")
    private Integer idComunicacaoExternaxAnexo;
    @JoinColumn(name = "idComunicacaoExterna", referencedColumnName = "idComunicacaoExterna")
    @ManyToOne(optional = false)
    private TbComunicacaoExterna idComunicacaoExterna;
    @JoinColumn(name = "idAnexo", referencedColumnName = "idAnexo")
    @ManyToOne(optional = false)
    private TbAnexo idAnexo;

    public TbComunicacaoExternaxAnexo() {
    }

    public TbComunicacaoExternaxAnexo(Integer idComunicacaoExternaxAnexo) {
        this.idComunicacaoExternaxAnexo = idComunicacaoExternaxAnexo;
    }

    public Integer getIdComunicacaoExternaxAnexo() {
        return idComunicacaoExternaxAnexo;
    }

    public void setIdComunicacaoExternaxAnexo(Integer idComunicacaoExternaxAnexo) {
        this.idComunicacaoExternaxAnexo = idComunicacaoExternaxAnexo;
    }

    public TbComunicacaoExterna getIdComunicacaoExterna() {
        return idComunicacaoExterna;
    }

    public void setIdComunicacaoExterna(TbComunicacaoExterna idComunicacaoExterna) {
        this.idComunicacaoExterna = idComunicacaoExterna;
    }

    public TbAnexo getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(TbAnexo idAnexo) {
        this.idAnexo = idAnexo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComunicacaoExternaxAnexo != null ? idComunicacaoExternaxAnexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TbComunicacaoExternaxAnexo)) {
            return false;
        }
        TbComunicacaoExternaxAnexo other = (TbComunicacaoExternaxAnexo) object;
        if ((this.idComunicacaoExternaxAnexo == null && other.idComunicacaoExternaxAnexo != null) || (this.idComunicacaoExternaxAnexo != null && !this.idComunicacaoExternaxAnexo.equals(other.idComunicacaoExternaxAnexo))) {
            return false;
        }
        return true;
    }

     @Override
    public String toString() {
        return String.format("%s > %s",getEntidade(),getDescricao());
    }
    
    private String getEntidade(){
        return "Comunicação Externa x Anexo";
    }
    private String getDescricao(){
        return "Comunicação Externa: " + getIdComunicacaoExterna().getDsComunicacao() +"\tAnexo: "+ idAnexo.getNmAnexo();
    }  
    
}
