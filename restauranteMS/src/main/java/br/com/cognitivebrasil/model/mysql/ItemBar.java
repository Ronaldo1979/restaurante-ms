package br.com.cognitivebrasil.model.mysql;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_bar")
public class ItemBar implements Serializable{

	private static final long serialVersionUID = 4862793788070903735L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long pedidoId;
	private String nome;
	private Integer quantidade;
	private String observacao;
	
	public ItemBar() {
		super();
	}

	public ItemBar(Long id, Long pedidoId, String nome, Integer quantidade, String observacao) {
		super();
		this.id = id;
		this.pedidoId = pedidoId;
		this.nome = nome;
		this.quantidade = quantidade;
		this.observacao = observacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome, observacao, pedidoId, quantidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemBar other = (ItemBar) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& Objects.equals(observacao, other.observacao) && Objects.equals(pedidoId, other.pedidoId)
				&& Objects.equals(quantidade, other.quantidade);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemBar [id=");
		builder.append(id);
		builder.append(", pedidoId=");
		builder.append(pedidoId);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", quantidade=");
		builder.append(quantidade);
		builder.append(", observacao=");
		builder.append(observacao);
		builder.append("]");
		return builder.toString();
	}
	
}
