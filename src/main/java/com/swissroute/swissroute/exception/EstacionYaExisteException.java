package com.swissroute.swissroute.exception;

/**
 * Excepción para cuando un usuario intenta crear una estación favorita duplicada
 */
public class EstacionYaExisteException extends RuntimeException {
    
    public EstacionYaExisteException(String estacionIdExterno) {
        super("Estación ya existe en favoritos: " + estacionIdExterno);
    }
}