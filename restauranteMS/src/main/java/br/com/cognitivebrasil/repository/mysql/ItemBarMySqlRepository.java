package br.com.cognitivebrasil.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cognitivebrasil.model.mysql.ItemBar;

@Repository
public interface ItemBarMySqlRepository extends JpaRepository<ItemBar, Long>{

	List<ItemBar> findByPedidoId(Long pedidoId);
}
