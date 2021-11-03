package br.com.cognitivebrasil.service.rabbitmq;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cognitivebrasil.model.mongo.ItemBar;
import br.com.cognitivebrasil.model.mongo.ItemCozinha;
import br.com.cognitivebrasil.model.mysql.Pedido;

@Service
public class RabbitMQSender {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	public void registraPedidoCozinha(String fila,  List<ItemCozinha> itnensCozinhaMongo) {
		this.rabbitTemplate.convertAndSend(fila, itnensCozinhaMongo);
	}
	
	public void registraPedidoBar(String fila, List<ItemBar> itensBarMongo) {
		this.rabbitTemplate.convertAndSend(fila, itensBarMongo);
	}
	
	public void notificacaoPedidoCozinha(String fila,  Pedido pedido) {
		this.rabbitTemplate.convertAndSend(fila, pedido);
	}
	
	public void notificacaoPedidoBar(String fila,  Pedido pedido) {
		this.rabbitTemplate.convertAndSend(fila, pedido);
	}
	
	public void notificacaoPedidoPronto(String fila, Pedido pedido) {
		this.rabbitTemplate.convertAndSend(fila, pedido);
	}
}