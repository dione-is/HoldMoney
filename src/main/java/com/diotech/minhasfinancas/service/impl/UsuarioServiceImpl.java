package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository){
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        return null;
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        return null;
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("já existe esse email Cadastrado");
        }
    }
}
