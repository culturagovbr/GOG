package br.com.xti.ouvidoria.controller;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import br.com.xti.ouvidoria.dao.PaisDAO;
import br.com.xti.ouvidoria.dao.UfDAO;
import br.com.xti.ouvidoria.model.TbMunicipio;
import br.com.xti.ouvidoria.model.TbPais;
import br.com.xti.ouvidoria.model.TbUF;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
@ManagedBean(name="localidadeBean")
@ApplicationScoped
public class LocalidadeBean implements Serializable {

    private Integer idUf;
    private Integer idMunicipio;
    private TbPais brasil;
    private HashMap<Integer, TbPais> paisCache = new HashMap<>();
    private HashMap<Integer, TbUF> estadoCache = new HashMap<>();
    private HashMap<String, TbUF> ufCache = new HashMap<>();
    
    @EJB private UfDAO ufDAO;
    @EJB private PaisDAO paisDAO;

    private ArrayList<TbPais> paises = new ArrayList<>();
    
    public TbPais getBrasil() {
        return brasil;
    }
    
    @PostConstruct
    public void init() {
        try {
            List<TbPais> paises = paisDAO.findAll();
            for (TbPais tbPais : paises) {
                paisCache.put(tbPais.getIdPais(), tbPais);
                this.paises.add(tbPais);
                if(tbPais.getNmPais().contains("Brasil")){
                    brasil = tbPais;
                }
            }
            List<TbUF> ufs = ufDAO.findAll();
            for (TbUF tbUf : ufs) {
                estadoCache.put(tbUf.getIdUF(), tbUf);
                ufCache.put(tbUf.getSgUF(), tbUf);
            }

            Collections.sort(this.paises, new Comparator<TbPais>() {
                @Override
                public int compare(TbPais o1, TbPais o2) {

                    Collator collator = Collator.getInstance();
                    collator.setStrength(Collator.TERTIARY);
                    
                    return collator.compare(o1.getNmPais(), o2.getNmPais());
                }
            });
            

            this.paises.remove(brasil);
            this.paises.add(0, brasil);
        } catch (Exception ex) {
            Logger.getLogger(LocalidadeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getIdUf() {
        return idUf;
    }

    public void setIdUf(Integer idUf) {
        this.idUf = idUf;
    }

    public Collection<TbMunicipio> getMunicipios() {
        if (idUf != null) {
            TbUF uf = ufDAO.find(idUf);
            return uf.getTbMunicipioCollection();
        }
        return new ArrayList<TbMunicipio>();
    }

    public Collection<TbUF> getEstados() {
        return estadoCache.values();
    }

    public Collection<TbPais> getPaises() {
        return paises;
    }

    public TbPais getPais(Integer idPais) {
        return paisCache.get(idPais);
    }

    public TbUF getEstado(Integer idEstado) {
        return estadoCache.get(idEstado);
    }

    public TbUF getEstado(String idEstado) {
        return ufCache.get(idEstado);
    }

    public Collection<TbMunicipio> buscaMunicipios(Integer idEstado) {
        if (idEstado != null) {
            TbUF uf = estadoCache.get(idEstado);
            if (uf != null) {
                return uf.getTbMunicipioCollection();
            }
        }
        return new ArrayList<TbMunicipio>();
    }
}