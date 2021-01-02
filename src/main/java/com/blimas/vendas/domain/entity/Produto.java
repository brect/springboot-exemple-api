package com.blimas.vendas.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "produto_id")
    private Integer id;

    @NotEmpty(message = "${campo.descricao.obrigatorio}")
    @Column
    private String descricao;

    @NotNull(message = "${campo.preco.obrigatorio}")
    @Column(name = "preco_unitario", scale = 2, precision = 20)
    private BigDecimal precoUnitario;
}
