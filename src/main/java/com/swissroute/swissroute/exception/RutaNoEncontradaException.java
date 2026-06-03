package com.swissroute.swissroute.exception;

/**
 * Excepcion para cuando no se encuentra una ruta por su ID
 */
public class RutaNoEncontradaException extends RuntimeException {
    
    public RutaNoEncontradaException(Long id) {
        super("La ruta con ID " + id + " no existe");
    }
    
    public RutaNoEncontradaException(String message) {
        super(message);
    }
}
