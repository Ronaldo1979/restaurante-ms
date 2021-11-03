package br.com.cognitivebrasil.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.cognitivebrasil.model.mongo.NotificacaoEmail;

public interface NotificacaoEmailRepository extends MongoRepository<NotificacaoEmail, Long>{

}
