package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {

    String GerarToken(Usuario usuario);
    Claims obterClaims(String token) throws ExpiredJwtException;
    Boolean isTokenValido(String token);
    String ObterLoginUsuario(String token);
}
