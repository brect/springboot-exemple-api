package com.blimas.vendas.api.controller;


import com.blimas.vendas.api.dto.AtualizaStatusPedidoDTO;
import com.blimas.vendas.api.dto.InformacaoItemPedidoDTO;
import com.blimas.vendas.api.dto.InformacoesPedidoDTO;
import com.blimas.vendas.api.dto.PedidoDTO;
import com.blimas.vendas.domain.entity.ItemPedido;
import com.blimas.vendas.domain.entity.Pedido;
import com.blimas.vendas.domain.entity.enums.StatusPedido;
import com.blimas.vendas.service.PedidoService;
import com.blimas.vendas.service.impl.PedidoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.DateFormatter;
import javax.validation.Valid;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos/")
public class PedidoController {

    private final PedidoServiceImpl service;

    public PedidoController(PedidoServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO request) {
        Pedido pedido = service.save(request);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidoDTO getOrderById(@PathVariable Integer id) {
        return service
                .getCompleteOrder(id)
                .map(pedido -> toInfoOrderDTO(pedido))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));

    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@PathVariable Integer id,
                            @RequestBody @Valid AtualizaStatusPedidoDTO request) {

        service.updateOrderStatus(id, StatusPedido.valueOf(request.getStatus()));

    }

    private InformacoesPedidoDTO toInfoOrderDTO(Pedido pedido) {
        return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatusPedido().name())
                .itens(toInfoItensOrderDTO(pedido.getItemPedidos()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> toInfoItensOrderDTO(List<ItemPedido> itemPedidos) {
        if (CollectionUtils.isEmpty(itemPedidos)) {
            return Collections.emptyList();
        }

        return itemPedidos
                .stream()
                .map(item -> InformacaoItemPedidoDTO.builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPrecoUnitario())
                        .quantidade(item.getQuantidade())
                        .build())
                .collect(Collectors.toList());
    }
}
