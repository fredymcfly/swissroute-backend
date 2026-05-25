package com.swissroute.swissroute.mapper;

import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.entity.Usuario;

public class UsuarioMapper {
    
    public static Usuario toUsuario(RegistroRequest registroRequest) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registroRequest.getNombre());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setPassword(registroRequest.getPassword()); // Password will be encoded later
        usuario.setCiudadBase(registroRequest.getCiudadBase());
        return usuario;
    }
    
    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCiudadBase(),
                usuario.getCreatedAt()
        );
    }
}