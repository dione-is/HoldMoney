package com.diotech.minhasfinancas.dto;

import com.diotech.minhasfinancas.entity.TipoMensagemExterna;
import lombok.Data;

@Data
public class MensagemDTO {

    private TipoMensagemExterna tipoMensagemExterna;
    private String assunto;
    private String mensagem;
    private String remetente;

}
