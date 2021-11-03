package br.com.cognitivebrasil.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.cognitivebrasil.model.mongo.ItemBar;

public interface ItemBarRepository extends MongoRepository<ItemBar, Long>{

}
