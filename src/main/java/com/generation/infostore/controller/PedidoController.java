package com.generation.infostore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.infostore.model.Pedido;
import com.generation.infostore.repository.ClienteRepository;
import com.generation.infostore.repository.PedidoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoController {
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping
	public ResponseEntity<List<Pedido>> getAll() {

		return ResponseEntity.ok(pedidoRepository.findAll());
		// Retorna todas os pedidos cadastradas no banco

	}

	// Busca um pedido pelo id informado na URL Se encontrar o produto retorna 200
	// OK com o objeto no corpo Se não encontrar, retorna 404 Not Found
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> getById(@PathVariable Long id) {

		return pedidoRepository.findById(id)

				.map(resposta -> ResponseEntity.ok(resposta))

				.orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/produto/{produto}")
	public ResponseEntity<List<Pedido>> getAllByProduto(@PathVariable String produto) {

		// Busca todas os produtos com o nome que contenha o texto informado,
		// sem diferenciar letras maiúsculas e minúsculas
		return ResponseEntity.ok(pedidoRepository.findAllByProdutoContainingIgnoreCase(produto));

	}

	// Verifica o cliente associado ao produto buscado existe no banco
	@PostMapping
	public ResponseEntity<Pedido> post(@Valid @RequestBody Pedido pedido) {

		if (clienteRepository.existsById(pedido.getCliente().getId())) {

			// Garante que a operação será tratada como cadastro de um novo pedido
			// O id será gerado automaticamente pelo banco
			pedido.setId(null);

			// Salva o novo pedido e retorna 201 CREATED
			return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRepository.save(pedido));

		}

		// Se o cliente informado não existir, retorna erro 400 Bad Request
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cliente não existe!", null);
	}

	// Atualiza um pedido já existente

	@PutMapping
	public ResponseEntity<Pedido> put(@Valid @RequestBody Pedido pedido) {
		// Primeiro verifica se o pedido existe
		if (pedidoRepository.existsById(pedido.getId())) {

			// Depois verifica se o cliente informado tambem existe
			if (clienteRepository.existsById(pedido.getCliente().getId())) {

				// Se ambos existirem, salva a atualização e retorna 200 OK
				return ResponseEntity.ok(pedidoRepository.save(pedido));

			}
			// Se o cliente nao existir, retorna erro 400 Bad Request
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cliente não existe!", null);
		}
		// Se o pedido nao existir, retorna 404 Not Found
		return ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		// Busca o pedido pelo id antes de excluir
		Optional<Pedido> pedido = pedidoRepository.findById(id);

		// Se não encontrar, retorna 404 Not Found
		if (pedido.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Se encontrar, exclui o pedido
		pedidoRepository.deleteById(id);

	}
}
