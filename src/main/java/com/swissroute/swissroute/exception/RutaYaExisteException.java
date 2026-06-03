package com.swissroute.swissroute.exception;

/**
 * Excepcion para cuando un usuario intenta crear una ruta con nombre duplicado
 */
public class RutaYaExisteException extends RuntimeException {
    
    public RutaYaExisteException(String nombre) {
        super("Ya existe una ruta con el nombre '" + nombre + "' para este usuario");
    }
}
