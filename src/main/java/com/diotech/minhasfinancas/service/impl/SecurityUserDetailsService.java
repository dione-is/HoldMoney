package com.diotech.minhasfinancas.service.impl;

import com.diotech.minhasfinancas.entity.Usuario;
import com.diotech.minhasfinancas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;
    public SecurityUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email não cadastrado"));
        return User.builder().username(usuario.getEmail()).password(usuario.getSenha()).roles("USER").build();
    }
}
