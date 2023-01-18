package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Lancamento;
import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.enums.StatusLancamento;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.repository.LancamentoRepository;
import com.diotech.minhasfinancas.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        lancamento.setUsuario(usuarioService.buscarUsuarioPorId(lancamento.getUsuario().getId()).orElse(null));
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        lancamento.setDataCadastro(LocalDate.now());
        return repository.save(lancamento);
    }

    @Override
    public Lancamento atualizar(Lancamento lancamento) {
        if (lancamento.getId() != null) {
            validar(lancamento);
            return repository.save(lancamento);
        }
        throw new RegraNegocioException("Lancamento invalido");
    }

    @Override
    public void deletar(Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
        throw new RegraNegocioException("Lancamento invalido");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        //example instacia o objeto que queremos buscar com as propriedade com filtros
        // withIgnoreCase para igonar maiscula e minuscula
        //Stringmatcher.COntaining para buscar o valor que contenha parte do valor
        Example<Lancamento> example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizar(lancamento);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("O campo Descrição deve ser informado!");
        }
        if (lancamento.getMes() == null || lancamento.getMes() > 12 || lancamento.getMes() < 1) {
            throw new RegraNegocioException("Informe um mês Válido");
        }
        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
            throw new RegraNegocioException("Informe um Ano válido");
        }
        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
            throw new RegraNegocioException("Informe um Usuário válido");
        }
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
            throw new RegraNegocioException("Informe um Valor válido");
        }
        if (lancamento.getTipo() == null) {
            throw new RegraNegocioException("Informe um Tipo Lançamento válido");
        }
    }

    @Override
    public Optional<Lancamento> obterLancamentoPorId(Long id) {
        return repository.findById(id);
    }
}
