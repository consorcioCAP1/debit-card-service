package com.nttdata.bootcamp.debitcardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.debitcardservice.documents.DebitCard;
import com.nttdata.bootcamp.debitcardservice.service.DebitCardService;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/")
public class DebitCardController {

	@Autowired
	private DebitCardService debitCardService;

	@PostMapping("/save")
	public Mono<DebitCard> saveDebitCard(@RequestBody DebitCard debitCard) {
	    return debitCardService.saveDebitCard(debitCard);
	}
	
	@PostMapping("/saveWithAllAccounts")
	public Mono<DebitCard> saveWithAllAccounts(@RequestBody DebitCard debitCard) {
	    return debitCardService.saveWithAllAccounts(debitCard);
	}
}
