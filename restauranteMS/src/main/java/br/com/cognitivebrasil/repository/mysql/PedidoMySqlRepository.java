package br.com.cognitivebrasil.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cognitivebrasil.model.mysql.Pedido;

@Repository
public interface PedidoMySqlRepository extends JpaRepository<Pedido, Long>{

}
