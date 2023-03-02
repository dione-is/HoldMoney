package com.diotech.minhasfinancas.controller;

import com.diotech.minhasfinancas.dto.MensagemDTO;
import com.diotech.minhasfinancas.dto.SmsDTO;
import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.entity.WebHookWhatsApp;
import com.diotech.minhasfinancas.exception.ErroAutenticacao;
import com.diotech.minhasfinancas.exception.RegraNegocioException;
import com.diotech.minhasfinancas.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService service;

    private final Logger logger = LoggerFactory.getLogger(WebHookWhatsApp.class);

    @PostMapping("/")
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        try {
            usuario = service.salvarUsuario(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Usuario> autenticar(@RequestBody Usuario usuario) {
        try {
            usuario = service.autenticar(usuario.getEmail(), usuario.getSenha());
            return ResponseEntity.ok(usuario);
        } catch (ErroAutenticacao e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("{id}/buscar-saldo")
    public ResponseEntity buscarSaldo(@PathVariable Long id) {
        return service.buscarUsuarioPorId(id).map(entity -> {
            try {
                return ResponseEntity.ok(service.BuscarSaldoUsuario(id));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("usuario informado nao encontrado", HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/enviar-sms-usuario")
    public ResponseEntity enviarSmsPorUsuario(@RequestBody SmsDTO sms){
        try{
            service.enviarSmsUsuario(sms);
            return ResponseEntity.ok(sms);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enviar-sms-todos")
    public ResponseEntity enviarMensagemSMSParaTodos(@RequestBody MensagemDTO msg){
        try {
            service.enviarSmsParaTodos(msg);
            return ResponseEntity.ok(true);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enviar-wpp-usuario")
    public ResponseEntity<String> enviarWhatsAppUsuario(@RequestBody SmsDTO sms){
        try{
            service.enviarWhatsAppUsuario(sms);
            return ResponseEntity.ok("Mensagem enviada por WhatsApp com Sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enviar-mensagem")
    public ResponseEntity<String> enviarMensagem(@RequestBody MensagemDTO mensagemDTO){
        try{
            service.enviarMensagem(mensagemDTO);
            return ResponseEntity.ok("Mensagem Enviada com Sucesso");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/atualizar-status-whatsapp")
    public ResponseEntity atualizarStatusMensagemWhatsApp(@RequestBody LinkedHashMap webHookWhatsApp) {
        try {
            logger.error("WEBHOOK: " + webHookWhatsApp);
            return new ResponseEntity(service.atualizarStatusMensagemWhatsApp(webHookWhatsApp));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
