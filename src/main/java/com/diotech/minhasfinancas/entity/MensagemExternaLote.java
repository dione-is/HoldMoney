package com.diotech.minhasfinancas.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "mensagemexternalote", schema = "financas")
public class MensagemExternaLote {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "dataemissao")
    private Date dataEmissao;

    @Column(name = "quantidadeenviado")
    private Integer quantidadeEnviado;
    @Column(name = "quantidadelido")
    private Integer quantidadeLido;
    @Column(name = "quantidadeerro")
    private Integer quantidadeErro;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mensagemexterna_id")
    private MensagemExterna mensagemExterna;

    @ManyToOne()
    @JoinColumn(name = "tipomensagemexterna_id")
    private TipoMensagemExterna tipoMensagemExterna;

    @Column(length = 250)
    private String assunto;

    @Column(length = 500, name = "mensagemexternacurta")
    private String mensagemExternaCurta;

    @Column(columnDefinition="TEXT", name = "mensagemexternalonga")
    private String mensagemExternaLonga;

    @Column(length = 500)
    private String link;
    @Column(name = "formatohtml")
    private boolean formatoHtml;

    private Long idrepositorioarquivo;

    @Column(length = 500, name = "nomefisicoarquivo")
    private String nomeFisicoArquivo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mensagemexternastatus_id")
    private MensagemExternaStatus mensagemExternaStatus;

}
