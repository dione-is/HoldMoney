package com.diotech.minhasfinancas.entity;

import com.diotech.minhasfinancas.enums.MensagemStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "mensagemexternastatus", schema = "financas")
public class MensagemExternaStatus {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date data;

    @Enumerated(EnumType.STRING)
    @Column(name = "mensagemstatus")
    private MensagemStatus mensagemStatus;
}
