package com.blimas.vendas.domain.repository;

import com.blimas.vendas.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Set<Pedido> findByCliente(Integer id);

    @Query(" select p from Pedido p left join fetch p.itemPedidos where p.id = :id ")
    Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);

}
