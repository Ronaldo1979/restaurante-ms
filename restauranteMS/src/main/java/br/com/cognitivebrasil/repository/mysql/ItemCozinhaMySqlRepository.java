package br.com.cognitivebrasil.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cognitivebrasil.model.mysql.ItemCozinha;

@Repository
public interface ItemCozinhaMySqlRepository extends JpaRepository<ItemCozinha, Long>{

	List<ItemCozinha> findByPedidoId(Long pedidoId);
}
