package br.com.cognitivebrasil.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.google.gson.Gson;

import br.com.cognitivebrasil.model.mysql.ItemBar;
import br.com.cognitivebrasil.model.mysql.ItemCozinha;
import br.com.cognitivebrasil.model.mysql.Pedido;

@Configuration
public class RabbitMQConection {

	//***************************************************************************************
	//RabbitMQ
	//***************************************************************************************
	private static final String NOME_EXCHANGE = "amq.direct";
	private AmqpAdmin amqpAdmin;
	
	@Value("${ms.rabbitmq.order.kitchen}")
	String orderKitchen;
	
	@Value("${ms.rabbitmq.order.bar}")
	String orderBar;
	
	@Value("${ms.rabbitmq.kitchen.order}")
	String kitchenOrder;
	
	@Value("${ms.rabbitmq.bar.order}")
	String barOrder;
	
	@Value("${ms.rabbitmq.notification}")
	String notification;
	
	public RabbitMQConection(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	private Queue fila(String nomeFila) {
		return new Queue(nomeFila,true, false, false);
	}
	
	private DirectExchange trocaDireta() {
		return new DirectExchange(NOME_EXCHANGE);
	}
	
	private Binding relacionamento(Queue fila, DirectExchange troca) {
		return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
	}
	
	@PostConstruct
	private void criaFilas() {
		
		Queue filaOrderKitchen = this.fila(orderKitchen);
		Queue filaOrderBar = this.fila(orderBar);
		Queue filaKitchenOrder = this.fila(kitchenOrder);
		Queue filaBarOrder = this.fila(barOrder);
		Queue filaNotification = this.fila(notification);
		
		DirectExchange exchange = this.trocaDireta();
		
		Binding ligacaoOrderKitchen =  this.relacionamento(filaOrderKitchen, exchange);
		Binding ligacaoOrderBar =  this.relacionamento(filaOrderBar, exchange);
		Binding ligacaoKitchenOrder =  this.relacionamento(filaKitchenOrder, exchange);
		Binding ligacaoBarOrder =  this.relacionamento(filaBarOrder, exchange);
		Binding ligacaoNotification =  this.relacionamento(filaNotification, exchange);
		
		this.amqpAdmin.declareQueue(filaOrderKitchen);
		this.amqpAdmin.declareQueue(filaOrderBar);
		this.amqpAdmin.declareQueue(filaKitchenOrder);
		this.amqpAdmin.declareQueue(filaBarOrder);
		this.amqpAdmin.declareQueue(filaNotification);
		
		this.amqpAdmin.declareExchange(exchange);
		
		this.amqpAdmin.declareBinding(ligacaoOrderKitchen);
		this.amqpAdmin.declareBinding(ligacaoOrderBar);
		this.amqpAdmin.declareBinding(ligacaoKitchenOrder);
		this.amqpAdmin.declareBinding(ligacaoBarOrder);
		this.amqpAdmin.declareBinding(ligacaoNotification);
		
		geraPedidoTeste();
				
	}
	
	//***************************************************************************************
	//Gera pedido teste
	//***************************************************************************************
	
	private void geraPedidoTeste() {
		
		List<ItemCozinha> itensCozinha = new ArrayList<>();
		List<ItemBar> itensBar = new ArrayList<>();
		Pedido pedido = new Pedido();
		ItemCozinha itemCozinha = null;
		ItemBar itemBar = null;
		
		pedido.setId(1L);
		pedido.setGarcon("Orlando");
		pedido.setMesa(22);
		
		itemCozinha = new ItemCozinha();
		itemCozinha.setId(1L);
		itemCozinha.setNome("Churrasco Misto");
		itemCozinha.setObservacao("Cliente pediu sem farofa");
		itemCozinha.setPedidoId(1L);
		itemCozinha.setQuantidade(2);
		
		itensCozinha.add(itemCozinha);
		
		
		itemBar = new ItemBar();
		itemBar.setId(1L);
		itemBar.setNome("Cerveja Heineken Long Neck");
		itemBar.setObservacao("");
		itemBar.setPedidoId(1L);
		itemBar.setQuantidade(4);
		
		itensBar.add(itemBar);
		
		itemBar = new ItemBar();
		itemBar.setId(2L);
		itemBar.setNome("Coca-Cola Lata");
		itemBar.setObservacao("");
		itemBar.setPedidoId(1L);
		itemBar.setQuantidade(2);
		
		itensBar.add(itemBar);
		
		pedido.setItensBar(itensBar);
		pedido.setItensCozinha(itensCozinha);
		pedido.setStatusBar(Pedido.STATUS_BAR_PREPARING);
		pedido.setStatusKitchen(Pedido.STATUS_KITCHEN_PREPARING);
		
		Gson gson = new Gson();
		String pedidoJson = gson.toJson(pedido);
		
		System.out.println(pedidoJson);
	}

}
