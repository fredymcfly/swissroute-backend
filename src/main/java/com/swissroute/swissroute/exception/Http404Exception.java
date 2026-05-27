package com.swissroute.swissroute.exception;

import org.springframework.http.HttpStatus;

public class Http404Exception extends HttpClientException {
    public Http404Exception(String message) {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }
}