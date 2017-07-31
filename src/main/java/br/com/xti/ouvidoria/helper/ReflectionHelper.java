package br.com.xti.ouvidoria.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.apache.commons.lang.WordUtils;

/**
 * @author Samuel Correia Guimarães
 */
public class ReflectionHelper {

    /**
     * Retorna o nome método referente ao getId da classe
     *
     * @param clazz
     * @return nome do método
     */
    public static String getMetodoID(Class<?> clazz) {
        String fieldId = getAtributoID(clazz);
        if(fieldId != null) {
        	return String.format("get%s", WordUtils.capitalize(fieldId));
        }
        return null;
    }
    
    /**
     * Retorna o atributo referente ao Id da classe
     *
     * @param clazz
     * @return nome do atributo
     */
    public static String getAtributoID(Class<?> clazz) {
    	Field[] fields = clazz.getDeclaredFields();
    	for (Field field : fields) {
    		if (field.getAnnotation(Id.class) != null) {
    			return field.getName();
    		}
    	}
    	return null;
    }

    public static Integer getValorID(Object u) throws Exception {
        Method m = u.getClass().getMethod(getMetodoID(u.getClass()));
        Object o = m.invoke(u);
        return (Integer) o;
    }

}
