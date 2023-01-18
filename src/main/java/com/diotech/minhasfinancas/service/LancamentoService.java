package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Lancamento;
import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Long id);

    List<Lancamento> buscar(Lancamento lancamentoFiltro);

    void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);

    void validar(Lancamento lancamento);

    Optional<Lancamento> obterLancamentoPorId(Long id);
}
