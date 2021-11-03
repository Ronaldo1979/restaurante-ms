package br.com.cognitivebrasil.model.mongo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NotificacaoEmail")
public class NotificacaoEmail implements Serializable{

	private static final long serialVersionUID = 3440744711808384520L;
	
	@Id
	private Long id;
	private String remetente;
	private String destinatario;
	private String menssage;
	private Date data;
	
	public NotificacaoEmail() {
		super();
	}

	public NotificacaoEmail(Long id, String remetente, String destinatario, String menssage, Date data) {
		super();
		this.id = id;
		this.remetente = remetente;
		this.destinatario = destinatario;
		this.menssage = menssage;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getMenssage() {
		return menssage;
	}

	public void setMenssage(String menssage) {
		this.menssage = menssage;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, destinatario, id, menssage, remetente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificacaoEmail other = (NotificacaoEmail) obj;
		return Objects.equals(data, other.data) && Objects.equals(destinatario, other.destinatario)
				&& Objects.equals(id, other.id) && Objects.equals(menssage, other.menssage)
				&& Objects.equals(remetente, other.remetente);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificacaoEmail [id=");
		builder.append(id);
		builder.append(", remetente=");
		builder.append(remetente);
		builder.append(", destinatario=");
		builder.append(destinatario);
		builder.append(", menssage=");
		builder.append(menssage);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
	
	

}
