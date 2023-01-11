package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    public void deveVerificarExistenciaEmail() {
        Usuario usuario = Usuario.builder().nome("Dione").email("dione0905@gmail.com").build();
        repository.save(usuario);

        boolean result = repository.existsByEmail("dione0905@gmail.com");

        Assertions.assertThat(result).isTrue();
    }
}
