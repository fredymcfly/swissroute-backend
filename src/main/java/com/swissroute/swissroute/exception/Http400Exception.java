package com.swissroute.swissroute.exception;

import org.springframework.http.HttpStatus;

public class Http400Exception extends HttpClientException {
    public Http400Exception(String message) {
        super(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
    }
}