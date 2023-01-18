package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

    Optional<Usuario> buscarUsuarioPorId(Long id);
}
