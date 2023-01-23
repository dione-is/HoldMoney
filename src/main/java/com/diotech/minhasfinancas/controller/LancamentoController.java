package com.diotech.minhasfinancas.controller;

import com.diotech.minhasfinancas.dto.StatusLancamentoDTO;
import com.diotech.minhasfinancas.entity.Lancamento;
import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.enums.StatusLancamento;
import com.diotech.minhasfinancas.enums.TipoLancamento;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.service.impl.LancamentoServiceImpl;
import com.diotech.minhasfinancas.service.impl.UsuarioServiceImpl;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/lancamento/")
public class LancamentoController {

    @Autowired
    private LancamentoServiceImpl service;

    @Autowired
    private UsuarioServiceImpl usuarioService;

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
            } catch (RegraNegocioException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lançamento não encontrado na base de Dados");
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable Long id, @RequestBody StatusLancamentoDTO status){

       return service.obterLancamentoPorId(id).map(entity -> {
           if(status == null){
               throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Status de lancamento nao informado!");
           }
           StatusLancamento statusLancamento = StatusLancamento.valueOf(status.getStatus());
            try {
                    service.atualizarStatus(entity, statusLancamento);
                    return ResponseEntity.ok(entity);
            }catch (RegraNegocioException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }).orElseGet( () -> new ResponseEntity("Lancamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            service.obterLancamentoPorId(id).ifPresent(lancamento -> service.deletar(lancamento.getId()));
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "descricao", required = false) String descricao,
                                 @RequestParam(value = "mes", required = false) Integer mes,
                                 @RequestParam(value = "ano", required = false) Integer ano,
                                 @RequestParam(value = "usuario") Long usuario,
                                 @RequestParam(value = "tipo", required = false) String tipo
    ) {
        try {
            Lancamento lancamento = new Lancamento();
            lancamento.setDescricao(descricao);
            lancamento.setAno(ano);
            lancamento.setMes(mes);
            lancamento.setTipo(tipo != null ? TipoLancamento.valueOf(tipo) : null);
            lancamento.setUsuario(usuarioService.buscarUsuarioPorId(usuario).orElse(null));
            if (lancamento.getUsuario() == null) {
                ResponseEntity.badRequest().body("Não foi possivel realizar a consulta, usuario não encontado");
            }
            List<Lancamento> lancamentos = service.buscar(lancamento);
            return ResponseEntity.ok(lancamentos);
        }catch (RegraNegocioException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
