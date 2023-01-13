package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.beans.PropertyEditorSupport;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
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

    @Test
    public void hasUsuarioCadastrado(){
        repository.deleteAll();
        boolean result = repository.existsByEmail("dione0905@gmail.com");

        Assertions.assertThat(result).isFalse();
    }
}
