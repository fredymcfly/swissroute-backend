package com.swissroute.swissroute.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String token;
    private final String type = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }

}