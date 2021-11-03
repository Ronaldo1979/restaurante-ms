package br.com.cognitivebrasil.service.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cognitivebrasil.model.mongo.ItemBar;
import br.com.cognitivebrasil.model.mongo.ItemCozinha;
import br.com.cognitivebrasil.model.mongo.NotificacaoEmail;
import br.com.cognitivebrasil.repository.mongo.ItemBarRepository;
import br.com.cognitivebrasil.repository.mongo.ItemCozinhaRepository;
import br.com.cognitivebrasil.repository.mongo.NotificacaoEmailRepository;

@Service
public class RestauranteServiceMongo {

	@Autowired
	private ItemBarRepository itemBarRepository;
	@Autowired
	private ItemCozinhaRepository itemCozinhaRepository;
	@Autowired
	private NotificacaoEmailRepository notificacaoEmailRepository;
	
	@SuppressWarnings("unused")
	public Boolean registraPedidoBar(List<ItemBar> itensBar) {
		try {
			for(ItemBar itemBar : itensBar) {
				itemBarRepository.save(itemBar);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	public Boolean registraPedidoCozinha(List<ItemCozinha> itensCozinha) {
		try {
			for(ItemCozinha itemCozinha: itensCozinha) {
				itemCozinhaRepository.save(itemCozinha);
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean registraNotificacaoEmail(NotificacaoEmail email) {
		try {
			notificacaoEmailRepository.save(email);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public ItemBar convertItemBarToMongo(br.com.cognitivebrasil.model.mysql.ItemBar itemBar) {
		
		ItemBar itemBarMongo = new ItemBar();
		
		try {
			
			if(itemBar.getId() != null)
				itemBarMongo.setId(itemBar.getId());
			if(itemBar.getNome() != null)
				itemBarMongo.setNome(itemBar.getNome());
			if(itemBar.getObservacao() != null)
				itemBarMongo.setObservacao(itemBar.getObservacao());
			if(itemBar.getPedidoId() != null)
				itemBarMongo.setPedidoId(itemBar.getPedidoId());
			if(itemBar.getQuantidade() != null)
				itemBarMongo.setQuantidade(itemBar.getQuantidade());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemBarMongo;
	}
	
	public ItemCozinha convertItemCozinhaToMongo(br.com.cognitivebrasil.model.mysql.ItemCozinha itemCozinha) {
		
		ItemCozinha itemCozinhaMongo = new ItemCozinha();
		
		try {
			
			if(itemCozinha.getId() != null)
				itemCozinhaMongo.setId(itemCozinha.getId());
			if(itemCozinha.getNome() != null)
				itemCozinhaMongo.setNome(itemCozinha.getNome());
			if(itemCozinha.getObservacao() != null)
				itemCozinhaMongo.setObservacao(itemCozinha.getObservacao());
			if(itemCozinha.getPedidoId() != null)
				itemCozinhaMongo.setPedidoId(itemCozinha.getPedidoId());
			if(itemCozinha.getQuantidade() != null)
				itemCozinhaMongo.setQuantidade(itemCozinha.getQuantidade());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return itemCozinhaMongo;
	}
}
