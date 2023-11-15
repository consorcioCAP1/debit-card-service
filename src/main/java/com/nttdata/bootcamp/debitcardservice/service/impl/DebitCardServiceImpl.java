package com.nttdata.bootcamp.debitcardservice.service.impl;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.bootcamp.debitcardservice.documents.DebitCard;
import com.nttdata.bootcamp.debitcardservice.dto.CustomerBankDto;
import com.nttdata.bootcamp.debitcardservice.repository.DebitCardRepository;
import com.nttdata.bootcamp.debitcardservice.service.DebitCardService;
import com.nttdata.bootcamp.debitcardservice.utilities.ExternalApiService;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
public class DebitCardServiceImpl implements DebitCardService{

	@Autowired
	DebitCardRepository repository;

	@Autowired
	ExternalApiService externalApiService;
	 
	
	private final ObjectMapper objectMapper;
	
	private final KafkaReceiver<String, String> kafkaReceiver;
	private final KafkaSender<String, String> kafkaSender;
    private final String responseTopic = "topicUpdateWallet";

    public DebitCardServiceImpl(KafkaReceiver<String, String> kafkaReceiver, ObjectMapper objectMapper,
    					KafkaSender<String, String> kafkaSender) {
    	 this.kafkaReceiver = kafkaReceiver;
    	 this.objectMapper = objectMapper;
    	 this.kafkaSender = kafkaSender;

    }

    @PostConstruct
    public void startConsuming() {
        consumeTopicPrueba();
    }
    
	@Override
    public Mono<DebitCard> saveDebitCard(DebitCard debitCard) {
        return repository.save(debitCard);
    }

	@Override
	public Mono<DebitCard> saveWithAllAccounts(DebitCard debitCard) {
	    //busco todas las cuentas bancarias que posee el cliente en base a su numero de documento
		return externalApiService.getBankAccountByNumberDocument(debitCard.getNumberDocument())
            .collectList()
            .flatMap(customerBankDtos -> {
            	//de la lista obtenida genero un array de String para asociarlas a todas
                String[] accountNumbers = customerBankDtos.stream()
                        .map(CustomerBankDto::getBankAccountNumber)
                        .toArray(String[]::new);
                debitCard.setAccountNumbers(accountNumbers);
                return repository.save(debitCard);
            });
	}

	public void consumeTopicPrueba() {
	    kafkaReceiver.receive()
	        .doOnNext(record -> {
	            String value = record.value();
	            try {
	                JsonNode jsonNode = objectMapper.readTree(value);
	                String numberCardDebit = jsonNode.get("numberCardDebit").asText();
	                String phone = jsonNode.get("phone").asText();
	                repository.findByNumberCard(numberCardDebit)
	                    .flatMap(debitCard -> {
	                        // cargando info para nuevo topic
	                        String message = "{\"numberCardDebit\": \"" + debitCard.getNumberCard() 
	                        	+ "\", \"phone\": \"" + phone
	                            + "\", \"primaryAccount\": \"" + debitCard.getPrimaryAccount() + "\"}";
	                        
	                        return kafkaSender.send(Mono.just(SenderRecord.create(
	                        			new ProducerRecord<>(responseTopic, message), null)))
	                            .then(); // "then()" para retornar un Mono<Void>
	                    })
	                    .doOnError(error -> {
	                    })
	                    .subscribe();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        })
	        .subscribe();
	}
	
}
