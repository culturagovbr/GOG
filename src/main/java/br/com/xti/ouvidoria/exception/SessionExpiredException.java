/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.xti.ouvidoria.exception;

/**
 *
 * @author renato
 */
@SuppressWarnings("serial")
public class SessionExpiredException extends Exception {

    public SessionExpiredException() {
        super("Sua sessão expirou.");
    }
}
