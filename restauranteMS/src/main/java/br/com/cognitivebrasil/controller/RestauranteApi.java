package br.com.cognitivebrasil.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.cognitivebrasil.model.mongo.ItemBar;
import br.com.cognitivebrasil.model.mongo.ItemCozinha;
import br.com.cognitivebrasil.model.mysql.Pedido;
import br.com.cognitivebrasil.service.mongo.RestauranteServiceMongo;
import br.com.cognitivebrasil.service.mysql.RestauranteServiceMySql;
import br.com.cognitivebrasil.service.rabbitmq.RabbitMQSender;

@RestController
@RequestMapping(value = "/restaurante/")
public class RestauranteApi {
	
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
	
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	@Autowired
	private RestauranteServiceMongo serviceMongo;
	
	@Autowired
	private RestauranteServiceMySql serviceMySql;
	
	
	@PostMapping(value = "pedido")
	public String pedido(@RequestBody String obj) {
		
		System.out.println("************************************");
		System.out.println("Registrando Pedido");
		System.out.println("************************************");
		
		try {
			
			Gson gson = new Gson();
			JsonObject jsonObject = JsonParser.parseString(obj).getAsJsonObject();
			
			//Registra pedido na fila
			Pedido pedido = gson.fromJson(jsonObject.toString(),Pedido.class);
			
			if((pedido.getItensBar() == null || pedido.getItensBar().isEmpty()) && (pedido.getItensCozinha() == null || pedido.getItensCozinha().isEmpty()))
				return "Adicione pelo menos 1 item ao pedido";
			
			serviceMySql.registraOrder(pedido);
			
			//Registra BarItens MongoBD
			if((pedido.getItensBar() != null) && (!pedido.getItensBar().isEmpty())) {
				
				List<ItemBar> itensBarMongo = convertListaItensBarToMongo(pedido.getItensBar());
				serviceMongo.registraPedidoBar(itensBarMongo);
				
				//Registra BarItens na fila
				rabbitMQSender.registraPedidoBar(orderBar, itensBarMongo);
			}
			
			if((pedido.getItensCozinha() != null) && (!pedido.getItensCozinha().isEmpty())) {
				
				//Registra ItensCozinha MongoDB
				List<ItemCozinha> itnensCozinhaMongo = convertListaItensCozinhaToMongo(pedido.getItensCozinha());
				serviceMongo.registraPedidoCozinha(itnensCozinhaMongo);
				
				//Registra ItensCozinha na fila
				rabbitMQSender.registraPedidoCozinha(orderKitchen, itnensCozinhaMongo);
			}
			
			cacheManager.getCache("listaPedidos").clear();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Pedido registrado com Sucesso!");
		return "Pedido registrado com Sucesso!";
	}
	
	@GetMapping(value = "pedido")
	@Cacheable(value = "listaPedidos")
	public List<Pedido> pedido(){
		
		System.out.println("************************************");
		System.out.println("Listando Pedidos");
		System.out.println("************************************");
		
		List<Pedido> pedidos = null;
		try {
			
			pedidos = serviceMySql.listaPedidos();
			
		    for(Pedido p : pedidos) {
		    	System.out.println("Pedido n° " + p.getId() + " Mesa: " + p.getMesa() + " StatusBar: " + p.getStatusBar() + " | StatusCozinha: " + p.getStatusKitchen());
		    }
			
		}catch (Exception e) {
			e.printStackTrace();
			return pedidos;
		}
		return pedidos;
	}
	
	@PatchMapping(value = "bar-orders/{pedidoId}")
	public Pedido atualizaPedidoBar(@PathVariable Long pedidoId) {
		
		System.out.println("************************************");
		System.out.println("Atualizando Status Bar");
		System.out.println("************************************");
		
		Pedido pedido = null;
		
		try {
			pedido = serviceMySql.buscaPedidoId(pedidoId);
			
			if(pedido != null) {
				pedido = serviceMySql.atualizaStatusBarOrder(pedido);
				//Registra notificação pedido bar
				rabbitMQSender.notificacaoPedidoBar(barOrder, pedido);
				
				cacheManager.getCache("listaPedidos").clear();
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pedido;
	}
	
	@PatchMapping(value = "kitchen-orders/{pedidoId}")
	public Pedido atualizaPedidoCozinha(@PathVariable Long pedidoId) {
		
		System.out.println("************************************");
		System.out.println("Atualizando Status Cozinha");
		System.out.println("************************************");
		
		Pedido pedido = null;
		
		try {
			pedido = serviceMySql.buscaPedidoId(pedidoId);
			
			if(pedido != null) {
				pedido = serviceMySql.atualizaStatusCozinhaOrder(pedido);
				//Registra notificação pedido cozinha
				rabbitMQSender.notificacaoPedidoCozinha(kitchenOrder, pedido);
				
				cacheManager.getCache("listaPedidos").clear();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pedido;
	}
	
	private List<ItemBar> convertListaItensBarToMongo(List<br.com.cognitivebrasil.model.mysql.ItemBar> itensBar) throws Exception{
		
		List<ItemBar> listaItensMongo = new ArrayList<ItemBar>();
		ItemBar itemBarMongo = null;
		
		for(br.com.cognitivebrasil.model.mysql.ItemBar item : itensBar) {
			itemBarMongo = serviceMongo.convertItemBarToMongo(item);
			listaItensMongo.add(itemBarMongo);
		}
		
		return listaItensMongo;
	}
	
	private List<ItemCozinha> convertListaItensCozinhaToMongo(List<br.com.cognitivebrasil.model.mysql.ItemCozinha> itensCozinha) throws Exception{
		
		List<ItemCozinha> listaItensCozinhaMongo = new ArrayList<ItemCozinha>();
		ItemCozinha itemCozinhaMongo = null;
		
		for(br.com.cognitivebrasil.model.mysql.ItemCozinha itemCozinha : itensCozinha) {
			itemCozinhaMongo = serviceMongo.convertItemCozinhaToMongo(itemCozinha);
			listaItensCozinhaMongo.add(itemCozinhaMongo);
		}
		
		return listaItensCozinhaMongo;
	}
	
	@PostMapping("atualizaCacheRedis")
	@CacheEvict(value = "listaPedidos", allEntries = true)
	public String atualizaCacheRedis() {

		System.out.println("************************************");
		System.out.println("Limpando Cache Redis");
		System.out.println("************************************");
		
		return "Cache cancelado!";
	}

}