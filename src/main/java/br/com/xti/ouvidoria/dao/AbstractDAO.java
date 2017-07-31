/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.dao;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import br.com.xti.ouvidoria.exception.InfrastructureException;
import br.com.xti.ouvidoria.helper.ReflectionHelper;
import br.com.xti.ouvidoria.model.TbLogOperacao;
import br.com.xti.ouvidoria.model.enums.LogOperacaoEnum;
import br.com.xti.ouvidoria.security.SecurityService;

/**
 * @author renato
 */
@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractDAO<T> {
	
	@Inject
	private SecurityService securityService;

    @PersistenceContext(unitName = "ouvidoriaPersistenceUnit")
    private EntityManager em;
    private Class<T> entityClass;

    public abstract String getNomeEntidade();

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    
    /**
     * Converte um objeto do tipo java.sql.Clob em String
     * 
     * @param obj
     * @return
     * @throws Exception
     */
	protected String converterTextoClob(Clob clob) throws Exception{
		
		Reader reader = clob.getCharacterStream();
		int c = -1;
		StringBuilder sb = new StringBuilder();
		while((c = reader.read()) != -1) {
			sb.append(((char)c));
		}
		
		String texto =  sb.toString();
		return texto;
	}


	
    public void refresh(T entity) {
        try {
            getEntityManager().flush();
            getEntityManager().refresh(entity);
        } catch (ConstraintViolationException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(T entity) throws InfrastructureException {
        try {
            getEntityManager().persist(entity);
            saveLog(LogOperacaoEnum.INSERIR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InfrastructureException("Erro ao cadastrar entidade: " + ex.getMessage());
        }
    }

    private void saveLog(LogOperacaoEnum tipo, T entidade) throws InfrastructureException {
        try {
            TbLogOperacao op = new TbLogOperacao();
            op.setDtDataLog(new Date());

            String nomeUsuario = securityService.getUserName();
            op.setDsOperacao(String.format("%s %s %s - %s", nomeUsuario, tipo.getAcao(), getNomeEntidade(), entidade.toString()));
            if (op.getDsOperacao().length() >= 250)
                op.setDsOperacao(op.getDsOperacao().substring(0, 240) + "...");
            
            op.setTpOperacao(tipo.getId());
            op.setIdUsuario(securityService.getUser());
            
            getEntityManager().persist(op);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InfrastructureException("Erro ao salvar log: " + ex.getMessage());
        }
    }

    public void edit(T entity) throws InfrastructureException {
        try {
            getEntityManager().merge(entity);
            saveLog(LogOperacaoEnum.ALTERAR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InfrastructureException("Erro ao editar entidade: " + ex.getMessage());
        }
    }

    public void clear() {
        getEntityManager().clear();
    }

    public void remove(T entity) throws InfrastructureException {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            saveLog(LogOperacaoEnum.EXCLUIR, entity);
            getEntityManager().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InfrastructureException("Erro ao remover entidade: " + ex.getMessage());
        }
    }

    public T find(Integer id) {
        return getEntityManager().find(entityClass, id);
    }

	public List<T> findAll() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public List<T> findRange(int[] range) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public int count() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
            cq.select(getEntityManager().getCriteriaBuilder().count(rt));
            javax.persistence.Query q = getEntityManager().createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public T getObject(String entityMethod, HashMap<String, ?> map) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);

        for (String key : map.keySet())
            query.setParameter(key, map.get(key));

        try {
            return (T) query.getSingleResult();
        } catch (Exception e) {
            throw e;
        }
    }

    public T selectObject(String select, HashMap<String, ?> map) throws Exception {
        try {
            Query query = em.createQuery(select);
            
            for (String key : map.keySet()) {
            	query.setParameter(key, map.get(key));
            }
            
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
        	// Do nothing
    	} catch (NonUniqueResultException e) {
    		throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> selectList(String select, HashMap<String, ?> map) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> selectList(String select, HashMap<String, ?> map, Integer first, Integer qtd) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> getList(String entityMethod, HashMap<String, ?> map) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> selectList(String select, HashMap<String, ?> map, int first, int qtd) throws Exception {
        Query query = em.createQuery(select);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }

    public List<T> getList(String entityMethod, HashMap<String, ?> map, int first, int qtd) throws Exception {
        Query query = em.createNamedQuery(getEntityClass().getSimpleName() + "." + entityMethod);
        List<T> res = new ArrayList<T>();
        try {
            for (String key : map.keySet())
                query.setParameter(key, map.get(key));
            
            if (first >= 0 & qtd >= 0) {
                query.setFirstResult(first);
                query.setMaxResults(qtd);
            }
            
            res.addAll(query.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }

        return res;
    }

    public List<T> findIn(List<Integer> listaIds) {
        if (listaIds == null || listaIds.isEmpty())
            return new ArrayList<T>();

        String query = "SELECT a FROM %s a WHERE a.%s IN :listaIds";
        List<T> res = new ArrayList<T>();
        HashMap<String, Object> param = new HashMap<>();
        param.put("listaIds", listaIds);

        try {
            String fieldId = ReflectionHelper.getAtributoID(entityClass);
            query = String.format(query, entityClass.getSimpleName(), fieldId);
            res = selectList(query, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return res;
    }

    /**
     * @return the entityClass
     */
	public Class getEntityClass() {
        return entityClass;
    }
}
