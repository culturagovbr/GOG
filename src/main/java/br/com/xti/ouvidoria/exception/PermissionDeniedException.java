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
public class PermissionDeniedException extends Exception {

    public PermissionDeniedException() {
        super("Você não tem permissão para acessar esta funcionalidade.");
    }
}
