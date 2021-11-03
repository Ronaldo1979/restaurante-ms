package br.com.cognitivebrasil.model.mysql;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "pedido")
public class Pedido implements Serializable{

	private static final long serialVersionUID = 4054089116853434184L;
	
	public static final String STATUS_BAR_PREPARING = "PREPARING";
	public static final String STATUS_BAR_DONE = "DONE";
	
	public static final String STATUS_KITCHEN_PREPARING = "PREPARING";
	public static final String STATUS_KITCHEN_DONE = "DONE";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String garcon;
	private Integer mesa;
	private String statusBar;
	private String statusKitchen;
	
	@Transient
	private List<ItemCozinha> itensCozinha;
	@Transient
	private List<ItemBar> itensBar;
	
	public Pedido() {
		super();
	}

	public Pedido(Long id, String garcon, Integer mesa, List<ItemCozinha> itensCozinha, List<ItemBar> itensBar,
			String statusBar, String statusKitchen) {
		super();
		this.id = id;
		this.garcon = garcon;
		this.mesa = mesa;
		this.itensCozinha = itensCozinha;
		this.itensBar = itensBar;
		this.statusBar = statusBar;
		this.statusKitchen = statusKitchen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGarcon() {
		return garcon;
	}

	public void setGarcon(String garcon) {
		this.garcon = garcon;
	}

	public Integer getMesa() {
		return mesa;
	}

	public void setMesa(Integer mesa) {
		this.mesa = mesa;
	}

	public List<ItemCozinha> getItensCozinha() {
		return itensCozinha;
	}

	public void setItensCozinha(List<ItemCozinha> itensCozinha) {
		this.itensCozinha = itensCozinha;
	}

	public List<ItemBar> getItensBar() {
		return itensBar;
	}

	public void setItensBar(List<ItemBar> itensBar) {
		this.itensBar = itensBar;
	}

	public String getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(String statusBar) {
		this.statusBar = statusBar;
	}

	public String getStatusKitchen() {
		return statusKitchen;
	}

	public void setStatusKitchen(String statusKitchen) {
		this.statusKitchen = statusKitchen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(garcon, id, itensBar, itensCozinha, mesa, statusBar, statusKitchen);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		return Objects.equals(garcon, other.garcon) && Objects.equals(id, other.id)
				&& Objects.equals(itensBar, other.itensBar) && Objects.equals(itensCozinha, other.itensCozinha)
				&& Objects.equals(mesa, other.mesa) && Objects.equals(statusBar, other.statusBar)
				&& Objects.equals(statusKitchen, other.statusKitchen);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pedido [id=");
		builder.append(id);
		builder.append(", garcon=");
		builder.append(garcon);
		builder.append(", mesa=");
		builder.append(mesa);
		builder.append(", itensCozinha=");
		builder.append(itensCozinha);
		builder.append(", itensBar=");
		builder.append(itensBar);
		builder.append(", statusBar=");
		builder.append(statusBar);
		builder.append(", statusKitchen=");
		builder.append(statusKitchen);
		builder.append("]");
		return builder.toString();
	}

}
