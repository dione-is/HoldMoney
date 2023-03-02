package com.diotech.minhasfinancas.entity;

import com.diotech.minhasfinancas.enums.StatusLancamento;
import com.diotech.minhasfinancas.enums.TipoLancamento;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamento", schema = "financas")
@Data
@Builder
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "ano")
    private Integer ano;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "data_cadastro")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusLancamento status;

    public Lancamento() {

    }


    public Lancamento(Long id, String descricao, Integer mes, Integer ano, Usuario usuario, BigDecimal valor, LocalDate dataCadastro, TipoLancamento tipo, StatusLancamento status) {
        this.id = id;
        this.descricao = descricao;
        this.mes = mes;
        this.ano = ano;
        this.usuario = usuario;
        this.valor = valor;
        this.dataCadastro = dataCadastro;
        this.tipo = tipo;
        this.status = status;
    }
}
