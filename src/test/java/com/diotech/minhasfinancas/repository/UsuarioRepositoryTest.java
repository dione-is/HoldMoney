package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarExistenciaEmail() {
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        boolean result = repository.existsByEmail("dione0905@gmail.com");

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void hasUsuarioCadastrado() {
        boolean result = repository.existsByEmail("dione0905@gmail.com");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void salvarUsuario() {
        Usuario usuario = criarUsuario();
        repository.save(usuario);

        Assertions.assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void buscarUsuarioPorEmail() {
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);
        Optional result = repository.findByEmailAndSenha(usuario.getEmail(), usuario.getSenha());

        Assertions.assertThat(result.isPresent()).isTrue();
    }

    public static Usuario criarUsuario() {
        return Usuario.builder().nome("Dione").email("dione0905@gmail.com").senha("senha10").build();
    }
}
