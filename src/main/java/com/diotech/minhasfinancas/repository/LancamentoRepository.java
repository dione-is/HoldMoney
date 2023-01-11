package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
