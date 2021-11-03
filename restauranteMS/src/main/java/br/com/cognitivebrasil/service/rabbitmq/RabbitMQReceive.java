package br.com.cognitivebrasil.service.rabbitmq;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.cognitivebrasil.mail.MailService;
import br.com.cognitivebrasil.mail.dto.MailDTO;
import br.com.cognitivebrasil.model.mongo.ItemBar;
import br.com.cognitivebrasil.model.mongo.ItemCozinha;
import br.com.cognitivebrasil.model.mongo.NotificacaoEmail;
import br.com.cognitivebrasil.model.mysql.Pedido;
import br.com.cognitivebrasil.service.mongo.RestauranteServiceMongo;

@Service
public class RabbitMQReceive {
	
	@Value("${ms.rabbitmq.notification}")
	String notification;
	
	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private RestauranteServiceMongo restauranteServiceMongo;

	@RabbitListener(queues = "${ms.rabbitmq.order.kitchen}")
	private void consumidorPedidoCozinha(List<ItemCozinha> itnensCozinhaMongo) {
		System.out.println("*************************************************************************************");
		System.out.println("Consumindo pedido cozinha..."); 
		
		for(ItemCozinha itemCozinha : itnensCozinhaMongo) {
			System.out.println("Item: " + itemCozinha.getNome());
		}
		System.out.println("*************************************************************************************");
	}
	
	@RabbitListener(queues = "${ms.rabbitmq.order.bar}")
	private void consumidorPedidoBar(List<ItemBar> itnensBarMongo) {
		System.out.println("*************************************************************************************");
		System.out.println("Consumindo pedido bar..."); 
		
		for(ItemBar itemBar : itnensBarMongo) {
			System.out.println("Item: " + itemBar.getNome());
		}
		System.out.println("*************************************************************************************");
	}
	
	@RabbitListener(queues = "${ms.rabbitmq.kitchen.order}")
	private void consumidorNotificacaoCozinha(Pedido pedido) {
		System.out.println("*************************************************************************************");
		System.out.println("Consumindo notificação de pedido cozinha..."); 
		System.out.println("Pedido n° " + pedido.getId() + " Mesa:  " + pedido.getMesa() + " Status: " + pedido.getStatusKitchen());
		
		notificaPedidoPronto(pedido);
		
		System.out.println("*************************************************************************************");
	}
	
	@RabbitListener(queues = "${ms.rabbitmq.bar.order}")
	private void consumidorNotificacaoBar(Pedido pedido) {
		System.out.println("*************************************************************************************");
		System.out.println("Consumindo notificação de pedido bar..."); 
		System.out.println("Pedido n° " + pedido.getId() + " Mesa: " + pedido.getMesa() + " Status " + pedido.getStatusBar());
		
		notificaPedidoPronto(pedido);
		
		System.out.println("*************************************************************************************");
	}
	
	@RabbitListener(queues = "${ms.rabbitmq.notification}")
	private void consumidorNotificacaoPedidoPronto(Pedido pedido) {
		System.out.println("*************************************************************************************");
		System.out.println("Consumindo notificação de pedido pronto..."); 
		System.out.println("Pedido n° " + pedido.getId() + " Mesa: " + pedido.getMesa() + " Status " + pedido.getStatusBar());
		
		MailDTO mailDTO = new MailDTO();
		mailDTO.setContent("Pedido n° " + pedido.getId() + ", mesa " + pedido.getMesa() + " está pronto!");
		mailDTO.setSubject("Notificação de pedido");
		mailDTO.setTo("ronaldo.fjv@gmail.com");
		
		this.registraNotificacaoEmail(pedido, mailDTO);
		this.enviaEmail(mailDTO);
		
		System.out.println("*************************************************************************************");
	}
	
	private void notificaPedidoPronto(Pedido pedido) {
		
		if(pedido.getStatusKitchen().equalsIgnoreCase(Pedido.STATUS_KITCHEN_DONE) && pedido.getStatusBar().equalsIgnoreCase(Pedido.STATUS_BAR_DONE)) {
			
			System.out.println("*************************************************************************************");
			System.out.println("Registrando notificação de pedido pronto"); 
			System.out.println("Pedido n° " + pedido.getId() + " mesa " + pedido.getMesa() + " pedido solicitado ao garson " + pedido.getGarcon()); 
			System.out.println("*************************************************************************************");
			
			rabbitMQSender.notificacaoPedidoPronto(notification, pedido);
			
		}
		
	}
	
	
	private void registraNotificacaoEmail(Pedido pedido, MailDTO mailDTO) {
		
		System.out.println("*************************************************************************************");
		System.out.println("Registrando notificação email"); 
		System.out.println("*************************************************************************************");
		
		try {
			
			NotificacaoEmail notificacaoEmail = new NotificacaoEmail();
			
			notificacaoEmail.setId(pedido.getId());
			notificacaoEmail.setData(new Date());
			notificacaoEmail.setDestinatario(mailDTO.getTo());
			notificacaoEmail.setRemetente("noreply@rndti.com");
			notificacaoEmail.setMenssage("O pedido n° " + pedido.getId() + " mesa " + pedido.getMesa() + " está pronto.");
			
			restauranteServiceMongo.registraNotificacaoEmail(notificacaoEmail);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void enviaEmail(MailDTO mailDTO) {
		
		System.out.println("*************************************************************************************");
		System.out.println("Enviando email de notificação"); 
		System.out.println("*************************************************************************************");
		
		try {
			mailService.sendMail(mailDTO);
			System.out.println("Email enviado com sucesso!");
			System.out.println("Destinatário: " + mailDTO.getTo());
			System.out.println("MSG: " + mailDTO.getContent());
		}catch (Exception e) {
			System.out.println("Ajuste as configurações do email e senha em application.properties.");
		}
	}
	
	
}
