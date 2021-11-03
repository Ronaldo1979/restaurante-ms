package br.com.cognitivebrasil.service.mysql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cognitivebrasil.model.mysql.ItemBar;
import br.com.cognitivebrasil.model.mysql.ItemCozinha;
import br.com.cognitivebrasil.model.mysql.Pedido;
import br.com.cognitivebrasil.repository.mysql.ItemBarMySqlRepository;
import br.com.cognitivebrasil.repository.mysql.ItemCozinhaMySqlRepository;
import br.com.cognitivebrasil.repository.mysql.PedidoMySqlRepository;

@Service
public class RestauranteServiceMySql {

	@Autowired
	private PedidoMySqlRepository pedidoRepository;
	@Autowired
	private ItemBarMySqlRepository itemBarRepository;
	@Autowired
	private ItemCozinhaMySqlRepository itemCozinhaRepository;
	
	
	public Pedido buscaPedidoId(Long pedidoId) {
		
		Pedido pedido = null;
		try {
			pedido = pedidoRepository.findById(pedidoId).orElse(null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pedido;
	}
	
	public Boolean registraOrder(Pedido pedido) {
		
		try {
			
			if(pedido.getItensBar() == null || pedido.getItensBar().isEmpty())
				pedido.setStatusBar(Pedido.STATUS_BAR_DONE);
			
			if(pedido.getItensCozinha() == null || pedido.getItensCozinha().isEmpty())
				pedido.setStatusKitchen(Pedido.STATUS_KITCHEN_DONE);
			
			Pedido ped = pedidoRepository.save(pedido);
			if(ped != null) {
				
				if(pedido.getStatusBar().equalsIgnoreCase(Pedido.STATUS_BAR_PREPARING))
					registraPedidoBar(pedido);
				
				if(pedido.getStatusKitchen().equalsIgnoreCase(Pedido.STATUS_KITCHEN_PREPARING))
					registraPedidoCozinha(pedido);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public Pedido atualizaStatusBarOrder(Pedido pedido) {
		
		Pedido ped = null;
		try {
			pedido.setStatusBar(Pedido.STATUS_BAR_DONE);
			ped = pedidoRepository.saveAndFlush(pedido);
			ped.setItensBar(itemBarRepository.findByPedidoId(pedido.getId()));
			ped.setItensCozinha(itemCozinhaRepository.findByPedidoId(pedido.getId()));
			return ped;
		}catch (Exception e) {
			e.printStackTrace();
			return ped;
		}
		
	}
	
	public Pedido atualizaStatusCozinhaOrder(Pedido pedido) {
		
		Pedido ped = null;
		try {
			pedido.setStatusKitchen(Pedido.STATUS_KITCHEN_DONE);
			ped = pedidoRepository.saveAndFlush(pedido);
			ped.setItensBar(itemBarRepository.findByPedidoId(pedido.getId()));
			ped.setItensCozinha(itemCozinhaRepository.findByPedidoId(pedido.getId()));
			return ped;
		}catch (Exception e) {
			e.printStackTrace();
			return ped;
		}
	}
	
	public Boolean registraPedidoBar(Pedido pedido) {
		try {
			for(ItemBar itemBar : pedido.getItensBar()) {
				itemBar.setPedidoId(pedido.getId());
				itemBarRepository.save(itemBar);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean registraPedidoCozinha(Pedido pedido) {
		try {
			for(ItemCozinha itemCozinha: pedido.getItensCozinha()) {
				itemCozinha.setPedidoId(pedido.getId());
				itemCozinhaRepository.save(itemCozinha);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Pedido> listaPedidos() {
		
		List<Pedido> pedidos = null;
		List<Pedido> pedidosItens = new ArrayList<Pedido>();
		
		try {
			pedidos = pedidoRepository.findAll();
			if(pedidos != null) {
				
				for(Pedido p : pedidos) {
					p.setItensBar(itemBarRepository.findByPedidoId(p.getId()));
					p.setItensCozinha(itemCozinhaRepository.findByPedidoId(p.getId()));
					pedidosItens.add(p);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pedidosItens;
	}
	
}
