package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.LoginRequest;
import com.swissroute.swissroute.dto.LoginResponse;
import com.swissroute.swissroute.service.impl.CustomUserDetailsService;
import com.swissroute.swissroute.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login de usuario",
        description = "Permite autenticar un usuario y obtener un token JWT"
    )
    public ResponseEntity<?> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        try {
            log.info("=== LOGIN START ===");

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            log.info("=== AUTH OK ===");

            UserDetails userDetails = userDetailsService.loadUserByUsername(
                loginRequest.getEmail()
            );

            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Usuario no encontrado"
                );
            }

            log.info("=== USER DETAILS OK ===");

            String jwt = jwtUtil.generateToken(userDetails);

            log.info("=== JWT GENERATED ===");

            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                "Credenciales inválidas"
            );
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Error interno: " + e.getMessage()
            );
        }
    }
}
