package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmailAndSenha(String email, String senha);

    @Query(value = "select sum(l.valor) " +
            "from financas.lancamento l " +
            "         inner join financas.usuario u on u.id = l.id_usuario " +
            "where u.id = :id " +
            "  and l.tipo = :tipo", nativeQuery = true)
    BigDecimal buscarSaldoPorUsuarioAndTipoLancamento(@Param("id") Long id, @Param("tipo") String tipo);
}
