package com.generation.infostore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

//cria a entidade pedido e a tabela pedidos
@Entity
@Table(name = "tb_pedidos")
public class Pedido {

	// Cria o ID Primary Key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// cria o atributo produto
	@NotBlank(message = "O atributo nome é obrigatório!")
	@Size(min = 2, max = 100, message = "O atributo nome deve conter no mínimo 2 e no máximo 100 caracteres!")
	@Column(length = 100)
	private String produto;

	// cria o atributo descricao
	@NotBlank(message = "O atributo descrição é obrigatório!")
	@Size(min = 5, max = 500, message = "O atributo descrição deve conter no mínimo 5 e no máximo 500 caracteres!")
	@Column(length = 500)
	private String descricao;

	// cria o atributo valor com BigDecimal para valor monetário
	@NotNull(message = "Atributo valor é obrigatório!")
	@Positive(message = "O valor deve ser positivo")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;

	@UpdateTimestamp // Cria e Atualiza a data/hora no momento de criar ou editar produtos
	private LocalDateTime data;

	// cria a chave estrangeira -> Varios Pedidos para 1 cliente
	@ManyToOne
	@JsonIgnoreProperties("pedidos")
	private Cliente cliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
