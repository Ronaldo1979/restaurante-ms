package br.com.cognitivebrasil.model.mongo;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ItemBar")
public class ItemBar implements Serializable{

	private static final long serialVersionUID = -1028915367261109072L;
	
	@Id
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
