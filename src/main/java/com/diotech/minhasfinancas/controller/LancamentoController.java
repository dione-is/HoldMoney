package com.diotech.minhasfinancas.controller;

import com.diotech.minhasfinancas.entity.Lancamento;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.service.impl.LancamentoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/lancamento/")
public class LancamentoController {

    @Autowired
    private LancamentoServiceImpl service;

    @PostMapping
    public ResponseEntity<Lancamento> salvar(@RequestBody Lancamento lancamento) {
        try {
            lancamento = service.salvar(lancamento);
            return new ResponseEntity<>(lancamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @RequestBody Lancamento lancamento) {
        if (service.obterLancamentoPorId(id).isPresent() && id.equals(lancamento.getId())) {
            try {
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch (RegraNegocioException e){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lançamento não encontrado na base de Dados");
    }
}
