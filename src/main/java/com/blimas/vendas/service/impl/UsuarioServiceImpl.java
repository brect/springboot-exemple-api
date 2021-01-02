package com.blimas.vendas.service.impl;

import com.blimas.vendas.domain.entity.Usuario;
import com.blimas.vendas.domain.repository.UsuarioRepository;
import com.blimas.vendas.exception.PasswordInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario save(Usuario user) {
        return usuarioRepository.save(user);
    }

    public UserDetails authenticate(Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getUsername());
        boolean isPasswordValid = passwordEncoder.matches(usuario.getPassword(), user.getPassword());

        if (isPasswordValid){
            return user;
        }

        throw new PasswordInvalidException("Senha inválida");

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        getRolesUser(usuario.isAdmin());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(getRolesUser(usuario.isAdmin()))
                .build();
    }

    private String[] getRolesUser(Boolean isAdmin) {
        return isAdmin ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};
    }

}
