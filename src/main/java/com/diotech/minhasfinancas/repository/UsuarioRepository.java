package com.diotech.minhasfinancas.repository;

import com.diotech.minhasfinancas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
