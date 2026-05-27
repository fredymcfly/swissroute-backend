package com.swissroute.swissroute.exception;

import org.springframework.http.HttpStatus;

public class Http500Exception extends HttpClientException {
    public Http500Exception(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", message);
    }
}