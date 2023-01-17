package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmailAndSenha(email, senha);
        if(usuario.isPresent()){
            return usuario.get();
        }
        throw new ErroAutenticacao("Email ou Senha Invalido");
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("já existe esse email Cadastrado");
        }
    }
}
