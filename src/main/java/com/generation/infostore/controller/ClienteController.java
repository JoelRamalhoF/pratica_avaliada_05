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

import com.generation.infostore.model.Cliente;
import com.generation.infostore.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClienteController {

	// Injeta o repositório responsável pelas operações da entidade Cliente
	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping
	public ResponseEntity<List<Cliente>> getAll() {
		return ResponseEntity.ok(clienteRepository.findAll());

	}

	// Busca um cliente pelo id informado na URL
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> getById(@PathVariable Long id) {

		return clienteRepository.findById(id)
				// Se encontrar o cliente, retorna 200 OK com o objeto no corpo
				.map(resposta -> ResponseEntity.ok(resposta))

				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Busca todas cclientes com o nome informado e Ignora diferença entre letras
	// maiúsculas e minúsculas
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Cliente>> getAllByNome(@PathVariable String nome) {

		return ResponseEntity.ok(clienteRepository.findAllByNomeContainingIgnoreCase(nome));

	}

	// Garante que o id será gerado automaticamente pelo banco
	// Isso evita que o cadastro tente reutilizar um id já existente
	@PostMapping
	public ResponseEntity<Cliente> post(@Valid @RequestBody Cliente cliente) {

		cliente.setId(null);

		// Salva o novo cadastro de cliente e retorna 201 CREATED
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(cliente));
	}

	@PutMapping
	public ResponseEntity<Cliente> put(@Valid @RequestBody Cliente cliente) {

		// Busca o cliente pelo id antes de atualizar, se o id existir, salva a
		// atualização
		return clienteRepository.findById(cliente.getId())

				// Se encontrar, salva o cliente e retorna a resposta
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(clienteRepository.save(cliente)))

				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		// Busca o cliente id antes de excluir
		Optional<Cliente> cliente = clienteRepository.findById(id);

		// Se não encontrar, lança exceção com status 404 Not Found
		if (cliente.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Se encontrar, exclui a cateorigia do banco de dados
		clienteRepository.deleteById(id);
	}
}
