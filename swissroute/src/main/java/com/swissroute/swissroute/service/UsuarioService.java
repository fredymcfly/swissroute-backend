package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.dto.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse registro(RegistroRequest request);
}