package com.swissroute.swissroute.exception;

/**
 * Excepción para cuando no se encuentra una estación favorita por su ID
 */
public class EstacionNoEncontradaException extends RuntimeException {
    
    public EstacionNoEncontradaException(Long id) {
        super("La estación favorita con ID " + id + " no existe");
    }
    
    public EstacionNoEncontradaException(String message) {
        super(message);
    }
}