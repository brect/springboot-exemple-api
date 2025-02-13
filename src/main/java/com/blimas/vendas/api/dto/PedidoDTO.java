package com.blimas.vendas.api.dto;

import com.blimas.vendas.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "${campo.codigo-cliente.obrigatorio}")
    private Integer cliente;

    @NotNull(message = "${campo.total-pedido.obrigatorio}")
    private BigDecimal total;

    @NotEmptyList(message = "${campo.items-pedido.obrigatorio}")
    private List<ItemPedidoDTO> itensPedido = Arrays.asList();

}
