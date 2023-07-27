package com.nttdata.bootcamp.debitcardservice.service;

import com.nttdata.bootcamp.debitcardservice.documents.DebitCard;

import reactor.core.publisher.Mono;

public interface DebitCardService {
	public Mono<DebitCard> saveDebitCard(DebitCard debitCard);
	public Mono<DebitCard> saveWithAllAccounts(DebitCard debitCard); 
}
