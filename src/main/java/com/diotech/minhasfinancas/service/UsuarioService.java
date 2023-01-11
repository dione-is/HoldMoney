package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);
}
