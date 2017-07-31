package br.com.xti.ouvidoria.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class PasswordUtils {

	/**
	 * Retorna a representação MD5 da String passada por parâmetro
	 */
	public static String getMD5(String value) {
		MessageDigest md;
		try {
			byte[] bytesOfMessage = value.getBytes("UTF-8");
			md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			return new String(toHex(thedigest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Converte um array de bytes para representação hexa
	 */
	private static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "x", bi);
	}
	
	/**
	 * @return Senha alfanumérica com 6 dígitos. A senha pode ser formada pelos
	 *         seguintes caracteres: a-z | A-Z | 0-9
	 */
	public static String generatePassword() {
		// Determia as letras que poderão estar presente na senha
		String validDigits = "abcdefghijklmnopqrstuvywxzABCDEFGHIJKLMNOPQRSTUVYWXZ0123456789";
		
		Random random = new Random();
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(validDigits.length());
			password.append(validDigits.substring(index, index + 1));
		}
		return password.toString();
	}

}
