package com.swissroute.swissroute.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.UsuarioYaExisteException;
import com.swissroute.swissroute.mapper.UsuarioMapper;
import com.swissroute.swissroute.repository.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UsuarioResponse registro(RegistroRequest request) {
        // Check if email already exists
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioYaExisteException("El email ya está registrado");
        }

        // Create new user
        Usuario usuario = UsuarioMapper.toUsuario(request);

        // Encode password
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set created at timestamp
        usuario.setCreatedAt(LocalDateTime.now());

        // Save user
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Return response
        return UsuarioMapper.toUsuarioResponse(savedUsuario);
    }
}