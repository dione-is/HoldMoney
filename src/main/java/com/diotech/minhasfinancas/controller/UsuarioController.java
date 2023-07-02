package com.diotech.minhasfinancas.controller;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService service;

    private final Logger logger = LoggerFactory.getLogger(String.class);

    @PostMapping("/")
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        try {
            usuario = service.salvarUsuario(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Usuario> autenticar(@RequestBody Usuario usuario) {
        try {
            usuario = service.autenticar(usuario.getEmail(), usuario.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (ErroAutenticacao e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("{id}/buscar-saldo")
    public ResponseEntity buscarSaldo(@PathVariable Long id) {
        return service.buscarUsuarioPorId(id).map(entity -> {
            try {
                return ResponseEntity.ok(service.BuscarSaldoUsuario(id));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("usuario informado nao encontrado", HttpStatus.BAD_REQUEST));
    }

}
