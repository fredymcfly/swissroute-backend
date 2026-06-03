package com.swissroute.swissroute.exception;

public class Http403Exception extends RuntimeException {
    
    public Http403Exception(String message) {
        super(message);
    }
    
    public Http403Exception() {
        super("Acceso no autorizado");
    }
}