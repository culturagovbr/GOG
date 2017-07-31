package br.com.xti.ouvidoria.helper;

import java.util.Collection;

/**
 * @author Samuel Correia Guimarães
 */
public class ValidacaoHelper {

	/**
	 * @param obj
	 *            objeto a ser validado
	 * @return TRUE se o objeto passado por parâmetro for NULL, VAZIO
	 *         (Collections, Arrays, Strings) ou menor/igual a zero para tipos
	 *         númericos
	 */
	public static boolean isEmpty(Object obj) {
		boolean vazio = false;

		if (obj == null) {
			vazio = true;
		} else {
			if (obj instanceof String) {
				vazio = ((String) obj).replaceAll("\\s+", " ").trim().isEmpty();
			} else if (obj instanceof Number) {
				if (((Number) obj).longValue() == 0)
					vazio = true;
			} else if (obj instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) obj;
				vazio = (col == null || col.isEmpty());
			} else if (obj instanceof Object[]) {
				vazio = (((Object[]) obj).length == 0);
			}

		}
		return vazio;
	}

	/**
	 * @param objs
	 *            objetos a serem validados
	 * @return TRUE se algum dos objetos passados por parâmetro for NULL ou
	 *         VAZIO
	 */
	public static boolean isEmpty(Object... objs) {
		boolean isEmpty = false;
		for (Object obj : objs) {
			isEmpty = isEmpty(obj);
			if (isEmpty) {
				break;
			}
		}
		return isEmpty;
	}

	/**
	 * @param obj
	 *            objeto a ser validado
	 * @return TRUE se o objeto passado por parâmetro for diferente de NULL,
	 *         VAZIO (Collections, Arrays, Strings) e maior que zero para tipos
	 *         númericos
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

}
