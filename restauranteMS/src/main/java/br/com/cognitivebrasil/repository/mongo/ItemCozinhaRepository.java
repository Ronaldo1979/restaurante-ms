package br.com.cognitivebrasil.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.cognitivebrasil.model.mongo.ItemCozinha;

public interface ItemCozinhaRepository extends MongoRepository<ItemCozinha, Long>{

}
