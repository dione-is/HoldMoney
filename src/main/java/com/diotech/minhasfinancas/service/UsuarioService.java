package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.dto.MensagemDTO;
import com.diotech.minhasfinancas.dto.SmsDTO;
import com.diotech.minhasfinancas.entity.Usuario;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

    Optional<Usuario> buscarUsuarioPorId(Long id);

    BigDecimal BuscarSaldoUsuario(Long id);

    void enviarSmsParaTodos(MensagemDTO mensagem);

    void enviarMensagem(MensagemDTO mensagemDTO);
    void enviarSmsUsuario(SmsDTO sms);

    void enviarWhatsAppUsuario(SmsDTO sms) throws IOException;

    HttpStatus atualizarStatusMensagemWhatsApp(LinkedHashMap webHookWhatsApp);
}
