package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiracao}")
    private String expiracao;
    @Value("${jwt.chave-assinatura}")
    private String chaveAssinatura;


    @Override
    public String GerarToken(Usuario usuario) {
        Long exp = Long.valueOf(expiracao);
        LocalDateTime dataHoraExp = LocalDateTime.now().plusMinutes(exp);
        Instant instant = dataHoraExp.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        return Jwts.builder()
                .setExpiration(data)
                .setSubject(usuario.getEmail())
                .claim("nome", usuario.getNome())
                .signWith(SignatureAlgorithm.ES512, chaveAssinatura)
                .compact();
    }

    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException {
        return null;
    }

    @Override
    public Boolean isTokenValido(String token) {
        return null;
    }

    @Override
    public String ObterLoginUsuario(String token) {
        return null;
    }
}
