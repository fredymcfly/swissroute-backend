package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    @Operation(
        summary = "Registra un nuevo usuario",
        description = "Permite registrar un nuevo usuario con sus datos básicos"
    )
    public ResponseEntity<UsuarioResponse> registro(
        @Valid @RequestBody RegistroRequest request
    ) {
        UsuarioResponse response = usuarioService.registro(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
