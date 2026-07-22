package com.generation.infostore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.infostore.model.Cliente;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {

	public List<Cliente> findAllByNomeContainingIgnoreCase(String nome);
}
