package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Lancamento;
import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.enums.StatusLancamento;
import com.diotech.minhasfinancas.enums.TipoLancamento;
import com.diotech.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LancamentoServiceTest {

    @Autowired
    LancamentoService service;

    @Autowired
    UsuarioService usuarioService;

    @Test
    @Order(2)
    public void validar() {
        Assertions.assertDoesNotThrow(() -> service.validar(createLancamento()));
    }

    @Test
    @Order(1)
    public void salvar() {
        usuarioService.salvarUsuario(new Usuario(1L, "dione", "dione@email", "123mudar"));
        org.assertj.core.api.Assertions.assertThat(service.salvar(createLancamento()).getId()).isNotNull();
    }

    @Test
    @Order(3)
    public void atualizar(){
        Lancamento oldLancamento = service.obterLancamentoPorId(1L).orElse(null);
        oldLancamento.setValor(new BigDecimal(1580.80));
        org.assertj.core.api.Assertions.assertThat(service.atualizar(oldLancamento)).isNotNull();
    }


    @Test
    @Order(4)
    public void buscarLancamentoPorId(){
        Assertions.assertNotNull(service.obterLancamentoPorId(1L).orElse(null));
    }

    @Test
    @Order(5)
    public void atualizarStatusLancamento(){
        Lancamento lancamento = service.obterLancamentoPorId(1L).orElse(null);
        Assertions.assertDoesNotThrow(() -> service.atualizarStatus(lancamento, StatusLancamento.EFETIVADO));
        Assertions.assertEquals(lancamento.getStatus(), StatusLancamento.EFETIVADO);
    }

    @Test
    @Order(6)
    public void buscarLancamentos(){
        Lancamento filtro = Lancamento.builder()
                                .usuario(usuarioService.buscarUsuarioPorId(1L).orElse(null))
                .descricao("tes").build();
        Assertions.assertNotNull(service.buscar(filtro));
    }

    @Test
    public void deletar(){
        service.deletar(1L);
        Assertions.assertNull(service.obterLancamentoPorId(1L).orElse(null));
    }
    
    public Lancamento createLancamento() {
        return Lancamento.builder()
                .ano(2022)
                .mes(1)
                .descricao("teste")
                .usuario(new Usuario(1L, "dione", "dione@email", "123mudar"))
                .tipo(TipoLancamento.RECEITA)
                .valor(BigDecimal.valueOf(580.80)).build();
    }
}
