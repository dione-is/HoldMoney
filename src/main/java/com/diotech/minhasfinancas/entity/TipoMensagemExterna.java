package com.diotech.minhasfinancas.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "tipomensagemexterna", schema = "financas")
public class TipoMensagemExterna {


    @Id
    private Long id;

    @NotBlank(message = "A descrição não pode estar vazia.")
    @Size(min = 1, max = 250, message = "A descrição deve conter no mínimo 1 e no máximo 250 caracteres.")
    @Column(length = 250, nullable = false)
    private String descricao;

    @Column(name = "permiteanexo")
    private Boolean permiteAnexo;
    @Column(name = "sotexto")
    private Boolean soTexto;
    @Column(name = "temlimitetamanho")
    private Boolean temLimiteTamanho;
    @Column(name = "numerocaracteres")
    private Long numeroCaracteres;
}
