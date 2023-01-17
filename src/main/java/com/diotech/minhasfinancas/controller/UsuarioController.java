package com.diotech.minhasfinancas.controller;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){
        try {
            usuario = service.salvarUsuario(usuario);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
           throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Usuario> autenticar(@RequestBody Usuario usuario){
        try{
            usuario = service.autenticar(usuario.getEmail(), usuario.getSenha());
            return ResponseEntity.ok(usuario);
        }catch (ErroAutenticacao e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
