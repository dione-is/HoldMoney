package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import com.diotech.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioServiceTest {

    @Autowired
    UsuarioServiceImpl service;

    @Autowired
    UsuarioRepository repository;

    @Test
    @Order(1)
    public void notHasEmailCadastro() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.validarEmail("dione0905@gmail.com"));
    }

    @Test
    @Order(2)
    public void salvarUsuario() {
        Usuario usuario = service.salvarUsuario(criarUsuario());
        Assertions.assertThat(usuario.getId()).isNotNull();
    }

    @Test
    @Order(3)
    public void hasEmailCasdastrado() {
        Usuario usuario = criarUsuario();

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> service.validarEmail("dione0905@gmail.com"));
    }

    @Test
    @Order(4)
    public void autenticarUsuarioSucess() {
        Usuario usuario = criarUsuario();
        usuario = service.autenticar(usuario.getEmail(), usuario.getSenha());
        Assertions.assertThat(usuario.getId()).isNotNull();

    }

    @Test
    @Order(5)
    public void autenticarUsuarioError() {
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> service.autenticar("dione0905@gmail.com", "123456"));

    }

    public static Usuario criarUsuario() {
        return Usuario.builder().nome("Dione").email("dione0905@gmail.com").senha("senha10").build();
    }

}
