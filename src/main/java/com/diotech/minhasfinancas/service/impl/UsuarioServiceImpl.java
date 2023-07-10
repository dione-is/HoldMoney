package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.enums.TipoLancamento;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    private final Logger logger = LoggerFactory.getLogger(String.class);
    private PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder encoder) {
        super();
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent()) {
            Boolean senhaCorreta = encoder.matches(senha, usuario.get().getSenha());
            if (senhaCorreta) {
                return usuario.get();
            }
        }
        throw new ErroAutenticacao("Email ou Senha Invalido");
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        criptografarSenha(usuario);
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    public void criptografarSenha(Usuario usuario) {
        String senha = usuario.getSenha();
        String senhaCripto = encoder.encode(senha);
        usuario.setSenha(senhaCripto);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || "".equals(usuario.getNome().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuario deve ser informado");
        }
        if (usuario.getEmail() == null || "".equals(usuario.getEmail().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Email do usuario deve ser informado");
        }
        if (usuario.getSenha() == null || "".equals(usuario.getSenha().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ser informado");
        }
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("já existe esse email Cadastrado");
        }
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public BigDecimal BuscarSaldoUsuario(Long id) {

        BigDecimal receita = repository.buscarSaldoPorUsuarioAndTipoLancamento(id, TipoLancamento.RECEITA.toString());
        BigDecimal despesa = repository.buscarSaldoPorUsuarioAndTipoLancamento(id, TipoLancamento.DESPESA.toString());
        receita = receita == null ? BigDecimal.ZERO : receita;
        return receita.subtract(despesa == null ? BigDecimal.ZERO : despesa);
    }

}
