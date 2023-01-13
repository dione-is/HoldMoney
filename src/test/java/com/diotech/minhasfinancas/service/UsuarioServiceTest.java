package com.diotech.minhasfinancas.service;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.util.LangUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test
    public void notHasEmailCadastro(){
        repository.deleteAll();
        Assertions.assertDoesNotThrow(()-> service.validarEmail("dione0905@gmail.com"));
    }

    @Test
    public void hasEmailCasdastrado(){
        Usuario usuario = Usuario.builder().nome("Dione").email("dione0905@gmail.com").build();
        repository.save(usuario);

        Assertions.assertThrows(RuntimeException.class, ()-> service.validarEmail("dione0905@gmail.com"));
    }

}
