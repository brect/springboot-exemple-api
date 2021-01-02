package com.blimas.vendas.service;

import com.blimas.vendas.api.dto.PedidoDTO;
import com.blimas.vendas.domain.entity.Pedido;
import com.blimas.vendas.domain.entity.enums.StatusPedido;
import javassist.NotFoundException;

import java.util.Optional;

public interface PedidoService {

    Pedido save(PedidoDTO pedidoDTO);
    Optional<Pedido> getCompleteOrder(Integer id);
    void updateOrderStatus(Integer id, StatusPedido statusPedido) throws NotFoundException;

}
