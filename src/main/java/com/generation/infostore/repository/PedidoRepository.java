package com.generation.infostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.infostore.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	public List<Pedido> findAllByProdutoContainingIgnoreCase(String produto);

}
