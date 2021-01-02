package com.blimas.vendas.domain.repository;

import com.blimas.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeLike(String nome);

    boolean existsByNome(@Param("nome") String nome);

    @Query("select c from Cliente c left join fetch c.pedidos where c.id = : id")
    Cliente findClienteByPedidos(@Param("id") Integer id);

}
