package com.blimas.vendas.service.impl;

import com.blimas.vendas.api.dto.ItemPedidoDTO;
import com.blimas.vendas.api.dto.PedidoDTO;
import com.blimas.vendas.domain.entity.Cliente;
import com.blimas.vendas.domain.entity.ItemPedido;
import com.blimas.vendas.domain.entity.Pedido;
import com.blimas.vendas.domain.entity.Produto;
import com.blimas.vendas.domain.entity.enums.StatusPedido;
import com.blimas.vendas.domain.repository.ClienteRepository;
import com.blimas.vendas.domain.repository.ItemPedidoRepository;
import com.blimas.vendas.domain.repository.PedidoRepository;
import com.blimas.vendas.domain.repository.ProdutoRepository;
import com.blimas.vendas.exception.BussinesException;
import com.blimas.vendas.exception.OrderNotFoundException;
import com.blimas.vendas.service.PedidoService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Transactional
    @Override
    public Pedido save(PedidoDTO pedidoDTO) {
        Integer idCliente = pedidoDTO.getCliente();
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new BussinesException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatusPedido(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItens(pedido, pedidoDTO.getItensPedido());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemPedidos);

        pedido.setItemPedidos(itemPedidos);

        return pedido;
    }

    @Override
    public Optional<Pedido> getCompleteOrder(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    public void updateOrderStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setStatusPedido(statusPedido);
                    return pedidoRepository.save(pedido);
                }).orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado"));
    }


    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itensPedido) {
        if (itensPedido.isEmpty()) {
            throw new BussinesException("Não é possível realizar um pedido sem itens.");
        }
        return itensPedido.stream()
                .map(request -> {
                    Integer idProduto = request.getProduto();
                    Produto produto = produtoRepository.findById(idProduto)
                            .orElseThrow(() -> new BussinesException("Código do produto inválido: " + idProduto));
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(request.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
