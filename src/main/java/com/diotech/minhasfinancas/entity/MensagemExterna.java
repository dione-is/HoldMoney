package com.diotech.minhasfinancas.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "mensagemexterna",schema = "financas")
public class MensagemExterna {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Usuario pessoa;

    @Column(length = 350, nullable=false)
    private String contato;

    @Column(length = 250)
    private String assunto;

    @Column(length = 500)
    private String mensagem;

    @Column(length = 500)
    private String link;

    @Column(name = "nomefisicoarquivo", length = 500)
    private String nomeFisicoArquivo;

    private Boolean enviado;
    private Boolean lido;
    private Boolean erro;
}
