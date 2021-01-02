package com.blimas.vendas.api.controller;

import com.blimas.vendas.api.dto.AuthDTO;
import com.blimas.vendas.api.dto.TokenDTO;
import com.blimas.vendas.configuration.security.jwt.JwtService;
import com.blimas.vendas.domain.entity.Usuario;
import com.blimas.vendas.exception.PasswordInvalidException;
import com.blimas.vendas.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioController(UsuarioServiceImpl usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/singin")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario createUser(@RequestBody @Valid Usuario usuario) {
        setPasswordEncode(usuario);
        return usuarioService.save(usuario);
    }

    private void setPasswordEncode(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    private TokenDTO auth(@RequestBody AuthDTO request) {

        try {
            Usuario user = Usuario.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .build();

            UserDetails userAuthenticate = usuarioService.authenticate(user);
            String token = jwtService.gerarToken(user);

            return new TokenDTO(userAuthenticate.getUsername(), token);

        } catch (UsernameNotFoundException | PasswordInvalidException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

}
