package br.com.xti.ouvidoria.helper;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Utilitário para usar injeção de dependencias em algumas classes
 * 
 * @author Samuel Correia Guimarães
 * @since 11/11/2014 v1.2.0 
 */
public class CdiHelper {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void programmaticInjection(Class clazz, T injectionObject) throws NamingException {
		InitialContext initialContext = new InitialContext();
		Object lookup = initialContext.lookup("java:comp/BeanManager");
		BeanManager beanManager = (BeanManager) lookup;
		AnnotatedType annotatedType = beanManager.createAnnotatedType(clazz);
		InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);
		CreationalContext creationalContext = beanManager.createCreationalContext(null);
		injectionTarget.inject(injectionObject, creationalContext);
		creationalContext.release();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFacadeWithJNDI(Class<T> classToFind) {
		BeanManager bm = getBeanManager();
		Bean<T> bean = (Bean<T>) bm.getBeans(classToFind).iterator().next();
		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		T instance = (T) bm.getReference(bean, classToFind, ctx);
		return instance;
	}

	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/BeanManager");
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
}