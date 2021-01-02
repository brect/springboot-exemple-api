package com.blimas.vendas.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usuario_id")
    private UUID id;

    @NotEmpty(message = "${campo.username.obrigatorio}")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "${campo.password.obrigatorio}")
    @Column(name = "password")
    private String password;

    @Column(name = "admin")
    private boolean admin;

}
