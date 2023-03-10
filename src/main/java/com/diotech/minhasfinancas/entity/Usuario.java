package com.diotech.minhasfinancas.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "usuario", schema = "financas")
@Data
@Builder
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email")
    private String email;
    @Column(name = "senha")
    private String senha;

    @Column(name = "telefone")
    private String telefone;

    public Usuario(Long id, String nome, String email, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public Usuario() {

    }

}
