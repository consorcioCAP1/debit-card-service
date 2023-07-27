package com.nttdata.bootcamp.debitcardservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.debitcardservice.documents.DebitCard;
import reactor.core.publisher.Mono;


public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String>{

	Mono<DebitCard> findByNumberCard(String numberCard);
}
