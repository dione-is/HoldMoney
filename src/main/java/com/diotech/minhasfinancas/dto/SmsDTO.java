package com.diotech.minhasfinancas.dto;

import com.diotech.minhasfinancas.entity.Usuario;

public class SmsDTO {

    private Usuario usuario;
    private String mensagem;

    public SmsDTO(Usuario usuario, String mensagem) {
        this.usuario = usuario;
        this.mensagem = mensagem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
