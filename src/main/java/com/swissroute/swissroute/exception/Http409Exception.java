package com.swissroute.swissroute.exception;

import org.springframework.http.HttpStatus;

public class Http409Exception extends HttpClientException{
    public Http409Exception(String message) {
        super(HttpStatus.CONFLICT, "Object created already", message);
    }
}
